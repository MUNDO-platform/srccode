# Copyright (C) 2015 - Orange Labs PL / CBR

import ckan.logic as logic
import requests
import logging
import ckan.plugins as p
import ckan.plugins.toolkit as toolkit
import json

log = logging.getLogger(__name__)

@logic.side_effect_free
def m19115store_create(context, data_dict): 
    '''Adds a new 19115store.

    **Params:**
    :name [String]: 19115store name.
    :api_url [String]: api url.
    :package_id [String] Existing package id.

    **Results:**

    :returns: The newly created data object.
    :rtype: dictionary
    '''    
    if not 'name' in data_dict:
        raise p.toolkit.ValidationError({'name': ['name required']}) 
    if not 'api_url' in data_dict:
        raise p.toolkit.ValidationError({'api_url': ['api_url required']}) 
    
    package_create_response = toolkit.get_action('package_create')(context, data_dict)    
    log.info(package_create_response)

    if package_create_response['id'] is None:  
        raise p.toolkit.ValidationError({'package_id': ['package_id is none']})

    package_id = package_create_response['id']
    resource_name = data_dict.get('name').lower()
    resource_url = data_dict.get('api_url')
    resource_webstore_url = data_dict.get('api_url')
    resource_dict = {'package_id': package_id,'name':resource_name,'url':resource_url,'webstore_url':resource_webstore_url,'resource_type':'19115store','format':'JSON'}
    resource_create_response = toolkit.get_action('resource_create')(context, resource_dict)
    log.info(resource_create_response)        
    return {'package_id': package_create_response['id']}                            
        
@logic.side_effect_free
def m19115store_delete(context, data_dict):
    '''Deletes a 19115store.

    :param name: 19115store id.
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
def m19115store_show(context, data_dict):
    '''Show a package resources.

    **Params:**
    :name [String]: 19115store package name.

    **Results:**

    :returns: 19115store package data.
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
def m19115store_getNotifications(context, data_dict):
    '''19115store data getNotifications method.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: 19115store data getNotifications method.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     
        
    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)
    
    if resource_show_response['resource_type'] != '19115store':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not 19115store']})    
    
        
    feature_url = resource_show_response['webstore_url']
    
    feature_url = feature_url+'?'
    if 'filters' in data_dict:
       feature_url =  feature_url + '&filters=' + data_dict.get('filters')
       
    if 'operators' in data_dict:
       feature_url =  feature_url + '&operators=' + data_dict.get('operators') 
        
    response = requests.get(feature_url)
    if response.status_code == requests.codes.ok or response.status_code == requests.codes.created: 
        return response.json()
    else:
        return response.text

@logic.side_effect_free
def m19115store_getNotificationsForDate(context, data_dict):
    '''19115store data getNotificationsforDate method.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: 19115store data getNotificationsForDate method.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     
        
    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)
    
    if resource_show_response['resource_type'] != '19115store':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not 19115store']})    
    
        
    feature_url = resource_show_response['webstore_url']
    feature_url = feature_url.replace("getNotifications", "getNotificationsForDate")
    feature_url = feature_url+'?'
    
    if 'dateFrom' in data_dict:
       feature_url =  feature_url + '&dateFrom=' + data_dict.get('dateFrom')
       
    if 'dateTo' in data_dict:
       feature_url =  feature_url + '&dateTo=' + data_dict.get('dateTo') 
        
    response = requests.get(feature_url)
    if response.status_code == requests.codes.ok or response.status_code == requests.codes.created: 
        return response.json()
    else:
        return response.text

@logic.side_effect_free
def m19115store_getNotificationsForDistrict(context, data_dict):
    '''19115store data getNotificationsFordistrict method.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: 19115store data getNotificationsForDistrict method.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     
        
    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)
    
    if resource_show_response['resource_type'] != '19115store':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not 19115store']})    
    
        
    feature_url = resource_show_response['webstore_url']
    feature_url = feature_url.replace("getNotifications", "getNotificationsForDistrict")
    feature_url = feature_url+'?'
    
    if 'district' in data_dict:
       feature_url =  feature_url + '&district=' + data_dict.get('district')
       
    response = requests.get(feature_url)
    if response.status_code == requests.codes.ok or response.status_code == requests.codes.created: 
        return response.json()
    else:
        return response.text

@logic.side_effect_free
def m19115store_getNotificationsForNotificationNumber(context, data_dict):
    '''19115store data getNotificationsForNotificationNumber method.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: 19115store data getNotificationsForNotificationNumber method.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     
        
    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)
    
    if resource_show_response['resource_type'] != '19115store':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not 19115store']})    
    
        
    feature_url = resource_show_response['webstore_url']
    feature_url = feature_url.replace("getNotifications", "getNotificationsForNotificationNumber")
    feature_url = feature_url+'?'
    
    if 'notificationNumber' in data_dict:
       feature_url =  feature_url + '&notificationNumber=' + data_dict.get('notificationNumber')
       
    response = requests.get(feature_url)
    if response.status_code == requests.codes.ok or response.status_code == requests.codes.created: 
        return response.json()
    else:
        return response.text

