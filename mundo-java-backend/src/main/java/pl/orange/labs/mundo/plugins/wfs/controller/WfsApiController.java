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

package pl.orange.labs.mundo.plugins.wfs.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsEntity;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsRepository;
import pl.orange.labs.mundo.plugins.wfs.dao.WfsService;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsCapabilities;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsFeatureCollection;
import pl.orange.labs.mundo.plugins.wfs.entity.XmlWfsPoint;
import pl.orange.labs.mundo.plugins.wfs.exception.WfsException;
import pl.orange.labs.mundo.service.ConfigService;

/**
 * Controls API requests for wfs resources.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@RestController
@RequestMapping("/api/wfs")
public class WfsApiController {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(WfsApiController.class);

    @Autowired
    private final WfsService wfsService; 
    @Autowired
    private final WfsRepository wfsRepository;
    @Autowired
    private final ConfigService configService;      
    
    /**
     * Constructor.
     * @param wfsRepository
     * @param wfsService
     * @param configService 
     */
    @Autowired
    public WfsApiController(WfsRepository wfsRepository, WfsService wfsService, ConfigService configService) 
    {
        this.wfsRepository = wfsRepository;
        this.wfsService = wfsService;
        this.configService = configService;
    }     
    
    /**
     * Returns all wfs instances.
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getWfsEntries() {
        
        LOGGER.info("displayWfsEntries");
        List<WfsEntity> list = wfsRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }     

    /**
     * Returns WFS capabilities from external resource.
     * @param name
     * @return ResponseEntity with result.
     */   
    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getWfsCapabilities(@PathVariable String name) {
        
        try {
            
            LOGGER.info("getWfsCapabilities: " + name);
            //Gets WfsEntity from repository
            WfsEntity wfsEntity = wfsRepository.findByName(name);
            Assert.notNull(wfsEntity, "No WFS entity for given name.");
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Sending request to WfsService: " + wfsEntity.getCapabilitiesUrl()); 
            //Sends request to external resource
            WfsCapabilities cap = wfsService.getCapabilities(wfsEntity.getCapabilitiesUrl());
            if (LOGGER.isTraceEnabled()) LOGGER.trace("Receiver response from WfsService: " + cap); 
            //Returns response with WFS capabilities in body.
            return new ResponseEntity<>(cap, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (WfsException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS: WfsException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e) {
            LOGGER.fatal(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }         
    }    
    
    /**
     * Get WFS feature from external resource.
     * @param name
     * @param namespace
     * @param feature
     * @param bbox
     * @param circle
     * @param maxFeatures
     * @param filter
     * @return ResponseEntity with result.
     * @throws UnsupportedEncodingException
     * @throws WfsException 
     */
    @RequestMapping(value = "/{name}/{namespace}/{feature}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getWfsFeature(
            @PathVariable String name,
            @PathVariable String namespace, 
            @PathVariable String feature, 
            @RequestParam(required = false) String bbox, 
            @RequestParam(required = false) String circle, 
            @RequestParam(required = false) String maxFeatures,
            @RequestParam(required = false) String filter) throws UnsupportedEncodingException, WfsException
    {   
        try {
            
            LOGGER.info("getWfsFeature");
        
            WfsEntity wfsEntity = wfsRepository.findByName(name);
            Assert.notNull(wfsEntity, "No WFS entity for given name.");
            
            String url = wfsEntity.getFeaturesUrl() + "&typeName=" + namespace + ":" + feature;
            
            maxFeatures = maxFeatures != null ? maxFeatures : configService.getStringValueForConfigVariable("wfs.params.maxFeatures");
            Assert.notNull(maxFeatures, "Param maxFeatures has wrong format."); 
            Assert.isTrue(Integer.parseInt(maxFeatures) > 0, "Param maxFeatures has to be positive integer.");  
            Assert.isTrue(Integer.parseInt(maxFeatures) <= 1000, "Param maxFeatures has to be les or equal than 1000");  
            Assert.isInstanceOf(Integer.class, Integer.parseInt(maxFeatures), "Param maxFeatures is not numeric."); 
            url += "&maxFeatures=" + maxFeatures;
            
            if (circle != null) url += "&bbox=" + this.getBboxByCircleArea(circle);
            
            if (bbox != null) {
                
                String[] bboxParts = bbox.split(",");
                Assert.notNull(bboxParts, "Param bbox has wrong format."); 
                Assert.isTrue(bboxParts.length == 4, "Param bbox has wrong format.");  
                Assert.isInstanceOf(Double.class, Double.parseDouble(bboxParts[0]), "Param bbox has wrong format.");                 
                Assert.isInstanceOf(Double.class, Double.parseDouble(bboxParts[1]), "Param bbox has wrong format.");                 
                Assert.isInstanceOf(Double.class, Double.parseDouble(bboxParts[2]), "Param bbox has wrong format.");                 
                Assert.isInstanceOf(Double.class, Double.parseDouble(bboxParts[3]), "Param bbox has wrong format.");                 
                url += "&bbox=" + bbox;
            }
            
            if (filter != null) url += "&Filter=" + URLEncoder.encode(filter, "UTF-8");            
            
            WfsFeatureCollection collection = wfsService.getFeature(url, namespace, feature);
            return new ResponseEntity<>(collection, HttpStatus.OK);
        }
        catch (NumberFormatException e) {
            
            LOGGER.warn(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS: NumberFormatException", e);
            return new ResponseEntity<>("NumberFormatException", HttpStatus.BAD_REQUEST);
        }        
        catch (IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS: IllegalArgumentException", e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (WfsException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS: WfsException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }         
        catch (Exception e) {
            
            LOGGER.fatal(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS: Exception", e);
            return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);            
        }
    }       
    
    @RequestMapping(value = {"/{name}/{namespace}/{feature}.props"}, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public List<HashMap<String, String>> getFeatureProperties(
            @PathVariable String name,
            @PathVariable String namespace, 
            @PathVariable String feature, 
            @RequestParam(required = false) String bbox, 
            @RequestParam(required = false) String circle, 
            @RequestParam(required = false) String maxFeatures,
            @RequestParam(required = false) String filter) throws UnsupportedEncodingException, WfsException
    {   
        WfsEntity wfsEntity = wfsRepository.findByName(name);
        String url = wfsEntity.getFeaturesUrl() + "&typeName=" + namespace + ":" + feature;
        if (filter != null) url += "&Filter=" + URLEncoder.encode(filter, "UTF-8");
        if (maxFeatures != null) url += "&maxFeatures=" + maxFeatures;
        if (bbox != null) url += "&bbox=" + bbox;
        if (circle != null) url += "&bbox=" + this.getBboxByCircleArea(circle);
        WfsFeatureCollection collection = wfsService.getFeature(url, namespace, feature);
        return collection.getFeatureMemberProperties();
    } 
    
    @RequestMapping(value = {"/{name}/{namespace}/{feature}.coordinates"}, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public List<XmlWfsPoint> getFeatureCoordinates(
            @PathVariable String name,
            @PathVariable String namespace, 
            @PathVariable String feature, 
            @RequestParam(required = false) String bbox, 
            @RequestParam(required = false) String circle, 
            @RequestParam(required = false) String maxFeatures,
            @RequestParam(required = false) String filter) throws UnsupportedEncodingException, WfsException
    {   
        WfsEntity wfsEntity = wfsRepository.findByName(name);
        String url = wfsEntity.getFeaturesUrl() + "&typeName=" + namespace + ":" + feature;
        if (filter != null) url += "&Filter=" + URLEncoder.encode(filter, "UTF-8");
        if (maxFeatures != null) url += "&maxFeatures=" + maxFeatures;
        if (bbox != null) url += "&bbox=" + bbox;
        if (circle != null) url += "&bbox=" + this.getBboxByCircleArea(circle);
        WfsFeatureCollection collection = wfsService.getFeature(url, namespace, feature);
        
        return collection.getFeatureMemberCoordinates();
    }    
    
    /**
     * Converts circle params to box params.
     * @param circle (x,y,radius)
     * @return bbox param (x1,y1,x2,y2)
     */
    private String getBboxByCircleArea(String circle) {
        
        Assert.notNull(circle, "Param circle is null."); 
        String[] parts = circle.split(",");
        Assert.notNull(parts, "Param circle has wrong format."); 
        Assert.isTrue(parts.length == 3, "Param circle has wrong format.");  
        Assert.isInstanceOf(Double.class, Double.parseDouble(parts[0]), "Param circle has wrong format."); 
        Assert.isInstanceOf(Double.class, Double.parseDouble(parts[1]), "Param circle has wrong format."); 
        Assert.isInstanceOf(Double.class, Double.parseDouble(parts[2]), "Param circle has wrong format."); 
        double x = Double.valueOf(parts[0]); 
        double y = Double.valueOf(parts[1]);
        double radius = Double.valueOf(parts[2]);  //m    
        Assert.isTrue(radius >= 100, "Radius must be greater or equal than 100");
        
        double R = 6371000;  // earth radius in m
        double x1 = x - Math.toDegrees(radius / R / Math.cos(Math.toRadians(y)));
        double x2 = x + Math.toDegrees(radius / R / Math.cos(Math.toRadians(y)));
        double y1 = y + Math.toDegrees(radius / R);
        double y2 = y - Math.toDegrees(radius / R);
        
        String result = x1 + "," + y1 + "," + x2 + "," + y2;
        LOGGER.info("circle -> bbox: " + result);
        return result;
    } 
}
