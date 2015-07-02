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

package pl.orange.labs.mundo.plugins.ws.queue.entity;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.map.annotate.JsonSerialize;


/**
 * Represents Entity for MUNDO API QueueResponse
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement(name="MundoApiQueueResponse")
public class MundoApiQueueResponse 
{
    private MundoApiStatusInfoObject apiStatusInfo = new MundoApiStatusInfoObject();
    private XmlQueueResponse apiResponse;

    public MundoApiStatusInfoObject getApiStatusInfo() 
    {
        return apiStatusInfo;
    }
        
    public void setApiStatusInfo(MundoApiStatusInfoObject apiStatusInfo) 
    {
        this.apiStatusInfo = apiStatusInfo;
    }
	
    @XmlTransient
    public XmlQueueResponse getApiResponse() 
    {
        return apiResponse;
    }
    
    public void setApiResponse(XmlQueueResponse apiResponse) 
    {
        this.apiResponse = apiResponse;
    }	
}