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

class I19115StorePlugin(plugins.SingletonPlugin):
    plugins.implements(plugins.IActions)
      
    def get_actions(self):
        actions = {
            '19115store_create':action.m19115store_create,
            '19115store_delete':action.m19115store_delete,
            '19115store_show':action.m19115store_show,
            '19115store_getNotifications': action.m19115store_getNotifications,
            '19115store_getNotificationsForDate': action.m19115store_getNotificationsForDate,
            '19115store_getNotificationsForDistrict': action.m19115store_getNotificationsForDistrict,
            '19115store_getNotificationsForNotificationNumber': action.m19115store_getNotificationsForNotificationNumber,
            '19115store_sendFreeform': action.m19115store_sendFreeForm,
            '19115store_sendIncident': action.m19115store_sendIncident,
            '19115store_sendInformational': action.m19115store_sendInformational,
            '19115store_getNotificationsPost': action.m19115store_getNotificationsPost,
            '19115store_sendIncidentPost': action.m19115store_sendIncidentPost,
            }
        return actions   
