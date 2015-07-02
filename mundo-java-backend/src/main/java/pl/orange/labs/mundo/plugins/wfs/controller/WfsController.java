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

package pl.orange.labs.mundo.plugins.wfs.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsEntity;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsRepository;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsService;
import pl.orange.labs.mundo.plugins.wfs.entity.XmlFeatureType;
import pl.orange.labs.mundo.plugins.wfs.entity.XmlFeatureTypeList;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsCapabilities;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsFeatureCollection;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsFeatureMember;
import pl.orange.labs.mundo.plugins.wfs.exception.WfsException;
/**
 * Controls GUI requests for wfs resources.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */

@Controller
@RequestMapping(value="/gui")
public class WfsController
{
    private static Logger LOGGER = Logger.getLogger(WfsController.class);

    @Autowired
    private WfsService wfsService; 
    @Autowired
    private WfsRepository wfsRepository;    
    
    
    @RequestMapping(value = "/user/wfs", method=RequestMethod.GET)
    public String displayWfsEntries(Model model)
    {
        LOGGER.info("displayWfs");
        return "wfs";
    }  
    
    @RequestMapping(value = "/user/wfs/{name}", method=RequestMethod.GET)
    public String displayWfsCapabilities(@PathVariable String name, Model model) throws WfsException
    {
        LOGGER.info("displayWfsCapabilities");
        WfsEntity wfsEntity = wfsRepository.findByName(name);
    	
        WfsCapabilities cap = wfsService.getCapabilities(wfsEntity.getCapabilitiesUrl());
        XmlFeatureTypeList features = cap.getFeatureTypeList();
        List<XmlFeatureType> list = features.getFeatureList();       

        model.addAttribute("capabilities", list);
        model.addAttribute("wfsEntity", wfsEntity);
        return "wfsCapabilities";
    }    
    
    @RequestMapping(value = "/user/wfs/{name}/{title}", method=RequestMethod.GET, produces="application/text; charset=utf-8")
    public String displayWfsFeature(
            @PathVariable String name, 
            @PathVariable String title, 
            @RequestParam(required = false) String bbox, 
            @RequestParam(required = false) String circle,             
            @RequestParam(required = false) String maxFeatures, 
            @RequestParam(required = false) String filter,
            Model model) throws UnsupportedEncodingException, WfsException
    {
        WfsEntity wfsEntity = wfsRepository.findByName(name);
    	
        String[] parts = title.split(":");
        String namespace = parts[0]; 
        String feature = parts[1];
        
        String url = wfsEntity.getFeaturesUrl() + "&typeName="+namespace+":"+feature;
        if (filter!=null) url += "&Filter=" + URLEncoder.encode(filter, "UTF-8");
        if (maxFeatures!=null) url += "&maxFeatures=" + maxFeatures;
        if (bbox!=null) url += "&bbox=" + bbox;
        if (circle!=null) url += "&bbox=" + this.getBboxByCircleArea(circle);
        WfsFeatureCollection collection = wfsService.getFeature(url,namespace,feature);
        
        List<WfsFeatureMember> list = collection.getFeatureMemberList();
        model.addAttribute("features", list);
        model.addAttribute("wfsEntity", wfsEntity);
        model.addAttribute("view","home");
        List<String> keyList = collection.getFeatureMemberPropertyKey();
        model.addAttribute("keyList",keyList); 
        model.addAttribute("namespace",namespace);
        model.addAttribute("feature",feature);
        model.addAttribute("wfsName",name);        

        return "wfsFeature";
    } 
    
