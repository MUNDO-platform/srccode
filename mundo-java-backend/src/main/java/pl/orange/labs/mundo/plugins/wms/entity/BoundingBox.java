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
import javax.xml.bind.annotation.XmlAttribute;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * WMS XML structure
 * @author Henryk Rosa <henryk.rosa@orange.com>
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@XmlAccessorType(XmlAccessType.FIELD)
public class BoundingBox {
	@XmlAttribute(name="SRS")
	private String srs;
	@XmlAttribute(name="minx")
	private String minx;
	@XmlAttribute(name="miny")
	private String miny;
	@XmlAttribute(name="maxx")
	private String maxx;
	@XmlAttribute(name="maxy")
	private String maxy;
	
	public String getSrs() {
		return srs;
	}
	public void setSrs(String srs) {
		this.srs = srs;
	}
	public String getMinx() {
		return minx;
	}
	public void setMinx(String minx) {
		this.minx = minx;
	}
	public String getMiny() {
		return miny;
	}
	public void setMiny(String miny) {
		this.miny = miny;
	}
	public String getMaxx() {
		return maxx;
	}
	public void setMaxx(String maxx) {
		this.maxx = maxx;
	}
	public String getMaxy() {
		return maxy;
	}
	public void setMaxy(String maxy) {
		this.maxy = maxy;
	}

	
}
