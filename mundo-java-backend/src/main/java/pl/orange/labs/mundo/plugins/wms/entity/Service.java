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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * WMS XML structure
 * @author Henryk Rosa <henryk.rosa@orange.com>
 */

@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@XmlAccessorType(XmlAccessType.FIELD)
public class Service {
	@XmlElement(name="Name")
	private String name;
	@XmlElement(name="Title")
	private String title;
	@XmlElement(name="Abstract")
	private String _abstract;
	@XmlElement(name="KeywordList")
	private KeywordList keyWordList; 
	@XmlElement(name="OnlineResource")
	private String onlineResource;
	@XmlElement(name="ContactInformation")
	private ContactInformation contactInformation;
	@XmlElement(name="Fees")
	private String fees;
	@XmlElement(name="AccessConstraints")
	private String accessConstraints;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String get_abstract() {
		return _abstract;
	}
	public void set_abstract(String _abstract) {
		this._abstract = _abstract;
	}
	public KeywordList getKeyWordList() {
		return keyWordList;
	}
	public void setKeyWordList(KeywordList keyWordList) {
		this.keyWordList = keyWordList;
	}
	public String getOnlineResource() {
		return onlineResource;
	}
	public void setOnlineResource(String onlineResource) {
		this.onlineResource = onlineResource;
	}
	public ContactInformation getContactInformation() {
		return contactInformation;
	}
	public void setContactInformation(ContactInformation contactInformation) {
		this.contactInformation = contactInformation;
	}
	public String getFees() {
		return fees;
	}
	public void setFees(String fees) {
		this.fees = fees;
	}
	public String getAccessConstraints() {
		return accessConstraints;
	}
	public void setAccessConstraints(String accessConstraints) {
		this.accessConstraints = accessConstraints;
	}
	
	
	
}
