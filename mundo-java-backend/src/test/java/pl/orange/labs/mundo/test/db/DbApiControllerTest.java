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

package pl.orange.labs.mundo.test.db;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
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
import pl.orange.labs.mundo.plugins.db.controller.DbApiController;
import pl.orange.labs.mundo.plugins.db.dao.ColumnEntity;
import pl.orange.labs.mundo.plugins.db.dao.ColumnRepository;
import pl.orange.labs.mundo.plugins.db.dao.DbEntity;
import pl.orange.labs.mundo.plugins.db.dao.DbRepository;
import pl.orange.labs.mundo.plugins.db.dao.TableEntity;
import pl.orange.labs.mundo.plugins.db.dao.TableRepository;
import pl.orange.labs.mundo.plugins.db.service.DbService;
import pl.orange.labs.mundo.service.ConfigService;
import pl.orange.labs.mundo.service.ConfigServiceImpl;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {JpaConfig.class, PersistenceTestConfig.class})
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class DbApiControllerTest {
    
    private final Logger logger = Logger.getLogger(DbApiControllerTest.class); 
    
    private MockMvc mvc;
    @Autowired 
    private DbRepository dbRepository; 
    @Autowired 
    private TableRepository tableRepository;
    @Autowired 
    private ColumnRepository columnRepository; 
    @Autowired
    private ConfigRepository configRepository;  
    @Autowired
    private HttpServletRequest httpServletRequest;
    
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
        
        DbService dbService = new DbServiceMockImpl();
        
        ConfigDao configDao = new ConfigDaoImpl(configRepository);
        ConfigService configService = new ConfigServiceImpl(configDao);
        configService.initFromDb();
        
        mvc = MockMvcBuilders.standaloneSetup(
                new DbApiController(dbRepository, tableRepository, columnRepository, dbService, configService, httpServletRequest)).build();                   
        
        dbRepository.deleteAll();
        tableRepository.deleteAll();
        columnRepository.deleteAll();            
    }   
    
    @Test
    public void getDbListTest() throws Exception  {
        
        mvc.perform(get("/api/db"))
                .andExpect(status().isOk());
    } 
    
    @Test
    public void getTableListTest() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getTablesTest");
        dbEntity.setDescription("getTablesTest");
        dbEntity.setJndiName("getTablesTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName()))
                .andExpect(status().isOk());        
    }
    
    @Test
    public void getTableListTest2() throws Exception {        
        
        mvc.perform(get("/api/db/unknown"))
                .andExpect(status().isBadRequest());        
    } 
    
    @Test
    public void getDataTest() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("table");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/" + resultTableEntity.getName()))
                .andExpect(status().isOk());          
                //.andExpect(status().isBadRequest())
                //.andExpect(content().string("Db not found."));                  
    }  
    
    @Test
    public void getDataTest2() throws Exception {               
        
        mvc.perform(get("/api/db/unknowndb/unknowntable"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Db not found."));    
    } 
    
    @Test
    public void getDataTest3() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/unknowntable"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Table not found."));       
    }
    
    @Test
    public void getDataTest4() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("table");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity);

        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/" + resultTableEntity.getName()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Column collection is empty."));           
    }     
    
    @Test
    public void getDataTest5() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("wrongdb");
        dbEntity.setDescription("getDataTest4");
        dbEntity.setJndiName("getDataTest4");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest4");
        tableEntity.setType("table");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity); 
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);        
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/" + resultTableEntity.getName()))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string("Service unavailable"));         
    }
    
    @Test
    public void getDataTest6() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("table");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity);        
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);        
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/" + resultTableEntity.getName() + "?page=asd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("NumberFormatException"));       
    }  
    
    @Test
    public void getDataTest7() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("table");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity); 
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);        
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/"
                + resultTableEntity.getName() + "?page=0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param page has to be positive integer."));       
    } 
    
    @Test
    public void getDataTest8() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("table");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity); 
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);        
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/"
                + resultTableEntity.getName() + "?size=asd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("NumberFormatException"));       
    } 
    
    @Test
    public void getDataTest9() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("table");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity); 
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);        
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/"
                + resultTableEntity.getName() + "?size=1001"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param size has to be les or equal than 1000"));       
    }  
    
    @Test
    public void getDataTest10() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("table");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity);  
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);        
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/"
                + resultTableEntity.getName() + "?size=0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param size has to be positive integer."));       
    } 
    
    @Test
    public void getDataTest11() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("table");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity); 
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);        
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/"
                + resultTableEntity.getName() + "?size=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Param size has to be positive integer."));       
    }     
     
    @Test
    public void getDataTest12() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("table");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity); 
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);        
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/"
                + resultTableEntity.getName() + "?orderBy=wrongcolumn"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string("Service unavailable"));
                      
    }
    
    @Test
    public void getDataTest13() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("procedure");
        tableEntity.setParams("call procedure($param$)");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/" + resultTableEntity.getName() + "?param=1"))
                .andExpect(status().isOk());                
    }  
    
    @Test
    public void getDataTest14() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("procedure");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/" + resultTableEntity.getName() + "?param=1"))
              .andExpect(status().isServiceUnavailable())
                .andExpect(content().string("Embdeded procedure definition does not contain any params."));            
    }
    
    @Test
    public void getDataTest15() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("procedure");
        tableEntity.setParams("call procedure($param$)");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/" + resultTableEntity.getName()))
              .andExpect(status().isBadRequest())
                .andExpect(content().string("Parameter param is requested."));               
    } 
    
    @Test
    public void getDataTest16() throws Exception {
        
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getDataTest");
        dbEntity.setDescription("getDataTest");
        dbEntity.setJndiName("getDataTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity); 
        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getDataTest");
        tableEntity.setType("procedure");
        tableEntity.setParams("call procedure($alwaysEquals1$)");
        tableEntity.setDbId(resultDbEntity.getId());
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("getDataTest");
        columnEntity.setTabId(resultTableEntity.getId());
        columnRepository.save(columnEntity);
        
        mvc.perform(get("/api/db/" + resultDbEntity.getName() + "/" + resultTableEntity.getName() + "?alwaysEquals1=2"))
              .andExpect(content().string("Parameter alwaysEquals1 should be equals to 1"))            
                .andExpect(status().isServiceUnavailable());
                
    }     
}
