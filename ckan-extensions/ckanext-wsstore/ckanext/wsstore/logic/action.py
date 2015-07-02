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
def wsstore_create(context, data_dict): 
    '''Adds a new Wsstore.
    
    **Params:**
    :name [String]: Dbstore name.
    :ws_url [String]: Ws url.
    :package_id [String] Existing package id.    

    **Results:**

    :returns: The newly created data object.
    :rtype: dictionary
    '''    
    if not 'name' in data_dict:
        raise p.toolkit.ValidationError({'name': ['name required']}) 
    if not 'ws_url' in data_dict:
        raise p.toolkit.ValidationError({'ws_url': ['ws_url required']})
    
    if 'package_id' in data_dict: 
        ws_url_response = requests.get(data_dict.get('ws_url'))

        if ws_url_response is None:
            raise p.toolkit.ValidationError({'ws_url': ['ws_url is wrong']})

        if ws_url_response.json() is None:
            raise p.toolkit.ValidationError({'ws_url_response': ['ws_url_response is None']})   

        resource_name = data_dict.get('name').lower()
        resource_url = data_dict.get('ws_url')
        resource_webstore_url = data_dict.get('ws_url')
        resource_dict = {'package_id': data_dict.get('package_id'),'name':resource_name,'url':resource_url,'webstore_url':resource_webstore_url,'resource_type':'wsstore','format':'JSON'}
        resource_create_response = toolkit.get_action('resource_create')(context, resource_dict)
        log.info(resource_create_response)

        return {'package_id': data_dict.get('package_id')}          
    else:    
        package_create_response = toolkit.get_action('package_create')(context, data_dict)    
        log.info(package_create_response)

        if package_create_response['id'] is None:  
            raise p.toolkit.ValidationError({'package_id': ['package_id is none']})

        ws_url_response = requests.get(data_dict.get('ws_url'))

        if ws_url_response is None:
            raise p.toolkit.ValidationError({'ws_url': ['ws_url is wrong']})

        if ws_url_response.json() is None:
            raise p.toolkit.ValidationError({'ws_url_response': ['ws_url_response is None']})   

        package_id = package_create_response['id']
        resource_name = data_dict.get('name').lower()
        resource_url = data_dict.get('ws_url')
        resource_webstore_url = data_dict.get('ws_url')
        resource_dict = {'package_id': package_id,'name':resource_name,'url':resource_url,'webstore_url':resource_webstore_url,'resource_type':'wsstore','format':'JSON'}
        resource_create_response = toolkit.get_action('resource_create')(context, resource_dict)
        log.info(resource_create_response)

        return {'package_id': package_create_response['id']}

@logic.side_effect_free
def wsstore_delete(context, data_dict):
    '''Deletes a wsstore.

    :param name: wsstore id.
    :type name: string

    **Results:**

    :returns: Deleted object id.
    :rtype: dictionary
    '''     
    if not 'name' in data_dict:
        raise p.toolkit.ValidationError({'name': ['name required']})  
    
    resource_dict = {'name_or_id': data_dict.get('name')}
    
    package_show_response = toolkit.get_action('package_show')(context, resource_dict)
    log.error(package_show_response)
    
    toolkit.get_action('package_delete')(context, package_show_response)      
    return package_show_response['id']

@logic.side_effect_free
def wsstore_show(context, data_dict):
    '''Show a package resources.

    **Params:**
    :name [String]: Dbstore package name.

    **Results:**

    :returns: Wsstore package data.
    :rtype: dictionary
    '''     
    if not 'name' in data_dict:
        raise p.toolkit.ValidationError({'name': ['name required']})     
        
    package_dict = {'name_or_id': data_dict.get('name')}         
    package_show_response = toolkit.get_action('package_show')(context, package_dict) 
    
    result_list = list()
    
    for resource in  package_show_response.get('resources'):
        resource_id = resource.get('id')
        resource_name = resource.get('name')
        resource_dict = {'id': resource_id,'name':resource_name}
        result_list.append(resource_dict)

    return result_list

@logic.side_effect_free
def wsstore_get(context, data_dict):
    '''Seletcs a wsstore data.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: Wsstore data.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     
        
    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)
    if resource_show_response['resource_type'] != 'wsstore':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not dbstore']})    
    
    feature_url = resource_show_response['webstore_url']
        
    response = requests.get(feature_url)
    return response.json()
