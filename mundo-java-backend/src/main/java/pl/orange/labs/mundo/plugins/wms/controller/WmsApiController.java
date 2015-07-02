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

package pl.orange.labs.mundo.plugins.wms.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import pl.orange.labs.mundo.exceptions.ConfigException;
import pl.orange.labs.mundo.geoconverter.GeoConversion;
import pl.orange.labs.mundo.geoconverter.GeoconversionFactory;
import pl.orange.labs.mundo.plugins.wms.dao.WmsEntity;
import pl.orange.labs.mundo.plugins.wms.dao.WmsRepository;
import pl.orange.labs.mundo.plugins.wms.dao.WmsService;
import pl.orange.labs.mundo.plugins.wms.entity.LayerWrap;
import pl.orange.labs.mundo.plugins.wms.entity.WmsCapabilities;
import pl.orange.labs.mundo.plugins.wms.exception.WmsException;
import pl.orange.labs.mundo.service.ConfigService;

/**
 * Controller for processing WMS API requests
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@RestController
@RequestMapping(value = "/api/wms")
public class WmsApiController {
    
    private static final Logger LOGGER = Logger.getLogger(WmsApiController.class);
    
    @Autowired
    private final WmsService wmsService; 
    @Autowired
    private final WmsRepository wmsRepository; 
    @Autowired
    private final ConfigService configService;    
    
    /**
     * Constructor.
     * @param wmsRepository repository.
     * @param wmsService service.
     * @param configService
     */
    @Autowired
    public WmsApiController(WmsRepository wmsRepository, WmsService wmsService, ConfigService configService) {
        
        this.wmsRepository = wmsRepository;
        this.wmsService = wmsService;
        this.configService = configService;
    }  
    
    /**
     * Gets list of WMS resources.
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getWmsEntries() {
        
        LOGGER.info("getWmsEntries");
        List<WmsEntity> list = wmsRepository.findAll();
        
        WmsApiResponse response = new WmsApiResponse();
        response.setResult(list);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Get list of capabilities from external source.
     * @param name - resource name.
     * @return ResponseEntity with result.
     * Method is cacheable.
     */    
    @RequestMapping(value = {"/{name}", "/{name}.json"}, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getWmsCapabilities(@PathVariable String name) {
        
        try {
            
            LOGGER.info("getWmsCapabilities");
            WmsEntity wmsEntity = wmsRepository.findByName(name);
            Assert.notNull(wmsEntity, "No WMS entity for given name.");
            WmsCapabilities capabilities = wmsService.getCapabilities(wmsEntity.getUrl());
            
            WmsApiResponse response = new WmsApiResponse();
            response.setResult(capabilities);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: IllegalArgumentException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (WmsException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: WmsException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (Exception e)
        {
            LOGGER.fatal(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }        
    }
    
    /**
     * Get selected map encoded in Base64 code.
     * @param name - resource name.
     * @param layers - coma separated list of selected layers.
     * @param center - coma separated coordinates of center point.
     * @param zoom - zoom value, default is 10.
     * @param size - size of returned image.
     * @param format - image format, ie. png
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "/{name}/getmap", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getWmsMap(
                @PathVariable String name,
                @RequestParam String layers,               
                @RequestParam(required = false) String center, 
                @RequestParam(required = false) String zoom, 
                @RequestParam(required = false) String size, 
                @RequestParam(required = false) String format            
            )
    {
        try
        {
            LOGGER.info("getWmsMap");
            WmsEntity wmsEntity = wmsRepository.findByName(name);
            Assert.notNull(wmsEntity, "No WMS entity for given name.");  
            WmsCapabilities capabilities = wmsService.getCapabilities(wmsEntity.getUrl());

            if (LOGGER.isDebugEnabled()) LOGGER.debug("Received params: \nlayers=" + layers 
                     + "\ncenter=" + center + "\nzoom=" + zoom + "\nsize=" + size + "\nformat=" + format); 

            center = center != null ? center : configService.getStringValueForConfigVariable("wms.params.center");
            zoom = zoom != null ? zoom : configService.getStringValueForConfigVariable("wms.params.zoom");
            size = size != null ? size : configService.getStringValueForConfigVariable("wms.params.size");
            format = format != null ? format : configService.getStringValueForConfigVariable("wms.params.format");
 
            String[] centerParts = center.split(",");
            Assert.notNull(centerParts, "Param center has wrong format."); 
            Assert.isTrue(centerParts.length == 2, "Param center has wrong format.");            
            Assert.isInstanceOf(Double.class, Double.parseDouble(centerParts[0]), "Param center has wrong format."); 
            Assert.isInstanceOf(Double.class, Double.parseDouble(centerParts[1]), "Param center has wrong format."); 
             
            String[] sizeParts = size.split("x");
            Assert.notNull(sizeParts, "Param size has wrong format."); 
            Assert.isTrue(sizeParts.length == 2, "Param size has wrong format.");            
            Assert.isInstanceOf(Integer.class, Integer.parseInt(sizeParts[0]), "Param size has wrong format."); 
            Assert.isInstanceOf(Integer.class, Integer.parseInt(sizeParts[1]), "Param size has wrong format.");             
            
            Assert.isInstanceOf(Integer.class, Integer.parseInt(zoom), "Param zoom has wrong format."); 
            
            Assert.isTrue((format.equals("png") || format.equals("gif")), "Param format is invalid.");
            
            String url = getUrl(true, wmsEntity, capabilities, layers, 
                    Double.parseDouble(centerParts[0]), 
                    Double.parseDouble(centerParts[1]), Integer.parseInt(zoom), 
                    Integer.parseInt(sizeParts[0]), Integer.parseInt(sizeParts[1]), format);            
            
            //WmsApiResponse response = new WmsApiResponse();
            //response.setResult(wmsService.getMap(url));

            WmsApiBase64MapResponse response = new WmsApiBase64MapResponse();
            response.setBase64map(wmsService.getMap(url));
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: NumberFormatException", e);
            return new ResponseEntity<>("NumberFormatException", HttpStatus.BAD_REQUEST);
        }        
        catch (IllegalArgumentException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: IllegalArgumentException", e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (WmsException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: WmsException", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }        
        catch (Exception e)
        {
            LOGGER.fatal(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: Exception", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);            
        }
    }
     
    /**
     * Forwards requests to resource.
     * @param name
     * @param response
     * @param request
     * @param layers
     * @param center
     * @param zoom
     * @param size
     * @param format
     * @throws WmsException 
     */
    @RequestMapping(value = "/{name}/redirect", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public void forwardWmsMap(
                @PathVariable String name,                
                HttpServletResponse response, 
                HttpServletRequest request, 
                @RequestParam String layers,
                @RequestParam(required = false) String center, 
                @RequestParam(required = false) String zoom, 
                @RequestParam(required = false) String size, 
                @RequestParam(required = false) String format) throws WmsException
    {    

        if (LOGGER.isDebugEnabled()) LOGGER.debug("Received params: \nlayers="+layers+"\ncenter="+center+"\nzoom="+zoom+"\nsize="+size+"\nformat="+format);
        
        
        WmsEntity wmsEntity = wmsRepository.findByName(name);
    	if (wmsEntity == null) {
    		//no WMS entity for given ID
    		throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "No WMS entity for name given in URL; name="+name); 
    	}

    	WmsCapabilities capabilities = wmsService.getCapabilities(wmsEntity.getUrl());
    	if (capabilities == null) {
    		//no WMS entity for given ID
    		throw new HttpClientErrorException(HttpStatus.FAILED_DEPENDENCY, "Incorrect answer from WMS resource"); 
    	}
    	LayerWrap availableLayers = capabilities.getCapability().getLayerWrap(); 
       
                
    	try 
        {
            center = center != null ? center : configService.getStringValueForConfigVariable("wms.params.center");
            zoom = zoom != null ? zoom : configService.getStringValueForConfigVariable("wms.params.zoom");
            size = size != null ? size : configService.getStringValueForConfigVariable("wms.params.size");
            format = format != null ? format : configService.getStringValueForConfigVariable("wms.params.format");     

            validate(request, layers, center, zoom, size, format, availableLayers);

    		String[] centerParts = center.split(",");
            String[] sizeParts = size.split("x");
            String url = getUrl(false, wmsEntity, capabilities, layers, Double.parseDouble(centerParts[0]), Double.parseDouble(centerParts[1]), Integer.parseInt(zoom), Integer.parseInt(sizeParts[0]), Integer.parseInt(sizeParts[1]), format);
            response.sendRedirect(url);
    	} 
        catch (IOException e) 
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: IOException", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ConfigException e) {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: ConfigException", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
    }
    
    /**
     * Creates resource URL.
     * @param wmsEntity
     * @param wmsCapabilities
     * @param layers
     * @param centerB
     * @param centerL
     * @param zoomInt
     * @param width
     * @param length
     * @param format
     * @return String with URL.
     * @throws WmsException 
     */
    public String getUrl(boolean encode, WmsEntity wmsEntity, WmsCapabilities wmsCapabilities, String layers, double centerB, double centerL, int zoomInt, int width, int length, String format) throws WmsException
    {
        try
        {
            Assert.notNull(wmsEntity, "wmsEntity is null");            
            GeoConversion geoConversion = GeoconversionFactory.create(wmsEntity.getSrs());
            Assert.notNull(geoConversion, "geoConversion is null");
            Assert.notNull(centerB, "centerB is null");
            Assert.notNull(centerL, "centerL is null");
            Assert.notNull(zoomInt, "zoomInt is null");
            Assert.notNull(zoomInt, "width is null");
            Assert.notNull(length, "length is null");
            geoConversion.setUrlGivenParams(centerB, centerL, zoomInt, width, length);
            
            Assert.notNull(wmsCapabilities, "wmsCapabilities is null");
            Assert.notNull(wmsCapabilities.getCapability(), "wmsCapabilities.getCapability() is null");
            Assert.notNull(wmsCapabilities.getCapability().getRequest(), "wmsCapabilities.getCapability().getRequest() is null");
            Assert.notNull(wmsCapabilities.getCapability().getRequest().getGetMap(), "wmsCapabilities.getCapability().getRequest().getGetMap() is null");
            Assert.notNull(wmsCapabilities.getCapability().getRequest().getGetMap().getDcpType(), "wmsCapabilities.getCapability().getRequest().getGetMap().getDcpType() is null");
            Assert.notNull(wmsCapabilities.getCapability().getRequest().getGetMap().getDcpType().getHttp(), "wmsCapabilities.getCapability().getRequest().getGetMap().getDcpType().getHttp() is null");
            Assert.notNull(wmsCapabilities.getCapability().getRequest().getGetMap().getDcpType().getHttp().getGet(), "wmsCapabilities.getCapability().getRequest().getGetMap().getDcpType().getHttp().getGet() is null");
            Assert.notNull(wmsCapabilities.getCapability().getRequest().getGetMap().getDcpType().getHttp().getGet().getOnlineResource(), "wmsCapabilities.getCapability().getRequest().getGetMap().getDcpType().getHttp().getGet().getOnlineResource() is null");
            Assert.notNull(wmsCapabilities.getCapability().getRequest().getGetMap().getDcpType().getHttp().getGet().getOnlineResource().getHref(), "wmsCapabilities.getCapability().getRequest().getGetMap().getDcpType().getHttp().getGet().getOnlineResource().getHref() is null");
            
            Assert.notNull(wmsCapabilities.getService(), "wmsCapabilities.getService() is null");            
            
            String url;
			try {
				if (encode)
					layers = URLEncoder.encode(layers, "UTF-8");
				url = wmsCapabilities.getCapability().getRequest().getGetMap().getDcpType().getHttp().getGet().getOnlineResource().getHref()+
					"?REQUEST=GetMap"+
					"&SERVICE="+wmsCapabilities.getService().getName()+
					"&VERSION="+wmsCapabilities.getVersion()+
					"&LAYERS="+layers+
					"&STYLES=&FORMAT=image/"+format+
					"&BGCOLOR=0xFFFFFF&TRANSPARENT=TRUE"+
					"&"+geoConversion.getSrsPart()+"&"+geoConversion.getConvertedBboxUrlPart()+"&"+geoConversion.getConvertedWidth()+"&"+geoConversion.getConvertedHeight();
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("Unsupported Encoding", e);
				throw new WmsException(e.getMessage());			
				}                    
            
            LOGGER.info("TRANSLATED URL="+url);
            return url.replace("??", "?"); 
        }
        catch(IllegalArgumentException e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: IllegalArgumentException", e);
            throw new WmsException(e.getMessage());
        }         
    }
    
    /**
     * Validates parameters.
     * @param request
     * @param layers
     * @param center
     * @param zoom
     * @param size
     * @param format
     * @param availableLayers 
     */
    public void validate(HttpServletRequest request, String layers, String center, String zoom, String size, String format, LayerWrap availableLayers)
    {
        
    	String tmp = request.getParameter("layers"); // get once again for manual decoding
    	//layers encoding validation
    	
        try 
        {
            byte[] tmpByte = tmp.getBytes("ISO_8859_1");
            String utf8str=new String (tmpByte, "UTF-8");
            if (LOGGER.isDebugEnabled()) LOGGER.debug(" layer in UTF8 str=" +utf8str);
    	       		
            layers = utf8str;
        } 
        catch (UnsupportedEncodingException e1) 
        {
            String error = "Invalid value of parameter 'layers'; Invalid layers parameter: unsupported encoding";
            LOGGER.error(e1);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: UnsupportedEncodingException", e1);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error);                         
        }        
 
        //layers validation
     	String[] layersParts=layers.split(",");
     	if (!(layersParts.length>0)) 
         {
             String error = "Invalid value of parameter 'layers'; At least one map leayer expected";
             throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error);             
     	}         
  
    	//center validation
    	String[] centerParts = center.split(",");
    	if (centerParts.length != 2)
        {
            String error = "Invalid value of parameter 'center'; Two coma separated map coordinates expected";
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error);             
        }

        try 
        {
            Double.parseDouble(centerParts[0]);
            Double.parseDouble(centerParts[1]);	    		
    	}
    	catch (NumberFormatException e) 
        {
            //case when given value generates parse exception
            LOGGER.warn(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: NumberFormatException", e);
            String error = "Invalid value of parameter 'center'; Invalid coordinates values";
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error);             
    	}
        
    	//zoom parsing/validation
    	int zoomInt = 0;
    	try 
        {
    		zoomInt = Integer.parseInt(zoom);
    	}
    	catch (Exception e) 
        {
            //case when given value generates parse exception
            LOGGER.warn(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: NumberFormatException", e);

            String error = "Invalid value of parameter 'zoom'; Expected integer value 1-21";
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error);            
    	}
        
    	if ((zoomInt < 1) || (zoomInt > 21)) 
        {
            String error = "Invalid value of parameter 'zoom'; Expected value 1-21";
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error);                 
    	} 
        
    	//size validation
    	String[] sizeParts = size.split("x");
    	if (sizeParts.length != 2)
        {
            String error = "Invalid value of parameter 'size'; Two integer values separated by x are expected";
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error); 
        }
        
    	//center parsing/validation
    	try 
        {
            Integer.parseInt(sizeParts[0]);
            Integer.parseInt(sizeParts[1]);	    		
    	}
    	catch (Exception e) 
        {
            //case when given value generates parse exception
            String error = "Invalid value of parameter 'size'; Invalid size values";
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error);             
    	}
        
    	if (!(format.equals("png") || format.equals("jpeg") || format.equals("gif"))) 
        {
            String error = "Invalid value of parameter 'format'; Expected one of png/jpeg/gif";
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error);             
    	}
        
        for (String layersPart : layersParts) {
            if (!(availableLayers.existsLayerForName(layersPart))) {
                String error = "Invalid value of parameter 'layers'; Invalid layer name:" + layersPart;
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, error);                        
            }
        }         
    }
    
    
}