@logic.side_effect_free
def m19115store_sendFreeForm(context, data_dict):
    '''19115store data sendFreeForm method.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: 19115store data sendFreeForm method.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     
        
    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)
    
    if resource_show_response['resource_type'] != '19115store':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not 19115store']})    
    
        
    feature_url = resource_show_response['webstore_url']
    feature_url = feature_url.replace("getNotifications", "sendFreeform")
    feature_url = feature_url+'?'
    
    if 'description' in data_dict:
        feature_url =  feature_url + '&description=' + data_dict.get('description')

    if 'email' in data_dict:
        feature_url =  feature_url + '&email=' + data_dict.get('email')
       
    if 'name' in data_dict:
        feature_url =  feature_url + '&name=' + data_dict.get('name')
       
    if 'lastName' in data_dict:
        feature_url =  feature_url + '&lastName=' + data_dict.get('lastName')

    if 'phoneNumber' in data_dict:
        feature_url =  feature_url + '&phoneNumber=' + data_dict.get('phoneNumber')
    
    ckan_request = p.toolkit.request
    emailCallBackHeader = ckan_request.headers.get('Callback-Email', '')
    addressCallBackHeader = ckan_request.headers.get('Callback-Address', '')
    
    myHeaders = '';
    
    if emailCallBackHeader and addressCallBackHeader:
        myHeaders = {'Callback-Email': emailCallBackHeader, 'Callback-Address': addressCallBackHeader}
    else:
        if emailCallBackHeader:
            myHeaders = {'Callback-Email': emailCallBackHeader}
        else:
            if addressCallBackHeader:
                myHeaders = {'Callback-Address': addressCallBackHeader}
    if myHeaders:       
        response = requests.get(feature_url, headers=myHeaders)
    else:
        response = requests.get(feature_url)
    
    if response.status_code == requests.codes.ok or response.status_code == requests.codes.created: 
        return response.json()
    else:
        return response.text
   

@logic.side_effect_free
def m19115store_sendIncident(context, data_dict):
    '''19115store data sendIncident method.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: 19115store data sendIncident method.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     
        
    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)

    if resource_show_response['resource_type'] != '19115store':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not 19115store']})    
    
        
    feature_url = resource_show_response['webstore_url']
    feature_url = feature_url.replace("getNotifications", "sendIncident")
    feature_url = feature_url+'?'
    
    if 'description' in data_dict:
       feature_url =  feature_url + '&description=' + data_dict.get('description')
   
    if 'email' in data_dict:
       feature_url =  feature_url + '&email=' + data_dict.get('email')

    if 'lastName' in data_dict:
       feature_url =  feature_url + '&lastName=' + data_dict.get('lastName')

    if 'name' in data_dict:
       feature_url =  feature_url + '&name=' + data_dict.get('name')

    if 'phoneNumber' in data_dict:
       feature_url =  feature_url + '&phoneNumber=' + data_dict.get('phoneNumber')

    if 'subcategory' in data_dict:
       feature_url =  feature_url + '&subcategory=' + data_dict.get('subcategory')

    if 'eventType' in data_dict:
       feature_url =  feature_url + '&eventType=' + data_dict.get('eventType')

    if 'xCoordWGS84' in data_dict:
       feature_url =  feature_url + '&xCoordWGS84=' + data_dict.get('xCoordWGS84')

    if 'yCoordWGS84' in data_dict:
       feature_url =  feature_url + '&yCoordWGS84=' + data_dict.get('yCoordWGS84')

    if 'street' in data_dict:
       feature_url =  feature_url + '&street=' + data_dict.get('street')

    if 'houseNumber' in data_dict:
       feature_url =  feature_url + '&houseNumber=' + data_dict.get('houseNumber')

    if 'apartmentNumber' in data_dict:
       feature_url =  feature_url + '&apartmentNumber=' + data_dict.get('apartmentNumber')
   
    ckan_request = p.toolkit.request
    emailCallBackHeader = ckan_request.headers.get('Callback-Email', '')
    addressCallBackHeader = ckan_request.headers.get('Callback-Address', '')

    myHeaders = '';
    
    if emailCallBackHeader and addressCallBackHeader:
        myHeaders = {'Callback-Email': emailCallBackHeader, 'Callback-Address': addressCallBackHeader}
    else:
        if emailCallBackHeader:
            myHeaders = {'Callback-Email': emailCallBackHeader}
        else:
            if addressCallBackHeader:
                myHeaders = {'Callback-Address': addressCallBackHeader}
    if myHeaders:       
        response = requests.get(feature_url, headers=myHeaders)
    else:
        response = requests.get(feature_url)
   
    if response.status_code == requests.codes.ok or response.status_code == requests.codes.created: 
        return response.json()
    else:
        return response.text
 
