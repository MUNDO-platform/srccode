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
def wmsstore_create(context, data_dict): 
    '''Adds a new Wmsstore.

    **Params:**
    :name [String]: Wmsstore name.
    :wms_url [String]: Wms url.
    :capabilities [Boolean]: If URL contains capabilities (Default false)
    :package_id [String] Existing package id.

    **Results:**

    :returns: The newly created data object.
    :rtype: dictionary
    '''    
    if not 'name' in data_dict:
        raise p.toolkit.ValidationError({'name': ['name required']}) 
    if not 'wms_url' in data_dict:
        raise p.toolkit.ValidationError({'wms_url': ['wms_url required']}) 
    
    if data_dict.get('capabilities'):
        if 'package_id' in data_dict:
            ws_url_response = requests.get(data_dict.get('wms_url'))

            if ws_url_response is None:
                raise p.toolkit.ValidationError({'wms_url': ['wms_url is wrong']})

            if ws_url_response.json() is None:
                raise p.toolkit.ValidationError({'wms_url_response': ['wms_url_response is None']})  

            for feature in ws_url_response.json().get('result').get('capability').get('layerWrap').get('layer'):
                resource_name = feature.get('name').lower()
                resource_url = data_dict.get('wms_url')+'/getmap?layers='+feature.get('name')
                resource_webstore_url = data_dict.get('wms_url')+'/getmap?layers='+feature.get('name')
                resource_dict = {'package_id': data_dict.get('package_id'),'name':resource_name,'url':resource_url,'webstore_url':resource_webstore_url,'resource_type':'wmsstore','format':'JSON'}
                resource_create_response = toolkit.get_action('resource_create')(context, resource_dict)
                log.info(resource_create_response)

            return {'package_id': data_dict.get('package_id')}            
        else:
            package_create_response = toolkit.get_action('package_create')(context, data_dict)    
            log.info(package_create_response)

            if package_create_response['id'] is None:  
                raise p.toolkit.ValidationError({'package_id': ['package_id is none']})

            ws_url_response = requests.get(data_dict.get('wms_url'))

            if ws_url_response is None:
                raise p.toolkit.ValidationError({'wms_url': ['wms_url is wrong']})

            if ws_url_response.json() is None:
                raise p.toolkit.ValidationError({'wms_url_response': ['wms_url_response is None']})  

            for feature in ws_url_response.json().get('result').get('capability').get('layerWrap').get('layer'):
                package_id = package_create_response['id']
                resource_name = feature.get('name').lower()
                resource_url = data_dict.get('wms_url')+'/getmap?layers='+feature.get('name')
                resource_webstore_url = data_dict.get('wms_url')+'/getmap?layers='+feature.get('name')
                resource_dict = {'package_id': package_id,'name':resource_name,'url':resource_url,'webstore_url':resource_webstore_url,'resource_type':'wmsstore','format':'JSON'}
                resource_create_response = toolkit.get_action('resource_create')(context, resource_dict)
                log.info(resource_create_response)

            return {'package_id': package_create_response['id']}            
    else:
        if 'package_id' in data_dict:
            ws_url_response = requests.get(data_dict.get('wms_url'))

            if ws_url_response is None:
                raise p.toolkit.ValidationError({'wms_url': ['wms_url is wrong']})

            if ws_url_response.json() is None:
                raise p.toolkit.ValidationError({'wms_url_response': ['wms_url_response is None']})   

            resource_name = data_dict.get('name').lower()
            resource_url = data_dict.get('wms_url')
            resource_webstore_url = data_dict.get('wms_url')
            resource_dict = {'package_id': data_dict.get('package_id'),'name':resource_name,'url':resource_url,'webstore_url':resource_webstore_url,'resource_type':'wmsstore','format':'JSON'}
            resource_create_response = toolkit.get_action('resource_create')(context, resource_dict)
            log.info(resource_create_response)        
            return {'package_id': data_dict.get('package_id')}            
        else:
            package_create_response = toolkit.get_action('package_create')(context, data_dict)    
            log.info(package_create_response)

            if package_create_response['id'] is None:  
                raise p.toolkit.ValidationError({'package_id': ['package_id is none']})

            ws_url_response = requests.get(data_dict.get('wms_url'))

            if ws_url_response is None:
                raise p.toolkit.ValidationError({'wms_url': ['wms_url is wrong']})

            if ws_url_response.json() is None:
                raise p.toolkit.ValidationError({'wms_url_response': ['wms_url_response is None']})   

            package_id = package_create_response['id']
            resource_name = data_dict.get('name').lower()
            resource_url = data_dict.get('wms_url')
            resource_webstore_url = data_dict.get('wms_url')
            resource_dict = {'package_id': package_id,'name':resource_name,'url':resource_url,'webstore_url':resource_webstore_url,'resource_type':'wmsstore','format':'JSON'}
            resource_create_response = toolkit.get_action('resource_create')(context, resource_dict)
            log.info(resource_create_response)        
            return {'package_id': package_create_response['id']}                            
        
@logic.side_effect_free
def wmsstore_delete(context, data_dict):
    '''Deletes a wmsstore.

    :param name: wmsstore id.
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
def wmsstore_show(context, data_dict):
    '''Show a package resources.

    **Params:**
    :name [String]: Wmsstore package name.

    **Results:**

    :returns: Wmsstore package data.
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
def wmsstore_get(context, data_dict):
    '''Seletcs a wmsstore data.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: Wmsstore data.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     
        
    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)
    
    if resource_show_response['resource_type'] != 'wmsstore':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not wmsstore']})    
    
        
    feature_url = resource_show_response['webstore_url']
    
    if 'center' in data_dict:
       feature_url =  feature_url + '&center=' + data_dict.get('center')
       
    if 'zoom' in data_dict:
       feature_url =  feature_url + '&zoom=' + data_dict.get('zoom') 
       
    if 'size' in data_dict:
       feature_url =  feature_url + '&size=' + data_dict.get('size') 
       
        
    response = requests.get(feature_url)
    return response.json()



