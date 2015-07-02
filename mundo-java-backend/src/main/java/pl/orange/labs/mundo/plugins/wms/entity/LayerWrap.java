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
public class LayerWrap {
	@XmlElement(name="Title")
	private String title;
	@XmlElement(name="SRS")
	private String[] srs;
	@XmlElement(name="LatLonBoundingBox")
	private LatLonBoundingBox latLonBoundingBox;
	@XmlElement(name="Layer")
	private Layer[] layer;
	
	//@XmlTransient
	//@JsonIgnore
	/**
	 * checks if Layer for given name exists
	 * @param name
	 * @return boolean
	 */
	public boolean existsLayerForName(String name){
		if (layer==null)
			return false;
		for (int i=0; i<layer.length; i++) 
			if (layer[i].getName()!=null) {
				if (layer[i].getName().equals(name))
					return true;
			} else {
				if (layer[i].getLayer()!=null) {
					for (int x=0; x<layer[i].getLayer().length; x++){
						if (layer[i].getLayer()[x].getName()!=null) {
							if (layer[i].getLayer()[x].getName().equals(name))
								return true;
						}
					}
				}
			}
		return false;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public Layer[] getLayer() {
		return layer;
	}
	public void setLayer(Layer[] layer) {
		this.layer = layer;
	}
}
