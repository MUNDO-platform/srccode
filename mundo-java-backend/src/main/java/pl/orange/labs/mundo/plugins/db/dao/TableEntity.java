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
package pl.orange.labs.mundo.plugins.db.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Entity
@Table(name="dbTable")
public class TableEntity 
{
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)    
    private Long id;
    @Column(name = "DB_ID", nullable=false)
    private Long dbId;    
    @Column(name = "NAME", length = 50, nullable=false)
    private String name;
    @Column(name = "TYPE", length = 50, nullable=false)
    private String type;
    @Column(name = "PARAMS", length = 255)
    private String params;   
    
    @Column(name = "CACHEVARIANT", nullable=false, columnDefinition="int default 0")
    private int cacheVariant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }    
    
    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }    

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }    
    
    
    public int getCacheVariant() {
		return cacheVariant;
	}

	public void setCacheVariant(int cacheVariant) {
		this.cacheVariant = cacheVariant;
	}

	@Override
    public String toString() 
    {
        return "id: " + id + ", dbId: " + dbId + ", name: " + name+", cacheVariant: "+cacheVariant;
    }    
}
