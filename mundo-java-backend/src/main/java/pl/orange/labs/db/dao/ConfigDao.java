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

package pl.orange.labs.db.dao;

import java.util.HashMap;
import java.util.List;
import org.springframework.data.domain.Sort;

import pl.orange.labs.db.entities.ConfigEntity;

/**
 * Configuration 
 * @author Henryk Rosa <henryk.rosa@orange.com>
 *
 */
public interface ConfigDao {

    /**
     * Provides the list of config variables from DataBase
     * @return list of all config variables.
     */
    List<ConfigEntity> getListOfConfigVariables();

    /**
     * modification of Config variable
     * @param configVariable
     */
    void update(ConfigEntity configVariable);

    /**
     * adding new Config variable
     * @param configVariable
     */
    void addNew(ConfigEntity configVariable);

    /**
     * returns ConfigVariableFormDb object for id
     * @param id
     * @return ConfigEntity object
     */
    ConfigEntity getById(int id);

    /**
     * removes ConfigEntity 
     * @param configVariable
     */
    void remove(ConfigEntity configVariable);
        
    /**
     * Returns Map of all config variables.
     * @return HashMap<String,String>
     */
    public HashMap<String, String> getAllVariables(); 
    
    /**
     * Provides the list of config variables from DataBase
     * @return list of all variables from db.
     */
    List<ConfigEntity> findAll();
    /**
     * Provides the list of config variables from DataBase with sorting
     * @param sort
     * @return sorted list of all variables from db
     */
    List<ConfigEntity> findAll(Sort sort);    
    /**
     * returns ConfigVariableFormDb object for id
     * @param id
     * @return ConfigEntity - object selected from db
     */
    ConfigEntity findById(Integer id);
    /**
     * returns ConfigVariableFormDb object by key.
     * @param key
     * @return ConfigEntity - object selected from db
     */
    ConfigEntity findByKey(String key);     
    /**
     * Saves config variable to db.
     * @param configEntity
     * @return ConfigEntity - object saved to db.
     */
    ConfigEntity save(ConfigEntity configEntity);
    /**
     * Deletes variable form db
     * @param id - identifier of deleted variable
     */
    void delete(Integer id); 
    /**
     * Deletes all variable from db.
     */
    void deleteAll();
}
