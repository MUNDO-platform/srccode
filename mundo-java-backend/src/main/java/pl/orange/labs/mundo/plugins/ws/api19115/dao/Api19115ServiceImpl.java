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

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.orange.labs.mundo.plugins.ws.api19115.exception.Api19115Exception;

/**
 * Service implementation
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@Component
public class Api19115ServiceImpl implements Api19115Service 
{
    @Autowired 
    Api19115Repository api19115Repository;    
    @Autowired
    private Api19115WebDao api19115WebDao;  
    
    public Api19115ServiceImpl() {}
    
    public Api19115ServiceImpl(Api19115WebDao wsQueueWebDao)
    {
        this.api19115WebDao = wsQueueWebDao;
    }

    @Override
    public List<Api19115Entity> getQueueList() 
    {
        return api19115Repository.findAll();
    }

    @Override
    public Api19115Entity getApi19115ById(Long id) 
    {
        return api19115Repository.findOne(id);
    }    

    @Override
    public String makeApiRequest(String url, String jsonParams, HashMap<String, String> httpHeaders) throws Api19115Exception 
    {
        return api19115WebDao.makeApiRequest(url, jsonParams, httpHeaders);
    }

    @Override
    public Api19115Entity getApi19115ByName(String name) 
    {
        return api19115Repository.findByName(name);
    }
}
