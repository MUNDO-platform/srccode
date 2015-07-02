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

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents Account data managed in DB.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Entity
@Table(name = "account")
public class Account implements Serializable {
    
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    private Long id;
    /**
     * Account type (ROLE_USER, ROLE_ADMIN, ROLE_SUPERADMIN)
     */
    private String type;    
    private String username;
    private String password; 
    private String firstName;
    private String lastName;
    private String email;
    /**
     * Limit of using password before expiring.
     */
    private Integer passwordLimit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets Acoount's type
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * Sets Account's type
     * @param type string with allowed values (ROLE_USER ,ROLE_ADMIN, ROLE_SUPERADMIN)
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    } 

    /**
     * Limit of using password before expiring.
     * @return limit
     */
    public Integer getPasswordLimit() {
        return passwordLimit;
    }

    public void setPasswordLimit(Integer passwordLimit) {
        this.passwordLimit = passwordLimit;
    }    
    
    @Override
    public String toString() {
        
        return "id: " + id + ", type: " + type + ", username: " + username
                + ", firstName: " + firstName + ", lastName: " + lastName
                + ", email: " + email;
    }     
}
