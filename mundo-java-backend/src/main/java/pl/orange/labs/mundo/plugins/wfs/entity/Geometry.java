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

package pl.orange.labs.mundo.plugins.wfs.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.util.Assert;
import pl.orange.labs.mundo.plugins.wfs.exception.GeometryException;

/**
 * Represents geometry part of wfs response.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@XmlAccessorType(XmlAccessType.FIELD)
public class Geometry {
    
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(Geometry.class);
    
    private String type;
    @XmlElement(name = "coordinates")
    private List<XmlWfsPoint> coordinates;    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<XmlWfsPoint> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<XmlWfsPoint> coordinates) {
        this.coordinates = coordinates;
    }
    
    /**
     * Parses string with coordinates and returns collection of Point objects.
     * @param coordinates - string with coordinates separated by space.
     * @return List<XmlWfsPoint>
     * @throws pl.orange.labs.mundo.plugins.wfs.exception.GeometryException
     */
    public List<XmlWfsPoint> getCoordinatesList(final String coordinates) throws GeometryException {
        
        try {
            
            Assert.notNull(coordinates, "Coordinates string is null.");
            ArrayList<XmlWfsPoint> pointList = new ArrayList<XmlWfsPoint>();
            String[] parts = coordinates.split(" ");
            Assert.notNull(parts, "Coordinates has wrong format.");
            Assert.isTrue(parts.length > 0, "Coordinates string is empty.");

            for (String item : parts) {

                XmlWfsPoint point = new XmlWfsPoint();
                String[] coordin = item.split(","); 
                Assert.notNull(coordin, "Coordinate string has wrong format: " + item);
                Assert.isTrue(coordin.length == 2, "Coordinate string has wrong format: " + item); 
                Assert.isInstanceOf(Double.class, Double.parseDouble(coordin[0]), "Longitude has wrong format."); 
                Assert.isInstanceOf(Double.class, Double.parseDouble(coordin[1]), "Latitude has wrong format.");                 
                
                point.setLongitude(coordin[0]);
                point.setLatitude(coordin[1]);            
                pointList.add(point);
            }

            return pointList;              
        }
        catch (NumberFormatException e) {
            
            LOGGER.warn(coordinates);
            LOGGER.error(e);
            throw new GeometryException("NumberFormatException: " + e.getMessage());
        }         
        catch (IllegalArgumentException e) {
            
            LOGGER.warn(coordinates);
            LOGGER.error(e);
            throw new GeometryException(e.getMessage());
        }  
    }
}
