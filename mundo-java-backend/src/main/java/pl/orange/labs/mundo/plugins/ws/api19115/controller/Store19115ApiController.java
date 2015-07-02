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
	
package pl.orange.labs.mundo.plugins.ws.api19115.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.orange.labs.mundo.common.Assist;
import pl.orange.labs.mundo.plugins.ws.api19115.dao.Api19115Entity;
import pl.orange.labs.mundo.plugins.ws.api19115.dao.Api19115Repository;
import pl.orange.labs.mundo.plugins.ws.api19115.dao.Api19115Service;
import pl.orange.labs.mundo.plugins.ws.api19115.entity.SendIncidentJsonContent;
import pl.orange.labs.mundo.plugins.ws.api19115.exception.Api19115Exception;

/**
 * Controller for 19115 API requests.
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@RestController
@RequestMapping("/api/api19115")
public class Store19115ApiController {
    
    private static final Logger LOGGER = Logger.getLogger(Store19115ApiController.class); 
    
    
     
    @Autowired
    private final Api19115Service api19115Service;
    @Autowired
    private final Api19115Repository api19115Repository; 
    
    @Autowired
    public Store19115ApiController(Api19115Repository api19115Repository, Api19115Service api19115Service) 
    {
        this.api19115Repository = api19115Repository;
        this.api19115Service = api19115Service;
    }  
    
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getApi19115List() {
        
        LOGGER.info("getApi19115List");
        List<Api19115Entity> list = api19115Repository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }    
    
    
    @RequestMapping(value = "/{name}/getNotifications", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getNotifications(
    		@PathVariable String name,
    		@RequestParam(required = false) String filters,
            @RequestParam(required = false) String operators
    		) {
        try {
        	//{"filters":[{"field":"DISTRICT","operator":"EQ","value":"Wola"}],"operators":null}
            Api19115Entity apiEntity = api19115Repository.findByName(name);
            Assert.notNull(apiEntity, "Api not found for name:"+name);

            
            String filtersJson=null;
            if ((filters!=null)&&(operators!=null)) {
            	filters = Assist.transcodedUtf8String(filters);
            	operators = Assist.transcodedUtf8String(operators);
            	filtersJson="{"+filters+","+operators+"}";
            }

            String result = null;
            if (filtersJson==null) {
                Date d =new Date();
                long tsCurrent = d.getTime();
                long ts2DaysBefore = tsCurrent - (long)172800000;
            	String params = "{\"dateFrom\":\""+ts2DaysBefore+"\", \"dateTo\":\""+tsCurrent+"\"}";
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"getNotificationsForDate", params, null);
            } 
            else {
                result = api19115Service.makeApiRequest(apiEntity.getUrl()+"getNotifications", filtersJson, null);
            }
            
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }      

    @RequestMapping(value = "/{name}/getNotifications", method = RequestMethod.HEAD, produces = "application/json; charset=utf-8")
    public ResponseEntity getNotificationsHead(
    		@PathVariable String name,
    		@RequestParam(required = false) String filters,
            @RequestParam(required = false) String operators
    		) {
        try {
        	//{"filters":[{"field":"DISTRICT","operator":"EQ","value":"Wola"}],"operators":null}
            Api19115Entity apiEntity = api19115Repository.findByName(name);
            Assert.notNull(apiEntity, "Api not found for name:"+name);

            
            String filtersJson=null;
            if ((filters!=null)&&(operators!=null)) {
            	filters = Assist.transcodedUtf8String(filters);
            	operators = Assist.transcodedUtf8String(operators);
            	filtersJson="{"+filters+","+operators+"}";
            }

            String result = null;
            if (filtersJson==null) {
                Date d =new Date();
                long tsCurrent = d.getTime();
                long ts2DaysBefore = tsCurrent - (long)172800000;
            	String params = "{\"dateFrom\":\""+ts2DaysBefore+"\", \"dateTo\":\""+tsCurrent+"\"}";
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"getNotificationsForDate", params, null);
            } 
            else {
                result = api19115Service.makeApiRequest(apiEntity.getUrl()+"getNotifications", filtersJson, null);
            }
            
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }      
    
    @RequestMapping(value = "/{name}/getNotifications", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity getNotificationsPost(
    		@PathVariable String name,
    		@RequestBody String content
    		) {
        try {
        	//{"filters":[{"field":"DISTRICT","operator":"EQ","value":"Wola"}],"operators":null}
            Api19115Entity apiEntity = api19115Repository.findByName(name);
            Assert.notNull(apiEntity, "Api not found for name:"+name);
            
            if (LOGGER.isInfoEnabled()) LOGGER.info("getNotifications POST request received for apiName:"+name);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestBody:"+content);
            
            String result = api19115Service.makeApiRequest(apiEntity.getUrl()+"getNotifications", content, null); 
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }      

    @RequestMapping(value = "/{name}/getNotificationsForDate", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getNotificationsForDate(
    		@PathVariable String name,
    		@RequestParam(required = false) String dateFrom, 
    		@RequestParam(required = false) String dateTo 
    		) {
        
        try {
            
            Api19115Entity apiEntity = api19115Repository.findByName(name);
            Assert.notNull(apiEntity, "Api not found for name:"+name);
            Assert.notNull(dateFrom, "Parameter 'dateFrom' is mandatory");
            Assert.notNull(dateTo, "Parameter 'dateTo' is mandatory");
            Assert.isTrue(!(dateFrom.length()<13), "Parameter 'dateFrom' is invalid");
            Assert.isTrue(!(dateTo.length()<13), "Parameter 'dateTo' is invalid");
            
            if (LOGGER.isInfoEnabled()) LOGGER.info("getNotificationsForDistrict GET request received for apiName:"+name);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestParams:dateFrom="+dateFrom+" dateTo="+dateTo);

            //TODO timestamValidation
            String params = "{\"dateFrom\":\""+dateFrom+"\", \"dateTo\":\""+dateTo+"\"}";
            
            String result = api19115Service.makeApiRequest(apiEntity.getUrl()+"getNotificationsForDate", params, null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }      
    
    @RequestMapping(value = "/{name}/getNotificationsForDistrict", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getNotificationsForDistrict(
    		@PathVariable String name,
    		@RequestParam(required = false) String district
    		) {
        
        try {
            
            Api19115Entity apiEntity = api19115Repository.findByName(name);
            Assert.notNull(apiEntity, "Api not found for name:"+name);
            Assert.notNull(district, "Parameter 'district' is mandatory");
            
            district= Assist.transcodedUtf8String(district);

            if (LOGGER.isInfoEnabled()) LOGGER.info("getNotificationsForDistrict GET request received for apiName:"+name);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestParams:district="+district);

 
            String params = "{\"district\":\""+district+"\"}";
            String result = api19115Service.makeApiRequest(apiEntity.getUrl()+"getNotificationsForDistrict", params, null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }
    
    @RequestMapping(value = "/{name}/getNotificationsForNotificationNumber", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getNotificationsForNotificationNumber(
    		@PathVariable String name,
       		@RequestParam(required = false) String notificationNumber
    		) {
        
        try {
            
            Api19115Entity apiEntity = api19115Repository.findByName(name);
            Assert.notNull(apiEntity, "Api not found for name:"+name);
            Assert.notNull(notificationNumber, "Parameter 'notificationNumber' is mandatory");
            if (LOGGER.isInfoEnabled()) LOGGER.info("getNotificationsForNotificationNumber GET request received for apiName:"+name);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestParams:notificationNumber="+notificationNumber);

            notificationNumber= Assist.transcodedUtf8String(notificationNumber);
  
            String params = "{\"notificationNumber\":\""+notificationNumber+"\"}";
            String result = api19115Service.makeApiRequest(apiEntity.getUrl()+"getNotificationsForNotificationNumber", params, null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }
    
    
    @RequestMapping(value = "/{name}/sendFreeform", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity sendFreeform(
    		@PathVariable String name, 
    		@RequestBody String content, 
    		@RequestHeader(value="Callback-Email", required=false) String callbackEmail,
    		@RequestHeader(value="Callback-Address", required=false) String callbackAddress
    		) {
        
        try {
            Api19115Entity apiEntity = api19115Repository.findByName(name);
            Assert.notNull(apiEntity, "Api not found for name:"+name);
            Assert.notNull(content, "Request must contain JSON content");
            Assert.isTrue(!content.isEmpty(), "Request content must not be empty");
            if (LOGGER.isInfoEnabled()) LOGGER.info("sendFreeform POST request received for apiName:"+name);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestBody:"+content);

            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestHeader - Callback-Email:"+callbackEmail);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestHeader - Callback-Address:"+callbackAddress);

            HashMap<String, String> httpHeaders = new HashMap<String, String>();
            if (callbackEmail!=null && !callbackEmail.isEmpty()){
            	httpHeaders.put("Callback-Email", callbackEmail);
            }
            if (callbackAddress!=null && !callbackAddress.isEmpty()){
            	httpHeaders.put("Callback-Address", callbackAddress);
            }

            String result="Unexpected Error";
            if (httpHeaders.size()>0)
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendFreeform", content,httpHeaders);
            else 
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendFreeform", content,null);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }  
    
    @RequestMapping(value = "/{name1}/sendFreeform", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity sendFreeformGet(
    		@PathVariable String name1, 
       		@RequestParam(required = false) String description,
    		@RequestParam(required = false) String email,
    		@RequestParam(required = false) String name,
    		@RequestParam(required = false) String lastName,
    		@RequestParam(required = false) String phoneNumber,
    		@RequestHeader(value="Callback-Email", required=false) String callbackEmail,
    		@RequestHeader(value="Callback-Address", required=false) String callbackAddress
    		) {
        
        try {
            
            Api19115Entity apiEntity = api19115Repository.findByName(name1);
            Assert.notNull(apiEntity, "Api not found for name:"+name1);
            Assert.notNull(email, "Param 'email' is mandatory");
            Assert.notNull(name, "Param 'name' is mandatory");
            Assert.notNull(lastName, "Param 'lastName' is mandatory");
            Assert.notNull(phoneNumber, "Param 'phoneNumber' is mandatory");
            Assert.isTrue(!description.isEmpty(), "Param 'description' is invalid");
            Assert.isTrue(!email.isEmpty(), "Param 'email' is invalid");
            Assert.isTrue(!name.isEmpty(), "Param 'name' is invalid");
            Assert.isTrue(!lastName.isEmpty(), "Param 'lastName' is invalid");
            Assert.isTrue(!phoneNumber.isEmpty(), "Param 'phoneNumber' is invalid");
 
            if (LOGGER.isInfoEnabled()) LOGGER.info("sendFreeform GET request received for apiName:"+name1);

            description= Assist.transcodedUtf8String(description);
            name= Assist.transcodedUtf8String(name);
            lastName= Assist.transcodedUtf8String(lastName);
            phoneNumber= Assist.transcodedUtf8String(phoneNumber);
 
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestHeader - Callback-Email:"+callbackEmail);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestHeader - Callback-Address:"+callbackAddress);

            SendIncidentJsonContent content = new SendIncidentJsonContent();
            content.setDescription(description);
            content.setEmail(email);
            content.setName(name);
            content.setLastName(lastName);
            content.setPhoneNumber(phoneNumber);
            
            String json = content.getJsonAcceptedForSendInformational();
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Json content prepared:"+json);

            HashMap<String, String> httpHeaders = new HashMap<String, String>();
            if (callbackEmail!=null && !callbackEmail.isEmpty()){
            	httpHeaders.put("Callback-Email", callbackEmail);
            }
            if (callbackAddress!=null && !callbackAddress.isEmpty()){
            	httpHeaders.put("Callback-Address", callbackAddress);
            }

            String result="Unexpected Error";
            if (httpHeaders.size()>0)
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendFreeform", json,httpHeaders);
            else
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendFreeform", json,null);
            
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }   
    
    @RequestMapping(value = "/{name}/sendIncident", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity sendIncident(
    		@PathVariable String name, 
    		@RequestBody String content,
    		@RequestHeader(value="Callback-Email", required=false) String callbackEmail,
    		@RequestHeader(value="Callback-Address", required=false) String callbackAddress
    		) {
        
        try {
            
            Api19115Entity apiEntity = api19115Repository.findByName(name);
            Assert.notNull(apiEntity, "Api not found for name:"+name);
            Assert.notNull(content, "Request must contain JSON content");
            Assert.isTrue(!content.isEmpty(), "Request content must not be empty");
            
            if (LOGGER.isInfoEnabled()) LOGGER.info("sendIncident POST request received for apiName:"+name);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestBody:"+content);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestHeader - Callback-Email:"+callbackEmail);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestHeader - Callback-Address:"+callbackAddress);

            HashMap<String, String> httpHeaders = new HashMap<String, String>();
            if (callbackEmail!=null && !callbackEmail.isEmpty()){
            	httpHeaders.put("Callback-Email", callbackEmail);
            }
            if (callbackAddress!=null && !callbackAddress.isEmpty()){
            	httpHeaders.put("Callback-Address", callbackAddress);
            }

            String result="Unexpected Error";
            if (httpHeaders.size()>0)
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendIncident", content, httpHeaders);
            else 
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendIncident", content, null);
            
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }

    @RequestMapping(value = "/{name1}/sendIncident", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity sendIncidentGet(
    		@PathVariable String name1, 
       		@RequestParam(required = false) String description,
    		@RequestParam(required = false) String email,
    		@RequestParam(required = false) String name,
    		@RequestParam(required = false) String lastName,
    		@RequestParam(required = false) String phoneNumber,
    		@RequestParam(required = false) String xCoordWGS84,
    		@RequestParam(required = false) String yCoordWGS84,
    		@RequestParam(required = false) String subcategory,
    		@RequestParam(required = false) String eventType,
    		@RequestParam(required = false) String street,
    		@RequestParam(required = false) String houseNumber,
       		@RequestParam(required = false) String apartmentNumber,
    		@RequestHeader(value="Callback-Email", required=false) String callbackEmail,
    		@RequestHeader(value="Callback-Address", required=false) String callbackAddress

    		) {
        
        try {
       
            Api19115Entity apiEntity = api19115Repository.findByName(name1);
            Assert.notNull(apiEntity, "Api not found for name:"+name1);
            Assert.notNull(description, "Param 'description' is mandatory");
            Assert.notNull(email, "Param 'email' is mandatory");
            Assert.notNull(name, "Param 'name' is mandatory");
            Assert.notNull(lastName, "Param 'lastName' is mandatory");
            Assert.notNull(phoneNumber, "Param 'phoneNumber' is mandatory");
            Assert.isTrue(!description.isEmpty(), "Param 'description' is invalid");
            Assert.isTrue(!email.isEmpty(), "Param 'email' is invalid");
            Assert.isTrue(!name.isEmpty(), "Param 'name' is invalid");
            Assert.isTrue(!lastName.isEmpty(), "Param 'lastName' is invalid");
            Assert.isTrue(!phoneNumber.isEmpty(), "Param 'phoneNumber' is invalid");
           
            if (LOGGER.isInfoEnabled()) LOGGER.info("sendIncident POST request received for apiName:"+name1);
 
            description= Assist.transcodedUtf8String(description);
            name= Assist.transcodedUtf8String(name);
            lastName= Assist.transcodedUtf8String(lastName);
            phoneNumber= Assist.transcodedUtf8String(phoneNumber);
            subcategory= Assist.transcodedUtf8String(subcategory);
            eventType= Assist.transcodedUtf8String(eventType);
            street= Assist.transcodedUtf8String(street);
            houseNumber= Assist.transcodedUtf8String(houseNumber);
            apartmentNumber= Assist.transcodedUtf8String(apartmentNumber);
            
            
            SendIncidentJsonContent content = new SendIncidentJsonContent();
            content.setDescription(description);
            content.setEmail(email);
            content.setName(name);
            content.setLastName(lastName);
            content.setPhoneNumber(phoneNumber);
            content.setxCoordWGS84(xCoordWGS84);
            content.setyCoordWGS84(yCoordWGS84);
            content.setSubcategory(subcategory);
            content.setEventType(eventType);
            content.setStreet(street);
            content.setHouseNumber(houseNumber);
            content.setApartmentNumber(apartmentNumber);

            String json = content.getJsonAcceptedForSendIncident(false);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Json content prepared:"+json);
            
            HashMap<String, String> httpHeaders = new HashMap<String, String>();
            if (callbackEmail!=null && !callbackEmail.isEmpty()){
            	httpHeaders.put("Callback-Email", callbackEmail);
            }
            if (callbackAddress!=null && !callbackAddress.isEmpty()){
            	httpHeaders.put("Callback-Address", callbackAddress);
            }

            String result="Unexpected Error";
            if (httpHeaders.size()>0)
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendIncident", json,httpHeaders);
            else 
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendIncident", json,null);
            
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }

    
    
    @RequestMapping(value = "/{name}/sendInformational", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity sendInformational(
    		@PathVariable String name, 
    		@RequestBody String content,
    		@RequestHeader(value="Callback-Email", required=false) String callbackEmail,
    		@RequestHeader(value="Callback-Address", required=false) String callbackAddress
    		) {
        
        try {
            
            Api19115Entity apiEntity = api19115Repository.findByName(name);
            Assert.notNull(apiEntity, "Api not found for name:"+name);
            Assert.notNull(content, "Request must contain JSON content");
            Assert.isTrue(!content.isEmpty(), "Request content must not be empty");

            if (LOGGER.isInfoEnabled()) LOGGER.info("sendInformational POST request received for apiName:"+name);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestBody:"+content);

            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestHeader - Callback-Email:"+callbackEmail);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestHeader - Callback-Address:"+callbackAddress);

            HashMap<String, String> httpHeaders = new HashMap<String, String>();
            if (callbackEmail!=null && !callbackEmail.isEmpty()){
            	httpHeaders.put("Callback-Email", callbackEmail);
            }
            if (callbackAddress!=null && !callbackAddress.isEmpty()){
            	httpHeaders.put("Callback-Address", callbackAddress);
            }

            String result="Unexpected Error";
            if (httpHeaders.size()>0)
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendInformational", content,httpHeaders);
            else 
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendInformational", content,null);
            
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }      

    @RequestMapping(value = "/{name1}/sendInformational", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity sendInformationalGet(
    		@PathVariable String name1, 
    		@RequestParam(required = false) String description,
    		@RequestParam(required = false) String email,
    		@RequestParam(required = false) String name,
    		@RequestParam(required = false) String lastName,
    		@RequestParam(required = false) String phoneNumber,
    		@RequestHeader(value="Callback-Email", required=false) String callbackEmail,
    		@RequestHeader(value="Callback-Address", required=false) String callbackAddress
    		) {
        
        try {
            Api19115Entity apiEntity = api19115Repository.findByName(name1);
            Assert.notNull(description, "Param 'description' is mandatory");
            Assert.notNull(email, "Param 'email' is mandatory");
            Assert.notNull(name, "Param 'name' is mandatory");
            Assert.notNull(lastName, "Param 'lastName' is mandatory");
            Assert.notNull(phoneNumber, "Param 'phoneNumber' is mandatory");
            Assert.isTrue(!description.isEmpty(), "Param 'description' is invalid");
            Assert.isTrue(!email.isEmpty(), "Param 'email' is invalid");
            Assert.isTrue(!name.isEmpty(), "Param 'name' is invalid");
            Assert.isTrue(!lastName.isEmpty(), "Param 'lastName' is invalid");
            Assert.isTrue(!phoneNumber.isEmpty(), "Param 'phoneNumber' is invalid");
            
            if (LOGGER.isInfoEnabled()) LOGGER.info("sendInformational GET request received for apiName:"+name1);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestHeader - Callback-Email:"+callbackEmail);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("RequestHeader - Callback-Address:"+callbackAddress);

            description= Assist.transcodedUtf8String(description);
            name= Assist.transcodedUtf8String(name);
            lastName= Assist.transcodedUtf8String(lastName);
            phoneNumber= Assist.transcodedUtf8String(phoneNumber);
  
            
            SendIncidentJsonContent content = new SendIncidentJsonContent();
            content.setDescription(description);
            content.setEmail(email);
            content.setName(name);
            content.setLastName(lastName);
            content.setPhoneNumber(phoneNumber);
            
            String json = content.getJsonAcceptedForSendInformational();
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Json content prepared:"+json);
            
            HashMap<String, String> httpHeaders = new HashMap<String, String>();
            if (callbackEmail!=null && !callbackEmail.isEmpty()){
            	httpHeaders.put("Callback-Email", callbackEmail);
            }
            if (callbackAddress!=null && !callbackAddress.isEmpty()){
            	httpHeaders.put("Callback-Address", callbackAddress);
            }

            String result="Unexpected Error";
            if (httpHeaders.size()>0)
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendInformational", json, httpHeaders);
            else 
            	result = api19115Service.makeApiRequest(apiEntity.getUrl()+"sendInformational", json, null);
            
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } 
        catch (Api19115Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Api19115Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }           
    }      



    
}
