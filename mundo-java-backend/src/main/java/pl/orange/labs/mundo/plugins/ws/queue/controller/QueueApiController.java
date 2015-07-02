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

package pl.orange.labs.mundo.plugins.ws.queue.controller;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.orange.labs.mundo.plugins.ws.queue.dao.QueueEntity;
import pl.orange.labs.mundo.plugins.ws.queue.dao.QueueRepository;
import pl.orange.labs.mundo.plugins.ws.queue.dao.QueueService;
import pl.orange.labs.mundo.plugins.ws.queue.entity.XmlQueueResponse;
import pl.orange.labs.mundo.plugins.ws.queue.exception.QueueException;

/**
 * Controller for API Queue requests
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RestController
@RequestMapping("/api/queue")
public class QueueApiController {
    
    private static final Logger LOGGER = Logger.getLogger(QueueApiController.class); 
    
    @Autowired
    private final QueueService queueService;
    @Autowired
    private final QueueRepository queueRepository; 
    
    @Autowired
    public QueueApiController(QueueRepository queueRepository, QueueService queueService) 
    {
        this.queueRepository = queueRepository;
        this.queueService = queueService;
    }  
    
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getQueuesList() {
        
        LOGGER.info("getQueuesList");
        List<QueueEntity> list = queueRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }    
    
    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getQueue(@PathVariable String name) {
        
        try {
            
            QueueEntity wsQueueEntity = queueRepository.findByName(name);
            Assert.notNull(wsQueueEntity, "Queue not found.");
            XmlQueueResponse result = queueService.getQueue(wsQueueEntity.getUrl());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Queue: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (QueueException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Queue: QueueException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Queue: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    } 
    
    @RequestMapping(value = "/{name}", method = RequestMethod.HEAD, produces = "application/json; charset=utf-8")
    public ResponseEntity getQueueHead(@PathVariable String name) {
        
        try {
            
            QueueEntity wsQueueEntity = queueRepository.findByName(name);
            Assert.notNull(wsQueueEntity, "Queue not found.");
            XmlQueueResponse result = queueService.getQueue(wsQueueEntity.getUrl());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Queue: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (QueueException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Queue: QueueException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Queue: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    } 
}
