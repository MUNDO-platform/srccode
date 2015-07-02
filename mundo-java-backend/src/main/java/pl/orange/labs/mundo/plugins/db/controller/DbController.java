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
package pl.orange.labs.mundo.plugins.db.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.orange.labs.mundo.plugins.db.dao.ColumnRepository;
import pl.orange.labs.mundo.plugins.db.dao.DbEntity;
import pl.orange.labs.mundo.plugins.db.dao.DbRepository;
import pl.orange.labs.mundo.plugins.db.dao.TableEntity;
import pl.orange.labs.mundo.plugins.db.dao.TableRepository;

/**
 * Controller class responsible for displaying GUI with DB management.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Controller
@RequestMapping(value = "/gui")
public class DbController {
    
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(DbController.class);

    @Autowired
    private DbRepository dbRepository;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private ColumnRepository columnRepository;     
    
    /**
     * Display all configured database resources.
     * @return name of view file
     */
    @RequestMapping(value = "/user/db", method = RequestMethod.GET)
    public String displayDbs() {
        
        LOGGER.info("displayDbs");
        return "dbs";                
    }
    
    /**
     * Display all configured tables for selected database.
     * @param name - name of selected database
     * @param model
     * @return name of view file
     */
    @RequestMapping(value = "/user/db/{name}", method = RequestMethod.GET)
    public String displayTables(@PathVariable String name, Model model) {        
        
        LOGGER.info("displayTables");        
        DbEntity dbEntity = dbRepository.findByName(name);
        model.addAttribute("dbEntity", dbEntity);                
        return "tables";
    } 
    
    /**
     * Display all configured columns dor selected table.
     * @param dbName - name of selected database
     * @param tableName - name of selected table
     * @param model
     * @return name of view file
     */
    @RequestMapping(value = "/user/db/{dbName}/{tableName}", method = RequestMethod.GET)
    public String displayColumns(@PathVariable String dbName, @PathVariable String tableName, Model model) {        
        
        LOGGER.info("displayColumns");        
        DbEntity dbEntity = dbRepository.findByName(dbName);
        model.addAttribute("dbEntity", dbEntity); 
        TableEntity tableEntity = tableRepository.findByName(tableName);
        model.addAttribute("tableEntity", tableEntity); 
        return "columns";
    }     
}

