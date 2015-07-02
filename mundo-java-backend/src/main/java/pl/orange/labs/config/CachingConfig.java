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

package pl.orange.labs.config;


import net.sf.ehcache.config.CacheConfiguration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class responsible for global cache configuration.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@Configuration
@EnableCaching
public class CachingConfig {
    
    /**
     * Returns instance of CacheManager.
     * @return net.sf.ehcache.CacheManager
     */
    @Bean(destroyMethod = "shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        
        //creates CacheCOnfiguration object
        CacheConfiguration cacheConfiguration1m = new CacheConfiguration();
        //Sets global name of cache object ame 
        //This must be unique. The / character is illegal. 
        cacheConfiguration1m.setName("mundoCache");
        cacheConfiguration1m.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration1m.setMaxEntriesLocalHeap(10000); 
        //Sets the time to idle for an element before it expires. 
        //Is only used if the element is not eternal.
        cacheConfiguration1m.setTimeToLiveSeconds(60);
        //Sets the time to idle for an element before it expires. 
        //Is only used if the element is not eternal.
        cacheConfiguration1m.setTimeToIdleSeconds(30);
//        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
//        config.addCache(cacheConfiguration);
        
        
        //creates CacheCOnfiguration object
        CacheConfiguration cacheConfiguration1h = new CacheConfiguration();
        //Sets global name of cache object ame 
        //This must be unique. The / character is illegal. 
        cacheConfiguration1h.setName("mundo1hCache");
        cacheConfiguration1h.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration1h.setMaxEntriesLocalHeap(10000); 
        //Sets the time to idle for an element before it expires. 
        //Is only used if the element is not eternal.
        cacheConfiguration1h.setTimeToLiveSeconds(3600);
        //Sets the time to idle for an element before it expires. 
        //Is only used if the element is not eternal.
        cacheConfiguration1h.setTimeToIdleSeconds(1800);

        
        CacheConfiguration cacheConfiguration12h = new CacheConfiguration();
        //Sets global name of cache object ame 
        //This must be unique. The / character is illegal. 
        cacheConfiguration12h.setName("mundo12hCache");
        cacheConfiguration12h.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration12h.setMaxEntriesLocalHeap(10000); 
        //Sets the time to idle for an element before it expires. 
        //Is only used if the element is not eternal.
        cacheConfiguration12h.setTimeToLiveSeconds(43200);
        //Sets the time to idle for an element before it expires. 
        //Is only used if the element is not eternal.
        cacheConfiguration12h.setTimeToIdleSeconds(21600);
    
        CacheConfiguration cacheConfiguration24h = new CacheConfiguration();
        //Sets global name of cache object ame 
        //This must be unique. The / character is illegal. 
        cacheConfiguration24h.setName("mundo24hCache");
        cacheConfiguration24h.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration24h.setMaxEntriesLocalHeap(10000); 
        //Sets the time to idle for an element before it expires. 
        //Is only used if the element is not eternal.
        cacheConfiguration24h.setTimeToLiveSeconds(86400);
        //Sets the time to idle for an element before it expires. 
        //Is only used if the element is not eternal.
        cacheConfiguration24h.setTimeToIdleSeconds(43200);
        
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(cacheConfiguration1m);
        config.addCache(cacheConfiguration1h);
        config.addCache(cacheConfiguration12h);
        config.addCache(cacheConfiguration24h);
        return net.sf.ehcache.CacheManager.newInstance(config);
    }    

    /**
     * Method responsible for returning instance of CacheManager Class.
     * @return CacheManager
     */
    @Bean
    public CacheManager cacheManager() {
         return new EhCacheCacheManager(ehCacheManager());
    }


}
