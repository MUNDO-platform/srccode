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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlFeatureType 
{
    @XmlElement(name="Name")
    private String wfsName;
    @XmlElement(name="Title")
    private String wfsTitle;
    @XmlElement(name="Abstract")
    private String wfsAbstract;
    @XmlElement(name="DefaultSRS")
    private String wfsDefaultSRS;
    @XmlElement(name="OutputFormats")
    private XmlWfsOutputFormats xmlWfsOutputFormats;
    @XmlElement(name="WGS84BoundingBox", namespace="http://www.opengis.net/ows")
    private XmlWGS84BoundingBox xmlWGS84BoundingBox;

    @XmlTransient
    public String getWfsName() {
        return wfsName;
    }

    public void setWfsName(String wfsName) {
        this.wfsName = wfsName;
    }

    @XmlTransient
    public String getWfsTitle() {
        return wfsTitle;
    }

    public void setWfsTitle(String wfsTitle) {
        this.wfsTitle = wfsTitle;
    }

    @XmlTransient
    public String getWfsAbstract() {
        return wfsAbstract;
    }

    public void setWfsAbstract(String wfsAbstract) {
        this.wfsAbstract = wfsAbstract;
    }

    @XmlTransient
    public String getWfsDefaultSRS() {
        return wfsDefaultSRS;
    }

    public void setWfsDefaultSRS(String wfsDefaultSRS) {
        this.wfsDefaultSRS = wfsDefaultSRS;
    }
    
    @XmlTransient
    public XmlWfsOutputFormats getXmlWfsOutputFormats() {
        return xmlWfsOutputFormats;
    }

    public void setXmlWfsOutputFormats(XmlWfsOutputFormats xmlWfsOutputFormats) {
        this.xmlWfsOutputFormats = xmlWfsOutputFormats;
    } 
    
    @XmlTransient
    public XmlWGS84BoundingBox getXmlWGS84BoundingBox() {
        return xmlWGS84BoundingBox;
    }

    public void setXmlWGS84BoundingBox(XmlWGS84BoundingBox xmlWGS84BoundingBox) {
        this.xmlWGS84BoundingBox = xmlWGS84BoundingBox;
    }    
}
