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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.orange.labs.db.entities.ConfigEntity;
import pl.orange.labs.mundo.service.ConfigService;

/**
 * Controls REST requests for platform config page.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RestController
@RequestMapping("/gui")
public class ConfigRestController {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ConfigRestController.class);    
    
    @Autowired
    private ConfigService configService;
    
    /**
     * Default constructor.
     */
    public ConfigRestController() {}
    
    /**
     * Constructor.
     * @param configService 
     */    
    public ConfigRestController(ConfigService configService) {
        
        this.configService = configService;
    }
    
    /**
     * Method responsible for returning all sorted config variables from db.
     * @return ResponseEntity with sorted config variables in http body and 200 OK in status.
     */
    @RequestMapping(value = "/superadmin/config", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getConfig() {
        
        List<ConfigEntity> result =  configService.findAll(new Sort(Sort.Direction.ASC, "id"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }  
    
    /**
     * Method responsible for creating new config variable in db.
     * @param config  - ConfigEntity object in JSON format
     * @return ResponseEntity with 201 CREATED as status.
     */
    @RequestMapping(value = "/superadmin/config", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity createConfig(@RequestBody final ConfigEntity config) {
        
        try {    
            
            ConfigEntity result = configService.save(config);
            Assert.notNull(result, "ConfigEntity has wrong format");
            configService.initFromDb();
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch (DataIntegrityViolationException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }        
    }  
    
    /**
     * Method responsible for updating config variable in DB.
     * @param config  - ConfigEntity object in JSON format
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "/superadmin/config", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
    public ResponseEntity updateConfig(@RequestBody final ConfigEntity config) {
        

        try {    
            
            ConfigEntity result = configService.save(config);
            Assert.notNull(result, "ConfigEntity has wrong format");
            configService.initFromDb();        
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (DataIntegrityViolationException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }         
    }     
    
    /**
     * Method responsible for deleting confgig variable in DB.
     * @param id - identifier of deleting variable
     * @return ResponseEntity with id in body.
     */
    @RequestMapping(value = "/superadmin/config/{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    public ResponseEntity removeConfig(@PathVariable Integer id) {
        
        try {
            
            configService.delete(id);
            configService.initFromDb();            
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        catch (EmptyResultDataAccessException e) {
            
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }
    }     
}
