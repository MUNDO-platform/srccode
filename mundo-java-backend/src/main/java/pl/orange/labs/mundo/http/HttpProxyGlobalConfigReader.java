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

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;

/**
 * Class provides static methods for reading HTTP proxy global settings
 * - to lunch JBOSS with http proxy configuration it is necessary to add jvm arguments like in example below:
 * -Dhttp.proxyHost=10.0.0.100 -Dhttp.proxyPort=8800
 * 
 * By default we skip adding HTTP proxy in HttpClient which connects to localhost or 127.0.0.1  
 * 
 * @author Henryk Rosa <henryk.rosa@orange.com>
 *
 */
public class HttpProxyGlobalConfigReader {
	
    private static Logger LOGGER = Logger.getLogger(HttpProxyGlobalConfigReader.class);


	/**
	 * Returns HttpProxy global settings on org.apache.http.HttpHost object
	 * @return
	 */
    public static final HttpHost getHttpProxyHost() {

    	if (getHttpProxyHostAddress()==null)
    		return null;
 
    	return new HttpHost(getHttpProxyHostAddress(), getHttpProxyHostPort());
    }
    
    /**
     * Returns HttpProxy global settings - IP address of HttpProxy
     * null if global setting is empty
     * @return
     */
    public static final String getHttpProxyHostAddress() {
        String httpProxyHost = System.getProperty("http.proxyHost");
        if (httpProxyHost == null)
            return null;
        if (httpProxyHost.isEmpty())
            return null;
        return httpProxyHost;
    }
    
    /**
     * Returns HttpProxy global settings - port of HttpProxy
     * Default value is "80" - if global setting is empty or null
     * @return
     */
    public static final int getHttpProxyHostPort() {
        String httpProxyPort = System.getProperty("http.proxyPort");

        if (httpProxyPort == null)
            httpProxyPort = "80";
        if (httpProxyPort.isEmpty())
            httpProxyPort = "80";
        
        int portNr;
        try {
        		portNr = Integer.parseInt(httpProxyPort);
        	} catch (NumberFormatException e) {
        		LOGGER.error("Config parameters ERROR - unable to parse the HTTP proxy port number:"+httpProxyPort+" assigned default value 80");
        		portNr=80;
        	}
        return portNr;
    }

    /**
     * Returns HttpProxy global settings on java.net.Proxy object
     * @return
     */
    public static final Proxy getHttpProxy() {
    	if (getHttpProxyHost()==null)
    		return null;
    	
    	InetSocketAddress address = new InetSocketAddress(getHttpProxyHostAddress(),getHttpProxyHostPort());
    	Proxy proxy = new Proxy(Proxy.Type.HTTP,address);
   	 	return proxy;
    }
}
