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

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.orange.labs.db.dao.AccountRepository;

/**
 * Main controller responsible for display login page.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RestController  
@RequestMapping("/")
public class LoginController {
    
    /**
     * Logger.
     */
    private final Logger logger = Logger.getLogger(LoginController.class);
    
    /**
     * Injected repository object for accounts.
     */
    @Autowired 
    private AccountRepository repository;      
    
    @RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
    public ModelAndView welcomePage() {
        
        ModelAndView model = new ModelAndView();
        model.setViewName("login");
        return model;
    } 
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            HttpServletRequest req,
            @RequestParam(value = "error", required = false) String error, 
            @RequestParam(value = "logout", required = false) String logout) {        
        
        ModelAndView model = new ModelAndView();      
        
        if (error != null) {
            
            model.addObject("error", "Invalid username and password!");
        }
 
        if (logout != null) {
            
            model.addObject("msg", "You've been logged out successfully.");
        }
        
        model.setViewName("login");
 
        return model;
    }    
}
