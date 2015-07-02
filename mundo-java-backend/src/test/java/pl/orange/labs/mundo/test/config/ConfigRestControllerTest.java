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

package pl.orange.labs.mundo.test.config;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.orange.labs.config.JpaConfig;
import pl.orange.labs.config.PersistenceTestConfig;
import pl.orange.labs.db.dao.ConfigDao;
import pl.orange.labs.db.dao.ConfigDaoImpl;
import pl.orange.labs.db.dao.ConfigRepository;
import pl.orange.labs.db.entities.ConfigEntity;
import pl.orange.labs.mundo.controller.ConfigRestController;
import pl.orange.labs.mundo.service.ConfigService;
import pl.orange.labs.mundo.service.ConfigServiceImpl;

/**
 *
 * @author Tomasz Janisiewicz <Tomasz.Janisiewicz@orange.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {JpaConfig.class, PersistenceTestConfig.class})
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class ConfigRestControllerTest {

    private MockMvc mvc;
    
    @Autowired
    private ConfigRepository configRepository;        
    
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
        
        ConfigDao configDao = new ConfigDaoImpl(configRepository);
        ConfigService configService = new ConfigServiceImpl(configDao);
        
        mvc = MockMvcBuilders.standaloneSetup(new ConfigRestController(configService)).build();                   
    }  
    
    @Test
    public void getConfigTest() throws Exception {
        
        mvc.perform(get("/gui/superadmin/config"))
                .andExpect(status().isOk());
    } 
    
    @Test
    public void createDbTest() throws Exception {
        
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setKey("createDbTest");
        configEntity.setValue("createDbTest");
        String configJson = json(configEntity);        
        
        mvc.perform(post("/gui/superadmin/config")
        .contentType(contentType)
        .content(configJson))
                .andExpect(status()
                        .isCreated());        
    }    
    
    @Test
    public void createDbTest2() throws Exception {
        
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setValue("createDbTest");
        String configJson = json(configEntity);        
        
        mvc.perform(post("/gui/superadmin/config")
        .contentType(contentType)
        .content(configJson))
                .andExpect(status().isBadRequest());         
    } 
    
    @Test
    public void createDbTest3() throws Exception {
        
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setKey("createDbTest");
        String configJson = json(configEntity);        
        
        mvc.perform(post("/gui/superadmin/config")
        .contentType(contentType)
        .content(configJson))
                .andExpect(status().isBadRequest());         
    } 
    
    @Test
    public void updateConfigTest() throws Exception {
        
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setKey("updateConfigTest");
        configEntity.setValue("updateConfigTest");
        String configJson = json(configEntity);        
        
        mvc.perform(put("/gui/superadmin/config")
        .contentType(contentType)
        .content(configJson))
                .andExpect(status()
                        .isOk());        
    }  
    
    @Test
    public void updateConfigTest2() throws Exception {
        
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setValue("createDbTest");
        String configJson = json(configEntity);        
        
        mvc.perform(put("/gui/superadmin/config")
        .contentType(contentType)
        .content(configJson))
                .andExpect(status().isBadRequest());         
    }    
    
    @Test
    public void updateConfigTest3() throws Exception {
        
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setKey("createDbTest");
        String configJson = json(configEntity);        
        
        mvc.perform(put("/gui/superadmin/config")
        .contentType(contentType)
        .content(configJson))
                .andExpect(status().isBadRequest());         
    } 
    
    @Test
    public void removeConfigTest() throws Exception {
        
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setKey("removeConfigTest");
        configEntity.setValue("removeConfigTest");
        ConfigEntity result = configRepository.save(configEntity);
        
        
        mvc.perform(delete("/gui/superadmin/config/" + result.getId()))
                .andExpect(status().isOk());        
    }     
}
