#  Platforma MUNDO – Dane po Warszawsku  http://danepowarszawsku.pl/
#  @authors Jaroslaw Legierski, Tomasz Janisiewicz, Henryk Rosa Centrum Badawczo – Rozwojowe/ Orange Labs
#  copyright (c) 2014-2015 Orange Polska S.A. niniejszy kod jest otwarty i dystrybuowany
#  na licencji:   Lesser General Public License v2.1 (LGPLv2.1), której  pełny tekst można
#  znaleźć pod adresem:  https://www.gnu.org/licenses/lgpl-2.1.html
#
# oprogramowanie stworzone w ramach Projektu : MUNDO Miejskie Usługi Na Danych Oparte
# Beneficjenci: Fundacja Techsoup, Orange Polska S.A., Politechnika  Warszawska,
# Fundacja Pracownia Badań i Innowacji Społecznych „Stocznia”, Fundacja Projekt Polska
# Wartość projektu: 1 108 978
# Wartość dofinansowania: 847 000
# Okres realizacji 01.04.2014 – 31.12.2015
# Projekt współfinansowany przez Narodowe Centrum Badań i Rozwoju w ramach
# Programu Innowacje Społeczne

import ckan.plugins as plugins
import logic.action as action
import logging

log = logging.getLogger(__name__)

class IWsStorePlugin(plugins.SingletonPlugin):
    plugins.implements(plugins.IActions)
      
    def get_actions(self):
        actions = {
            'wsstore_create':action.wsstore_create,
            'wsstore_delete':action.wsstore_delete,
            'wsstore_show':action.wsstore_show,
            'wsstore_get': action.wsstore_get
            }
        return actions   
