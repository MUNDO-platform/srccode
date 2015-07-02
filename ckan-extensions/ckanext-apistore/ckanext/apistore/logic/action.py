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

import ckan.logic as logic
import requests
import logging
import ckan.plugins as p

log = logging.getLogger(__name__)

@logic.side_effect_free
def apistore_create(context, data_dict):
    '''
    Adds a new ApiStore.

    :param resource: resource dictionary that is passed to
        :meth:`~ckan.logic.action.create.resource_create`.
        Use instead of ``resource_id`` (optional)
    :type resource: dictionary
    
    **Results:**

    :returns: The newly created data object.
    :rtype: dictionary
    '''
    resource = data_dict.pop('resource', None)

    if resource:
        data_dict['resource'] = resource

    if 'resource' in data_dict:
        res = p.toolkit.get_action('resource_create')(context,data_dict['resource'])
        data_dict['resource_id'] = res['id']

    return {'success': True}

@logic.side_effect_free
def apistore_upsert(context, data_dict):
    return {'success': True}
@logic.side_effect_free
def apistore_delete(context, data_dict):
    return {'success': True}
@logic.side_effect_free
def apistore_search(context, data_dict):
    '''Search a ApiStore resource.

    The apistore_search action allows you to search data in a resource.

    :param id: id or alias of the resource to be searched against
    :type id: string

    **Results:**

    The result of this action is a result returned by external api.

    '''    
    if 'id' in data_dict:
        res = p.toolkit.get_action('resource_show')(context,data_dict)
        response = requests.get(res['url'])
        return response.json()
