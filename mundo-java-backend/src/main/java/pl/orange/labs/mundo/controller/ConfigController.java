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

package pl.orange.labs.mundo.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.orange.labs.db.dao.ConfigRepository;


/**
 * Controls requests for platform config page
 * 
 * @author Henryk Rosa (henryk.rosa@orange.com)
 *
 */
@Controller
@RequestMapping("/gui/superadmin/config")
public class ConfigController {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ConfigController.class);

    /**
     * Injected repository object for config.
     */
    @Autowired
    ConfigRepository configRepository;
    
    /**
     * Method responsible for displaying page with config values.
     * @return name of veiw file
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayConfigEntries() {
        
        LOGGER.info("displayConfigEntries");
        return "config";
    } 
}
