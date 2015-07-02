/*
*  Platforma MUNDO – Dane po Warszawsku  http://danepowarszawsku.pl/
*   
*  @authors Jarosław Legierski, Tomasz Janisiewicz, Henryk Rosa Centrum Badawczo – Rozwojowe/ Orange Labs
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

package pl.orange.labs.mundo.plugins.wfs.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Tomasz Janisiewicz <tomasz.janisiewicz@orange.com>
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@XmlRootElement (name="FeatureCollection")
public class WfsFeatureCollection 
{
    private static Logger LOGGER = Logger.getLogger(WfsFeatureCollection.class);
    
    @XmlElement(name="featureMember", namespace="http://www.opengis.net/gml")
    private List<WfsFeatureMember> featureMemberList;    

    @XmlTransient
    public List<WfsFeatureMember> getFeatureMemberList() {
        return featureMemberList;
    }

    public void setFeatureMemberList(List<WfsFeatureMember> featureMemberList) {
        this.featureMemberList = featureMemberList;
    }
    
    public List<String> getFeatureMemberPropertyKey()
    {
        List<String> resultList = new ArrayList<String>();
        
        if (featureMemberList.size() > 0)
        {
            WfsFeatureMember member = featureMemberList.get(0);
            List<WfsFeatureMemberProperty> properties = member.getProperties();
            
            for (WfsFeatureMemberProperty property : properties)
            {
                resultList.add(property.getKey());
            }
        }
        
        return resultList;
    }
    
    public List<HashMap<String,String>> getFeatureMemberProperties()
    {
        List<HashMap<String,String>> resultList = new ArrayList<HashMap<String,String>>();
        
        if (featureMemberList.size() > 0)
        {
            for(WfsFeatureMember featureMember : featureMemberList)
            {
                HashMap<String,String> map = new HashMap<String,String>();
                
                for(WfsFeatureMemberProperty property : featureMember.getProperties())
                {
                    map.put(property.getKey(), property.getValue());
                }
                
                resultList.add(map);
            }
        }
        
        return resultList;
    }
    
    public List<XmlWfsPoint> getFeatureMemberCoordinates()
    {
        List<XmlWfsPoint> resultList = new ArrayList<XmlWfsPoint>();
        
        if (featureMemberList.size() > 0)
        {
            for(WfsFeatureMember featureMember : featureMemberList)
            {
                for(XmlWfsPoint point : featureMember.getGeometry().getCoordinates())
                {
                    resultList.add(point);
                }
            }
        }
        
        return resultList;
    }    
}
