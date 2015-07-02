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

package pl.orange.labs.mundo.security;

import java.util.List;
 
import org.springframework.security.core.GrantedAuthority;
 
public class Role implements GrantedAuthority 
{ 
    private static final long serialVersionUID = 1L;
    private String name;
 
    private List<Privilege> privileges;
 
    public String getName() 
    {
        return name;
    }
 
    public void setName(String name) 
    {
        this.name = name;
    }
 
    @Override
    public String getAuthority() 
    {
        return this.name;
    }
 
    public List<Privilege> getPrivileges() 
    {
        return privileges;
    }
 
    public void setPrivileges(List<Privilege> privileges) 
    {
        this.privileges = privileges;
    }
 
    @Override
    public String toString() 
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Role [name=");
        builder.append(name);
        builder.append(", privileges=");
        builder.append(privileges);
        builder.append("]");
        return builder.toString();
    }
}