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

package pl.orange.labs.db.dao;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import pl.orange.labs.db.entities.Account;

/**
 * Repository class responsible for account management.
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
public interface AccountRepository extends CrudRepository<Account, Long> {   
    
    @Override
    List<Account> findAll();
    /**
     * Returns sorted list of account records from DB.
     * @param sort - object with sorting configuration
     * @return List of Account object.
     */
    List<Account> findAll(Sort sort);
    /**
     * Returns Account object selected by username field.
     * @param username - username field of user account
     * @return Account
     */
    Account findByUsername(String username);
    /**
     * Returns Account object selected by id field.
     * @param id - identifier of user account
     * @return Account
     */
    Account findById(Long id);  
}
