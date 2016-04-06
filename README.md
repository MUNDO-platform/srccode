
Platforma MUNDO – Dane po Warszawsku  http://danepowarszawsku.pl/
@authors Jaroslaw Legierski, Tomasz Janisiewicz, Henryk Rosa Centrum Badawczo – Rozwojowe/ Orange Labs
copyright (c) 2014-2015 Orange Polska S.A. niniejszy kod jest otwarty i dystrybuowany
na licencji:   Lesser General Public License v2.1 (LGPLv2.1), której  pełny tekst można
znaleźć pod adresem:  https://www.gnu.org/licenses/lgpl-2.1.html

* oprogramowanie stworzone w ramach Projektu : MUNDO Miejskie Usługi Na Danych Oparte
* Beneficjenci: Fundacja Techsoup, Orange Polska S.A., Politechnika  Warszawska,
* Fundacja Pracownia Badań i Innowacji Społecznych „Stocznia”, Fundacja Projekt Polska
* Wartość projektu: 1 108 978
* Wartość dofinansowania: 847 000
* Okres realizacji 01.04.2014 – 31.12.2015
* Projekt współfinansowany przez Narodowe Centrum Badań i Rozwoju w ramach Programu Innowacje Społeczne


Elements of MUNDO platform:

• Proxy server – security layer responsible, among others, for traffic distribution (Apache);
• CKAN – Data Server based on CKAN, serving as: data catalogue, server and file and table data repository; 
• Function server – middleware server for API calls to primary data sources based on Web Services, databases etc. 
This system allows access to dynamic data. It’s also responsible for limiting calls, data buffering, converting calls and data formats, in order to obtain the most consistent API format possible.

The following plugins (components) for data conversion were implemented within the Function server: 

• WMS module – allows integration with raster maps repository;

• WFS module – allows integration with vector maps repository;

• WS module – allows integration with web services (e.g. queue system or 19115 alert system API); 

• DB module – allows integration with databases (public transport schedules and databases); 

• DB module – real time version (integration with public transport database with trams position online).


Proper cooperation between CKAN and Function Server is ensured by a number of extensions for CKAN, that support: communication between CKAN and Function Server, configuration of datasets and resources, as well as display a help section as a website (snippets) on the CKAN server side.

No Extension Supported data type 

1 mundo Shared component for all other extensions 

2 wmsstore Raster maps from WMS resources 

3 wfsstore Vector maps from WFS resources 

4 wsstore Web services – queues 

5 dbstore Data base – Trams online 

6 19115store 19115 alert system API


