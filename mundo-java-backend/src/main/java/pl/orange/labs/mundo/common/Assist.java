/*
*  Platforma MUNDO – Dane po Warszawsku  http://danepowarszawsku.pl/
*   
*  @authors Jaroslaw Legierski, Tomasz Janisiewicz, Henryk Rosa Centrum Badawczo – Rozwojowe/ Orange Labs
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

 
package pl.orange.labs.mundo.common;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

/**
 * provides Common Assist static methods used in various classes 
 * @author Henryk Rosa <henryk.rosa@orange.com>
 *
 */
public class Assist {
	
	private static Logger LOGGER = Logger.getLogger(Assist.class);    
	
    public static String transcodedUtf8String(String src) {
        try 
        {
            byte[] tmpByte = src.getBytes("ISO_8859_1");
            String utf8str=new String (tmpByte, "UTF-8");
            if (LOGGER.isDebugEnabled()) LOGGER.debug(" Param in UTF8 str=" +utf8str);
    	       		
            return utf8str;
        } 
        catch (UnsupportedEncodingException e1) 
        {
            LOGGER.error(e1);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("UnsupportedEncodingException", e1);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ERROR in parameter encoding: "+e1.getMessage());                         
        }        
    }

}
