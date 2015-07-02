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

package pl.orange.labs.mundo.plugins.wms.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import pl.orange.labs.mundo.exceptions.ConfigException;
import pl.orange.labs.mundo.http.LocalDefaultHttpClient;
import pl.orange.labs.mundo.plugins.wms.entity.WmsCapabilities;
import pl.orange.labs.mundo.plugins.wms.exception.WmsException;
import pl.orange.labs.mundo.service.ConfigService;

/**
 * Implementation of DAO - based on backend WMS API 
 * @author Henryk Rosa <henryk.rosa@orange.com>
 *
 */
@Component
public class WmsWebDaoImpl implements WmsWebDao
{
    private static Logger LOGGER = Logger.getLogger(WmsWebDaoImpl.class);
    
    @Autowired ConfigService config;
    
    @Override
    @Cacheable("mundoCache")
    public WmsCapabilities getCapabilities(final String url) throws WmsException
    {
        try 
        {
        	
        	LocalDefaultHttpClient httpClient = new LocalDefaultHttpClient();
        	ResponseEntity<InputStream> response= httpClient.getHttpResponseInputStream(
        			url, 
        			config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_CONNECT_TIMEOUT_KEY), 
        			config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_READ_TIMEOUT_KEY),
        			200);
        	 
        	if (LOGGER.isDebugEnabled()) LOGGER.debug("HTTP resppnse receivied "+response.getStatusCode());
        	 
        	 InputStream io = response.getBody();
        	 InputStreamEntity entity=new InputStreamEntity(io);
        	 
        	 String s = EntityUtils.toString(entity, "UTF-8");
 	         StringReader sr = new StringReader(s);
	          
	          XMLInputFactory xif = XMLInputFactory.newInstance();
	          XMLStreamReader xsr = xif.createXMLStreamReader(sr);

            JAXBContext jaxcontext = JAXBContext.newInstance(WmsCapabilities.class);
            Object objRsp = jaxcontext.createUnmarshaller().unmarshal(xsr);
            
            
            
            
            if (objRsp instanceof WmsCapabilities) 
            {
                WmsCapabilities apiResp = (WmsCapabilities) objRsp;
                return apiResp;    
            }
            else
            {
                LOGGER.error("ERROR - not possible to parse response WmsCapabilities from API resource");
                throw new WmsException("Bad response type!");
            }

        } 
      catch (RestClientException ex) 
      {
          LOGGER.error(ex);
          if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: RestClientException", ex);
          throw new WmsException("Wms Capabilities error: " + ex.getMessage());
      } 

        catch (JAXBException ex) 
        {
            LOGGER.error(ex);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: JAXBException", ex);
            throw new WmsException("Wms Capabilities error: " + ex.getMessage());
        } 
        catch (XMLStreamException ex) 
        {
            LOGGER.error(ex);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: XMLStreamException", ex);
            throw new WmsException("Wms Capabilities error: " + ex.getMessage());
        } catch (IOException e) {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: IOException", e);
            throw new WmsException("Wms Capabilities error: " + e.getMessage());		
        } catch (ConfigException e) {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: ConfigException", e);
            throw new WmsException("Wms Capabilities error: " + e.getMessage());		
		} 
     }

    @Override
    @Cacheable("mundoCache")
    public String getMap(String url)  throws WmsException
    {
        try 
        {   
        	
        	
           	LocalDefaultHttpClient httpClient = new LocalDefaultHttpClient();
        	ResponseEntity<InputStream> response= httpClient.getHttpResponseInputStream(
        			url, 
        			config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_CONNECT_TIMEOUT_KEY), 
        			config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_READ_TIMEOUT_KEY),
        			200
        			);
        	 if (LOGGER.isDebugEnabled()) LOGGER.debug("HTTP resppnse receivied "+response.getStatusCode());

        	
//            DefaultHttpClient httpCli = new DefaultHttpClient();
//            
//            //proxy support
//            httpCli.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, HttpProxyGlobalConfigReader.getHttpProxyHost());
//            
//            
//            HttpGet get = new HttpGet(url);
//            LOGGER.info("Requested URL: " + url);
//            HttpResponse resp = httpCli.execute(get);
//            HttpEntity entity = resp.getEntity();
//        	 
//            LOGGER.info(entity.getContentType());
//            LOGGER.info(entity.getContentLength());
//        	 
//            InputStream instream = entity.getContent();
  
        	InputStream instream = response.getBody(); 
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            
            while ((len = instream.read(buffer)) != -1) 
            {
                baos.write(buffer, 0, len);
            }            
        	 
            String result = Base64.encodeBase64String(baos.toByteArray());
            if (LOGGER.isDebugEnabled()) LOGGER.debug(result);
            return result;
        } 
        catch (IOException ex) 
        {
            LOGGER.error(ex);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: IOException", ex);
             throw new WmsException("Wms error: " + ex.getMessage());
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WMS: Exception", e);
            throw new WmsException("Wms error: " + e.getMessage());            
        }
    }
}
