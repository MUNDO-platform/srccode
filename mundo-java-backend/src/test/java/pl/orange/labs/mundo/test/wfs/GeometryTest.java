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

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import pl.orange.labs.config.JpaConfig;
import pl.orange.labs.config.PersistenceTestConfig;
import pl.orange.labs.mundo.plugins.wfs.entity.Geometry;
import pl.orange.labs.mundo.plugins.wfs.entity.XmlWfsPoint;
import pl.orange.labs.mundo.plugins.wfs.exception.GeometryException;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@ContextConfiguration(classes = {JpaConfig.class, PersistenceTestConfig.class})
@ActiveProfiles("test")
@Transactional
@TransactionConfiguration(defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
public class GeometryTest {
    
    private final Logger logger = Logger.getLogger(GeometryTest.class);     
    
    @Test
    public void getCoordinatesListTest() throws GeometryException {
        
        String coordinates = "20.997167,52.249013 20.997173,52.248989 20.997182,52.248989 20.997203,52.248889";
        Geometry geometry = new Geometry();
        
        List<XmlWfsPoint> list = geometry.getCoordinatesList(coordinates);
        Assert.assertNotNull(list);
        assertThat(list, instanceOf(ArrayList.class));
        Assert.assertTrue(list.size() == 4);        
    }
    
    @Test(expected = GeometryException.class)
    public void getCoordinatesListTest2() throws GeometryException {
        
        String coordinates = "";
        Geometry geometry = new Geometry();
        
        List<XmlWfsPoint> list = geometry.getCoordinatesList(coordinates);
        Assert.assertNotNull(list);
        assertThat(list, instanceOf(ArrayList.class));      
    }   
    
    @Test(expected = GeometryException.class)
    public void getCoordinatesListTest3() throws GeometryException {
        
        Geometry geometry = new Geometry();
        
        List<XmlWfsPoint> list = geometry.getCoordinatesList(null);
        Assert.assertNotNull(list);
        assertThat(list, instanceOf(ArrayList.class));      
    }
    
    @Test(expected = GeometryException.class)
    public void getCoordinatesListTest4() throws GeometryException {
        
        String coordinates = "20.997167,asdasd 20.997173,52.248989 20.997182,52.248989 20.997203,52.248889";
        Geometry geometry = new Geometry();
        
        List<XmlWfsPoint> list = geometry.getCoordinatesList(coordinates);
        Assert.assertNotNull(list);
        assertThat(list, instanceOf(ArrayList.class));
        Assert.assertTrue(list.size() == 4);        
    }  
    
    @Test(expected = GeometryException.class)
    public void getCoordinatesListTest5() throws GeometryException {
        
        String coordinates = "20.997167,20.997167,20.997167 20.997173,52.248989 20.997182,52.248989 20.997203,52.248889";
        Geometry geometry = new Geometry();
        
        List<XmlWfsPoint> list = geometry.getCoordinatesList(coordinates);
        Assert.assertNotNull(list);
        assertThat(list, instanceOf(ArrayList.class));
        Assert.assertTrue(list.size() == 4);        
    }    
}
