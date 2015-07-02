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

import java.util.List;
import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import pl.orange.labs.config.JpaConfig;
import pl.orange.labs.config.PersistenceTestConfig;
import pl.orange.labs.mundo.plugins.db.dao.ColumnRepository;
import pl.orange.labs.mundo.plugins.db.dao.DbEntity;
import pl.orange.labs.mundo.plugins.db.dao.DbRepository;
import pl.orange.labs.mundo.plugins.db.dao.TableRepository;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@ContextConfiguration(classes={JpaConfig.class, PersistenceTestConfig.class})
@ActiveProfiles("test")
@Transactional
@TransactionConfiguration(defaultRollback=true)
@RunWith(SpringJUnit4ClassRunner.class)
public class DbDaoTest 
{
    private final Logger logger = Logger.getLogger(DbDaoTest.class); 
    
    @Autowired 
    private DbRepository dbRepository; 
    @Autowired 
    private TableRepository tableRepository;
    @Autowired 
    private ColumnRepository columnRepository;
    
    @Before
    public void setUp() throws Exception 
    {

    } 
    
    @Test
    public void findAllTest()
    {
        dbRepository.deleteAll();        
        DbEntity db = new DbEntity();
        db.setName("dbName1");
        db.setDescription("dbDescription1");
        db.setJndiName("dbJndi1");
        dbRepository.save(db);
        
        List<DbEntity> list = dbRepository.findAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size()==1);
    } 
    
    @Test
    public void findAllTest2()
    {
        dbRepository.deleteAll();        
        DbEntity db = new DbEntity();
        db.setName("dbName1");
        db.setDescription("dbDescription1");
        db.setJndiName("dbJndi1");
        dbRepository.save(db);        
        
        List<DbEntity> list =  dbRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size()==1);
    }     
    
    @Test
    public void findByNameTest()
    {
        dbRepository.deleteAll();        
        DbEntity db = new DbEntity();
        db.setName("dbName1");
        db.setDescription("dbDescription1");
        db.setJndiName("dbJndi1");
        dbRepository.save(db);        
        
        DbEntity result = dbRepository.findByName("dbName1");
        Assert.assertNotNull(result);
        assertThat(result, instanceOf(DbEntity.class));        
    }
    
    @Test
    public void findByNameTest2()
    {
        dbRepository.deleteAll();                        
        DbEntity result = dbRepository.findByName("notfound");
        Assert.assertNull(result);
    }    
    
    @Test
    public void saveTest()
    {
        dbRepository.deleteAll();
        DbEntity db = new DbEntity();
        db.setName("saveTest");
        db.setDescription("saveTest");
        db.setJndiName("saveTest");
        DbEntity result = dbRepository.save(db);
        Assert.assertNotNull(result);
        
    }
    
    @Test(expected=DataIntegrityViolationException.class)
    public void saveTest2() throws DataIntegrityViolationException
    {
        dbRepository.deleteAll();
        DbEntity db = new DbEntity();
        db.setName("tolongname11111111111111111111111111111111111111111111111111111111111111111");
        db.setDescription("saveTest");
        db.setJndiName("saveTest");
        DbEntity result = dbRepository.save(db);
        Assert.assertNotNull(result);        
    }  
    
    @Test(expected=DataIntegrityViolationException.class)
    public void saveTest3() throws DataIntegrityViolationException
    {
        dbRepository.deleteAll();
        DbEntity db = new DbEntity();
        db.setName("saveTest3");
        db.setDescription("saveTest32222222222222222222222222222222222222222222222222222222222");
        db.setJndiName("saveTest3");
        DbEntity result = dbRepository.save(db);
        Assert.assertNotNull(result);        
    }     
    
    @Test(expected=DataIntegrityViolationException.class)
    public void saveTest4() throws DataIntegrityViolationException
    {
        dbRepository.deleteAll();
        DbEntity db = new DbEntity();
        db.setDescription("saveTest");
        db.setJndiName("saveTest");
        dbRepository.save(db);        
    }  
    
    @Test(expected=DataIntegrityViolationException.class)
    public void saveTest5() throws DataIntegrityViolationException
    {
        dbRepository.deleteAll();
        DbEntity db = new DbEntity();
        db.setName("saveTest5");
        db.setDescription("saveTest5");
        dbRepository.save(db);        
    }     
    
    @Test(expected=EmptyResultDataAccessException.class)
    public void deleteTest() throws EmptyResultDataAccessException 
    {
        dbRepository.deleteAll();
        dbRepository.delete(Long.valueOf("1"));        
    }     
}
