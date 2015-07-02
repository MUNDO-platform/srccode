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

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.orange.labs.mundo.plugins.db.dao.ColumnEntity;
import pl.orange.labs.mundo.plugins.db.dao.ColumnRepository;
import pl.orange.labs.mundo.plugins.db.dao.DbEntity;
import pl.orange.labs.mundo.plugins.db.dao.DbRepository;
import pl.orange.labs.mundo.plugins.db.dao.TableEntity;
import pl.orange.labs.mundo.plugins.db.dao.TableRepository;

/**
 * Controls REST requests for db resources page.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RestController
@RequestMapping("/gui")
public class DbRestController {
    
    /**
     * Logger.
     */
    private static Logger LOGGER = Logger.getLogger(DbRestController.class);    
    
    @Autowired
    private DbRepository dbRepository; 
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private ColumnRepository columnRepository;
    
    /**
     * Default constructor.
     */
    public DbRestController(){}
    
    /**
     * Constructor.
     * @param dbRepository
     * @param tableRepository
     * @param columnRepository 
     */
    public DbRestController(DbRepository dbRepository, TableRepository tableRepository, ColumnRepository columnRepository)
    {
        this.dbRepository = dbRepository;
        this.tableRepository = tableRepository;
        this.columnRepository = columnRepository;
    }
    
    /**
     * Method responsible for returning all sorted dbs from db.
     * @return ResponseEntity with sorted dbs in http body and 200 OK in status.
     */
    @RequestMapping(value = "/user/db", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getDbs() {
        
        List<DbEntity> result =  dbRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }  
    
    /**
     * Method responsible for creating new dbs in db.
     * @param db - DbEntity object in JSON format.
     * @return ResponseEntity with 201 CREATED as status.
     */
    @RequestMapping(value = "/admin/db", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity createDb(@RequestBody final DbEntity db) {
        
        try {    
            
            DbEntity result = dbRepository.save(db);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch (DataIntegrityViolationException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }  
    
    /**
     * Method responsible for updating dbs in DB.
     * @param db - DbEntity object in JSON format.
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "/admin/db", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
    public ResponseEntity updateDb(@RequestBody final DbEntity db) {
        
        try {
            
            DbEntity result = dbRepository.save(db);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (DataIntegrityViolationException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }        
    }     
    
    /**
     * Method responsible for deleting dbs in DB.
     * @param id - identifier of deleting dbs
     * @return ResponseEntity with id in body.
     */
    @RequestMapping(value = "/admin/db/{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    public ResponseEntity removeDb(@PathVariable Long id) {
        
        try {
            
            dbRepository.delete(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        catch (EmptyResultDataAccessException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }
    } 
    
    /**
     * Method responsible for returning all tables in selected database.
     * @param name - name of selected database
     * @return ResponseEntity with tables in http body and 200 OK in status.
     */
    @RequestMapping(value = "/user/db/{name}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getTables(@PathVariable String name) {
        
        try {
            
            DbEntity dbEntity = dbRepository.findByName(name);
            Assert.notNull(dbEntity, "dbName not found.");
            List<TableEntity> list = tableRepository.findByDbId(dbEntity.getId());
            
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }        
        catch (EmptyResultDataAccessException e) {
            
            return new ResponseEntity<>(name, HttpStatus.NOT_FOUND);
        }
    } 
    
    /**
     * Method responsible for creaing new table for selected database.
     * @param name - name of selected database
     * @param table - TableEntity object in JSON format
     * @return ResponseEntity with 201 CREATED as status.
     */
    @RequestMapping(value = "/admin/db/{name}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity createTable(@PathVariable String name, @RequestBody final TableEntity table) {
        
        try {    
            
            DbEntity dbEntity = dbRepository.findByName(name);
            Assert.notNull(dbEntity, "dbName not found.");
            Assert.isTrue(dbEntity.getId().equals(table.getDbId()), "Wrong dbId!");
            TableEntity result = tableRepository.save(table);
            return new ResponseEntity<>(result, HttpStatus.CREATED);        
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }         
        catch (DataIntegrityViolationException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }        
    }
    
    /**
     * Method responsible for updating table for selected database.
     * @param name - name of selected database
     * @param table - TableEntity object in JSON format
     * @return ResponseEntity with result
     */
    @RequestMapping(value = "/admin/db/{name}", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
    public ResponseEntity updateTable(@PathVariable String name, @RequestBody final TableEntity table) {
        
        try {
            
            DbEntity dbEntity = dbRepository.findByName(name);
            Assert.notNull(dbEntity, "dbName not found.");
            Assert.isTrue(dbEntity.getId().equals(table.getDbId()), "Wrong dbId!");            
            TableEntity result = tableRepository.save(table);
            return new ResponseEntity<>(result, HttpStatus.CREATED);        
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }         
        catch (DataIntegrityViolationException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }          
    } 
    
    /**
     * Method responsible for deleting table from selected database.
     * @param name name of selected database
     * @param id - identifier of deteted table
     * @return ResponseEntity with id of deleted table.
     */
    @RequestMapping(value = "/admin/db/{name}/{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    public ResponseEntity removeTable(@PathVariable String name, @PathVariable Long id) {
        
        try {
            
            DbEntity dbEntity = dbRepository.findByName(name);
            Assert.notNull(dbEntity, "dbName not found.");            
            tableRepository.delete(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }         
        catch (EmptyResultDataAccessException e) {
            
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }    
    }  
    
    /**
     * Method responsible for returning all column configured for table.
     * @param dbName - name of selected database
     * @param tableName - name of selected table
     * @return ResponseEntity with list of configured column
     */
    @RequestMapping(value = "/user/db/{dbName}/{tableName}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getColumns(@PathVariable String dbName, @PathVariable String tableName) {
        
        try {
            
            DbEntity dbEntity = dbRepository.findByName(dbName);
            Assert.notNull(dbEntity, "dbName not found.");
            TableEntity tableEntity = tableRepository.findByNameAndDbId(tableName, dbEntity.getId());
            Assert.notNull(tableEntity, "tableName not found.");
            List<ColumnEntity> list = columnRepository.findByTabId(tableEntity.getId());
            
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }        
        catch (EmptyResultDataAccessException e) {
            
            return new ResponseEntity<>(tableName, HttpStatus.NOT_FOUND);
        }
    } 
    
    /**
     * Method responsible for creating new column in selected table
     * @param dbName - name of selected database
     * @param tableName - name of selected table
     * @param column - ColumnEntity object in JSON format
     * @return ResponseEntity with 201 CREATED as status
     */
    @RequestMapping(value = "/admin/db/{dbName}/{tableName}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity createColumn(@PathVariable String dbName, @PathVariable String tableName, @RequestBody final ColumnEntity column) {
        
        try {
            
            DbEntity dbEntity = dbRepository.findByName(dbName);
            Assert.notNull(dbEntity, "dbName not found.");  
            TableEntity tableEntity = tableRepository.findByNameAndDbId(tableName, dbEntity.getId());
            Assert.notNull(tableEntity, "tableName not found.");            
            ColumnEntity result = columnRepository.save(column);
            return new ResponseEntity<>(result, HttpStatus.CREATED);        
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }         
        catch (DataIntegrityViolationException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }         
    }
    
    /**
     * Method responsible for updating column in selected table
     * @param dbName - name of selected database
     * @param tableName - name of selected table
     * @param column - ColumnEntity object in JSON format
     * @return ResponseEntity with result
     */
    @RequestMapping(value = "/admin/db/{dbName}/{tableName}", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
    public ResponseEntity updateColumn(@PathVariable String dbName, @PathVariable String tableName, @RequestBody final ColumnEntity column) {
        
        try {
            
            DbEntity dbEntity = dbRepository.findByName(dbName);
            Assert.notNull(dbEntity, "dbName not found.");  
            TableEntity tableEntity = tableRepository.findByNameAndDbId(tableName, dbEntity.getId());
            Assert.notNull(tableEntity, "tableName not found.");         
            ColumnEntity result = columnRepository.save(column);
            return new ResponseEntity<>(result, HttpStatus.CREATED);        
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }         
        catch (DataIntegrityViolationException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }         
    } 
    
    /**
     * Method responsible for deleting column from selected table
     * @param dbName - name of selected database
     * @param tableName - name of selected table
     * @param id - identifier of deleted column
     * @return  ResponseEntity with deleted column id
     */
    @RequestMapping(value = "/admin/db/{dbName}/{tableName}/{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    public ResponseEntity removeColumn(@PathVariable String dbName, @PathVariable String tableName, @PathVariable Long id) {
        
        try {
            
            DbEntity dbEntity = dbRepository.findByName(dbName);
            Assert.notNull(dbEntity, "dbName not found.");  
            TableEntity tableEntity = tableRepository.findByNameAndDbId(tableName, dbEntity.getId());
            Assert.notNull(tableEntity, "tableName not found.");             
            columnRepository.delete(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }         
        catch (EmptyResultDataAccessException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }
    }     
}
