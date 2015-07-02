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

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.orange.labs.db.entities.ConfigEntity;

/**
 * Implementation of access to configuration data in DB
 * @author Henryk Rosa <henryk.rosa@orange.com>
 *
 */
@Transactional
@Repository
public class ConfigDaoImpl implements ConfigDao {
    
    /**
     * Logger.
     */
    private static Logger LOGGER = Logger.getLogger(ConfigDaoImpl.class);
    
    @Autowired
    private ConfigRepository configRepository; 
    
    /**
     * Constructor.
     */
    public ConfigDaoImpl(){}
    
    /**
     * Constructor. 
     * @param configRepository
     */
    public ConfigDaoImpl(ConfigRepository configRepository) {
        
        this.configRepository = configRepository;
    }
    
    /*
     * Time stamp for update and adding entities
     */
    private Timestamp getCurrentTimeStamp() {
        
        Date d = new Date();
        return new Timestamp(d.getTime());
    }
	
    @Override
    public List<ConfigEntity> getListOfConfigVariables() {
        
        return configRepository.findAll();
    }
	
    @Override
    public void addNew(ConfigEntity configVariable) {
    	if (LOGGER.isInfoEnabled()) LOGGER.info("addNew config variable: "+configVariable.getKey());
        configVariable.setLast_modified(getCurrentTimeStamp());
        configRepository.save(configVariable);
    }

    @Override
    public void update(ConfigEntity configVariable) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("Update config variable: "+configVariable.getKey());
        configVariable.setLast_modified(getCurrentTimeStamp());
        configRepository.save(configVariable);
    }

    @Override
    public void remove(ConfigEntity configVariable) {
    	if (LOGGER.isInfoEnabled()) LOGGER.info("Remove config variable: "+configVariable.getKey());
        configRepository.delete(configVariable);
    }

    @Override
    public ConfigEntity getById(int id) {
        
        ConfigEntity tmp = configRepository.findById(id);
        return tmp;
    }

    /**
     * Gets all variables from db.
     * @return 
     */
    @Override
    @Cacheable("mundoCache")
    public HashMap<String, String> getAllVariables() {
        
        List<ConfigEntity> configList = configRepository.findAll();
        HashMap<String, String> result = new HashMap<String, String>();
        
        for (ConfigEntity configEntity : configList){
            
            result.put(configEntity.getKey(), configEntity.getValue());
        }
        
        return result;
    }

    @Override
    public List<ConfigEntity> findAll() {
        
        return configRepository.findAll();
    }

    @Override
    public List<ConfigEntity> findAll(Sort sort) {
        
        return configRepository.findAll(sort);
    }

    @Override
    public ConfigEntity findById(Integer id) {

        return configRepository.findById(id);
    }

    @Override
    public ConfigEntity findByKey(String key) {
        
        return configRepository.findByKey(key);
    }

    @Override
    public ConfigEntity save(ConfigEntity configEntity) {

        return configRepository.save(configEntity);
    }

    @Override
    public void delete(Integer id) {

        configRepository.delete(id);
    }

    @Override
    public void deleteAll() {

        configRepository.deleteAll();
    }
}

