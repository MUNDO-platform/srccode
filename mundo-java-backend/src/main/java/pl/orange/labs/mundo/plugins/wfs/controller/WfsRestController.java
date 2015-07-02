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

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsEntity;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsRepository;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RestController
@RequestMapping("/gui")
public class WfsRestController 
{
    private static final Logger LOGGER = Logger.getLogger(WfsRestController.class);      
    
    @Autowired
    private WfsRepository wfsRepository; 
    
    public WfsRestController(){}
    
    public WfsRestController(WfsRepository wfsRepository)
    {
        this.wfsRepository = wfsRepository;
    }    
        
    @RequestMapping(value = "/user/wfs", method=RequestMethod.GET, produces="application/json; charset=utf-8")
    public ResponseEntity getWfs()
    {
        List<WfsEntity> result =  wfsRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }  
    
    @RequestMapping(value = "/admin/wfs", method=RequestMethod.POST, produces="application/json; charset=utf-8")
    public ResponseEntity createWfs(@RequestBody final WfsEntity wfs)
    {
        try
        {
            WfsEntity result = wfsRepository.save(wfs);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch(DataIntegrityViolationException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS: DataIntegrityViolationException", e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }         
    }  
    
    @RequestMapping(value = "/admin/wfs", method=RequestMethod.PUT, produces="application/json; charset=utf-8")
    public ResponseEntity updateWfs(@RequestBody final WfsEntity wfs)
    {
        try
        {
            WfsEntity result = wfsRepository.save(wfs);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch(DataIntegrityViolationException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS: DataIntegrityViolationException", e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }         
    }     
    
    @RequestMapping(value = "/admin/wfs/{id}", method=RequestMethod.DELETE, produces="application/json; charset=utf-8")
    public ResponseEntity removeWfs(@PathVariable Long id)
    {
        try
        {
            wfsRepository.delete(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        catch(EmptyResultDataAccessException e)
        {
        	LOGGER.error(e);
        	if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS: EmptyResultDataAccessException", e);
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }
    }     
}
