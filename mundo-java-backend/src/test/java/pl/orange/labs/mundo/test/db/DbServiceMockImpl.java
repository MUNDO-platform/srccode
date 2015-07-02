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

package pl.orange.labs.mundo.test.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.util.Assert;
import pl.orange.labs.db.entities.Account;
import pl.orange.labs.mundo.plugins.db.dao.ColumnEntity;
import pl.orange.labs.mundo.plugins.db.dao.DbEntity;
import pl.orange.labs.mundo.plugins.db.dao.DbResultRow;
import pl.orange.labs.mundo.plugins.db.dao.TableEntity;
import pl.orange.labs.mundo.plugins.db.exception.DbException;
import pl.orange.labs.mundo.plugins.db.exception.QueryBuilderException;
import pl.orange.labs.mundo.plugins.db.exception.WrongTabTypeException;
import pl.orange.labs.mundo.plugins.db.query.QueryBuilder;
import pl.orange.labs.mundo.plugins.db.query.QueryBuilderFactory;
import pl.orange.labs.mundo.plugins.db.service.DbService;

/**
 *
 * @author Tomasz Janisiewicz <Tomasz.Janisiewicz@orange.com>
 */
public class DbServiceMockImpl implements DbService {
 

    @Override
    public List<DbResultRow> findAll(DbEntity dbEntity, TableEntity tableEntity, List<ColumnEntity> columnList, HashMap<String, String> params, Pageable pageable, Sort sort) throws DbException  {

        try {
        
            if (dbEntity.getName().equals("wrongdb")) {

                throw new DbException("Service unavailable");
            }

            Iterator<Sort.Order> iterator = sort.iterator();
            Sort.Order order = iterator.next();

            if (order.getProperty().equals("wrongcolumn")) {

                throw new DbException("Service unavailable");
            } 

            String jndi = dbEntity.getJndiName();
            Assert.notNull(jndi, "JNDI is null.");

            QueryBuilder builder = QueryBuilderFactory.create(tableEntity.getType());            
            String query = builder.build(tableEntity, params, pageable, sort);            
            Assert.notNull(query, "DbQuery is null."); 
            
            if (params.containsKey("alwaysEquals1")) {
                Assert.isTrue(params.get("alwaysEquals1").equals("1"), "Parameter alwaysEquals1 should be equals to 1");
                
            }

            return new ArrayList<DbResultRow>();               
        }
        catch (IllegalArgumentException e) {
            
            System.err.println(e);
            throw new DbException(e.getMessage());
        } 
        catch (BadSqlGrammarException e) {

            System.err.println(e);
            throw new DbException("BadSqlGrammarException");
        }          
        catch (WrongTabTypeException e) {
            
            System.err.println(e);
            throw new DbException(e.getMessage());
        } 
        catch (QueryBuilderException e) {
            
            System.err.println(e);
            throw new DbException(e.getMessage());            
        }           
     
    }
}
