/*
*  Platforma MUNDO – Dane po Warszawsku  http://danepowarszawsku.pl/
*   
*  @authors Jaroslaw Legierski, Tomasz Janisiewicz, Henryk Rosa Centrum Badawczo – Rozwojowe/ Orange Labs
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

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RestController
@RequestMapping("/gui")
public class AccountRestController {
  
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(AccountRestController.class);    
    
    /**
     * Injected repository object for resources.
     */      
    @Autowired
    private AccountRepository accountRepository;  
    @Autowired
    private ConfigService config;
    
    /**
     * Method responsible for returning all sorted accounts from db.
     * @return ResponseEntity with sorted accounts in http body and 200 OK in status.
     */    
    @RequestMapping(value = "/superadmin/account", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getAccountList() {
        
        List<Account> result =  accountRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }  
    
    /**
     * Method responsible for creating new account in db.
     * @param account - Account object in JSON format.
     * @return ResponseEntity with 201 CREATED as status.
     * @throws ConfigException 
     */    
    @RequestMapping(value = "/superadmin/account", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity addAccount(@RequestBody final Account account) throws ConfigException {
        if (account!=null)
        	account.setPasswordLimit(config.getIntValueForConfigVariable(ConfigService.ACCOUNT_DEF_PSW_LIMIT));
        Account result = accountRepository.save(account);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    
    /**
     * Method responsible for updating account in DB.
     * @param account - Account object in JSON format.
     * @return ResponseEntity with result.
     */    
    @RequestMapping(value = "/superadmin/account", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity updateAccount(@RequestBody final Account account) {
        
        Account result = accountRepository.save(account);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }     
    
    /**
     * Method responsible for deleting account from DB.
     * @param id - identifier of deleting account
     * @return ResponseEntity with id in body.
     */    
    @RequestMapping(value = "/superadmin/account/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity removeAccount(@PathVariable Long id) {
        
        try {
            
            accountRepository.delete(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        catch (EmptyResultDataAccessException e) {
            
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }
    }     
}
