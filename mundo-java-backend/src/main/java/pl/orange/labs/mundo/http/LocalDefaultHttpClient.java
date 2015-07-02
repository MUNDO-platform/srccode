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
package pl.orange.labs.mundo.http;



import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;



/**
 * Default HTTP client, with HTTP proxy support.
 * Used by DAO which connect to HTTP resources.
 * 
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */


public class LocalDefaultHttpClient 
{
    private static Logger LOGGER = Logger.getLogger(LocalDefaultHttpClient.class);
 

    /**
     * Http GET to url - Returns ResponseEntity<String> or throws RestClientException in case of error  
     * @param url
     * @param connectTimeout - in milliseconds (0 - infinite timeout)
     * @param readTimeout - in milliseconds (0 - infinite timeout)
     * @return
     * @throws RestClientException
     */
    public ResponseEntity<String> get(String url, int connectTimeout, int readTimeout) throws RestClientException
    {
        return request(UriComponentsBuilder.fromHttpUrl(url), HttpMethod.GET, connectTimeout, readTimeout);
    }
    
    /**
     * Http POST to url - Returns ResponseEntity<String> or throws RestClientException in case of error  
     * @param url
     * @param connectTimeout - in milliseconds (0 - infinite timeout)
     * @param readTimeout - in milliseconds (0 - infinite timeout)
     * @return
     * @throws RestClientException
     */
    public ResponseEntity<String> post(String url, int connectTimeout, int readTimeout) throws RestClientException
    {
        return request(UriComponentsBuilder.fromHttpUrl(url), HttpMethod.POST, connectTimeout,  readTimeout);
    } 
    
    /**
     * Http PUT to url - Returns ResponseEntity<String> or throws RestClientException in case of error  
     * @param url
     * @param connectTimeout - in milliseconds (0 - infinite timeout)
     * @param readTimeout - in milliseconds (0 - infinite timeout)
     * @return
     * @throws RestClientException
     */
    public ResponseEntity<String> put(String url,int connectTimeout, int readTimeout) throws RestClientException
    {
        return request(UriComponentsBuilder.fromHttpUrl(url), HttpMethod.PUT, connectTimeout,  readTimeout);
    } 
    
    /**
     * Http DELETE to url - Returns ResponseEntity<String> or throws RestClientException in case of error  
     * @param url
     * @param connectTimeout - in milliseconds (0 - infinite timeout)
     * @param readTimeout - in milliseconds (0 - infinite timeout)
     * @return
     * @throws RestClientException
     */
    public ResponseEntity<String> delete(String url, int connectTimeout, int readTimeout) throws RestClientException
    {
        return request(UriComponentsBuilder.fromHttpUrl(url), HttpMethod.DELETE, connectTimeout, readTimeout);
    }    
    

