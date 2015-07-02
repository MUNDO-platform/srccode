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

import java.util.Collection;
import java.util.List;
 
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
 
public class User implements UserDetails 
{
    private static final long serialVersionUID = 1L;
 
    private String username;
    private String password;
 
    /* Spring Security fields*/
    private List<Role> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
 
    @Override
    public String getUsername() 
    {
        return username;
    }
 
    public void setUsername(String username) 
    {
        this.username = username;
    }
     
    @Override
    public String getPassword() 
    {
        return password;
    }
 
    public void setPassword(String password) 
    {
        this.password = password;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() 
    {
        return this.authorities;
    }
     
    public void setAuthorities(List<Role> authorities) 
    {
        this.authorities = authorities;
    }
 
    @Override
    public boolean isAccountNonExpired() 
    {
        return this.accountNonExpired;
    }
     
    public void setAccountNonExpired(boolean accountNonExpired) 
    {
        this.accountNonExpired = accountNonExpired;
    }
 
    @Override
    public boolean isAccountNonLocked() 
    {
        return this.accountNonLocked;
    }
     
    public void setAccountNonLocked(boolean accountNonLocked) 
    {
        this.accountNonLocked = accountNonLocked;
    }
 
    @Override
    public boolean isCredentialsNonExpired() 
    {
        return this.credentialsNonExpired;
    }
     
    public void setCredentialsNonExpired(boolean credentialsNonExpired) 
    {
        this.credentialsNonExpired = credentialsNonExpired;
    }
 
    @Override
    public boolean isEnabled() 
    {
        return this.enabled;
    }
 
    public void setEnabled(boolean enabled) 
    {
        this.enabled = enabled;
    }
 
    @Override
    public String toString() 
    {
        StringBuilder builder = new StringBuilder();
        builder.append("User [username=");
        builder.append(username);
        builder.append(", password=");
        builder.append(password);
        builder.append(", authorities=");
        builder.append(authorities);
        builder.append(", accountNonExpired=");
        builder.append(accountNonExpired);
        builder.append(", accountNonLocked=");
        builder.append(accountNonLocked);
        builder.append(", credentialsNonExpired=");
        builder.append(credentialsNonExpired);
        builder.append(", enabled=");
        builder.append(enabled);
        builder.append("]");
        return builder.toString();
    }
}
