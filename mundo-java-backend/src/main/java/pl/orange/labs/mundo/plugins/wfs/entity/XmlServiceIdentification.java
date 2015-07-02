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
public class XmlServiceIdentification 
{
    @XmlElement(name="Title", namespace="http://www.opengis.net/ows")
    private String owsTitle;
    @XmlElement(name="Abstract", namespace="http://www.opengis.net/ows")
    private String owsAbstract;
    @XmlElement(name="Keywords")
    private String[] owsKeywords;
    @XmlElement(name="ServiceType", namespace="http://www.opengis.net/ows")
    private String owsServiceType;
    @XmlElement(name="ServiceTypeVersion", namespace="http://www.opengis.net/ows")
    private String owsServiceTypeVersion;
    @XmlElement(name="Fees", namespace="http://www.opengis.net/ows")
    private String owsFees;
    @XmlElement(name="AccessConstraints", namespace="http://www.opengis.net/ows")
    private String owsAccessConstraints;

    @XmlTransient
    public String getOwsTitle() {
        return owsTitle;
    }

    public void setOwsTitle(String owsTitle) {
        this.owsTitle = owsTitle;
    }

    @XmlTransient
    public String getOwsAbstract() {
        return owsAbstract;
    }

    public void setOwsAbstract(String owsAbstract) {
        this.owsAbstract = owsAbstract;
    }

    @XmlTransient
    public String[] getOwsKeywords() {
        return owsKeywords;
    }

    public void setOwsKeywords(String[] owsKeywords) {
        this.owsKeywords = owsKeywords;
    }

    @XmlTransient
    public String getOwsServiceType() {
        return owsServiceType;
    }

    public void setOwsServiceType(String owsServiceType) {
        this.owsServiceType = owsServiceType;
    }

    @XmlTransient
    public String getOwsServiceTypeVersion() {
        return owsServiceTypeVersion;
    }

    public void setOwsServiceTypeVersion(String owsServiceTypeVersion) {
        this.owsServiceTypeVersion = owsServiceTypeVersion;
    }

    @XmlTransient
    public String getOwsFees() {
        return owsFees;
    }

    public void setOwsFees(String owsFees) {
        this.owsFees = owsFees;
    }

    @XmlTransient
    public String getOwsAccessConstraints() {
        return owsAccessConstraints;
    }

    public void setOwsAccessConstraints(String owsAccessConstraints) {
        this.owsAccessConstraints = owsAccessConstraints;
    }
    
}
