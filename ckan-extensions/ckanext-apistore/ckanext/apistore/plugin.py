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

def group_create(context, data_dict=None):
    return {'success': False, 'msg': 'No one is allowed to create groups'}

log = logging.getLogger(__name__)

class IAipStorePlugin(plugins.SingletonPlugin):
    plugins.implements(plugins.IAuthFunctions)
    plugins.implements(plugins.IActions)
    plugins.implements(plugins.IResourceController)
    
    def get_auth_functions(self):
        return {'group_create': group_create}
    
    def get_actions(self):
        actions = {'apistore_create': action.apistore_create,
                   'apistore_upsert': action.apistore_upsert,
                   'apistore_delete': action.apistore_delete,
                   'apistore_search': action.apistore_search,
                  }
        return actions    
    
    def before_show(self, resource_dict):
        if resource_dict.get('url_type') == 'apistore':
            log.info(resource_dict.get('url_type'))
            log.info(resource_dict.get('id',''))  
        return resource_dict

