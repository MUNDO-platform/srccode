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
import pl.orange.labs.mundo.plugins.wfs.entity.geo.parser.GeometryLinearStringParser;
import pl.orange.labs.mundo.plugins.wfs.entity.geo.parser.GeometryMultiPolygonParser;
import pl.orange.labs.mundo.plugins.wfs.entity.geo.parser.GeometryParser;
import pl.orange.labs.mundo.plugins.wfs.entity.geo.parser.GeometryParserFactory;
import pl.orange.labs.mundo.plugins.wfs.exception.GeometryParserNotFoundException;
import pl.orange.labs.mundo.plugins.wfs.entity.geo.parser.GeometryPointParser;
import pl.orange.labs.mundo.plugins.wfs.entity.geo.parser.ShapeLinearStringParser;
import pl.orange.labs.mundo.plugins.wfs.entity.geo.parser.ShapeMultiLineStringParser;
import pl.orange.labs.mundo.plugins.wfs.entity.geo.parser.ShapePointParser;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@ContextConfiguration(classes={JpaConfig.class, PersistenceTestConfig.class})
@ActiveProfiles("test")
@Transactional
@TransactionConfiguration(defaultRollback=true)
@RunWith(SpringJUnit4ClassRunner.class)
public class GeometryParserFactoryTest 
{
    private final Logger logger = Logger.getLogger(GeometryParserFactoryTest.class);    
    
    @Test(expected=GeometryParserNotFoundException.class)
    public void geometryParserNotFoundExceptionTest() throws GeometryParserNotFoundException 
    {
        GeometryParserFactory.createParser("wrong type");
    }     
    
    @Test
    public void createParserTest() throws GeometryParserNotFoundException
    {
        String featureMemberString = "<SHAPE><gml:Point><gml:coordinates>00000</gml:coordinates></gml:Point></SHAPE>";
        GeometryParser geometryParser = GeometryParserFactory.createParser(featureMemberString);
        Assert.assertNotNull(geometryParser);
        assertThat(geometryParser, instanceOf(ShapePointParser.class));
        
        featureMemberString = "<GEOMETRY><gml:Point><gml:coordinates>00000</gml:coordinates></gml:Point></GEOMETRY>";
        geometryParser = GeometryParserFactory.createParser(featureMemberString);
        Assert.assertNotNull(geometryParser);
        assertThat(geometryParser, instanceOf(GeometryPointParser.class));
        
        featureMemberString = "<SHAPE><gml:LineString><gml:coordinates>00000</gml:coordinates></gml:LineString></SHAPE>";
        geometryParser = GeometryParserFactory.createParser(featureMemberString);
        Assert.assertNotNull(geometryParser);
        assertThat(geometryParser, instanceOf(ShapeLinearStringParser.class));
        
        featureMemberString = "<GEOMETRY><gml:LineString><gml:coordinates>00000</gml:coordinates></gml:LineString></GEOMETRY>";
        geometryParser = GeometryParserFactory.createParser(featureMemberString);
        Assert.assertNotNull(geometryParser);
        assertThat(geometryParser, instanceOf(GeometryLinearStringParser.class));
        
        featureMemberString = "<GEOMETRY><gml:MultiPolygon><gml:polygonMember>00000</gml:polygonMember></gml:MultiPolygon></GEOMETRY>";
        geometryParser = GeometryParserFactory.createParser(featureMemberString);
        Assert.assertNotNull(geometryParser);
        assertThat(geometryParser, instanceOf(GeometryMultiPolygonParser.class));
        
        featureMemberString = "<SHAPE><gml:MultiLineString>00000</gml:MultiLineString></SHAPE>";
        geometryParser = GeometryParserFactory.createParser(featureMemberString);
        Assert.assertNotNull(geometryParser);
        assertThat(geometryParser, instanceOf(ShapeMultiLineStringParser.class));        
    }     
}

