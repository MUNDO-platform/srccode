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


package pl.orange.labs.mundo.plugins.ws.api19115.entity;
import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;





/**
 * Represents object JSON structure for sending incident
 * 
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@JsonSerialize
public class SendIncidentJsonContent {

	@JsonProperty
	private String description;
	@JsonProperty
	private String email;
	@JsonProperty
	private String lastName;
	@JsonProperty
	private String name;
	@JsonProperty
	private String phoneNumber;
	@JsonProperty
	private String photoBase64;
	@JsonProperty
	private String subcategory;
	@JsonProperty
	private String eventType;
	@JsonProperty
	private String xCoordWGS84;
	@JsonProperty
	private String yCoordWGS84;
	@JsonProperty
	private String street;
	@JsonProperty
	private String houseNumber;
	@JsonProperty
	private String apartmentNumber; 
	
	
	
	
	
	public String getDescription() {
		return description;
	}





	public void setDescription(String description) {
		this.description = description;
	}





	public String getEmail() {
		return email;
	}





	public void setEmail(String email) {
		this.email = email;
	}





	public String getLastName() {
		return lastName;
	}





	public void setLastName(String lastName) {
		this.lastName = lastName;
	}





	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}





	public String getPhoneNumber() {
		return phoneNumber;
	}





	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}





	public String getPhotoBase64() {
		return photoBase64;
	}





	public void setPhotoBase64(String photoBase64) {
		this.photoBase64 = photoBase64;
	}





	public String getSubcategory() {
		return subcategory;
	}





	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}





	public String getEventType() {
		return eventType;
	}





	public void setEventType(String eventType) {
		this.eventType = eventType;
	}





	public String getxCoordWGS84() {
		return xCoordWGS84;
	}





	public void setxCoordWGS84(String xCoordWGS84) {
		this.xCoordWGS84 = xCoordWGS84;
	}





	public String getyCoordWGS84() {
		return yCoordWGS84;
	}





	public void setyCoordWGS84(String yCoordWGS84) {
		this.yCoordWGS84 = yCoordWGS84;
	}





	public String getStreet() {
		return street;
	}





	public void setStreet(String street) {
		this.street = street;
	}





	public String getHouseNumber() {
		return houseNumber;
	}





	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}





	public String getApartmentNumber() {
		return apartmentNumber;
	}





	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}


	/**
	 * Prepares string JSON - accepted by API 19115 - sendInformational, sendFreeForm functions
	 * @param withPhoto
	 * @return
	 */
	public String getJsonAcceptedForSendIncident(boolean withPhoto) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"email\":"+withQouteString(email)+",");	
		sb.append("\"name\":"+withQouteString(name)+",");	
		sb.append("\"lastName\":"+withQouteString(lastName)+",");
		sb.append("\"phoneNumber\":"+withQouteString(phoneNumber)+",");
		if (withPhoto)
			sb.append("\"phoneNumber\":"+withQouteString(photoBase64)+",");
		else
			sb.append("\"photoBase64\":null,");
		
		sb.append("\"xCoordWGS84\":"+withQouteString(xCoordWGS84)+",");
		sb.append("\"yCoordWGS84\":"+withQouteString(yCoordWGS84)+",");
		sb.append("\"description\":"+withQouteString(description)+",");
		sb.append("\"subcategory\":"+withQouteString(subcategory)+",");
		sb.append("\"eventType\":"+withQouteString(eventType)+",");
		sb.append("\"street\":"+withQouteString(street)+",");
		sb.append("\"houseNumber\":"+withQouteString(houseNumber)+",");
		sb.append("\"apartmentNumber\":"+withQouteString(apartmentNumber));

		sb.append("}");
		
		return sb.toString();
	}


	/**
	 * Prepares string JSON - accepted by API 19115 - sendIncident function
	 * @param withPhoto
	 * @return
	 */
	public String getJsonAcceptedForSendInformational() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"email\":"+withQouteString(email)+",");	
		sb.append("\"name\":"+withQouteString(name)+",");	
		sb.append("\"lastName\":"+withQouteString(lastName)+",");
		sb.append("\"phoneNumber\":"+withQouteString(phoneNumber)+",");
		sb.append("\"description\":"+withQouteString(description));
		sb.append("}");
		
		return sb.toString();
	}

	private String withQouteString(String s) {
		if (s==null)
			return "null";
		else
			return "\""+s+"\"";
	}

}
