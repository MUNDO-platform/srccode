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

package pl.orange.labs.mundo.plugins.wfs.entity.geo.parser;

import pl.orange.labs.mundo.plugins.wfs.exception.GeometryParserNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * Class responsible for creating GeometryParser.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
public class GeometryParserFactory {
    
    /**
     * Logger.
     */
    private static Logger LOGGER = Logger.getLogger(GeometryParserFactory.class);
    
    /**
     * Method responsible for creating geometry parser.
     * @param featureMemberString
     * @return GeometryParser implementation.
     * @throws GeometryParserNotFoundException 
     */
    public static GeometryParser createParser(final String featureMemberString) throws GeometryParserNotFoundException
    {
        Pattern pattern = Pattern.compile("<SHAPE><gml:Point><gml:coordinates>(.+?)</gml:coordinates></gml:Point></SHAPE>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(featureMemberString);
        if (matcher.find()) {
            
            return new ShapePointParser();
        }
        
        pattern = Pattern.compile("<GEOMETRY><gml:Point><gml:coordinates>(.+?)</gml:coordinates></gml:Point></GEOMETRY>", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(featureMemberString);
        if (matcher.find()) {
            
            return new GeometryPointParser();
        }        
        
        pattern = Pattern.compile("<SHAPE><gml:LineString><gml:coordinates>(.+?)</gml:coordinates></gml:LineString></SHAPE>", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(featureMemberString);
        if (matcher.find()) {
            
            return new ShapeLinearStringParser();
        }        
        
        pattern = Pattern.compile("<GEOMETRY><gml:LineString><gml:coordinates>(.+?)</gml:coordinates></gml:LineString></GEOMETRY>", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(featureMemberString);
        if (matcher.find()) {
            
            return new GeometryLinearStringParser();
        }  
        
        pattern = Pattern.compile("<GEOMETRY><gml:Polygon srsName=\".+?\">(.+?)</gml:Polygon></GEOMETRY>", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(featureMemberString);
        if (matcher.find()) {
            
            return new GeometryPolygonParser();
        }        
        
        pattern = Pattern.compile("<GEOMETRY><gml:MultiPolygon><gml:polygonMember>(.+?)</gml:polygonMember></gml:MultiPolygon></GEOMETRY>", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(featureMemberString);
        if (matcher.find()) {
            
            return new GeometryMultiPolygonParser();
        }        
        
        pattern = Pattern.compile("<GEOMETRY><gml:MultiLineString>(.+?)</gml:MultiLineString></GEOMETRY>", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(featureMemberString);
        if (matcher.find()) {
            
            return new GeometryMultiLineStringParser();
        } 
        
        pattern = Pattern.compile("<SHAPE><gml:MultiLineString>(.+?)</gml:MultiLineString></SHAPE>", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(featureMemberString);
        if (matcher.find()) {
            
            return new ShapeMultiLineStringParser();
        }         
        
        throw new GeometryParserNotFoundException("Geometry pattern not found - " + featureMemberString);        
    }
}
