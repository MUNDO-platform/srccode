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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.orange.labs.db.dao.AccountRepository;
import pl.orange.labs.db.entities.Account;
import pl.orange.labs.mundo.exceptions.ConfigException;
import pl.orange.labs.mundo.service.ConfigService;

/**
 * Controls REST requests for accounts page.
 * @author Henryk Rosa <henryk.rosa@orange.com>
 */
@RestController
@RequestMapping("/gui/user")
public class UserRestController {
  
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(UserRestController.class);    
    
    /**
     * Injected repository object for resources.
     */      
    @Autowired
    private AccountRepository accountRepository;  
    @Autowired
    private ConfigService config;
    
    /**
     * Method responsible for returning details of user account read from db.
     * @return ResponseEntity with sorted accounts in http body and 200 OK in status.
     */    
    @RequestMapping(value = "/accountdetails/{name}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getAccountDetails(@PathVariable String name) {
        
    	String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    	if ((username==null) || (!username.equals(name))) {
    		LOGGER.warn("NOT Authorized attempt to access the user account data by user logged in:"+username+" - attempt to access user:"+name);
    		return new ResponseEntity<>("Forbidden !!!", HttpStatus.FORBIDDEN);
    	}
        Account result =  accountRepository.findByUsername(name);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }  
    
//    /**
//     * Method responsible for creating new account in db.
//     * @param account - Account object in JSON format.
//     * @return ResponseEntity with 201 CREATED as status.
//     * @throws ConfigException 
//     */    
//    @RequestMapping(value = "/superadmin/account", method = RequestMethod.POST, produces = "application/json")
//    public ResponseEntity addAccount(@RequestBody final Account account) throws ConfigException {
//        if (account!=null)
//        	account.setPasswordLimit(config.getIntValueForConfigVariable(ConfigService.ACCOUNT_DEF_PSW_LIMIT));
//        Account result = accountRepository.save(account);
//        return new ResponseEntity<>(result, HttpStatus.CREATED);
//    }
    
    /**
     * Method responsible for updating account in DB.
     * @param account - Account object in JSON format.
     * @return ResponseEntity with result.
     * @throws ConfigException 
     */    
    @RequestMapping(value = "/saveaccount", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity updateAccount(@RequestBody final Account account) throws ConfigException {

    	String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    	if ((username==null) || (!username.equals(account.getUsername()))) {
    		LOGGER.warn("NOT Authorized attempt to change the user account data by user logged in:"+username+" - attempt to modify user:"+account.getUsername());
    		return new ResponseEntity<>("Forbidden !!!", HttpStatus.FORBIDDEN);
    	}

    	if (account!=null)
        	account.setPasswordLimit(config.getIntValueForConfigVariable(ConfigService.ACCOUNT_DEF_PSW_LIMIT));
        Account result = accountRepository.save(account);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }     
    
}
