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

import java.util.List;
import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import pl.orange.labs.config.JpaConfig;
import pl.orange.labs.config.PersistenceTestConfig;
import pl.orange.labs.mundo.plugins.wms.dao.WmsEntity;
import pl.orange.labs.mundo.plugins.wms.dao.WmsRepository;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@ContextConfiguration(classes={JpaConfig.class, PersistenceTestConfig.class})
@ActiveProfiles("test")
@Transactional
@TransactionConfiguration(defaultRollback=true)
@RunWith(SpringJUnit4ClassRunner.class)
public class WmsTest 
{
    private final Logger logger = Logger.getLogger(WmsTest.class);
    
    @Autowired 
    WmsRepository wmsRepository;
    
    @Before
    public void setUp() throws Exception 
    {
        wmsRepository.deleteAll();
        
        WmsEntity wms = new WmsEntity();
        wms.setName("wms1");
        wms.setUrl("wms1");
        wms.setSrs("wms1");
        wms.setDescription("wms1");
        
        wmsRepository.save(wms);
    } 
    
    @Test
    public void findAllTest()
    {
        List<WmsEntity> list = wmsRepository.findAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size()==1);
    } 
    
    @Test
    public void findByNameTest()
    {
        WmsEntity wms = wmsRepository.findByName("wms1");
        Assert.assertNotNull(wms);
        assertThat(wms, instanceOf(WmsEntity.class));
    }
    
    @Test
    public void deleteTest()
    {
        WmsEntity wms = wmsRepository.findByName("wms1");
        wmsRepository.delete(wms);
    }     
}
