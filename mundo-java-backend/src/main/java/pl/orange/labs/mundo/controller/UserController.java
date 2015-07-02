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

package pl.orange.labs.mundo.controller;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controls request for displaying page with GUI user settings. 
 * @author Henryk Rosa <henryk.rosa@orange.com>
 */
@Controller
@RequestMapping(value = "/gui")
public class UserController {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(UserController.class);     
    
    
    /**
     * Diplays current GUI user setting
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView getAccount()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        ModelAndView model = new ModelAndView();
        model.addObject("view", "user");
        model.addObject("username", username);	
        model.setViewName("user"); 
        return model; 
    }    
 
}
