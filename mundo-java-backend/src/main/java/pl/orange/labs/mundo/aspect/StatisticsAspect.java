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

package pl.orange.labs.mundo.aspect;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.orange.labs.db.dao.StatisticsRepository;
import pl.orange.labs.db.entities.Statistics;

/**
 * Acspect class responsible for catch statistic data.
 * @author Tomasz Janisiewicz <Tomasz.Janisiewicz@orange.com>
 */
@Component
@Aspect
public class StatisticsAspect {
   
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(StatisticsAspect.class); 
    
    /**
     * Injected repository object for logs.
     */
    @Autowired
    private StatisticsRepository statisticsRepository; 
    
    @Autowired
    private HttpServletRequest request;   
    
    /**
     * Method responsible for catching controllers method.
     * @param joinPoint
     * @param result 
     */
    @AfterReturning(pointcut = "execution(* pl.orange.labs.mundo.controller.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {  
       
        if (LOGGER.isDebugEnabled()) LOGGER.debug(request.getRequestURI());
        counterIncrease(request.getRequestURI());
    }    
    
    /**
     * Method responsible for catching controllers method.
     * @param joinPoint
     * @param result 
     */
    @AfterReturning(pointcut = "execution(* pl.orange.labs.mundo.plugins.*.controller.*.*(..))", returning = "result")
    public void logAfterReturningPlugin(JoinPoint joinPoint, Object result) {  
       
    	if (LOGGER.isDebugEnabled()) LOGGER.debug(request.getRequestURI());
        counterIncrease(request.getRequestURI());
    } 
    
    /**
     * Method responsible for catching controllers method.
     * @param joinPoint
     * @param result 
     */
    @AfterReturning(pointcut = "execution(* pl.orange.labs.mundo.plugins.*.*.controller.*.*(..))", returning = "result")
    public void logAfterReturningPluginWs(JoinPoint joinPoint, Object result) {  
       
    	if (LOGGER.isDebugEnabled()) LOGGER.debug(request.getRequestURI());
        counterIncrease(request.getRequestURI());
    }   
    
    /**
     * Updates couter for selected path.
     * @param path 
     */
    @Transactional
    private synchronized void counterIncrease(final String path) {
        
        Statistics statistics = statisticsRepository.findByPath(request.getRequestURI()); 
        
        if (statistics != null) {            
            
            statistics.setCounter(statistics.getCounter() + 1);
            statisticsRepository.save(statistics);
        }
        else {
                
            statistics = new Statistics();
            statistics.setPath(request.getRequestURI());
            statistics.setCounter(Long.valueOf(1));
            statisticsRepository.save(statistics);            
        }         
    }
}
