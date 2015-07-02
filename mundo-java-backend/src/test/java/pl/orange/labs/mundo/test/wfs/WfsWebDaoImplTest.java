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
import static org.hamcrest.CoreMatchers.instanceOf;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.orange.labs.config.JpaConfig;
import pl.orange.labs.config.PersistenceTestConfig;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsWebDaoImpl;
import pl.orange.labs.mundo.plugins.wfs.entity.Geometry;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsFeatureCollection;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsFeatureMember;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsFeatureMemberProperty;
import pl.orange.labs.mundo.plugins.wfs.exception.WfsPropertiesParserException;

/**
 *
 * @author Tomasz Janisiewicz <Tomasz.Janisiewicz@orange.com>
 */
@ContextConfiguration(classes = {JpaConfig.class, PersistenceTestConfig.class})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class WfsWebDaoImplTest {
    
    @Test
    public void parseXmlTest() throws Exception {
        
        WfsWebDaoImpl wfsWebDaoImpl = new WfsWebDaoImpl();
        String wfsString = "<gml:featureMember>"
                + "<name>"
                + "<SHAPE><gml:Point>"
                + "<gml:coordinates>12.1212,23.2323 32.2323,12.232323</gml:coordinates>"
                + "</gml:Point></SHAPE>"
                + "<test>abc</test><test>abc</test>"
                + "</name>"
                + "</gml:featureMember>";
        String name = "name";
        String namespace = "namespace";
        
        WfsFeatureCollection featureList = wfsWebDaoImpl.parseXml(wfsString, namespace, name);
        Assert.assertNotNull(featureList); 
        assertThat(featureList, instanceOf(WfsFeatureCollection.class));
        Assert.assertNotNull(featureList.getFeatureMemberList()); 
        assertThat(featureList.getFeatureMemberList(), instanceOf(List.class));        
        Assert.assertTrue(featureList.getFeatureMemberList().size() == 1);
        Assert.assertNotNull(featureList.getFeatureMemberList().get(0)); 
        assertThat(featureList.getFeatureMemberList().get(0), instanceOf(WfsFeatureMember.class));        
        Assert.assertNotNull(featureList.getFeatureMemberList().get(0).getGeometry());
        assertThat(featureList.getFeatureMemberList().get(0).getGeometry(), instanceOf(Geometry.class));
        Assert.assertNotNull(featureList.getFeatureMemberList().get(0).getGeometry().getCoordinates());
        assertThat(featureList.getFeatureMemberList().get(0).getProperties(), instanceOf(List.class));
        Assert.assertNotNull(featureList.getFeatureMemberList().get(0).getProperties());
        Assert.assertTrue(featureList.getFeatureMemberList().get(0).getProperties().size() == 2);
    }    
    
    @Test
    public void parseFeatureMemberPropertiesTest() throws Exception {
        
        WfsWebDaoImpl wfsWebDaoImpl = new WfsWebDaoImpl();
        String featureMemberString = "<test>abc</test>";
        List<WfsFeatureMemberProperty> list = wfsWebDaoImpl.parseFeatureMemberProperties(featureMemberString);
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() == 1);
        Assert.assertEquals("test", list.get(0).getKey());
        Assert.assertEquals("abc", list.get(0).getValue());
        assertThat(list, instanceOf(List.class));
        assertThat(list.get(0), instanceOf(WfsFeatureMemberProperty.class));        
    }
    
    @Test(expected = WfsPropertiesParserException.class)
    public void parseFeatureMemberPropertiesTest2() throws Exception {
        
        WfsWebDaoImpl wfsWebDaoImpl = new WfsWebDaoImpl();
        String featureMemberString = "";
        wfsWebDaoImpl.parseFeatureMemberProperties(featureMemberString);
    }   
    
    @Test(expected = WfsPropertiesParserException.class)
    public void parseFeatureMemberPropertiesTest3() throws Exception {
        
        WfsWebDaoImpl wfsWebDaoImpl = new WfsWebDaoImpl();
        wfsWebDaoImpl.parseFeatureMemberProperties(null);
    } 
    
    @Test(expected = WfsPropertiesParserException.class)
    public void parseFeatureMemberPropertiesTest4() throws Exception {
        
        WfsWebDaoImpl wfsWebDaoImpl = new WfsWebDaoImpl();
        String featureMemberString = "<test>abc</test";
        wfsWebDaoImpl.parseFeatureMemberProperties(featureMemberString);
    }
    
    @Test
    public void parseFeatureMemberPropertiesTest5() throws Exception {
        
        WfsWebDaoImpl wfsWebDaoImpl = new WfsWebDaoImpl();
        String featureMemberString = "<test></test>";
        List<WfsFeatureMemberProperty> list = wfsWebDaoImpl.parseFeatureMemberProperties(featureMemberString);
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() == 1);
        Assert.assertEquals("test", list.get(0).getKey());
        Assert.assertEquals("", list.get(0).getValue());
        assertThat(list, instanceOf(List.class));
        assertThat(list.get(0), instanceOf(WfsFeatureMemberProperty.class));       
    } 
    
    @Test
    public void parseFeatureMemberPropertiesTest6() throws Exception {
        
        WfsWebDaoImpl wfsWebDaoImpl = new WfsWebDaoImpl();
        String featureMemberString = "<test>abc</test><wrontag>";
        List<WfsFeatureMemberProperty> list = wfsWebDaoImpl.parseFeatureMemberProperties(featureMemberString);
        Assert.assertNotNull(list);
        Assert.assertEquals("test", list.get(0).getKey());
        Assert.assertEquals("abc", list.get(0).getValue());
        assertThat(list, instanceOf(List.class));
        assertThat(list.get(0), instanceOf(WfsFeatureMemberProperty.class));
        Assert.assertTrue(list.size() == 1);    
        
    }   
    
    @Test
    public void parseFeatureMemberPropertiesTest7() throws Exception {
        
        WfsWebDaoImpl wfsWebDaoImpl = new WfsWebDaoImpl();
        String featureMemberString = "<test>abc</test><wrontag></wrontag>";
        List<WfsFeatureMemberProperty> list = wfsWebDaoImpl.parseFeatureMemberProperties(featureMemberString);
        Assert.assertNotNull(list);
        Assert.assertEquals("test", list.get(0).getKey());
        Assert.assertEquals("abc", list.get(0).getValue());
        assertThat(list, instanceOf(List.class));
        assertThat(list.get(0), instanceOf(WfsFeatureMemberProperty.class));
        Assert.assertTrue(list.size() == 2); 
    }  
    
    @Test
    public void parseFeatureMemberPropertiesTest8() throws Exception {
        
        WfsWebDaoImpl wfsWebDaoImpl = new WfsWebDaoImpl();
        String featureMemberString = "<test>abc</test><test2>abc</test2><wrontag></wrontag>";
        List<WfsFeatureMemberProperty> list = wfsWebDaoImpl.parseFeatureMemberProperties(featureMemberString);
        Assert.assertNotNull(list);
        Assert.assertEquals("test", list.get(0).getKey());
        Assert.assertEquals("abc", list.get(0).getValue());
        assertThat(list, instanceOf(List.class));
        assertThat(list.get(0), instanceOf(WfsFeatureMemberProperty.class));
        Assert.assertTrue(list.size() == 3); 
    } 
    
    @Test(expected = WfsPropertiesParserException.class)
    public void parseFeatureMemberPropertiesTest9() throws Exception {
        
        WfsWebDaoImpl wfsWebDaoImpl = new WfsWebDaoImpl();
        String featureMemberString = "<testA>abc</testB>";
        wfsWebDaoImpl.parseFeatureMemberProperties(featureMemberString);       
    } 
    
    @Test
    public void parseFeatureMemberPropertiesTest10() throws Exception {
        
        WfsWebDaoImpl wfsWebDaoImpl = new WfsWebDaoImpl();
        String featureMemberString = "<test>abc</test><testA>abcA</testB><test2>abc2</test2>";
        List<WfsFeatureMemberProperty> list = wfsWebDaoImpl.parseFeatureMemberProperties(featureMemberString);
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() == 2); 
        Assert.assertEquals("test", list.get(0).getKey());
        Assert.assertEquals("abc", list.get(0).getValue());
        Assert.assertEquals("test2", list.get(1).getKey());
        Assert.assertEquals("abc2", list.get(1).getValue());        
        assertThat(list, instanceOf(List.class));
        assertThat(list.get(0), instanceOf(WfsFeatureMemberProperty.class));
        
    }     
    
}
