/*
*  Platforma MUNDO – Dane po Warszawsku  http://danepowarszawsku.pl/
*   
*  @authors Jaroslaw Legierski, Tomasz Janisiewicz, Henryk Rosa Centrum Badawczo – Rozwojowe/ Orange Labs
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

package pl.orange.labs.db.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents configuration data managed in DataBase table
 * @author Henryk Rosa <henryk.rosa@orange.com>
 *
 */
@Entity
@Table(name = "config")
public class ConfigEntity {
    
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
	int id;
	
	@Column(name = "key", nullable=false)
	String key;
	
	@Column(name = "value", nullable=false)
	String value;
	
	@Column(name = "last_modified")
	Timestamp last_modified;
	
	@Column(name = "description")
	String description;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Timestamp getLast_modified() {
		return last_modified;
	}
	public void setLast_modified(Timestamp last_modified) {
		this.last_modified = last_modified;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override 
	public String toString(){
		StringBuffer sb = new StringBuffer(ConfigEntity.class.getSimpleName() + " value:{");
		sb.append("id:" + this.id);
		sb.append(",key:" + this.key);
		sb.append(",value:" + this.value);
		sb.append(",last_modified:" + this.last_modified);
		sb.append(",description:" + this.description);
		sb.append("}");
		return sb.toString();
	}
	
}
