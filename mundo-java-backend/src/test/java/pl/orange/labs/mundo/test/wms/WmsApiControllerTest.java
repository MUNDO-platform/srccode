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
package pl.orange.labs.mundo.test.wms;

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
import pl.orange.labs.mundo.plugins.wms.controller.WmsApiController;
import pl.orange.labs.mundo.plugins.wms.dao.WmsEntity;
import pl.orange.labs.mundo.plugins.wms.dao.WmsRepository;
import pl.orange.labs.mundo.plugins.wms.dao.WmsService;
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
public class WmsApiControllerTest {
    
    private MockMvc mvc;
    @Autowired
    private WmsRepository wmsRepository;  
    @Autowired
    private ConfigRepository configRepository;
    
    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));   
    
    private final HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(); 
    
    protected String json(Object o) throws IOException 
    {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    } 
    
    @Before
    public void setUp() throws Exception 
    {
        WmsService wmsService = new WmsServiceMockImpl();
        
        ConfigDao configDao = new ConfigDaoImpl(configRepository);
        ConfigService configService = new ConfigServiceImpl(configDao);
        configService.initFromDb();
        
        mvc = MockMvcBuilders.standaloneSetup(new WmsApiController(wmsRepository, wmsService, configService)).build();
        
        wmsRepository.deleteAll();        
    }
    
    @Test
    public void getWmsEntriesTest() throws Exception 
    {
        mvc.perform(get("/api/wms"))
                .andExpect(status().isOk());
    } 
    
    @Test
    public void getWmsCapabilitiesTest() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsCapabilitiesTest");
        wmsEntity.setDescription("getWmsCapabilitiesTest"); 
        wmsEntity.setUrl("getWmsCapabilitiesTest");
        wmsEntity.setSrs("getWmsCapabilitiesTest");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity);
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName()))
                .andExpect(status().isOk());        
    }    
    
    @Test
    public void getWmsCapabilitiesTest2() throws Exception
    {
        mvc.perform(get("/api/wms/unknown"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No WMS entity for given name."));        
    }
    
    @Test
    public void getWmsCapabilitiesTest3() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsCapabilitiesTest3");
        wmsEntity.setDescription("getWmsCapabilitiesTest3"); 
        wmsEntity.setSrs("getWmsCapabilitiesTest3");
        wmsEntity.setUrl("wrongurl");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity);
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName()))
                .andExpect(status().isServiceUnavailable());        
    } 
    
    @Test
    public void getWmsMapTest() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("getWmsMapTest");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap?layers=test"))
                .andExpect(status().isOk());         
    }
    
    @Test
    public void getWmsMapTest2() throws Exception
    {
        mvc.perform(get("/api/wms/unknown/getmap?layers=test"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No WMS entity for given name."));         
    }
    
    @Test
    public void getWmsMapTest3() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("wrongurl");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap?layers=test"))
                .andExpect(status().isServiceUnavailable());         
    } 
    
    @Test
    public void getWmsMapTest4() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("getWmsMapTest");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap"))
                .andExpect(status().isBadRequest());         
    } 
    
    @Test
    public void getWmsMapTest5() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("getWmsMapTest");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap?layers=test&center=52.240616,20.998012"))
                .andExpect(status().isOk());         
    } 
    
    @Test
    public void getWmsMapTest6() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("getWmsMapTest");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap?layers=test&center=badvalue"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param center has wrong format."));         
    } 
    
    @Test
    public void getWmsMapTest7() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("getWmsMapTest");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap?layers=test&center=bad,value"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("NumberFormatException"));         
    } 
    
    @Test
    public void getWmsMapTest8() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("getWmsMapTest");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap?layers=test&center=32.2323,34.232&size=500x800"))
                .andExpect(status().isOk());         
    } 
    
    @Test
    public void getWmsMapTest9() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("getWmsMapTest");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap?layers=test&center=32.2323,34.232&size=badvalue"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param size has wrong format."));         
    }
    

    @Test
    public void getWmsMapTest10() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("getWmsMapTest");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap?layers=test&zoom=10"))
                .andExpect(status().isOk());         
    }    
    
    @Test
    public void getWmsMapTest11() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("getWmsMapTest");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap?layers=test&zoom=abc"))                
                .andExpect(status().isBadRequest())
                .andExpect(content().string("NumberFormatException"));         
    } 
    
    @Test
    public void getWmsMapTest12() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("getWmsMapTest");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap?layers=test&format=png"))                
                .andExpect(status().isOk());         
    } 
    
    @Test
    public void getWmsMapTest13() throws Exception
    {
        WmsEntity wmsEntity = new WmsEntity();
        wmsEntity.setName("getWmsMapTest");
        wmsEntity.setDescription("getWmsMapTest"); 
        wmsEntity.setUrl("getWmsMapTest");
        wmsEntity.setSrs("EPSG:2178");
        WmsEntity resultWmsEntity = wmsRepository.save(wmsEntity); 
        
        mvc.perform(get("/api/wms/" + resultWmsEntity.getName() + "/getmap?layers=test&format=abc"))                
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param format is invalid."));         
    }   
    
    
}
