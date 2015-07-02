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

package pl.orange.labs.mundo.controller;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.orange.labs.db.dao.StatisticsRepository;
import pl.orange.labs.db.entities.Statistics;

/**
 * Rest controller for statistics.
 * @author Tomasz Janisiewicz <Tomasz.Janisiewicz@orange.com>
 */
@RestController
@RequestMapping("/gui/admin")
public class StatisticsRestController {
   
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(StatisticsRestController.class);
    
    /**
     * Injected repository object for stats.
     */
    @Autowired
    private StatisticsRepository statisticsRepository; 
    
    /**
     * Default constructor.
     */
    public StatisticsRestController() {}
    
    /**
     * Constructor.
     * @param statisticsRepository 
     */
    public StatisticsRestController(StatisticsRepository statisticsRepository) {
        
        this.statisticsRepository = statisticsRepository;
    }
    
    /**
     * Rest method responsible for getting stats.
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getStatsList() {
        
        /**
         * Gets records.
         */
        List<Statistics> result =   statisticsRepository.findAll(new Sort(Sort.Direction.DESC, "counter"));
        /**
         * return http response with 200 OK status and data result in http body.
         */
        return new ResponseEntity<>(result, HttpStatus.OK);
    }     
}
