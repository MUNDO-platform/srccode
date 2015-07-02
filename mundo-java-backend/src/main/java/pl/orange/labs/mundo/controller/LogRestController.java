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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.orange.labs.db.dao.LogRepository;
import pl.orange.labs.db.entities.Log;

/**
 * Rest controller for logs.
 * @author Tomasz Janisiewicz <Tomasz.Janisiewicz@orange.com>
 */
@RestController
@RequestMapping("/gui/admin")
public class LogRestController {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LogRestController.class);     
    
    /**
     * Injected repository object for logs.
     */
    @Autowired
    private LogRepository logRepository; 
    
    /**
     * Rest method responsible for getting last logs page.
     * @param page - number of returned page.
     * @param size - limit of returned records with logs.
     * @return ResponseEntity with result.
     */
    @RequestMapping(value = "/log", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getLogList(@RequestParam int page, @RequestParam int size)
    {
        /**
         * Gets sorted (by stamp column) page of records.
         */
        Page<Log> result =  logRepository.findAll(new PageRequest(page, size, Sort.Direction.DESC, "stamp"));
        /**
         * return http response with 200 OK status and data result in http body.
         */
        return new ResponseEntity<>(result, HttpStatus.OK);
    }    
}
