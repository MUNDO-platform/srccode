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

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Persistence configuration file.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Configuration
@EnableJpaRepositories("pl.orange.labs")
@EnableTransactionManagement
public class PersistenceConfig {
    
	private static Logger LOGGER = Logger.getLogger(PersistenceConfig.class);
	
    @Bean 
    //Reading of properties filea - ClassPath resources - for some JVM installations works in one place only (..)
    public static PropertyPlaceholderConfigurer properties() {
        
    	LOGGER.info("PropertyPlaceholderConfigurer configuring ClassPathResource -list of *.properties files");
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ClassPathResource[] resources = new ClassPathResource[ ] {
            new ClassPathResource("persistence.properties")
        };
        ppc.setLocations( resources );
        ppc.setIgnoreUnresolvablePlaceholders( true );
        LOGGER.info("PropertyPlaceholderConfigurer configuring ClassPathResource -number of files="+resources.length);
        return ppc;
    }
    
    @Value("${db.jndiName}")
    private String dbJndiName;  
    
    /**
     * Creates DataSource from JNDI resource.
     * @return DataSource
     */
    @Bean
    public DataSource dataSourceJndi() {
        
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        DataSource dataSource = dsLookup.getDataSource(dbJndiName);
        return dataSource;
    }    
}
