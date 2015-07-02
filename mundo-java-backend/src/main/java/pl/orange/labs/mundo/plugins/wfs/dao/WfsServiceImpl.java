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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsCapabilities;
import pl.orange.labs.mundo.plugins.wfs.entity.WfsFeatureCollection;
import pl.orange.labs.mundo.plugins.wfs.exception.WfsException;

/**
 * Represents service layer for WFS resources.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Component
public class WfsServiceImpl implements WfsService {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(WfsServiceImpl.class);
        
    @Autowired
    private WfsWebDao wfsWebDao;   
    
    /**
     * Default constructor.
     */
    public WfsServiceImpl(){}    

    /**
     * Constructor.
     * @param wfsWebDao 
     */
    public WfsServiceImpl(WfsWebDao wfsWebDao) {
        
        this.wfsWebDao = wfsWebDao;
    }
    
    public WfsWebDao getWfsWebDao() {
        return wfsWebDao;
    }

    public void setWfsWebDao(WfsWebDao wfsWebDao) {
        this.wfsWebDao = wfsWebDao;
    }

    @Override
    public WfsCapabilities getCapabilities(String url) throws WfsException {
        
        if (LOGGER.isTraceEnabled()) LOGGER.trace("getCapabilities: " + url); 
        return wfsWebDao.getCapabilities(url);
    }

    @Override
    public WfsFeatureCollection getFeature(String url, String namespace, String name) throws WfsException {
        
        if (LOGGER.isTraceEnabled()) LOGGER.trace("getFeature: " + url); 
        return wfsWebDao.getFeature(url, namespace, name);
    }
}
