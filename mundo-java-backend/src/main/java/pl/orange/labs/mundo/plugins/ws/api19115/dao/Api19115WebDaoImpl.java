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

package pl.orange.labs.mundo.plugins.ws.api19115.dao;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.ParseException;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import pl.orange.labs.mundo.exceptions.ConfigException;
import pl.orange.labs.mundo.http.LocalDefaultHttpClient;
import pl.orange.labs.mundo.plugins.ws.api19115.exception.Api19115Exception;
import pl.orange.labs.mundo.service.ConfigService;

/**
 * Implementation of Web access to API
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@Component
public class Api19115WebDaoImpl implements Api19115WebDao
{
    private static Logger LOGGER = Logger.getLogger(Api19115WebDaoImpl.class);
    
    @Autowired ConfigService config;
    
    @Override
    public String makeApiRequest(final String url, final String jsonParams, final HashMap<String, String> httpHeaders) throws Api19115Exception
    {
        try 
        {   
        	
        	LocalDefaultHttpClient httpClient = new LocalDefaultHttpClient();
        	
        	ResponseEntity<InputStream> response=httpClient.sendHttpPost(
        			url, 
        			config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_CONNECT_TIMEOUT_KEY), 
        			config.getIntValueForConfigVariable(ConfigService.HTTP_DEFAULT_READ_TIMEOUT_KEY),
        			"application/json; charset=UTF-8", 
        			jsonParams,
        			httpHeaders
        			);

        	InputStream io = response.getBody();
	       	InputStreamEntity entity=new InputStreamEntity(io);
	       	 
	       	String s = EntityUtils.toString(entity, "UTF-8");

        	 if (LOGGER.isDebugEnabled()) LOGGER.debug("HTTP resppnse receivied "+response.getStatusCode());

          if (LOGGER.isDebugEnabled()) LOGGER.debug("Http response (api19115): " + s); 
          return s;
	          
        } 
        catch (RestClientException ex) 
        {
            LOGGER.error(ex);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115:DAO RestClientException", ex);
            throw new Api19115Exception("Api19115 error: RestClientException "+ex.getMessage());
        } catch (ParseException e) {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115:DAO ParseException", e);
            throw new Api19115Exception("Api19115 error: ParseException: "+e.getMessage());
 		} catch (IOException e) {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115:DAO IOException", e);
            throw new Api19115Exception("Api19115 error: IOException: "+e.getMessage());
 		} catch (ConfigException e) {
            LOGGER.error(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Api19115:DAO ConfigException", e);
            throw new Api19115Exception("Api19115 error: ConfigException: "+e.getMessage());
		} 
    }
 
}
