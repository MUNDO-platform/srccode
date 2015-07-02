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
package pl.orange.labs.mundo.test.wms;

import pl.orange.labs.mundo.plugins.wms.dao.WmsService;
import pl.orange.labs.mundo.plugins.wms.entity.Capability;
import pl.orange.labs.mundo.plugins.wms.entity.DCPType;
import pl.orange.labs.mundo.plugins.wms.entity.Get;
import pl.orange.labs.mundo.plugins.wms.entity.GetMap;
import pl.orange.labs.mundo.plugins.wms.entity.Http;
import pl.orange.labs.mundo.plugins.wms.entity.OnlineResource;
import pl.orange.labs.mundo.plugins.wms.entity.Request;
import pl.orange.labs.mundo.plugins.wms.entity.Service;
import pl.orange.labs.mundo.plugins.wms.entity.WmsCapabilities;
import pl.orange.labs.mundo.plugins.wms.exception.WmsException;

/**
 *
 * @author Tomasz Janisiewicz <Tomasz.Janisiewicz@orange.com>
 */
public class WmsServiceMockImpl implements WmsService
{
    @Override
    public WmsCapabilities getCapabilities(String url) throws WmsException 
    {
        if(url.equals("wrongurl"))
        {
            throw new WmsException("Wroong url");
        }
        
        OnlineResource onlineResource = new OnlineResource();
        onlineResource.setHref("someurl");
        Get get = new Get();
        get.setOnlineResource(onlineResource);
        Http http = new Http();
        http.setGet(get);
        DCPType dcpType = new DCPType();
        dcpType.setHttp(http);
        GetMap getMap = new GetMap();
        getMap.setDcpType(dcpType);
        Request request = new Request();
        request.setGetMap(getMap);
        Capability capability = new Capability();
        capability.setRequest(request);
        Service service = new Service();
        WmsCapabilities wmsCapabilities = new WmsCapabilities();
        wmsCapabilities.setCapability(capability);
        wmsCapabilities.setService(service);
        
        return wmsCapabilities;
    }

    @Override
    public String getMap(String url) throws WmsException 
    {
        return "Base64EncodedImage";
    }    
}
