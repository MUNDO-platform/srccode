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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pl.orange.labs.db.entities.Account;
import pl.orange.labs.db.dao.AccountRepository;
 
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider 
{
    private final Logger logger = Logger.getLogger(CustomAuthenticationProvider.class); 

    @Autowired 
    private AccountRepository repository;     

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException 
    {        
        Account account = authRequest(authentication.getName(), (String) authentication.getCredentials());
 
        User user = new User();
        user.setUsername(authentication.getName());
        user.setPassword((String) authentication.getCredentials());
        Role r = new Role();
        r.setName(account.getType());
        List<Role> roles = new ArrayList<>();
        roles.add(r);
        user.setAuthorities(roles);
 
        if (user == null) 
        {
            throw new BadCredentialsException("Username not found.");
        }
 
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();      
 
        return new UsernamePasswordAuthenticationToken(authentication.getName(), (String) authentication.getCredentials(), authorities);
    }
 
    @Override
    public boolean supports(Class<?> arg0) 
    {
        return true;
    }
    
    
    private Account authRequest(final String phone, final String code)
    {
        try
        {
            logger.info("authRequest");
            Account account = repository.findByUsername(phone);
            
            if (account.getPassword().equals(code) && account.getPasswordLimit() > 0)
            {
                account.setPasswordLimit(account.getPasswordLimit()-1);
                return repository.save(account);               
            }
            else
            {
                logger.fatal("Username not found.");
                throw new BadCredentialsException("Username not found.");
            }            
        }
        catch(DataAccessException e)
        {
            logger.fatal(e);
            throw new BadCredentialsException(e.getMessage());    
        }
        catch(Exception e)
        {
            logger.fatal(e);
            throw new BadCredentialsException(e.getMessage());  
        }
    } 
}
