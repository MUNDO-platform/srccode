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

package pl.orange.labs.mundo.test;

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
import pl.orange.labs.db.entities.ResourceEntity;
import pl.orange.labs.db.dao.ResourceRepository;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@ContextConfiguration(classes={JpaConfig.class, PersistenceTestConfig.class})
@ActiveProfiles("test")
@Transactional
@TransactionConfiguration(defaultRollback=true)
@RunWith(SpringJUnit4ClassRunner.class)
public class ResourceTest 
{
    private final Logger logger = Logger.getLogger(ResourceTest.class);
    
    @Autowired 
    ResourceRepository resourceRepository;
    
    @Before
    public void setUp() throws Exception 
    {
        resourceRepository.deleteAll();
        
        ResourceEntity resource = new ResourceEntity();
        resource.setName("resource1");
        resource.setDescription("resource1");
        
        resourceRepository.save(resource);
    }
    
    @Test
    public void findAllTest()
    {
        List<ResourceEntity> list = resourceRepository.findAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size()==1);
    } 
    
    @Test
    public void findByNameTest()
    {
        ResourceEntity resource = resourceRepository.findByName("resource1");
        Assert.assertNotNull(resource);
        assertThat(resource, instanceOf(ResourceEntity.class));
    }
    
    @Test
    public void deleteTest()
    {
        ResourceEntity resource = resourceRepository.findByName("resource1");
        resourceRepository.delete(resource);
    }     
}
