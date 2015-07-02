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
package pl.orange.labs.mundo.plugins.db.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import pl.orange.labs.mundo.plugins.db.cachedservice.DbCachedService;
import pl.orange.labs.mundo.plugins.db.dao.ColumnEntity;
import pl.orange.labs.mundo.plugins.db.dao.DbEntity;
import pl.orange.labs.mundo.plugins.db.dao.DbResultCol;
import pl.orange.labs.mundo.plugins.db.dao.DbResultRow;
import pl.orange.labs.mundo.plugins.db.dao.TableEntity;
import pl.orange.labs.mundo.plugins.db.exception.DbException;
import pl.orange.labs.mundo.plugins.db.exception.QueryBuilderException;
import pl.orange.labs.mundo.plugins.db.exception.WrongTabTypeException;
import pl.orange.labs.mundo.plugins.db.query.QueryBuilder;
import pl.orange.labs.mundo.plugins.db.query.QueryBuilderFactory;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Component
public class DbServiceImpl implements DbService {
    
    private static final Logger LOGGER = Logger.getLogger(DbServiceImpl.class);

    @Autowired 
    DbCachedService dbCachedService;
    
    
    @Override 
    public List<DbResultRow> findAll(DbEntity dbEntity, 
            TableEntity tableEntity, 
            List<ColumnEntity> columnList, 
            HashMap<String, String> params, 
            Pageable pageable,             
            Sort sort) throws DbException {
    	
        try {
        	
        	
            String jndi = dbEntity.getJndiName();
            Assert.notNull(jndi, "JNDI is null.");
            LOGGER.info("JNDI: " + jndi);
        
            JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
            dsLookup.setResourceRef(true);
            DataSource dataSource = dsLookup.getDataSource(jndi);
            Assert.notNull(dataSource, "DataSource is null.");
            LOGGER.info("DataSource: " + dataSource);   
            
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            QueryBuilder builder = QueryBuilderFactory.create(tableEntity.getType());  
            //builder.
            
            String query = builder.build(tableEntity, params, pageable, sort);            
            Assert.notNull(query, "DbQuery is null.");
            //LOGGER.info("Query: " + query);
                       
            List<DbResultRow> resultRowList = new ArrayList<DbResultRow>();        
            //List<Map<String, Object>> empRows = jdbcTemplate.queryForList(query);
            //List<Map<String, Object>> empRows = this.makeDbRequest(jndi, query);
            
            
            
            List<Map<String, Object>> empRows;
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Cached request: " + tableEntity.getCacheVariant());   

            switch (tableEntity.getCacheVariant()) {
			case 60:
				empRows = dbCachedService.makeDbOrCache1mRequest(jndi, query);
				break;
			case 3600:
				empRows = dbCachedService.makeDbOrCache1hRequest(jndi, query);
				break;
			case 43200:
				empRows = dbCachedService.makeDbOrCache12hRequest(jndi, query);
				break;
			case 86400:
				empRows = dbCachedService.makeDbOrCache24hRequest(jndi, query);
				break;

			default:
				empRows = dbCachedService.makeDbRequest(jndi, query);
				//without cache
				break;
			}
            
            Assert.notNull(empRows, "Result rows is null.");
        
            for (Map<String, Object> empRow : empRows) {
                
                DbResultRow resultRow = new DbResultRow();
                resultRow.setValues(new ArrayList<DbResultCol>());
                for (ColumnEntity c : columnList) {
                    
                    DbResultCol resultCol = new DbResultCol();
                    resultCol.setKey(c.getName());
                    resultCol.setValue(String.valueOf(empRow.get(c.getName())));
                    resultRow.getValues().add(resultCol);
                }

                resultRowList.add(resultRow);
            }
        
            return resultRowList;     
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.fatal(e);
            throw new DbException(e.getMessage());
        } 
        catch (BadSqlGrammarException e) {

            LOGGER.fatal(e);
            throw new DbException("BadSqlGrammarException");
        }          
        catch (WrongTabTypeException e) {
            
            LOGGER.fatal(e);
            throw new DbException(e.getMessage());
        } 
        catch (QueryBuilderException e) {
            
            LOGGER.warn(e);
            throw new DbException(e.getMessage());            
        }
    }
    
    
	
	
    
}
