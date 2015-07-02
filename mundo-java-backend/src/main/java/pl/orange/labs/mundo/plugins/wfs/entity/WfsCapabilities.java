/*
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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement (name="WFS_Capabilities")
public class WfsCapabilities 
{
    @XmlElement(name="ServiceIdentification")
    private XmlServiceIdentification serviceIdentification; 
    @XmlElement(name="ServiceProvider")
    private XmlServiceProvider serviceProvider;
    @XmlElement(name="OperationsMetadata")
    private XmlOperationsMetadata operationsMetadata;
    @XmlElement(name="FeatureTypeList")
    private XmlFeatureTypeList featureTypeList;
    @XmlElement(name="Filter_Capabilities")
    private XmlWfsFilterCapabilities filterCapabilities;

    @XmlTransient
    public XmlServiceIdentification getServiceIdentification() {
        return serviceIdentification;
    }

    public void setServiceIdentification(XmlServiceIdentification serviceIdentification) {
        this.serviceIdentification = serviceIdentification;
    }

    @XmlTransient
    public XmlServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(XmlServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @XmlTransient
    public XmlOperationsMetadata getOperationsMetadata() {
        return operationsMetadata;
    }

    public void setOperationsMetadata(XmlOperationsMetadata operationsMetadata) {
        this.operationsMetadata = operationsMetadata;
    }

    @XmlTransient
    public XmlFeatureTypeList getFeatureTypeList() {
        return featureTypeList;
    }

    public void setFeatureTypeList(XmlFeatureTypeList featureTypeList) {
        this.featureTypeList = featureTypeList;
    }

    @XmlTransient
    public XmlWfsFilterCapabilities getFilterCapabilities() {
        return filterCapabilities;
    }

    public void setFilterCapabilities(XmlWfsFilterCapabilities filterCapabilities) {
        this.filterCapabilities = filterCapabilities;
    }
}
