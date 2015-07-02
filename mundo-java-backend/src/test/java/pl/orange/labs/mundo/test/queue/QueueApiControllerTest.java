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

package pl.orange.labs.mundo.test.queue;

import java.io.IOException;
import java.nio.charset.Charset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.orange.labs.config.JpaConfig;
import pl.orange.labs.config.PersistenceTestConfig;
import pl.orange.labs.mundo.plugins.ws.queue.controller.QueueApiController;
import pl.orange.labs.mundo.plugins.ws.queue.dao.QueueEntity;
import pl.orange.labs.mundo.plugins.ws.queue.dao.QueueRepository;
import pl.orange.labs.mundo.plugins.ws.queue.dao.QueueService;
import pl.orange.labs.mundo.plugins.ws.queue.exception.QueueException;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {JpaConfig.class, PersistenceTestConfig.class})
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class QueueApiControllerTest {
    
    private MockMvc mvc;
    @Autowired
    private QueueRepository queueRepository;        
    
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));   
    
    private HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(); 
    
    protected String json(Object o) throws IOException {
        
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    } 
    
    @Before
    public void setUp() throws Exception {
        
        QueueService queueService = new QueueServiceMockImpl();
        
        mvc = MockMvcBuilders.standaloneSetup(new QueueApiController(queueRepository, queueService)).build();
        
        queueRepository.deleteAll();

        QueueEntity queue = new QueueEntity();
        queue.setName("starynkiewicza");
        queue.setUrl("ok");
        queueRepository.save(queue);  
        
        QueueEntity badqueue = new QueueEntity();
        badqueue.setName("nook");
        badqueue.setUrl("nook");
        queueRepository.save(badqueue);          
    }  
    
    @Test
    public void getQueuesListTest() throws Exception 
    {
        mvc.perform(get("/api/queue"))
                .andExpect(status().isOk());
        
        mvc.perform(get("/api/queue/"))
                .andExpect(status().isOk());        
    } 
    
    @Test
    public void getQueueTest() throws Exception
    {              
        mvc.perform(get("/api/queue/starynkiewicza"))
                .andExpect(status().isOk());       
    } 
    
    @Test
    public void getQueueNotFoundTest() throws Exception
    {
        mvc.perform(get("/api/queue/unknown"))
                .andExpect(status().isBadRequest());               
    }    
    
    @Test
    public void testQueueException() throws QueueException, Exception 
    {
        mvc.perform(get("/api/queue/nook"))
                .andExpect(status().isServiceUnavailable());      
    }  
}
