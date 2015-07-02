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

package pl.orange.labs.mundo.plugins.wfs.dao;

//import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.apache.http.entity.InputStreamEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;

import pl.orange.labs.mundo.exceptions.ConfigException;
import pl.orange.labs.mundo.http.LocalDefaultHttpClient;
import pl.orange.labs.mundo.plugins.wfs.entity.Geometry;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsCapabilities;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsFeatureCollection;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsFeatureMember;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsFeatureMemberProperty;
import pl.orange.labs.mundo.plugins.wfs.entity.geo.parser.GeometryParser;
import pl.orange.labs.mundo.plugins.wfs.entity.geo.parser.GeometryParserFactory;
import pl.orange.labs.mundo.plugins.wfs.exception.WfsException;
import pl.orange.labs.mundo.plugins.wfs.exception.WfsParserException;
import pl.orange.labs.mundo.plugins.wfs.exception.WfsPropertiesParserException;
import pl.orange.labs.mundo.service.ConfigService;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Component
public class WfsWebDaoImpl implements WfsWebDao {
    
    private static Logger LOGGER = Logger.getLogger(WfsWebDaoImpl.class);
    
    @Autowired ConfigService config;
    
    @Override
    @Cacheable("mundoCache")
    public WfsCapabilities getCapabilities(final String url) throws WfsException {
        
        try {
            
            if (LOGGER.isTraceEnabled()) LOGGER.trace("getCapabilities: " + url);
            LocalDefaultHttpClient httpClient = new LocalDefaultHttpClient();
            ResponseEntity<String> response = httpClient.get(url,
                    config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_CONNECT_TIMEOUT_KEY),
                    config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_READ_TIMEOUT_KEY));
                
            if (LOGGER.isDebugEnabled()) LOGGER.debug("HTTP resppnse receivied " + response.getStatusCode());
            
            String s = response.getBody(); 
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Http response: " + s); 
            

            StringReader sr = new StringReader(s);
            
            XMLInputFactory xif = XMLInputFactory.newInstance();
            XMLStreamReader xsr = xif.createXMLStreamReader(sr);
            xsr = new SiteStreamReaderDelegate(xsr);            
            JAXBContext jaxcontext = JAXBContext.newInstance(WfsCapabilities.class);
            Object objRsp = jaxcontext.createUnmarshaller().unmarshal(xsr);

