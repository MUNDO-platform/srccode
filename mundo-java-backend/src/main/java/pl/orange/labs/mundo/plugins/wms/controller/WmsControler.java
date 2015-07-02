/*
*  Platforma MUNDO – Dane po Warszawsku  http://danepowarszawsku.pl/
*   
*  @authors Jarosław Legierski, Tomasz Janisiewicz, Henryk Rosa Centrum Badawczo – Rozwojowe/ Orange Labs
*  copyright (c) 2014-2015 Orange Polska S.A. niniejszy kod jest otwarty i dystrybuowany
*  na licencji:   Lesser General Public License v2.1 (LGPLv2.1), której  pełny tekst można
*  znaleźć pod adresem:  https://www.gnu.org/licenses/lgpl-2.1.html
*
* oprogramowanie stworzone w ramach Projektu : MUNDO Miejskie Usługi Na Danych Oparte
* Beneficjenci: Fundacja Techsoup, Orange Polska S.A., Politechnika  Warszawska,
* Fundacja Pracownia Badań i Innowacji Społecznych „Stocznia”, Fundacja Projekt Polska
* Wartość projektu: 1 108 978
* Wartość dofinansowania: 847 000
* Okres realizacji 01.04.2014 – 31.12.2015
* Projekt współfinansowany przez Narodowe Centrum Badań i Rozwoju w ramach
* Programu Innowacje Społeczne
*
*/

package pl.orange.labs.mundo.plugins.wms.controller;

import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.orange.labs.mundo.geoconverter.GeoConversion;
import pl.orange.labs.mundo.geoconverter.GeoconversionFactory;
import pl.orange.labs.mundo.plugins.wms.dao.WmsEntity;
import pl.orange.labs.mundo.plugins.wms.dao.WmsRepository;
import pl.orange.labs.mundo.plugins.wms.dao.WmsService;
import pl.orange.labs.mundo.plugins.wms.entity.WmsCapabilities;
import pl.orange.labs.mundo.plugins.wms.exception.WmsException;

/**
 * WMS controller controlls WMS GUI requests
 * @author Henryk Rosa <henryk.rosa@orange.com>
 *
 */
@Controller
@RequestMapping(value="/gui")
public class WmsControler 
{
    private static Logger LOGGER = Logger.getLogger(WmsControler.class);
    
    @Autowired
    private WmsService wmsService; 
    @Autowired
    private WmsRepository wmsRepository;      
    
    @RequestMapping(value = "/user/wms", method=RequestMethod.GET)
    public String displayWmsEntries(Model model)
    {
        LOGGER.info("displayWmsEntries");
        return "wms";        
    } 
    
    @RequestMapping(value = "/user/wms/{name}", method=RequestMethod.GET)
    public String displayWmsCapabilities(@PathVariable String name, Model model) throws WmsException
    {
        LOGGER.info("displayWmsCapabilities");
        WmsEntity wmsEntity = wmsRepository.findByName(name);
    	
        WmsCapabilities wmsCapabilities = wmsService.getCapabilities(wmsEntity.getUrl());
        model.addAttribute("capabilities", wmsCapabilities);
        model.addAttribute("wmsEntity", wmsEntity);

        return "wmsCapabilities";
    }       
    
    @RequestMapping(value = "/user/wms/{name}/getmap", method=RequestMethod.GET, produces="application/text; charset=utf-8")
    public String displayWmsMap(
            @PathVariable String name, 
            @RequestParam String layers,
            @RequestParam String center, 
            @RequestParam String zoom, 
            @RequestParam String size, 
            @RequestParam String format, 
            Model model) throws UnsupportedEncodingException, WmsException
    {
        LOGGER.info("displayWmsMap");        
        WmsEntity wmsEntity = wmsRepository.findByName(name);
        WmsCapabilities capabilities = wmsService.getCapabilities(wmsEntity.getUrl());

    	String[] centerParts=center.split(",");
    	double centerB=0;
    	double centerL=0;
        centerB = Double.parseDouble(centerParts[0]);
    	centerL = Double.parseDouble(centerParts[1]);
        int zoomInt=Integer.parseInt(zoom);
        String[] sizeParts=size.split("x");
    	int width=Integer.parseInt(sizeParts[0]);
    	int length=Integer.parseInt(sizeParts[1]);    	
        
        GeoConversion gcnv = GeoconversionFactory.create(wmsEntity.getSrs());
    	gcnv.setUrlGivenParams(centerB, centerL, zoomInt, width, length);
        
    	String url = capabilities.getCapability().getRequest().getGetMap().getDcpType().getHttp().getGet().getOnlineResource().getHref()+
    			"?REQUEST=GetMap&"+
    			"SERVICE="+capabilities.getService().getName()+
    			"&VERSION="+capabilities.getVersion()+
    			"&LAYERS="+layers+
    			"&STYLES=&FORMAT=image/"+format+
    			"&BGCOLOR=0xFFFFFF&TRANSPARENT=TRUE"+
    			"&"+gcnv.getSrsPart()+"&"+gcnv.getConvertedBboxUrlPart()+"&"+gcnv.getConvertedWidth()+"&"+gcnv.getConvertedHeight(); 
        

        String wmsMap = wmsService.getMap(url.replace("??", "?"));
        model.addAttribute("wmsMap", wmsMap);

        return "wmsMap";
    }    
}