@logic.side_effect_free
def m19115store_sendInformational(context, data_dict):
    '''19115store data sendInformational method.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: 19115store data sendInformational method.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     
        
    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)
    
    if resource_show_response['resource_type'] != '19115store':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not 19115store']})    
    
        
    feature_url = resource_show_response['webstore_url']
    feature_url = feature_url.replace("getNotifications", "sendInformational")
    feature_url = feature_url+'?'
 
    if 'description' in data_dict:
        feature_url =  feature_url + '&description=' + data_dict.get('description')

    if 'email' in data_dict:
        feature_url =  feature_url + '&email=' + data_dict.get('email')
       
    if 'name' in data_dict:
        feature_url =  feature_url + '&name=' + data_dict.get('name')
       
    if 'lastName' in data_dict:
        feature_url =  feature_url + '&lastName=' + data_dict.get('lastName')

    if 'phoneNumber' in data_dict:
        feature_url =  feature_url + '&phoneNumber=' + data_dict.get('phoneNumber')
    
    ckan_request = p.toolkit.request
    emailCallBackHeader = ckan_request.headers.get('Callback-Email', '')
    addressCallBackHeader = ckan_request.headers.get('Callback-Address', '')
  
    myHeaders = '';
    
    if emailCallBackHeader and addressCallBackHeader:
        myHeaders = {'Callback-Email': emailCallBackHeader, 'Callback-Address': addressCallBackHeader}
    else:
        if emailCallBackHeader:
            myHeaders = {'Callback-Email': emailCallBackHeader}
        else:
            if addressCallBackHeader:
                myHeaders = {'Callback-Address': addressCallBackHeader}
    if myHeaders:       
        response = requests.get(feature_url, headers=myHeaders)
    else:
        response = requests.get(feature_url)
  
    if response.status_code == requests.codes.ok or response.status_code == requests.codes.created: 
        return response.json()
    else:
        return response.text
            
@logic.side_effect_free
def m19115store_getNotificationsPost(context, data_dict):
    '''19115store data getNotificationsPost method.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: 19115store data getNotificationsPost method.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     

    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)
    if resource_show_response['resource_type'] != '19115store':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not 19115store']})    
     
    feature_url = resource_show_response['webstore_url']
    headers = {'Content-type': 'application/json'}   

    for k in data_dict.keys():
        if k == 'id':
            del data_dict[k]
        if k == 'apikey':
            del data_dict[k]
             
    jsonObj = json.dumps(data_dict) 
        
    response = requests.post(feature_url, headers=headers, data=jsonObj)
    if response.status_code == requests.codes.ok or response.status_code == requests.codes.created: 
        return response.json()
    else:
        return response.text
           
             
@logic.side_effect_free
def m19115store_sendIncidentPost(context, data_dict):
    '''19115store data sendFreeFormPost method.

    :param name: resource id.
    :type name: string

    **Results:**

    :returns: 19115store data sendFreeFormPost method.
    :rtype: dictionary
    '''     
    if not 'id' in data_dict:
        raise p.toolkit.ValidationError({'id': ['id required']})     
        
    resource_show_response = toolkit.get_action('resource_show')(context,data_dict)
    
    if resource_show_response['resource_type'] != '19115store':
        raise p.toolkit.ValidationError({'resource_type': ['resource_type is not 19115store']})    
    
        
    feature_url = resource_show_response['webstore_url']
    feature_url = feature_url.replace("getNotifications", "sendIncident")
    
    for k in data_dict.keys():
        if k == 'id':
            del data_dict[k]
        if k == 'apikey':
            del data_dict[k]

    jsonObj = json.dumps(data_dict)
    
    
        
    ckan_request = p.toolkit.request
    emailCallBackHeader = ckan_request.headers.get('Callback-Email', '')
    addressCallBackHeader = ckan_request.headers.get('Callback-Address', '')
  

    myHeaders = {'Content-type': 'application/json'}  
    
    if emailCallBackHeader and addressCallBackHeader:
        myHeaders = {'Content-type': 'application/json','Callback-Email': emailCallBackHeader, 'Callback-Address': addressCallBackHeader}
    else:
        if emailCallBackHeader:
            myHeaders = {'Content-type': 'application/json','Callback-Email': emailCallBackHeader}
        else:
            if addressCallBackHeader:
                myHeaders = {'Content-type': 'application/json','Callback-Address': addressCallBackHeader}
  
    response = requests.post(feature_url, headers=myHeaders, data=jsonObj)
    if response.status_code == requests.codes.ok or response.status_code == requests.codes.created: 
        return response.json()
    else:
        return response.text
      
