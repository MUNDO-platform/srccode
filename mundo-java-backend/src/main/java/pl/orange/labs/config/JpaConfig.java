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

import java.util.Collections;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Class with JPA configuration.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Configuration
@EnableJpaRepositories(basePackages = {"pl.orange.labs"})
@EnableTransactionManagement
public class JpaConfig {
    
    /**
     * Injected DataSOurce object.
     */
    @Autowired 
    private DataSource dataSource;
    
    /**
     * Creates EntityManagerFactory.
     * @return LocalContainerEntityManagerFactoryBean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        
        HibernateJpaVendorAdapter hibernateJpa = new HibernateJpaVendorAdapter();
        hibernateJpa.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        hibernateJpa.setShowSql(false);
        hibernateJpa.setGenerateDdl(true);
        
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("pl.orange.labs");
        emf.setJpaVendorAdapter(hibernateJpa);
        emf.setJpaPropertyMap(Collections.singletonMap("javax.persistence.validation.mode", "none"));
        emf.setPersistenceUnitName("Mundo");
        return emf;
    }  
    
    /**
     * Creates JpaTransactionManager.
     * @return JpaTransactionManager
     */
    @Bean
    public JpaTransactionManager transactionManager() {
        
        JpaTransactionManager txnMgr = new JpaTransactionManager();
        txnMgr.setEntityManagerFactory(entityManagerFactory().getObject());
        return txnMgr;
    }    
}
