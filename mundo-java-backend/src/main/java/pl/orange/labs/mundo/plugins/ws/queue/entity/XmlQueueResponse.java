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

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.map.annotate.JsonSerialize;


/**
 * Represents XMl response from WebService - City Queue
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement (name="ROOT")
public class XmlQueueResponse{
		
	@XmlAttribute(name="date")
	String date;
	@XmlAttribute(name="time")
	String time;
	@XmlElement(name="GRUPY")
	private List<Grupy> grupy;

	@XmlTransient
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@XmlTransient
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@XmlTransient
	public List<Grupy> getGrupy() {
		return grupy;
	}
	public void setGrupy(List<Grupy> grupy) {
		this.grupy = grupy;
	}

}
