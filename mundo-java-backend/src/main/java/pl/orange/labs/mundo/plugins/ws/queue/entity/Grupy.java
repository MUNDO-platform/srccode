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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Represents XML tag "GRUPY"
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@XmlAccessorType(XmlAccessType.FIELD)
public class Grupy {

	@XmlAttribute(name="status")
	private String status;
	@XmlElement(name="LP")
	private String lp;
	@XmlElement(name="IDGRUPY")
	private String idGrupy;
	@XmlElement(name="LITERAGRUPY")
	private String literaGrupy;
	@XmlElement(name="NAZWAGRUPY")
	private String nazwaGrupy;
	@XmlElement(name="AKTUALNYNUMER")	
	private int aktualnyNumer;
	@XmlElement(name="LICZBAKLWKOLEJCE")		
	private int liczbaKlwKolejce;
	@XmlElement(name="LICZBACZYNNYCHSTAN")			
	private int liczbaCzynnychStan;
	@XmlElement(name="CZASOBSLUGI")			
	private String czasObslugi;

	@XmlTransient
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@XmlTransient
	public String getLp() {
		return lp;
	}
	public void setLp(String lp) {
		this.lp = lp;
	}
	@XmlTransient
	public String getIdGrupy() {
		return idGrupy;
	}
	public void setIdGrupy(String idGrupy) {
		this.idGrupy = idGrupy;
	}
	@XmlTransient
	public String getLiteraGrupy() {
		return literaGrupy;
	}
	public void setLiteraGrupy(String literaGrupy) {
		this.literaGrupy = literaGrupy;
	}
	@XmlTransient
	public String getNazwaGrupy() {
		return nazwaGrupy;
	}
	public void setNazwaGrupy(String nazwaGrupy) {
		this.nazwaGrupy = nazwaGrupy;
	}
	@XmlTransient
	public int getAktualnyNumer() {
		return aktualnyNumer;
	}
	public void setAktualnyNumer(int aktualnyNumer) {
		this.aktualnyNumer = aktualnyNumer;
	}
	@XmlTransient
	public int getLiczbaKlwKolejce() {
		return liczbaKlwKolejce;
	}
	public void setLiczbaKlwKolejce(int liczbaKlwKolejce) {
		this.liczbaKlwKolejce = liczbaKlwKolejce;
	}
	@XmlTransient
	public int getLiczbaCzynnychStan() {
		return liczbaCzynnychStan;
	}
	public void setLiczbaCzynnychStan(int liczbaCzynnychStan) {
		this.liczbaCzynnychStan = liczbaCzynnychStan;
	}
	@XmlTransient
	public String getCzasObslugi() {
		return czasObslugi;
	}
	public void setCzasObslugi(String czasObslugi) {
		this.czasObslugi = czasObslugi;
	}
	
}
