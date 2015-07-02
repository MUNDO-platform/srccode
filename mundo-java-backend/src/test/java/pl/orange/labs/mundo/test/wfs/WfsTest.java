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
import pl.orange.labs.mundo.plugins.wfs.dao.WfsEntity;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsRepository;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@ContextConfiguration(classes={JpaConfig.class, PersistenceTestConfig.class})
@ActiveProfiles("test")
@Transactional
@TransactionConfiguration(defaultRollback=true)
@RunWith(SpringJUnit4ClassRunner.class)
public class WfsTest 
{
    private final Logger logger = Logger.getLogger(WfsTest.class);
    
    @Autowired 
    WfsRepository wfsRepository;
    
    @Before
    public void setUp() throws Exception 
    {
        wfsRepository.deleteAll();
        
        WfsEntity wfs = new WfsEntity();
        wfs.setName("wfs1");
        wfs.setCapabilitiesUrl("wfs1");
        wfs.setFeaturesUrl("wfs1");
        wfs.setDescription("wfs1");
        
        wfsRepository.save(wfs);
    } 
    
    @Test
    public void findAllTest()
    {
        List<WfsEntity> list = wfsRepository.findAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size()==1);
    } 
    
    @Test
    public void findByNameTest()
    {
        WfsEntity wfs = wfsRepository.findByName("wfs1");
        Assert.assertNotNull(wfs);
        assertThat(wfs, instanceOf(WfsEntity.class));
    }
    
    @Test
    public void deleteTest()
    {
        WfsEntity wfs = wfsRepository.findByName("wfs1");
        wfsRepository.delete(wfs);
    }    
}
