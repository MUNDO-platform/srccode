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
import pl.orange.labs.db.dao.ResourceRepository;
import pl.orange.labs.db.entities.ResourceEntity;

/**
 * Controls REST requests for platform resources page.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RestController
@RequestMapping("/gui")
public class ResourceRestController {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ResourceRestController.class);    
    
    /**
     * Injected repository object for resources.
     */      
    @Autowired
    private ResourceRepository resourceRepository;  
    
    /**
     * Method responsible for returning all sorted platform resources from db.
     * @return ResponseEntity with sorted resources in http body and 200 OK in status.
     */
    @RequestMapping(value = "/user/resource", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getResource() {
        
        List<ResourceEntity> result =  resourceRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }  
    
    /**
     * Method responsible for creating new resource in db.
     * @param resource - ResourceEntity object in JSON format.
     * @return ResponseEntity with 201 CREATED as status.
     */
    @RequestMapping(value = "/admin/resource", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity createResource(@RequestBody final ResourceEntity resource) {
        
        ResourceEntity result = resourceRepository.save(resource);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }  
    
    /**
     * Method responsible for updating resource in DB.
     * @param resource - ResourceEntity object in JSON format.
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "/admin/resource", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
    public ResponseEntity updateResource(@RequestBody final ResourceEntity resource)
    {
        ResourceEntity result = resourceRepository.save(resource);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }     
    
    /**
     * Method responsible for deleting resource in DB.
     * @param id - identifier of deleting resource
     * @return ResponseEntity with id in body.
     */
    @RequestMapping(value = "/admin/resource/{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    public ResponseEntity removeResource(@PathVariable Long id) {
        
        try {
            
            resourceRepository.delete(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        catch (EmptyResultDataAccessException e) {
            
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }
    }     
}