    @RequestMapping(value = "/{id}/capabilities/{title}.grid", method=RequestMethod.GET, produces="application/text; charset=utf-8")
    public String displayFeatureGrid(
            @PathVariable Long id, 
            @PathVariable String title, 
            @RequestParam(required = false) String bbox, 
            @RequestParam(required = false) String circle,             
            @RequestParam(required = false) String maxFeatures, 
            @RequestParam(required = false) String filter,
            Model model) throws UnsupportedEncodingException, WfsException
    {
    	WfsEntity wfsEntity = wfsRepository.findById(id);
    	
        String[] parts = title.split(":");
        String namespace = parts[0]; 
        String name = parts[1];
        
        String url = wfsEntity.getFeaturesUrl() + "&typeName="+namespace+":"+name;
        if (filter!=null) url += "&Filter=" + URLEncoder.encode(filter, "UTF-8");
        if (maxFeatures!=null) url += "&maxFeatures=" + maxFeatures;
        if (bbox!=null) url += "&bbox=" + bbox;
        if (circle!=null) url += "&bbox=" + this.getBboxByCircleArea(circle);
        WfsFeatureCollection collection = wfsService.getFeature(url,namespace,name);
        
        List<WfsFeatureMember> list = collection.getFeatureMemberList();
        model.addAttribute("features", list);
        model.addAttribute("wfsEntity", wfsEntity);
        model.addAttribute("view","grid");
        model.addAttribute("title",title);        
        List<String> keyList = collection.getFeatureMemberPropertyKey();
        model.addAttribute("keyList",keyList);
        model.addAttribute("namespace",namespace);
        model.addAttribute("name",name);
        model.addAttribute("wfsId",id);

        return "wfsFeature";
    }  
    
    @RequestMapping(value = "/{id}/capabilities/{title}.map", method=RequestMethod.GET, produces="application/text; charset=utf-8")
    public String displayFeatureMap(
            @PathVariable Long id, 
            @PathVariable String title, 
            @RequestParam(required = false) String bbox, 
            @RequestParam(required = false) String circle,             
            @RequestParam(required = false) String maxFeatures, 
            @RequestParam(required = false) String filter,
            Model model) throws UnsupportedEncodingException, WfsException
    {
    	WfsEntity wfsEntity = wfsRepository.findById(id);
    	
        String[] parts = title.split(":");
        String namespace = parts[0]; 
        String name = parts[1];
        
        String url = wfsEntity.getFeaturesUrl() + "&typeName="+namespace+":"+name;
        if (filter!=null) url += "&Filter=" + URLEncoder.encode(filter, "UTF-8");
        if (maxFeatures!=null) url += "&maxFeatures=" + maxFeatures;
        if (bbox!=null) url += "&bbox=" + bbox;
        if (circle!=null) url += "&bbox=" + this.getBboxByCircleArea(circle);
        WfsFeatureCollection collection = wfsService.getFeature(url,namespace,name);
        
        List<WfsFeatureMember> list = collection.getFeatureMemberList();
        model.addAttribute("features", list);
        model.addAttribute("wfsEntity", wfsEntity);
        model.addAttribute("view","map");
        model.addAttribute("title",title);
        List<String> keyList = collection.getFeatureMemberPropertyKey();
        model.addAttribute("keyList",keyList);
        model.addAttribute("namespace",namespace);
        model.addAttribute("name",name);
        model.addAttribute("wfsId",id);

        return "wfsFeature";
    }
    
    private String getBboxByCircleArea(String circle) 
    {
        String[] parts = circle.split(",");
        double x = Double.valueOf(parts[0]); 
        double y = Double.valueOf(parts[1]);
        double radius = Double.valueOf(parts[2]);  //m        
        double R = 6371000;  // earth radius in m
        double x1 = x - Math.toDegrees(radius/R/Math.cos(Math.toRadians(y)));
        double x2 = x + Math.toDegrees(radius/R/Math.cos(Math.toRadians(y)));
        double y1 = y + Math.toDegrees(radius/R);
        double y2 = y - Math.toDegrees(radius/R);
        
        String result = x1+","+y1+","+x2+","+y2;
        LOGGER.info("circle -> bbox: " + result);
       return result;
    }    
}
