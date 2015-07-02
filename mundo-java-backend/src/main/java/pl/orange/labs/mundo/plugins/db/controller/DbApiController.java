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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.orange.labs.mundo.common.Assist;
import pl.orange.labs.mundo.plugins.db.cachedservice.DbCachedService;
import pl.orange.labs.mundo.plugins.db.dao.ColumnEntity;
import pl.orange.labs.mundo.plugins.db.dao.ColumnRepository;
import pl.orange.labs.mundo.plugins.db.dao.DbEntity;
import pl.orange.labs.mundo.plugins.db.dao.DbRepository;
import pl.orange.labs.mundo.plugins.db.dao.DbResultRow;
import pl.orange.labs.mundo.plugins.db.dao.TableEntity;
import pl.orange.labs.mundo.plugins.db.dao.TableRepository;
import pl.orange.labs.mundo.plugins.db.exception.CheckParamException;
import pl.orange.labs.mundo.plugins.db.exception.DbException;
import pl.orange.labs.mundo.plugins.db.service.DbService;
import pl.orange.labs.mundo.service.ConfigService;

/**
 * Controls REST API requests for db resources.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RestController
@RequestMapping("/api/db")
public class DbApiController {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DbApiController.class); 
    
    @Autowired
    private DbRepository dbRepository;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private ColumnRepository columnRepository; 
    @Autowired
    private DbService dbService;
    @Autowired
    private ConfigService configService; 
    @Autowired
    private HttpServletRequest httpServletRequest;
    
    @Autowired
    private DbCachedService dbCachedService;
  
    
    public DbApiController(){}
    
    /**
     * Constructor.
     * @param dbRepository
     * @param tableRepository
     * @param columnRepository 
     * @param dbService 
     * @param configService 
     */
    public DbApiController(
            DbRepository dbRepository, 
            TableRepository tableRepository, 
            ColumnRepository columnRepository, 
            DbService dbService, 
            ConfigService configService,
            HttpServletRequest httpServletRequest
    ) {
        
        this.dbRepository = dbRepository;
        this.tableRepository = tableRepository;
        this.columnRepository = columnRepository;
        this.dbService = dbService;
        this.configService = configService;
        this.httpServletRequest = httpServletRequest;
    }    
    
    /**
     * Returns db resources.
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getDbList() {
        
        LOGGER.info("getDbList");
        try{
            List<DbEntity> result =  dbRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e) { 
            LOGGER.fatal(e);
            return new ResponseEntity<>("Error in request processing (mundo-java-backend):"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } 

    } 

    /**
     * Returns db resources.
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "", method = RequestMethod.HEAD, produces = "application/json; charset=utf-8")
    public ResponseEntity getDbListHead() {
        
        LOGGER.info("getDbList");
        try{
            List<DbEntity> result =  dbRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e) { 
            LOGGER.fatal(e);
            return new ResponseEntity<>("Error in request processing (mundo-java-backend):"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } 

    } 

    /**
     * Returns tables created in db resource.
     * @param dbName
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "/{dbName}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getTableList(@PathVariable String dbName) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("getTableList - for dbName="+dbName);
        
        try {
            
            DbEntity dbEntity = dbRepository.findByName(dbName);
            Assert.notNull(dbEntity, "Db not found.");
            List<TableEntity> result = tableRepository.findByDbId(dbEntity.getId());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Exception e) { 
            LOGGER.fatal(e);
            return new ResponseEntity<>("Error in request processing (mundo-java-backend):"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } 

    }  

    /**
     * Returns tables created in db resource.
     * @param dbName
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "/{dbName}", method = RequestMethod.HEAD, produces = "application/json; charset=utf-8")
    public ResponseEntity getTableListHead(@PathVariable String dbName) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("getTableList - for dbName="+dbName);
        
        try {
            
            DbEntity dbEntity = dbRepository.findByName(dbName);
            Assert.notNull(dbEntity, "Db not found.");
            List<TableEntity> result = tableRepository.findByDbId(dbEntity.getId());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Exception e) { 
            LOGGER.fatal(e);
            return new ResponseEntity<>("Error in request processing (mundo-java-backend):"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } 

    }  

    /**
     * Returns data from selected table.
     * @param dbName
     * @param tableName
     * @param page
     * @param size
     * @param orderBy
     * @return ResponseEntity with result.
     */  
    @RequestMapping(value = {"/{dbName}/{tableName}", "/{dbName}/{tableName}.json"}, 
            method = RequestMethod.GET, produces = "application/json; charset=utf-8")    
    public ResponseEntity getData(
            @PathVariable String dbName,
            @PathVariable String tableName,            
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,            
            @RequestParam(required = false) String orderBy            
    ) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("getData - for dbName="+dbName+" tableName"+tableName);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("page="+page+" size="+size+" orderBy="+orderBy);

        try {
            
            DbEntity dbEntity = dbRepository.findByName(dbName);
            Assert.notNull(dbEntity, "Db not found.");
            LOGGER.info(dbEntity);
            TableEntity tableEntity = tableRepository.findByNameAndDbId(tableName, dbEntity.getId());
            Assert.notNull(tableEntity, "Table not found.");
            LOGGER.info(tableEntity);             
            List<ColumnEntity> columnList = columnRepository.findByTabId(tableEntity.getId());        
            Assert.notNull(columnList, "Column collection not found.");
            Assert.isTrue(columnList.size() > 0, "Column collection is empty.");  
            LOGGER.info(columnList); 
            
            size = size != null ? size : configService.getStringValueForConfigVariable("db.params.pageSize");
            Assert.notNull(size, "Param size has wrong format.");
            Assert.isTrue(Integer.parseInt(size) > 0, "Param size has to be positive integer.");  
            Assert.isTrue(Integer.parseInt(size) <= 1000, "Param size has to be les or equal than 1000");  
            Assert.isInstanceOf(Integer.class, Integer.parseInt(size), "Param size is not numeric."); 
            
            page = page != null ? page : "1";
            Assert.notNull(page, "Param page has wrong format.");
            Assert.isTrue(Integer.parseInt(page) > 0, "Param page has to be positive integer.");  
            Assert.isInstanceOf(Integer.class, Integer.parseInt(page), "Param page is not numeric.");             
            
            Pageable pageable = new PageRequest(Integer.valueOf(page), Integer.valueOf(size));
            
            if (orderBy == null) {
                
                Iterator<ColumnEntity> iterator = columnList.iterator();
                Assert.notNull(iterator, "Column collection has wrong format.");
                Assert.isTrue(iterator.hasNext(), "Column collection is empty.");                  
                orderBy = iterator.next().getName();                
            }  
            Assert.notNull(orderBy, "Param orderBy is null.");
            
            Sort sort = new Sort(Sort.Direction.ASC, orderBy);

            HashMap<String, String> paramsMap = getParamsMap(httpServletRequest);
            
            List<DbResultRow> resultRowList = dbService.findAll(dbEntity, tableEntity, columnList, paramsMap, pageable, sort);
            return new ResponseEntity<>(resultRowList, HttpStatus.OK);
        }
        catch (NumberFormatException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>("NumberFormatException", HttpStatus.BAD_REQUEST);
        }         
        catch (IllegalArgumentException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (CheckParamException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }                 
        catch (DbException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } 
        catch (Exception e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);            
        }        
    } 

    
    /**
     * Returns data from selected table.
     * @param dbName
     * @param tableName
     * @param page
     * @param size
     * @param orderBy
     * @return ResponseEntity with result.
     */  
    @RequestMapping(value = {"/{dbName}/{tableName}", "/{dbName}/{tableName}.json"}, 
            method = RequestMethod.HEAD, produces = "application/json; charset=utf-8")    
    public ResponseEntity getDataHead(
            @PathVariable String dbName,
            @PathVariable String tableName,            
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,            
            @RequestParam(required = false) String orderBy            
    ) {
        if (LOGGER.isInfoEnabled()) LOGGER.info("getData - for dbName="+dbName+" tableName"+tableName);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("page="+page+" size="+size+" orderBy="+orderBy);

        try {
            
            DbEntity dbEntity = dbRepository.findByName(dbName);
            Assert.notNull(dbEntity, "Db not found.");
            LOGGER.info(dbEntity);
            TableEntity tableEntity = tableRepository.findByNameAndDbId(tableName, dbEntity.getId());
            Assert.notNull(tableEntity, "Table not found.");
            LOGGER.info(tableEntity);             
            List<ColumnEntity> columnList = columnRepository.findByTabId(tableEntity.getId());        
            Assert.notNull(columnList, "Column collection not found.");
            Assert.isTrue(columnList.size() > 0, "Column collection is empty.");  
            LOGGER.info(columnList); 
            
            size = size != null ? size : configService.getStringValueForConfigVariable("db.params.pageSize");
            Assert.notNull(size, "Param size has wrong format.");
            Assert.isTrue(Integer.parseInt(size) > 0, "Param size has to be positive integer.");  
            Assert.isTrue(Integer.parseInt(size) <= 1000, "Param size has to be les or equal than 1000");  
            Assert.isInstanceOf(Integer.class, Integer.parseInt(size), "Param size is not numeric."); 
            
            page = page != null ? page : "1";
            Assert.notNull(page, "Param page has wrong format.");
            Assert.isTrue(Integer.parseInt(page) > 0, "Param page has to be positive integer.");  
            Assert.isInstanceOf(Integer.class, Integer.parseInt(page), "Param page is not numeric.");             
            
            Pageable pageable = new PageRequest(Integer.valueOf(page), Integer.valueOf(size));
            
            if (orderBy == null) {
                
                Iterator<ColumnEntity> iterator = columnList.iterator();
                Assert.notNull(iterator, "Column collection has wrong format.");
                Assert.isTrue(iterator.hasNext(), "Column collection is empty.");                  
                orderBy = iterator.next().getName();                
            }  
            Assert.notNull(orderBy, "Param orderBy is null.");
            
            Sort sort = new Sort(Sort.Direction.ASC, orderBy);

            HashMap<String, String> paramsMap = getParamsMap(httpServletRequest);
            
            List<DbResultRow> resultRowList = dbService.findAll(dbEntity, tableEntity, columnList, paramsMap, pageable, sort);
            return new ResponseEntity<>(resultRowList, HttpStatus.OK);
        }
        catch (NumberFormatException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>("NumberFormatException", HttpStatus.BAD_REQUEST);
        }         
        catch (IllegalArgumentException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (CheckParamException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }                 
        catch (DbException e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        } 
        catch (Exception e) {
            
            LOGGER.fatal(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);            
        }        
    } 

    /**
     * Builds HashMap<String, String> grom queryString params.
     * @param httpServletRequest
     * @return HashMap
     */
    private HashMap<String, String> getParamsMap(HttpServletRequest httpServletRequest) {

        HashMap<String, String> paramsMap = new HashMap<String, String>();
        Map<String, String[]> requestParams = httpServletRequest.getParameterMap();
        
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {

            String key = entry.getKey();         // parameter name
            String[] value = entry.getValue();   // parameter values as array of String
            String valueString = "";
            
            if (value.length > 1) {

                for (int i = 0; i < value.length; i++) {
                
                    valueString += value[i];
                }
                } else {

                    valueString = value[0];
                }
            
            paramsMap.put(key, Assist.transcodedUtf8String(valueString));
        }        

        return paramsMap;
    } 
    
  
    
}
