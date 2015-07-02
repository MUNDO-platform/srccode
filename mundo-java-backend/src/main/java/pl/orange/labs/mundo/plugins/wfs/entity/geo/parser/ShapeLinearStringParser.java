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

import pl.orange.labs.mundo.plugins.wfs.exception.GeometryException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.orange.labs.mundo.plugins.wfs.entity.Geometry;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
public class ShapeLinearStringParser implements GeometryParser {
    

    @Override
    public Geometry parse(String featureMemberString) throws GeometryException {
        
        Geometry geometry = new Geometry();
        geometry.setType("ShapeLinearString");

        String patternString = "<SHAPE><gml:LineString><gml:coordinates>(.+?)</gml:coordinates></gml:LineString></SHAPE>";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(featureMemberString);
            
        if (matcher.find()) {
            
            geometry.setCoordinates(geometry.getCoordinatesList(matcher.group(1)));
        }        
        return geometry;
    }
    
}
