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
import pl.orange.labs.mundo.geoconverter.GeoConversion;
import pl.orange.labs.mundo.geoconverter.GeoconversionFactory;
import pl.orange.labs.mundo.geoconverter.UrlParamsGeoConversionEPSG102175;
import pl.orange.labs.mundo.geoconverter.UrlParamsGeoConversionEPSG2178;
import pl.orange.labs.mundo.plugins.wms.exception.WmsException;

/**
 *
 * @author Tomasz Janisiewicz <Tomasz.Janisiewicz@orange.com>
 */
@ContextConfiguration(classes={JpaConfig.class, PersistenceTestConfig.class})
@ActiveProfiles("test")
@Transactional
@TransactionConfiguration(defaultRollback=true)
@RunWith(SpringJUnit4ClassRunner.class)
public class GeoconversionFactoryTest 
{
    @Test
    public void createTest() throws WmsException 
    {
        GeoConversion result = GeoconversionFactory.create("EPSG:2178");
        Assert.assertNotNull(result);
        assertThat(result, instanceOf(UrlParamsGeoConversionEPSG2178.class));        
    }  
    
    @Test
    public void createTest2() throws WmsException 
    {
        GeoConversion result = GeoconversionFactory.create("EPSG:102175");
        Assert.assertNotNull(result);
        assertThat(result, instanceOf(UrlParamsGeoConversionEPSG102175.class));        
    }      
    
    @Test(expected=WmsException.class)
    public void createTest3() throws WmsException 
    {
        GeoconversionFactory.create("wrong type");
    }    
}
