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

package pl.orange.labs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import pl.orange.labs.mundo.security.CustomAuthenticationProvider;

/**
 * Security configuration class.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Configuration
@EnableWebMvcSecurity
@ComponentScan(basePackages = {"pl.orange.labs"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {        
    
    /**
     * Injected object with custom authentication provider.
     */
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;      
    
    /**
     * Main configuration method.
     * @param http
     * @throws Exception 
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/gui/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
                .antMatchers("/gui/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
                .antMatchers("/gui/superadmin/**").access("hasRole('ROLE_SUPERADMIN')")
                .and()
                .formLogin()
                .loginProcessingUrl("/j_spring_security_check")                
                .loginPage("/login")            
                .failureUrl("/login?error")                
		.usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/gui/user/home")
                .and()
                .logout()
                .logoutUrl("/j_spring_security_logout")
                .logoutSuccessUrl("/login?logout");
                //.and().requiresChannel().anyRequest().requiresSecure()
                //.and().portMapper().http(80).mapsTo(443).http(8080).mapsTo(443);
    }   
   
    @Override
    protected void configure(AuthenticationManagerBuilder  auth) throws Exception 
    {
        auth.authenticationProvider(customAuthenticationProvider);
    }   
}
