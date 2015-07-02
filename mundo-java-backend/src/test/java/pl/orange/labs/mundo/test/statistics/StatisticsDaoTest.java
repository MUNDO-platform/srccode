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
package pl.orange.labs.mundo.test.statistics;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import pl.orange.labs.config.JpaConfig;
import pl.orange.labs.config.PersistenceTestConfig;
import pl.orange.labs.db.dao.StatisticsRepository;
import pl.orange.labs.db.entities.Statistics;

/**
 *
 * @author Tomasz Janisiewicz <Tomasz.Janisiewicz@orange.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {JpaConfig.class, PersistenceTestConfig.class})
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class StatisticsDaoTest {
    
    @Autowired 
    private StatisticsRepository statisticsRepository;
    
    @Before
    public void setUp() throws Exception {

        statisticsRepository.deleteAll();
    }    
    
    @Test
    public void findAllTest() {
            
        Statistics statistics = new Statistics();
        statistics.setPath("findAllTest");
        statistics.setCounter(Long.valueOf(1));
        statisticsRepository.save(statistics);
        
        List<Statistics> list = statisticsRepository.findAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() == 1);
    } 
    
    @Test
    public void findByPathTest() {
            
        Statistics statistics = new Statistics();
        statistics.setPath("findByPathTest");
        statistics.setCounter(Long.valueOf(1));
        statisticsRepository.save(statistics);
        
        Statistics result = statisticsRepository.findByPath("findByPathTest");
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getPath().equals("findByPathTest"));
    } 
    
    @Test
    public void saveTest() {
            
        Statistics statistics = new Statistics();
        statistics.setPath("saveTest");
        statistics.setCounter(Long.valueOf(1));
        statisticsRepository.save(statistics);
    } 
    
    @Test (expected = DataIntegrityViolationException.class)
    public void saveTest2() {
            
        Statistics statistics = new Statistics();
        statistics.setCounter(Long.valueOf(1));
        statisticsRepository.save(statistics);
    }
    
    @Test (expected = DataIntegrityViolationException.class)
    public void saveTest3() {
            
        Statistics statistics = new Statistics();
        statistics.setPath("saveTest");
        statisticsRepository.save(statistics);
    }     
}
