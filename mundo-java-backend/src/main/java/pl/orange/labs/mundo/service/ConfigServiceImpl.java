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

package pl.orange.labs.mundo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import pl.orange.labs.db.entities.ConfigEntity;
import pl.orange.labs.db.dao.ConfigDao;
import pl.orange.labs.mundo.exceptions.ConfigException;


/**
 * Provides methods for configuration variables from data base reading/parsing
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@Component
public class ConfigServiceImpl implements ConfigService {
    
    private static Logger LOGGER = Logger.getLogger(ConfigServiceImpl.class);
    private List<ConfigEntity> variables = new ArrayList<ConfigEntity>();
    public static String LACK_OF_PARAM_MESSAGE = "Unable to read ConfigParameter from DataBase"; 
    
    @Autowired
    private ConfigDao confdb;
    
    public ConfigServiceImpl(){}

    public ConfigServiceImpl(ConfigDao confdb){
        this.confdb = confdb;
    }

    //possible to add static KeyValues for new config variables
    //or use String values from DataBase
     /**
     * Initiates config variables from data base
     */
    @Override
    @PostConstruct
    public void initFromDb() {
        
        variables = confdb.getListOfConfigVariables();
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Config variables re-read from DataBase; Number of elements=" + this.variables.size());
    }
    
    /**
     * Returns String value for configuration variable specified by key 
     * @param key
     * @return String
     * @throws ConfigException 
     */
    @Override
    public String getStringValueForConfigVariable (String key) throws ConfigException {
        
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Checking config varialbe for key=" + key);
        Iterator<ConfigEntity> iterator = this.variables.iterator();
        
        while (iterator.hasNext()) {
            
            ConfigEntity cfv = iterator.next();
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Found: " + cfv.getKey());
            if (cfv.getKey().equals(key))
            return cfv.getValue();
        }
        
        LOGGER.error("Config variable String value not found for specified key=" + key);
        throw new ConfigException(LACK_OF_PARAM_MESSAGE, key);
    }

    /**
     * Returns ConfigServiceImpl variable object for specified key
     * @param key
     * @return ConfigEntity
     * @throws ConfigException 
     */
    @Override
    public ConfigEntity getConfigVariableObject (String key) throws ConfigException {
        
        Iterator<ConfigEntity> iterator = this.variables.iterator();
        
        while (iterator.hasNext()) {
            
            ConfigEntity cfv = iterator.next();
            if (cfv.getKey().equals(key))
            return cfv;
	}
        
        LOGGER.error("Config variable object not found for specified key=" + key);
        throw new ConfigException(LACK_OF_PARAM_MESSAGE, key);
    }

    /**
     * Returns Int value for configuration variable specified by key 
     * -1 in case of error
     * @param key
     * @return integer
     * @throws ConfigException 
     */
    @Override
    public int getIntValueForConfigVariable (String key) throws ConfigException {
        
        Iterator<ConfigEntity> iterator = this.variables.iterator();
	
        while (iterator.hasNext()) {
            
            ConfigEntity cfv = iterator.next();
            if (cfv.getKey().equals(key))
            return Integer.parseInt(cfv.getValue());
        }
		
        LOGGER.error("Config variable Int value not found for specified key=" + key);
        throw new ConfigException(LACK_OF_PARAM_MESSAGE, key);
    }

	/**
	 * Returns boolean value for configuration variable specified by key
	 * @param key
	 * @return boolean
	 * @throws ConfigException 
	 */
    @Override
	public boolean getBolleanValueForConfigVariable (String key) throws ConfigException {
		Iterator<ConfigEntity> iterator = this.variables.iterator();
		while (iterator.hasNext()) {
			ConfigEntity cfv = iterator.next();
			if (cfv.getKey().equals(key))
				return Boolean.parseBoolean(cfv.getValue());
		}
		
		LOGGER.error("Config variable Bolean value not found for specified key=" + key);
		throw new ConfigException(LACK_OF_PARAM_MESSAGE, key);
	}

        @Override
	public List<ConfigEntity> getVariables() {
		return variables;
	}

        @Override
	public void setVariables(List<ConfigEntity> variables) {
		this.variables = variables;
	}

    @Override
    public List<ConfigEntity> findAll() {

        return confdb.findAll();
    }

    @Override
    public List<ConfigEntity> findAll(Sort sort) {

        return confdb.findAll(sort);
    }

    @Override
    public ConfigEntity findById(Integer id) {

        return confdb.getById(id);
    }

    @Override
    public ConfigEntity findByKey(String key) {

        return confdb.findByKey(key);
    }

    @Override
    public ConfigEntity save(ConfigEntity configEntity) {

        return confdb.save(configEntity);
    }

    @Override
    public void delete(Integer id) {

        confdb.delete(id);
    }

    @Override
    public void deleteAll() {

        confdb.deleteAll();
    }

    @Override
    public HashMap<String, String> getAllVariables() {

        return confdb.getAllVariables();
    }
}
