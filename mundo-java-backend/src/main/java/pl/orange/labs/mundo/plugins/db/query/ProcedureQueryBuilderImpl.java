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
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import pl.orange.labs.mundo.plugins.db.dao.TableEntity;
import pl.orange.labs.mundo.plugins.db.exception.CheckParamException;
import pl.orange.labs.mundo.plugins.db.exception.QueryBuilderException;

/**
 * Query builder for Embdeded procedure.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
public class ProcedureQueryBuilderImpl implements QueryBuilder {    

    private static final Logger LOGGER = Logger.getLogger(ProcedureQueryBuilderImpl.class);
    
    @Override
    public String build(TableEntity tab, HashMap<String, String> params, Pageable pageable, Sort sort) throws QueryBuilderException {
        
        try {
            
            Assert.notNull(tab, "TableEntity is null.");
            Assert.notNull(params, "UserParams HashMap is null.");
            
            String query = tab.getParams();
            
            Assert.notNull(query, "Embdeded procedure definition does not contain any params.");

            //check requested parameters
            String patternString = "\\$(.+?)\\$";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(query);

            while (matcher.find()) {

                String requestedKey = matcher.group(1);

                //if params HashMap does not contain requested key QueryBuilderException is throws.
                if (!params.containsKey(requestedKey)) {
                    throw new CheckParamException("Parameter " + requestedKey + " is requested.");
                }
            }        

            //insert values to proper params
            for (Entry<String, String> entry : params.entrySet()) {

                query = query.replace("$" + entry.getKey() + "$", entry.getValue());
            }                   

            return query;            
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            throw new QueryBuilderException(e.getMessage());            
        }        
    }
}
