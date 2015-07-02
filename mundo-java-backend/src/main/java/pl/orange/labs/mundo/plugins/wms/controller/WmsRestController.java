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

package pl.orange.labs.mundo.plugins.wms.controller;

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
import pl.orange.labs.mundo.plugins.wms.dao.WmsEntity;
import pl.orange.labs.mundo.plugins.wms.dao.WmsRepository;

/**
 * WMS controller - controls JS GUI requests
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RestController
@RequestMapping("/gui")
public class WmsRestController 
{
    private static final Logger LOGGER = Logger.getLogger(WmsRestController.class);      
    
    @Autowired
    private WmsRepository wmsRepository;  
    
    
    public WmsRestController(){}
    
    public WmsRestController(WmsRepository wmsRepository)
    {
        this.wmsRepository = wmsRepository;
    }      
    
    @RequestMapping(value = "/user/wms", method=RequestMethod.GET, produces="application/json; charset=utf-8")
    public ResponseEntity getWms()
    {
        List<WmsEntity> result =  wmsRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }  
    
    @RequestMapping(value = "/admin/wms", method=RequestMethod.POST, produces="application/json; charset=utf-8")
    public ResponseEntity createWms(@RequestBody final WmsEntity wms)
    {
        try
        {
            WmsEntity result = wmsRepository.save(wms);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch(DataIntegrityViolationException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: DataIntegrityViolationException", e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }        
    }  
    
    @RequestMapping(value = "/admin/wms", method=RequestMethod.PUT, produces="application/json; charset=utf-8")
    public ResponseEntity updateWms(@RequestBody final WmsEntity wms)
    {
        try
        {
            WmsEntity result = wmsRepository.save(wms);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch(DataIntegrityViolationException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: DataIntegrityViolationException", e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }        
    }     
    
    @RequestMapping(value = "/admin/wms/{id}", method=RequestMethod.DELETE, produces="application/json; charset=utf-8")
    public ResponseEntity removeWms(@PathVariable Long id)
    {
        try
        {
            wmsRepository.delete(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        catch(EmptyResultDataAccessException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: EmptyResultDataAccessException", e);
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }
    }     
}