    private ResponseEntity<String> request(UriComponentsBuilder uriComponentsBuilder, HttpMethod method, int connectTimeout, int readTimeout) throws RestClientException
    {
    	LOGGER.info(uriComponentsBuilder.build(false).encode().toUriString()+" (ConenectTimeout="+connectTimeout+" ReadTimeout="+readTimeout+")");

       	//proxy support
    	SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Http PROXY settings:"+HttpProxyGlobalConfigReader.getHttpProxy());
    	if (HttpProxyGlobalConfigReader.getHttpProxy()!=null)
    		factory.setProxy(HttpProxyGlobalConfigReader.getHttpProxy());
    	factory.setConnectTimeout(connectTimeout);
    	factory.setReadTimeout(readTimeout);
    	

        RestTemplate restTemplate = new RestTemplate();
      
        restTemplate.setRequestFactory(factory);
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<String>(requestHeaders);
        return restTemplate.exchange(uriComponentsBuilder.build(false).encode().toUriString(), method, requestEntity, String.class);  
    }
    
 
    /**
     * Sends http get to specified URL, Returns response as byte[] 
     * @param url
     * @param connectTimeout
     * @param readTimeout
     * @param requiredHttpCode
     * @return
     * @throws IOException
     */
    public ResponseEntity<InputStream> getHttpResponseInputStream(String url,int connectTimeout, int readTimeout, int requiredHttpCode) throws IOException {
    	
    	LOGGER.info("GET:"+url+" (ConenectTimeout="+connectTimeout+" ReadTimeout="+readTimeout+")");
    	URL urlHttp = new URL(url);
    	HttpURLConnection httpCon;
    	if (HttpProxyGlobalConfigReader.getHttpProxy()!=null)
    		httpCon = (HttpURLConnection)urlHttp.openConnection(HttpProxyGlobalConfigReader.getHttpProxy());
    	else 
    		httpCon = (HttpURLConnection)urlHttp.openConnection();
    	httpCon.setRequestMethod("GET");
		httpCon.setDoOutput(true);
		httpCon.setConnectTimeout(connectTimeout);
		httpCon.setReadTimeout(readTimeout);
		httpCon.connect();
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Received response: "+httpCon.getResponseCode()+" "+httpCon.getResponseMessage());
		
		if (httpCon.getResponseCode()==requiredHttpCode) {
	 		ResponseEntity<InputStream> rsp= new ResponseEntity<InputStream>(httpCon.getInputStream(), HttpStatus.OK) ;       
	    	return rsp ;
		} else 
			throw new IOException("Invalid response from HTTP resource: "+httpCon.getResponseCode()+" "+httpCon.getResponseMessage());
    }

 
    /**
     * Sends http POST to specified URL, Returns response as byte[] 
     * @param url
     * @param connectTimeout
     * @param readTimeout
     * @param requiredHttpCode
     * @return
     * @throws IOException
     */
    public ResponseEntity<InputStream> sendHttpPost(String url,int connectTimeout, int readTimeout,  String contentType, String content, HashMap<String, String> httpHeaders) throws IOException {
    	
    	LOGGER.info("POST:"+url+" (ConenectTimeout="+connectTimeout+" ReadTimeout="+readTimeout+")");
    	URL urlHttp = new URL(url);
    	HttpURLConnection httpCon;
    	if (HttpProxyGlobalConfigReader.getHttpProxy()!=null)
    		httpCon = (HttpURLConnection)urlHttp.openConnection(HttpProxyGlobalConfigReader.getHttpProxy());
    	else 
    		httpCon = (HttpURLConnection)urlHttp.openConnection();
    	httpCon.setRequestMethod("POST");
		httpCon.setDoOutput(true);
		httpCon.setConnectTimeout(connectTimeout);
		httpCon.setReadTimeout(readTimeout);
		httpCon.setRequestProperty("Content-Type", contentType);
		httpCon.setRequestProperty("Accept", "*/*");
		if (httpHeaders!=null) {
			for (Map.Entry<String, String> entry : httpHeaders.entrySet())
			{
				httpCon.setRequestProperty(entry.getKey(), entry.getValue());
				if (LOGGER.isDebugEnabled()) LOGGER.debug("Added header: "+entry.getKey()+":"+entry.getValue());
			}
		}
		if (content!=null)
			httpCon.setRequestProperty("Content-Length", Integer.toString(content.getBytes().length));
		else 
			httpCon.setRequestProperty("Content-Length", "0");
		httpCon.connect();
		if (content!=null) {
			DataOutputStream wr= new DataOutputStream(httpCon.getOutputStream());
			wr.write(content.getBytes());
		}
		if (LOGGER.isDebugEnabled()) LOGGER.debug("Received response: "+httpCon.getResponseCode()+" "+httpCon.getResponseMessage());
		
		if (httpCon.getResponseCode()==200||httpCon.getResponseCode()==201) {
	 		ResponseEntity<InputStream> rsp= new ResponseEntity<InputStream>(httpCon.getInputStream(), HttpStatus.OK) ;       
	    	return rsp ;
		} else 
			throw new IOException("Invalid response from HTTP resource: "+httpCon.getResponseCode()+" "+httpCon.getResponseMessage());
    }


}
