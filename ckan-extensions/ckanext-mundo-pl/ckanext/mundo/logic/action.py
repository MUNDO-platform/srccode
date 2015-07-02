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
import ckan.plugins.toolkit as toolkit

log = logging.getLogger(__name__)

@logic.side_effect_free
def mundo_get(context, data_dict):
    '''Seletcs a dbstore data.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: Dbstore data.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     
        
    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)
    if resource_show_response['resource_type'] != 'dbstore':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not dbstore']})    
    
    feature_url = resource_show_response['url']
        
    response = requests.get(feature_url)
    return response.json()
