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

package pl.orange.labs.mundo.plugins.wms.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * WMS XML structure
 * @author Henryk Rosa <henryk.rosa@orange.com>
 */

@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name="WMT_MS_Capabilities")
public class WmsCapabilities {
	@XmlAttribute(name="version")
	private String version;
	@XmlElement(name="Service")
	private Service service; //service metadata
	@XmlElement(name="Capability")
	private Capability capability; //
	
	@XmlTransient
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@XmlTransient
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	@XmlTransient
	public Capability getCapability() {
		return capability;
	}
	public void setCapability(Capability capability) {
		this.capability = capability;
	}
}
