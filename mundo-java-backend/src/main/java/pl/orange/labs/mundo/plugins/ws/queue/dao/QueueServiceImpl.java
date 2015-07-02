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

package pl.orange.labs.mundo.plugins.ws.queue.dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.orange.labs.mundo.plugins.ws.queue.entity.XmlQueueResponse;
import pl.orange.labs.mundo.plugins.ws.queue.exception.QueueException;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Component
public class QueueServiceImpl implements QueueService 
{
    @Autowired 
    QueueRepository wsQueueRepository;    
    @Autowired
    private QueueWebDao wsQueueWebDao;  
    
    public QueueServiceImpl() {}
    
    public QueueServiceImpl(QueueWebDao wsQueueWebDao)
    {
        this.wsQueueWebDao = wsQueueWebDao;
    }

    @Override
    public List<QueueEntity> getQueueList() 
    {
        return wsQueueRepository.findAll();
    }

    @Override
    public QueueEntity getQueueById(Long id) 
    {
        return wsQueueRepository.findOne(id);
    }    

    @Override
    public XmlQueueResponse getQueue(String url) throws QueueException 
    {
        return wsQueueWebDao.getQueue(url);
    }

    @Override
    public QueueEntity getQueueByName(String name) 
    {
        return wsQueueRepository.findByName(name);
    }
}
