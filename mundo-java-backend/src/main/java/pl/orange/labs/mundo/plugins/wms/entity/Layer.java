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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * WMS XML structure
 * @author Henryk Rosa <henryk.rosa@orange.com>
 */

@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@XmlAccessorType(XmlAccessType.FIELD)
public class Layer {
	@XmlAttribute(name="queryable")
	private String queryable;
	@XmlAttribute(name="cascaded")
	private String cascaded;
	@XmlAttribute(name="noSubsets")
	private String noSubsets;
	@XmlAttribute(name="opaque")
	private String opaque;
	@XmlElement(name="Name")
	private String name;
	@XmlElement(name="Title")
	private String title;
	@XmlElement(name="Abstract")
	private String _abstract;
	@XmlElement(name="SRS")
	private String[] srs;
	@XmlElement(name="LatLonBoundingBox")
	private LatLonBoundingBox latLonBoundingBox;
	@XmlElement(name="BoundingBox")
	private BoundingBox[] boundingBox;
	@XmlElement(name="Layer", required=false)
	private Layer[] layer;
		
	/**
	 * Returns max bounding box for given SRS EPSG_4326
	 * @return BoundingBox
	 */
	@XmlTransient
	@JsonIgnore
	public BoundingBox getBoundingBoxForSrsEPSG_4326() {
		if (boundingBox==null)
			return null;

		for (int i =0; i<boundingBox.length; i++)
			if(boundingBox[i].getSrs().equals("EPSG:4326"))
				return boundingBox[i];
		
		return null;
	}
	
	
	public String getQueryable() {
		return queryable;
	}
	public void setQueryable(String queryable) {
		this.queryable = queryable;
	}
	public String getCascaded() {
		return cascaded;
	}
	public void setCascaded(String cascaded) {
		this.cascaded = cascaded;
	}
	public String getNoSubsets() {
		return noSubsets;
	}
	public void setNoSubsets(String noSubsets) {
		this.noSubsets = noSubsets;
	}
	public String getOpaque() {
		return opaque;
	}
	public void setOpaque(String opaque) {
		this.opaque = opaque;
	}
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
	public String[] getSrs() {
		return srs;
	}
	public void setSrs(String[] srs) {
		this.srs = srs;
	}
	public LatLonBoundingBox getLatLonBoundingBox() {
		return latLonBoundingBox;
	}
	public void setLatLonBoundingBox(LatLonBoundingBox latLonBoundingBox) {
		this.latLonBoundingBox = latLonBoundingBox;
	}
	public BoundingBox[] getBoundingBox() {
		return boundingBox;
	}
	public void setBoundingBox(BoundingBox[] boundingBox) {
		this.boundingBox = boundingBox;
	}


	public Layer[] getLayer() {
		return layer;
	}


	public void setLayer(Layer[] layer) {
		this.layer = layer;
	}
	
	
	
}
