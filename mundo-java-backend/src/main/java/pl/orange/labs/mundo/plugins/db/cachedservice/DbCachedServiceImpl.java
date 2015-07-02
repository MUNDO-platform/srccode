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
package pl.orange.labs.mundo.plugins.db.cachedservice;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/**
 * Service used for APIs based on external DB.
 * Provides methods for data access with cache.
 * 
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@Component
public class DbCachedServiceImpl implements DbCachedService {
    
    private static final Logger LOGGER = Logger.getLogger(DbCachedServiceImpl.class);

	
	

	@Override
	public List<Map<String, Object>> makeDbRequest(String jndi,
			String query) {
        JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
       
        DataSource dataSource = dsLookup.getDataSource(jndi);
        Assert.notNull(dataSource, "DataSource is null.");
        LOGGER.info("DataSource: " + dataSource);   
        
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        
		LOGGER.info("Executing query:"+query);
		LOGGER.info("JdbcTemplate:"+jdbcTemplate);
        return jdbcTemplate.queryForList(query);
	}


	@Override
	@Cacheable("mundoCache")
	public List<Map<String, Object>> makeDbOrCache1mRequest(String jndi,
			String query) {
        JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
       
        DataSource dataSource = dsLookup.getDataSource(jndi);
        Assert.notNull(dataSource, "DataSource is null.");
        LOGGER.info("DataSource: " + dataSource);   
        
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        
		LOGGER.info("Executing query:"+query);
		LOGGER.info("JdbcTemplate:"+jdbcTemplate);
        return jdbcTemplate.queryForList(query);
	}
    
	@Override
	@Cacheable("mundo1hCache")
	public List<Map<String, Object>> makeDbOrCache1hRequest(String jndi,
			String query) {
        JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
       
        DataSource dataSource = dsLookup.getDataSource(jndi);
        Assert.notNull(dataSource, "DataSource is null.");
        LOGGER.info("DataSource: " + dataSource);   
        
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        
		LOGGER.info("Executing query:"+query);
		LOGGER.info("JdbcTemplate:"+jdbcTemplate);
        return jdbcTemplate.queryForList(query);
	}

	@Override
	@Cacheable("mundo12hCache")
	public List<Map<String, Object>> makeDbOrCache12hRequest(String jndi,
			String query) {
        JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
       
        DataSource dataSource = dsLookup.getDataSource(jndi);
        Assert.notNull(dataSource, "DataSource is null.");
        LOGGER.info("DataSource: " + dataSource);   
        
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        
		LOGGER.info("Executing query:"+query);
		LOGGER.info("JdbcTemplate:"+jdbcTemplate);
        return jdbcTemplate.queryForList(query);
	}

	@Override
	@Cacheable("mundo24hCache")
	public List<Map<String, Object>> makeDbOrCache24hRequest(String jndi,
			String query) {
        JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
       
        DataSource dataSource = dsLookup.getDataSource(jndi);
        Assert.notNull(dataSource, "DataSource is null.");
        LOGGER.info("DataSource: " + dataSource);   
        
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        
		LOGGER.info("Executing query:"+query);
		LOGGER.info("JdbcTemplate:"+jdbcTemplate);
        return jdbcTemplate.queryForList(query);
	}
	
}
