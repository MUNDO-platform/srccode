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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.orange.labs.mundo.plugins.ws.queue.dao.QueueEntity;
import pl.orange.labs.mundo.plugins.ws.queue.dao.QueueRepository;
import pl.orange.labs.mundo.plugins.ws.queue.dao.QueueService;
import pl.orange.labs.mundo.plugins.ws.queue.entity.XmlQueueResponse;
import pl.orange.labs.mundo.plugins.ws.queue.exception.QueueException;

/**
 * Controller for API Queue GUI 
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Controller
@RequestMapping(value="/gui")
public class QueueController 
{
    private static Logger LOGGER = Logger.getLogger(QueueController.class);    
    
    @Autowired
    private QueueRepository queueRepository;
    @Autowired
    private QueueService queueService;     
    
    @RequestMapping(value = "/user/queue", method=RequestMethod.GET)
    public String displayQueues(Model model)
    {
        LOGGER.info("displayQueues");
        return "queues";
    }  
    
    @RequestMapping(value = "/user/queue/{name}", method=RequestMethod.GET)
    public String displayQueue(@PathVariable String name, Model model) throws QueueException
    {        
        LOGGER.info("displayQueue");
        
        QueueEntity queueEntity = queueRepository.findByName(name);
        XmlQueueResponse xmlQueueResponse = queueService.getQueue(queueEntity.getUrl());        
        model.addAttribute("groups", xmlQueueResponse.getGrupy());
        model.addAttribute("queueEntity", queueEntity);
                
        return "queue";
    }     
}
