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

package pl.orange.labs.mundo.plugins.ws.api19115.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.orange.labs.mundo.plugins.ws.api19115.dao.Api19115Entity;
import pl.orange.labs.mundo.plugins.ws.api19115.dao.Api19115Repository;
import pl.orange.labs.mundo.plugins.ws.api19115.dao.Api19115Service;
import pl.orange.labs.mundo.plugins.ws.api19115.exception.Api19115Exception;

/**
 * Controller for API 19115 GUI requests 
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@Controller
@RequestMapping(value="/gui")
public class Store19115Controller 
{
    private static Logger LOGGER = Logger.getLogger(Store19115Controller.class);    
    
    @Autowired
    private Api19115Repository api19115Repository;
    @Autowired
    private Api19115Service api19115Service;     
    
    @RequestMapping(value = "/user/api19115", method=RequestMethod.GET)
    public String displayApis(Model model)
    {
        LOGGER.info("display 19115 APIs");
        return "apis19115";
    }  
    
    @RequestMapping(value = "/user/api19115/{name}", method=RequestMethod.GET)
    public String displayQueue(@PathVariable String name, Model model) throws Api19115Exception
    {        
        LOGGER.info("displayApi19115");
        
        Api19115Entity entity  = api19115Repository.findByName(name);
        Date d =new Date();
        long tsCurrent = d.getTime();
        long ts2DaysBefore = tsCurrent - (long)172800000;
        
        model.addAttribute("apiEntity", entity);
        model.addAttribute("dateExampleFrom", ts2DaysBefore+"");
        model.addAttribute("dateExampleTo", tsCurrent+"");
        String fex = "\"filters\":[{\"field\":\"DISTRICT\",\"operator\":\"EQ\",\"value\":\"Wola\"}]";
        String oex = "\"operators\":null";
        try {
			model.addAttribute("filterExample", URLEncoder.encode(fex, "UTF-8"));
			model.addAttribute("operatorExample", URLEncoder.encode(oex, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("UnsupportedEncodingException:"+e.getMessage());
		}
        return "api19115";
    }     
}
