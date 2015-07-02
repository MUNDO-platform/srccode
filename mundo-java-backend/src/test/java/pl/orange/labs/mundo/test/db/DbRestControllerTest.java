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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.orange.labs.config.JpaConfig;
import pl.orange.labs.config.PersistenceTestConfig;
import pl.orange.labs.mundo.plugins.db.controller.DbRestController;
import pl.orange.labs.mundo.plugins.db.dao.ColumnEntity;
import pl.orange.labs.mundo.plugins.db.dao.ColumnRepository;
import pl.orange.labs.mundo.plugins.db.dao.DbEntity;
import pl.orange.labs.mundo.plugins.db.dao.DbRepository;
import pl.orange.labs.mundo.plugins.db.dao.TableEntity;
import pl.orange.labs.mundo.plugins.db.dao.TableRepository;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {JpaConfig.class, PersistenceTestConfig.class})
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class DbRestControllerTest 
{
    private final Logger logger = Logger.getLogger(DbRestControllerTest.class); 
    
    private MockMvc mvc;
    @Autowired 
    private DbRepository dbRepository; 
    @Autowired 
    private TableRepository tableRepository;
    @Autowired 
    private ColumnRepository columnRepository;    
    
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));   
    
    private HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(); 
    
    protected String json(Object o) throws IOException 
    {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }  
    
    @Before
    public void setUp() throws Exception 
    {
        mvc = MockMvcBuilders.standaloneSetup(new DbRestController(dbRepository,tableRepository,columnRepository)).build();      
        
        dbRepository.deleteAll();
        tableRepository.deleteAll();
        columnRepository.deleteAll();        
    }
    
    @Test
    public void getDbsTest() throws Exception 
    {
        mvc.perform(get("/gui/user/db"))
                .andExpect(status().isOk());
    } 
    
    @Test
    public void createDbTest() throws Exception
    {
        DbEntity db = new DbEntity();
        db.setName("createDbTest");
        db.setDescription("createDbTest");
        db.setJndiName("createDbTest");
        String dbJson = json(db);        
        
        mvc.perform(post("/gui/admin/db")
        .contentType(contentType)
        .content(dbJson))
                .andExpect(status()
                        .isCreated());        
    }
    
    @Test
    public void createDbTest2() throws Exception
    {
        DbEntity db = new DbEntity();
        db.setName("toolongname1111111111111111111111111111111111111111111111111111111111111");
        db.setDescription("createDbTest2");
        db.setJndiName("createDbTest2");
        String dbJson = json(db);        
        
        mvc.perform(post("/gui/admin/db")
        .contentType(contentType)
        .content(dbJson))
                .andExpect(status().is4xxClientError());        
    }
    
    @Test
    public void createDbTest3() throws Exception
    {
        DbEntity db = new DbEntity();
        db.setName("createDbTest3");
        db.setDescription("tooLongDescription222222222222222222222222222222222222222222222222");
        db.setJndiName("createDbTest3");
        String dbJson = json(db);        
        
        mvc.perform(post("/gui/admin/db")
        .contentType(contentType)
        .content(dbJson))
                .andExpect(status().is4xxClientError());        
    } 
    
    @Test
    public void createDbTest4() throws Exception
    {
        DbEntity db = new DbEntity();
        db.setDescription("createDbTest4");
        db.setJndiName("createDbTest4");
        String dbJson = json(db);        
        
        mvc.perform(post("/gui/admin/db")
        .contentType(contentType)
        .content(dbJson))
                .andExpect(status().is4xxClientError());        
    }
    
    @Test
    public void createDbTest5() throws Exception
    {
        DbEntity db = new DbEntity();
        db.setName("createDbTest5");
        db.setDescription("createDbTest5");
        String dbJson = json(db);        
        
        mvc.perform(post("/gui/admin/db")
        .contentType(contentType)
        .content(dbJson))
                .andExpect(status().is4xxClientError());        
    }   
    
    @Test
    public void createDbTest6() throws Exception
    {            
        mvc.perform(post("/gui/admin/db")
        .contentType(contentType))
                .andExpect(status().isBadRequest());        
    } 
    
    @Test
    public void removeDbTest() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("removeDbTest");
        dbEntity.setDescription("removeDbTest");
        dbEntity.setJndiName("removeDbTest");
        DbEntity result = dbRepository.save(dbEntity);
        
        mvc.perform(delete("/gui/admin/db/"+result.getId()))
                .andExpect(status().isOk());        
    }    
    
    @Test
    public void removeDbTest2() throws Exception
    {
        mvc.perform(delete("/gui/admin/db/1"))
                .andExpect(status().isNotFound());        
    }
    
    @Test
    public void removeDbTest3() throws Exception
    {
        mvc.perform(delete("/gui/admin/db/"))
                .andExpect(status().isMethodNotAllowed());        
    }
    
    @Test
    public void removeDbTest4() throws Exception
    {
        mvc.perform(delete("/gui/user/db/"))
                .andExpect(status().isMethodNotAllowed());        
    }  
    
    @Test
    public void getTablesTest() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getTablesTest");
        dbEntity.setDescription("getTablesTest");
        dbEntity.setJndiName("getTablesTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        mvc.perform(get("/gui/user/db/"+resultDbEntity.getName()))
                .andExpect(status().isOk());
    }
    
    @Test
    public void getTablesTest2() throws Exception
    {
        mvc.perform(get("/gui/user/db/unknown"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void createTableTest() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createTableTest");
        dbEntity.setDescription("createTableTest");
        dbEntity.setJndiName("createTableTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("createTableTest");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("createTableTest");
        tableEntity.setType("createTableTest");
        String tableJson = json(tableEntity);        
        
        mvc.perform(post("/gui/admin/db/"+resultDbEntity.getName())
        .contentType(contentType)
        .content(tableJson))
                .andExpect(status()
                        .isCreated());        
    }
    
    @Test
    public void createTableTest2() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createTableTest2");
        dbEntity.setDescription("createTableTest2");
        dbEntity.setJndiName("createTableTest2");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("createTableTest2");
        tableEntity.setType("createTableTest2");
        String tableJson = json(tableEntity);        
        
        mvc.perform(post("/gui/admin/db/"+resultDbEntity.getName())
        .contentType(contentType)
        .content(tableJson))
                .andExpect(status()
                        .isBadRequest());        
    }  
    
    @Test
    public void createTableTest3() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createTableTest");
        dbEntity.setDescription("createTableTest");
        dbEntity.setJndiName("createTableTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("createTableTest");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("createTableTest");
        String tableJson = json(tableEntity);        
        
        mvc.perform(post("/gui/admin/db/"+resultDbEntity.getName())
        .contentType(contentType)
        .content(tableJson))
                .andExpect(status()
                        .isBadRequest());        
    }  
    
    @Test
    public void createTableTest4() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createTableTest");
        dbEntity.setDescription("createTableTest");
        dbEntity.setJndiName("createTableTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("createTableTest");
        tableEntity.setParams("createTableTest");
        tableEntity.setType("createTableTest");
        String tableJson = json(tableEntity);        
        
        mvc.perform(post("/gui/admin/db/"+resultDbEntity.getName())
        .contentType(contentType)
        .content(tableJson))
                .andExpect(status()
                        .isBadRequest());        
    }   
    
    @Test
    public void createTableTest5() throws Exception
    {       
        TableEntity tableEntity = new TableEntity();
        tableEntity.setDbId(Long.valueOf(1));
        tableEntity.setName("createTableTest5");
        tableEntity.setParams("createTableTest5");
        tableEntity.setType("createTableTest5");
        String tableJson = json(tableEntity);        
        
        mvc.perform(post("/gui/admin/db/unknown")
        .contentType(contentType)
        .content(tableJson))
                .andExpect(status()
                        .isBadRequest());        
    } 
    
    @Test
    public void createTableTest6() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createTableTest6");
        dbEntity.setDescription("createTableTest6");
        dbEntity.setJndiName("createTableTest6");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);               
        
        mvc.perform(post("/gui/admin/db/"+resultDbEntity.getName())
        .contentType(contentType))
                .andExpect(status()
                        .isBadRequest());        
    } 
    
    @Test
    public void createTableTest7() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createTableTest7");
        dbEntity.setDescription("createTableTest7");
        dbEntity.setJndiName("createTableTest7");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("createTableTest7");
        tableEntity.setDbId(Long.valueOf("1"));
        tableEntity.setParams("createTableTest7");
        tableEntity.setType("createTableTest7");
        String tableJson = json(tableEntity);        
        
        mvc.perform(post("/gui/admin/db/"+resultDbEntity.getName())
        .contentType(contentType)
        .content(tableJson))
                .andExpect(status()
                        .isBadRequest());        
    }  
    
    @Test
    public void removeTableTest() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("removeTableTest");
        dbEntity.setDescription("removeTableTest");
        dbEntity.setJndiName("removeTableTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("removeTableTest");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("removeTableTest");
        tableEntity.setType("removeTableTest"); 
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        mvc.perform(delete("/gui/admin/db/"+resultDbEntity.getName()+"/"+resultTableEntity.getId()))
                .andExpect(status().isOk());        
    }  
    
    @Test
    public void removeTableTest2() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("removeTableTest2");
        dbEntity.setDescription("removeTableTest2");
        dbEntity.setJndiName("removeTableTest2");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("removeTableTest2");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("removeTableTest2");
        tableEntity.setType("removeTableTest2"); 
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        mvc.perform(delete("/gui/admin/db/"+resultDbEntity.getName()+"/"+100))
                .andExpect(status().isNotFound());        
    }
    
    @Test
    public void removeTableTest3() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("removeTableTest2");
        dbEntity.setDescription("removeTableTest2");
        dbEntity.setJndiName("removeTableTest2");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("removeTableTest2");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("removeTableTest2");
        tableEntity.setType("removeTableTest2"); 
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        mvc.perform(delete("/gui/admin/db/unknown/"+resultTableEntity.getId()))
                .andExpect(status().isBadRequest());        
    }
    
    @Test
    public void getColumnsTest() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getColumnsTest");
        dbEntity.setDescription("getColumnsTest");
        dbEntity.setJndiName("getColumnsTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getColumnsTest");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("getColumnsTest");
        tableEntity.setType("getColumnsTest"); 
        TableEntity resultTableEntity = tableRepository.save(tableEntity);        
        
        mvc.perform(get("/gui/user/db/"+resultDbEntity.getName()+"/"+resultTableEntity.getName()))
                .andExpect(status().isOk());
    }  
    
    @Test
    public void getColumnsTest2() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getColumnsTest2");
        dbEntity.setDescription("getColumnsTest2");
        dbEntity.setJndiName("getColumnsTest2");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getColumnsTest2");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("getColumnsTest2");
        tableEntity.setType("getColumnsTest2"); 
        TableEntity resultTableEntity = tableRepository.save(tableEntity);        
        
        mvc.perform(get("/gui/user/db/unknown/"+resultTableEntity.getName()))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void getColumnsTest3() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("getColumnsTest3");
        dbEntity.setDescription("getColumnsTest3");
        dbEntity.setJndiName("getColumnsTest3");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("getColumnsTest3");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("getColumnsTest3");
        tableEntity.setType("getColumnsTest3"); 
        TableEntity resultTableEntity = tableRepository.save(tableEntity);        
        
        mvc.perform(get("/gui/user/db/"+resultDbEntity.getName()+"/unknown"))
                .andExpect(status().isBadRequest());
    }   
    
    @Test
    public void createColumnTest() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createColumnTest");
        dbEntity.setDescription("createColumnTest");
        dbEntity.setJndiName("createColumnTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("createColumnTest");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("createColumnTest");
        tableEntity.setType("createColumnTest");
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("createColumnTest");
        columnEntity.setTabId(resultTableEntity.getId());        
        String columnJson = json(columnEntity);        
        
        mvc.perform(post("/gui/admin/db/"+resultDbEntity.getName()+"/"+resultTableEntity.getName())
        .contentType(contentType)
        .content(columnJson))
                .andExpect(status()
                        .isCreated());        
    } 
    
    @Test
    public void createColumnTest2() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createColumnTest2");
        dbEntity.setDescription("createColumnTest2");
        dbEntity.setJndiName("createColumnTest2");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("createColumnTest2");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("createColumnTest2");
        tableEntity.setType("createColumnTest2");
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setTabId(resultTableEntity.getId());        
        String columnJson = json(columnEntity);        
        
        mvc.perform(post("/gui/admin/db/"+resultDbEntity.getName()+"/"+resultTableEntity.getName())
        .contentType(contentType)
        .content(columnJson))
                .andExpect(status()
                        .isBadRequest());        
    } 
    
    @Test
    public void createColumnTest3() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createColumnTest3");
        dbEntity.setDescription("createColumnTest3");
        dbEntity.setJndiName("createColumnTest3");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("createColumnTest3");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("createColumnTest3");
        tableEntity.setType("createColumnTest3");
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("createColumnTest3");        
        String columnJson = json(columnEntity);        
        
        mvc.perform(post("/gui/admin/db/"+resultDbEntity.getName()+"/"+resultTableEntity.getName())
        .contentType(contentType)
        .content(columnJson))
                .andExpect(status()
                        .isBadRequest());        
    } 
    
    @Test
    public void createColumnTest4() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createColumnTest4");
        dbEntity.setDescription("createColumnTest4");
        dbEntity.setJndiName("createColumnTest4");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("createColumnTest4");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("createColumnTest4");
        tableEntity.setType("createColumnTest4");
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
       
        
        mvc.perform(post("/gui/admin/db/"+resultDbEntity.getName()+"/"+resultTableEntity.getName())
        .contentType(contentType))
                .andExpect(status()
                        .isBadRequest());        
    } 
    
    @Test
    public void createColumnTest5() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createColumnTest5");
        dbEntity.setDescription("createColumnTest5");
        dbEntity.setJndiName("createColumnTest5");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("createColumnTest5");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("createColumnTest5");
        tableEntity.setType("createColumnTest5");
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("createColumnTest5");
        columnEntity.setTabId(resultTableEntity.getId());        
        String columnJson = json(columnEntity);        
        
        mvc.perform(post("/gui/admin/db/unknown/"+resultTableEntity.getName())
        .contentType(contentType)
        .content(columnJson))
                .andExpect(status()
                        .isBadRequest());        
    }
    
    @Test
    public void createColumnTest6() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("createColumnTest6");
        dbEntity.setDescription("createColumnTest6");
        dbEntity.setJndiName("createColumnTest6");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);        
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("createColumnTest6");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("createColumnTest6");
        tableEntity.setType("createColumnTest6");
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("createColumnTest6");
        columnEntity.setTabId(resultTableEntity.getId());        
        String columnJson = json(columnEntity);        
        
        mvc.perform(post("/gui/admin/db/"+resultDbEntity.getName()+"/"+100)
        .contentType(contentType)
        .content(columnJson))
                .andExpect(status()
                        .isBadRequest());        
    } 
    
    @Test
    public void removeColumnTest() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("removeColumnTest");
        dbEntity.setDescription("removeColumnTest");
        dbEntity.setJndiName("removeColumnTest");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("removeColumnTest");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("removeColumnTest");
        tableEntity.setType("removeColumnTest"); 
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("removeColumnTest");
        columnEntity.setTabId(resultTableEntity.getId());  
        ColumnEntity resultColumnEntity = columnRepository.save(columnEntity);
        
        mvc.perform(delete("/gui/admin/db/"+resultDbEntity.getName()+"/"+resultTableEntity.getName()+"/"+resultColumnEntity.getId()))
                .andExpect(status().isOk());        
    } 
    
    @Test
    public void removeColumnTest2() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("removeColumnTest2");
        dbEntity.setDescription("removeColumnTest2");
        dbEntity.setJndiName("removeColumnTest2");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("removeColumnTest2");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("removeColumnTest2");
        tableEntity.setType("removeColumnTest2"); 
        TableEntity resultTableEntity = tableRepository.save(tableEntity);

        mvc.perform(delete("/gui/admin/db/"+resultDbEntity.getName()+"/"+resultTableEntity.getName()+"/"+1))
                .andExpect(status().isNotFound());        
    }
    
    @Test
    public void removeColumnTest3() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("removeColumnTest3");
        dbEntity.setDescription("removeColumnTest3");
        dbEntity.setJndiName("removeColumnTest3");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("removeColumnTest3");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("removeColumnTest3");
        tableEntity.setType("removeColumnTest3"); 
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("removeColumnTest3");
        columnEntity.setTabId(resultTableEntity.getId());  
        ColumnEntity resultColumnEntity = columnRepository.save(columnEntity);
        
        mvc.perform(delete("/gui/admin/db/unknown/"+resultTableEntity.getName()+"/"+resultColumnEntity.getId()))
                .andExpect(status().isBadRequest());        
    } 
    
    @Test
    public void removeColumnTest4() throws Exception
    {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName("removeColumnTest4");
        dbEntity.setDescription("removeColumnTest4");
        dbEntity.setJndiName("removeColumnTest4");
        DbEntity resultDbEntity = dbRepository.save(dbEntity);
        
        TableEntity tableEntity = new TableEntity();
        tableEntity.setName("removeColumnTest4");
        tableEntity.setDbId(resultDbEntity.getId());
        tableEntity.setParams("removeColumnTest4");
        tableEntity.setType("removeColumnTest4"); 
        TableEntity resultTableEntity = tableRepository.save(tableEntity);
        
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setName("removeColumnTest4");
        columnEntity.setTabId(resultTableEntity.getId());  
        ColumnEntity resultColumnEntity = columnRepository.save(columnEntity);
        
        mvc.perform(delete("/gui/admin/db/"+resultDbEntity.getName()+"/unknown/"+resultColumnEntity.getId()))
                .andExpect(status().isBadRequest());        
    }    
}
