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
package pl.orange.labs.mundo.test.wfs;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.orange.labs.config.JpaConfig;
import pl.orange.labs.config.PersistenceTestConfig;
import pl.orange.labs.db.dao.ConfigDao;
import pl.orange.labs.db.dao.ConfigDaoImpl;
import pl.orange.labs.db.dao.ConfigRepository;
import pl.orange.labs.mundo.plugins.wfs.controller.WfsApiController;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsEntity;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsRepository;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsService;
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
public class WfsApiControllerTest {
    
    private MockMvc mvc;
    @Autowired
    private WfsRepository wfsRepository;  
    @Autowired
    private ConfigRepository configRepository;    
    
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));   
    
    private final HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(); 
    
    protected String json(Object o) throws IOException {
        
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    } 
    
    @Before
    public void setUp() throws Exception {
        
        WfsService wfsService = new WfsServiceMockImpl();
        
        ConfigDao configDao = new ConfigDaoImpl(configRepository);
        ConfigService configService = new ConfigServiceImpl(configDao);
        configService.initFromDb();
        
        mvc = MockMvcBuilders.standaloneSetup(new WfsApiController(wfsRepository, wfsService, configService)).build();
        
        wfsRepository.deleteAll();        
    }
    
    @Test
    public void getWmsEntriesTest() throws Exception {
        
        mvc.perform(get("/api/wfs"))
                .andExpect(status().isOk());
    }
    
    @Test
    public void getWfsCapabilitiesTest() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest");
        wfsEntity.setDescription("getWmsCapabilitiesTest"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity);
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName()))
                .andExpect(status().isOk());         
    }
    
    @Test
    public void getWfsCapabilitiesTest2() throws Exception {
                
        mvc.perform(get("/api/wfs/unknown"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No WFS entity for given name."));         
    } 
    
    @Test
    public void getWfsCapabilitiesTest3() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("wrongurl");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity);
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName()))
                .andExpect(status().isServiceUnavailable());        
    }  
    
    @Test
    public void getWfsFeatureTest() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature"))
                .andExpect(status().isOk());         
    }
    
    @Test
    public void getWfsFeatureTest2() throws Exception {        
        
        mvc.perform(get("/api/wfs/wrongname/namespace/feature"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No WFS entity for given name."));        
    }   
    
    @Test
    public void getWfsFeatureTest3() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/wrongnamespace/feature"))
                .andExpect(status().isServiceUnavailable());         
    }  
    
    @Test
    public void getWfsFeatureTest4() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/wrongname"))
                .andExpect(status().isServiceUnavailable());         
    }  
    
    @Test
    public void getWfsFeatureTest5() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace"))
                .andExpect(status().isNotFound());         
    } 
    
    @Test
    public void getWfsFeatureTest6() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature?maxFeatures=asd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("NumberFormatException"));       
    }  
    
    @Test
    public void getWfsFeatureTest7() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature?maxFeatures=1001"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param maxFeatures has to be les or equal than 1000"));       
    }  
    
    @Test
    public void getWfsFeatureTest8() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature?maxFeatures=1000"))
                .andExpect(status().isOk());       
    } 
    
    @Test
    public void getWfsFeatureTest9() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature?circle=10,10,100"))
                .andExpect(status().isOk());
    }     
    
    @Test
    public void getWfsFeatureTest10() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature?circle=asdasd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param circle has wrong format."));    
    }  
    
    @Test
    public void getWfsFeatureTest11() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature?circle=10,10,10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Radius must be greater or equal than 100"));    
    } 
    
    @Test
    public void getWfsFeatureTest12() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature?circle=10,10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param circle has wrong format."));    
    }  
    
    @Test
    public void getWfsFeatureTest13() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature?bbox=10,10,10,10"))
                .andExpect(status().isOk());    
    } 
    
    @Test
    public void getWfsFeatureTest14() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature?bbox=asdasd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param bbox has wrong format."));    
    } 
    
    @Test
    public void getWfsFeatureTest15() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature?bbox=10,10,10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param bbox has wrong format."));    
    }   
    
    @Test
    public void getWfsFeatureTest16() throws Exception {
        
        WfsEntity wfsEntity = new WfsEntity();
        wfsEntity.setName("getWmsCapabilitiesTest3");
        wfsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wfsEntity.setCapabilitiesUrl("getWmsCapabilitiesTest3");
        wfsEntity.setFeaturesUrl("getWmsCapabilitiesTest3");
        WfsEntity resultWfsEntity = wfsRepository.save(wfsEntity); 
        
        mvc.perform(get("/api/wfs/" + resultWfsEntity.getName() + "/namespace/feature?bbox=10,10,asdasd,10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("NumberFormatException"));    
    }      
}