            if (objRsp instanceof WfsCapabilities) {
                    
                WfsCapabilities apiResp = (WfsCapabilities) objRsp;
                return apiResp;    
            }
            else {
                    
                LOGGER.error("Bad response type!");
                throw new WfsException("Bad response type!");
            }
        } 
        catch (RestClientException ex) {
        
            LOGGER.error(ex);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS:DAO: RestClientException", ex);
            throw new WfsException("Wfs error: " + ex.getMessage());
        } 
        catch (JAXBException ex) {
            
            LOGGER.error(ex);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS:DAO: JAXBException", ex);
            throw new WfsException("Wfs error: " + ex.getMessage());
        } 
        catch (XMLStreamException ex) {
            
            LOGGER.error(ex);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS:DAO: XMLStreamException", ex);
            throw new WfsException("Wfs error: " + ex.getMessage());
        } catch (ConfigException e) {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS:DAO: XMLStreamException", e);
            throw new WfsException("Wfs error: " + e.getMessage());
		}        
    }
    
    @Override
    @Cacheable("mundoCache")
    public WfsFeatureCollection getFeature(final String url, final String namespace, final String name)  throws WfsException {
        
        try {
            
            
            if (LOGGER.isTraceEnabled()) LOGGER.trace("getFeature: " + url);
            LocalDefaultHttpClient httpClient = new LocalDefaultHttpClient();
            ResponseEntity<InputStream> response = httpClient.getHttpResponseInputStream(
        			url, 
        			config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_CONNECT_TIMEOUT_KEY), 
        			config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_READ_TIMEOUT_KEY),
        			200
        			);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("HTTP resppnse receivied " + response.getStatusCode());
        	
            InputStream io = response.getBody();
            InputStreamEntity entity = new InputStreamEntity(io);
            String s = EntityUtils.toString(entity, "UTF-8");
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Http response: " + s); 

            return this.parseXml(s, namespace, name);
        } 
        catch (RestClientException ex) {
            
          LOGGER.error(ex);
          if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS:DAO: RestClientException", ex);
          throw new WfsException("Wfs error: " + ex.getMessage());
        } 
        catch (Exception e) {
            
            LOGGER.fatal(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS:DAO: Exception", e);
            throw new WfsException("Wfs error: " + e.getMessage());            
        }
    }    

    /**
     * Method responsible for parsing wfs xml data
     * @param wfsString
     * @param namespace
     * @param name
     * @return WfsFeatureCollection
     * @throws WfsParserException 
     */
    public WfsFeatureCollection parseXml(String wfsString, String namespace, String name) throws WfsParserException {        
        
        try {
            
            LOGGER.info("parseXml");
            if (LOGGER.isDebugEnabled()) LOGGER.debug("namespace: " + namespace); 
            if (LOGGER.isDebugEnabled()) LOGGER.debug("name: " + name); 

            String modifiedInput = wfsString.replaceAll(namespace + ":", "");
            if (LOGGER.isTraceEnabled()) LOGGER.trace("modifiedInput: " + modifiedInput); 
            String patternString = "<gml:featureMember>(<" + name + ">(.*)</" + name + ">)+</gml:featureMember>";
            if (LOGGER.isDebugEnabled()) LOGGER.debug("patternString: " + patternString);  
            
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(modifiedInput);
            List<WfsFeatureMember> featureMemberList = new ArrayList<WfsFeatureMember>();
            
            
            while (matcher.find()) {

                WfsFeatureMember featureMember = new WfsFeatureMember();

                String featureMemberString = matcher.group(2);
                if (LOGGER.isTraceEnabled()) LOGGER.trace("featureMemberString: " + featureMemberString); 

                GeometryParser geometryParser = GeometryParserFactory.createParser(featureMemberString);
                Geometry geometry = geometryParser.parse(featureMemberString);
                featureMember.setGeometry(geometry);
                
                featureMember.setProperties(parseFeatureMemberProperties(featureMemberString));
                featureMemberList.add(featureMember);
            } 
            
            Assert.notEmpty(featureMemberList, "FeatureMember list is empty");
            WfsFeatureCollection featureCollection = new WfsFeatureCollection();
            featureCollection.setFeatureMemberList(featureMemberList);

            return featureCollection;            
        }
        catch (final IllegalArgumentException e) {
            
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS:DAO: IllegalArgumentException", e);
            throw new WfsParserException("IllegalArgumentException: " + e.getMessage());           
        }           
    }            
    
    /**
     * Method responsible for parsing properties list of wfs feature member.
     * @param featureMemberString
     * @return List<WfsFeatureMemberProperty> 
     * @throws pl.orange.labs.mundo.plugins.wfs.exception.WfsPropertiesParserException 
     */
    public List<WfsFeatureMemberProperty> parseFeatureMemberProperties(final String featureMemberString) throws WfsPropertiesParserException {
        
        try {
            
            Assert.notNull(featureMemberString, "featureMemberString is null");
            String tagPatternString = "<(.+?)>(.*?)</(.+?)>";
            Pattern tagPattern = Pattern.compile(tagPatternString);
            Matcher tagMatcher = tagPattern.matcher(featureMemberString);         
            List<WfsFeatureMemberProperty> featureMemberPropertyList = new ArrayList<WfsFeatureMemberProperty>();
            
            while (tagMatcher.find()) {
                
                String keyStart = tagMatcher.group(1);
                String value = tagMatcher.group(2);
                String keyEnd = tagMatcher.group(3);
                
                if (keyStart.equals(keyEnd)) {
                    
                    WfsFeatureMemberProperty featureMemberProperty = new WfsFeatureMemberProperty();
                    featureMemberProperty.setKey(keyStart);
                    featureMemberProperty.setValue(value);
                    featureMemberPropertyList.add(featureMemberProperty);   
                }
            } 
            
            Assert.notEmpty(featureMemberPropertyList, "Properties list is empty");
            return featureMemberPropertyList;
        }
        catch (final IllegalArgumentException e) {
            
            LOGGER.error(e);
            LOGGER.warn("featureMemberString: " + featureMemberString);   
            if (LOGGER.isDebugEnabled()) LOGGER.debug("WFS:DAO: IllegalArgumentException", e);
            throw new WfsPropertiesParserException(e.getMessage());            
        }  
    }

    private static class SiteStreamReaderDelegate extends StreamReaderDelegate {
        
        public SiteStreamReaderDelegate(XMLStreamReader xsr) {
            
            super(xsr);
        }

        @Override
        public String getLocalName() {            
            
            String localName = super.getLocalName();
            return localName;

        }
    } 
}
