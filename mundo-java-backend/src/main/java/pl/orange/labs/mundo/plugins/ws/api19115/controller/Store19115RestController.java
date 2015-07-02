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

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.orange.labs.mundo.plugins.ws.api19115.dao.Api19115Entity;
import pl.orange.labs.mundo.plugins.ws.api19115.dao.Api19115Repository;


/**
 * Controller for API 19115 JS GUI requests
 * 
 *  @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@Controller
@RequestMapping("/gui")
public class Store19115RestController 
{
    private static Logger LOGGER = Logger.getLogger(Store19115RestController.class);

    @Autowired
    private Api19115Repository api19115Repository;

    @RequestMapping(value = "/user/api19115", method=RequestMethod.GET, produces="application/json; charset=utf-8")
    public ResponseEntity getResource()
    {
        List<Api19115Entity> result =  api19115Repository.findAll(new Sort(Sort.Direction.ASC, "id"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }  
    
    @RequestMapping(value = "/admin/api19115", method=RequestMethod.POST, produces="application/json; charset=utf-8")
    public ResponseEntity createResource(@RequestBody final Api19115Entity apiEntity)
    {
        Api19115Entity result = api19115Repository.save(apiEntity);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }  
    
    @RequestMapping(value = "/admin/api19115", method=RequestMethod.PUT, produces="application/json; charset=utf-8")
    public ResponseEntity updateResource(@RequestBody final Api19115Entity apiEntity)
    {
    	Api19115Entity result = api19115Repository.save(apiEntity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }     
    
    @RequestMapping(value = "/admin/api19115/{id}", method=RequestMethod.DELETE, produces="application/json; charset=utf-8")
    public ResponseEntity removeResource(@PathVariable Long id)
    {
        try
        {
        	api19115Repository.delete(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        catch(EmptyResultDataAccessException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Queue: EmptyResultDataAccessException", e);
        	return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }
    }      
}
