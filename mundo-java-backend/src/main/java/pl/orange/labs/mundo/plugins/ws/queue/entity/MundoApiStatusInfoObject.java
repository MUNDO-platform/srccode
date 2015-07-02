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

package pl.orange.labs.mundo.plugins.ws.queue.entity;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import pl.orange.labs.mundo.enums.ApiRequestStatus;

/**
 * Represents basic MUNDO API response data 
 * - if success or error + optional error reason
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class MundoApiStatusInfoObject {
	//API name - will be returned as first property in JSON object
	private String apiName = "MUNDO City Offices queues"; //TODO move to config
	//API request status  
	private ApiRequestStatus status;
	private String errorPhrase;
	
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public ApiRequestStatus getStatus() {
		return status;
	}
	public void setStatus(ApiRequestStatus status) {
		this.status = status;
	}
	public String getErrorPhrase() {
		return errorPhrase;
	}
	public void setErrorPhrase(String errorPhrase) {
		this.errorPhrase = errorPhrase;
	}
}
