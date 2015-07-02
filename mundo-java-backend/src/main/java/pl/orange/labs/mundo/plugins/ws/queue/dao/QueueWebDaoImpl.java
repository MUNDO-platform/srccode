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

package pl.orange.labs.mundo.plugins.ws.queue.dao;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;





import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;





import pl.orange.labs.mundo.exceptions.ConfigException;
import pl.orange.labs.mundo.http.LocalDefaultHttpClient;
import pl.orange.labs.mundo.plugins.ws.queue.entity.XmlQueueResponse;
import pl.orange.labs.mundo.plugins.ws.queue.exception.QueueException;
import pl.orange.labs.mundo.service.ConfigService;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Component
public class QueueWebDaoImpl implements QueueWebDao
{
    private static Logger LOGGER = Logger.getLogger(QueueWebDaoImpl.class);
    
    @Autowired ConfigService config;
    
    @Override
    @Cacheable("mundoCache")
    public XmlQueueResponse getQueue(final String url) throws QueueException
    {
        try 
        {   
        	
        	LocalDefaultHttpClient httpClient = new LocalDefaultHttpClient();
        	ResponseEntity<String> response= httpClient.get(
        			url, 
        			config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_CONNECT_TIMEOUT_KEY), 
        			config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_READ_TIMEOUT_KEY)
        			);
        	 if (LOGGER.isDebugEnabled()) LOGGER.debug("HTTP resppnse receivied "+response.getStatusCode());
        	
	          String s = response.getBody(); 
	          if (LOGGER.isDebugEnabled()) LOGGER.debug("Http response (queue): " + s); 
	          StringReader sr = new StringReader(s);
	          
	          XMLInputFactory xif = XMLInputFactory.newInstance();
	          XMLStreamReader xsr = xif.createXMLStreamReader(sr);
	          xsr = new QueueWebDaoImpl.SiteStreamReaderDelegate(xsr);            
	          JAXBContext jaxcontext = JAXBContext.newInstance(XmlQueueResponse.class);
	          Object objRsp = jaxcontext.createUnmarshaller().unmarshal(xsr);
	
	          if (objRsp instanceof XmlQueueResponse) 
	          {
	              XmlQueueResponse apiResp = (XmlQueueResponse) objRsp;
	              return apiResp;    
	          }
	          else
	          {
	              throw new QueueException("Bad response type!");
	          }
         } 
        catch (RestClientException ex) 
        {
            LOGGER.error(ex);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Queue:DAO RestClientException", ex);
            throw new QueueException("Queue error: RestClientException");
        } 
        catch (JAXBException ex) 
        {
            LOGGER.error(ex);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Queue:DAO JAXBException", ex);
            throw new QueueException("Queue error: JAXBException");
        } 
        catch (XMLStreamException ex) 
        {
            LOGGER.error(ex);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Queue:DAO XMLStreamException", ex);
            throw new QueueException("Queue error: XMLStreamException");
        } catch (ConfigException e) {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Queue:DAO ConfigException", e);
            throw new QueueException("Queue error: ConfigException");
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
