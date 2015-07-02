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

import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import pl.orange.labs.config.JpaConfig;
import pl.orange.labs.config.PersistenceTestConfig;
import pl.orange.labs.mundo.plugins.db.dao.TableEntity;
import pl.orange.labs.mundo.plugins.db.exception.CheckParamException;
import pl.orange.labs.mundo.plugins.db.exception.QueryBuilderException;
import pl.orange.labs.mundo.plugins.db.query.ProcedureQueryBuilderImpl;
import pl.orange.labs.mundo.plugins.db.query.QueryBuilder;

/**
 *
 * @author Tomasz Janisiewicz <Tomasz.Janisiewicz@orange.com>
 */
@ContextConfiguration(classes = {JpaConfig.class, PersistenceTestConfig.class})
@ActiveProfiles("test")
@Transactional
@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProcedureQueryBuilderImplTest {
    
    @Test
    public void buildTest() throws Exception {
        
        QueryBuilder builder = new ProcedureQueryBuilderImpl();
        TableEntity tableEntity = new TableEntity();
        tableEntity.setParams("CALL pobierz_odjazdy_linii_z_przystanku(curdate(),'$busstopId$','$stake$','$busline$')");
        HashMap<String, String> params = new HashMap<>();
        params.put("busstopId", "1");
        params.put("stake", "01");
        params.put("busline", "512");
        
        String queryResult = builder.build(tableEntity, params, null, null);
        System.out.println(queryResult);
        
        Assert.assertNotNull(queryResult);
        Assert.assertTrue(queryResult.equals("CALL pobierz_odjazdy_linii_z_przystanku(curdate(),'1','01','512')"));
    } 
    
    @Test
    public void buildTest2() throws Exception {
        
        QueryBuilder builder = new ProcedureQueryBuilderImpl();
        TableEntity tableEntity = new TableEntity();
        //if parameter is not definied properly with '$$'
        tableEntity.setParams("CALL test_procedura('param')");
        HashMap<String, String> params = new HashMap<>();
        params.put("param", "1");
        
        String queryResult = builder.build(tableEntity, params, null, null);
        System.out.println(queryResult);
        
        Assert.assertNotNull(queryResult);
        Assert.assertTrue(queryResult.equals("CALL test_procedura('param')"));
    } 
    
    @Test(expected = CheckParamException.class)
    public void buildTest3() throws Exception {
        
        QueryBuilder builder = new ProcedureQueryBuilderImpl();
        TableEntity tableEntity = new TableEntity();
        tableEntity.setParams("CALL test_procedura('$param$')");
        //if user request does not contain requested parameters
        HashMap<String, String> params = new HashMap<>();
        
        String queryResult = builder.build(tableEntity, params, null, null);
        System.out.println(queryResult);
        
        Assert.assertNotNull(queryResult);
    }
    
    @Test(expected = QueryBuilderException.class)
    public void buildTest4() throws Exception {
        
        QueryBuilder builder = new ProcedureQueryBuilderImpl();
        TableEntity tableEntity = new TableEntity();        
        tableEntity.setParams("CALL test_procedura('param')");
        //if params is null
        builder.build(tableEntity, null, null, null);
    } 
    
    @Test(expected = QueryBuilderException.class)
    public void buildTest5() throws Exception {
        
        QueryBuilder builder = new ProcedureQueryBuilderImpl();
        HashMap<String, String> params = new HashMap<>();
        params.put("param", "1");   
        //if TableEntity is null
        builder.build(null, params, null, null);
    }
    
    @Test(expected = QueryBuilderException.class)
    public void buildTest6() throws Exception {
        
        QueryBuilder builder = new ProcedureQueryBuilderImpl();
        TableEntity tableEntity = new TableEntity();        
        HashMap<String, String> params = new HashMap<>();
        
        builder.build(tableEntity, params, null, null);
    }    
     
}
