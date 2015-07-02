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
package pl.orange.labs.mundo.plugins.db.query;

import java.util.HashMap;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.orange.labs.mundo.plugins.db.dao.TableEntity;
import pl.orange.labs.mundo.plugins.db.exception.QueryBuilderException;

/**
 * Responsible for building sql query.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
public interface QueryBuilder {
    
    /**
     * Builds query.
     * @param tab
     * @param params - map with parameters HashMap<String, String>
     * @param pageable
     * @param sort
     * @return String - sql query.
     * @throws pl.orange.labs.mundo.plugins.db.exception.QueryBuilderException
     */
    public String build(TableEntity tab, 
            HashMap<String, String> params, 
            Pageable pageable, 
            Sort sort) throws QueryBuilderException;
}
