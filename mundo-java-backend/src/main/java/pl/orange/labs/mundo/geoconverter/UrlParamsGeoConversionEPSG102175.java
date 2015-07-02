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
package pl.orange.labs.mundo.geoconverter;
/** 
 * 
 */

/**
 * @author Jarosław Legierski
 *
 */
public class UrlParamsGeoConversionEPSG102175 implements GeoConversion {

	
	public String inputurl;
	public String outputurl;
	
	public double Burl;
	public double Lurl;
	public double Xurl;
	public double Yurl;
	
	private int zoom;
	private int sizeW;
	private int sizeH;
//	private String centerparname="center";
//	private String zoomparname="zoom";
//	private String sizeparname="size";
	
	//private String prefix = "http://wms.um.warszawa.pl/serwis"; //WMS Warszawa
//	private String prefix = "http://91.235.231.143/arcgis/services/WMS_Ortofotomapa2011/MapServer/WMSServer"; //WMS Gda�sk
//	private String request = "GetMap";
//	private String service = "WMS";
//	private String version = "1.1.1";
	//private String layers  = "WMS/ENOM_Ulice"; //W-wa
//	private String layers  = "0"; // Gda�sk
	//private String layers  = "WMS/Raster_orto2010_8m"; //W-wa
	//private String layers  = "WMS/Raster_plan_39"; //W-wa
	
	
//	private String styles = "";
//	private String format = "image/png";
//	private String bgcolor = "0xFFFFFF";
//	private String transparent ="TRUE";
	//private String srs = "EPSG:2178";  //EPSG:102175
	private String srs = "EPSG:102175";  
//	private String srsWGS84 = "EPSG:4326";
	private double[][] bbox = new double[2][2];
	private int wmsW;
	private int wmsH;
	
	
	public static Geoconversion102175  convert;
		
//	public void parseurl() {
//		
//		// wyszukujemy x i y z input url
//			
//		int index1=0;
//		int index2=0;
//		
//		index1=inputurl.indexOf(centerparname)+centerparname.length()+1;
//		index2=inputurl.indexOf(",");
//		
//		Burl= Double.parseDouble(inputurl.substring(index1,index2));
//		
//		index1=inputurl.indexOf("&");
//		
//		Lurl= Double.parseDouble(inputurl.substring(index2+1,index1));
//		
//		// wyszukujemy warto�� zoom  eg- zoom=10
//		
//		index1=inputurl.indexOf(zoomparname)+zoomparname.length()+1;
//		index2=inputurl.indexOf("&",index1);
//		
//		
//		zoom=  Integer.parseInt(inputurl.substring(index1,index2));
//		
//		//wyszukujemy waro�� size=200x200
//		
//		index1=inputurl.indexOf(sizeparname)+sizeparname.length()+1;
//		index2=inputurl.indexOf("x");
//		
//		sizeW= Integer.parseInt(inputurl.substring(index1,index2));
//		
//		index1=inputurl.length();
//		
//		sizeH= Integer.parseInt(inputurl.substring(index2+1,index1));
//				
//		
//	}
	
//	private void converturlWGS84()
//	{
//		
//		wmsW=sizeW;
//		wmsH=sizeH;
//		
////		double WmapyWGS84 = 0.48; //szeroko�� k�towa mapy 200px w terenie dla zoom = 10
////	
////	    
////		double scalefactorWGS84 =(Math.PI)/(2*Math.pow(2,zoom)); //wsp�czynnik skalowania 
////				
////		bbox[0][0]=Burl-WmapyWGS84*sizeH*scalefactorWGS84;
////		bbox[1][0]=Lurl-WmapyWGS84*sizeW*scalefactorWGS84;
////		bbox[0][1]=Burl+WmapyWGS84*sizeH*scalefactorWGS84;
////		bbox[1][1]=Lurl+WmapyWGS84*sizeW*scalefactorWGS84;
//
//	
//	    
//		double scalefactorWGS84 = 360/(Math.pow(2, zoom)*256); //wsp�czynnik skalowania 
//				
//		bbox[0][0]=Burl-0.5*sizeH*scalefactorWGS84;
//		bbox[1][0]=Lurl-0.5*sizeW*scalefactorWGS84;
//		bbox[0][1]=Burl+0.5*sizeH*scalefactorWGS84;
//		bbox[1][1]=Lurl+0.5*sizeW*scalefactorWGS84;
//
//		
////		outputurl=prefix+"?"+
////				"REQUEST="+request+"&"+
////				"SERVICE="+service+"&"+
////				"VERSION="+version+"&"+
////				"LAYERS="+layers+"&"+
////				"STYLES="+styles+"&"+
////				"FORMAT="+format+"&"+
////				"BGCOLOR="+bgcolor+"&"+
////				"TRANSPARENT="+transparent+"&"+
////				"SRS="+srsWGS84+"&"+
////				"BBOX="+ 
////						Double.toString(bbox[1][0])+","+
////						Double.toString(bbox[0][0])+","+
////						Double.toString(bbox[1][1])+","+
////						Double.toString(bbox[0][1])+"&"+
////				"WIDTH="+wmsW+"&"+
////				"HEIGHT="+wmsH;
////				; 
//		
//	}
	
	
	
	
	private void converturl() {
	
		wmsW=sizeW;
		wmsH=sizeH;
		
		convert = new Geoconversion102175(Burl, Lurl, 200);
		Xurl=convert.Xpl2000; 
		Yurl=convert.Ypl2000; 
		
//		double Wmapy= 35000.00 ; //szeroko�� mapy 200px w terenie dla zoom = 10
//		
//		double scalefactor =2*180/Math.pow(2,zoom)/256; //wsp�czynnik skalowania 
//
//		
//		    bbox[0][0]=Xurl-Wmapy*sizeH*scalefactor;
//			bbox[1][0]=Yurl-Wmapy*sizeW*scalefactor;
//			bbox[0][1]=Xurl+Wmapy*sizeH*scalefactor;
//			bbox[1][1]=Yurl+Wmapy*sizeW*scalefactor;
	
		double grad2linfactor = (40075704/(2*Math.PI)*Math.cos(Burl*Math.PI/180)*2*Math.PI)/360; // km na grad
		double scalefactor =360/(Math.pow(2,zoom)*256); //wsp�czynnik skalowania 

		
		    bbox[0][0]=Xurl-0.5*grad2linfactor*sizeH*scalefactor;
			bbox[1][0]=Yurl-0.5*grad2linfactor*sizeW*scalefactor;
			bbox[0][1]=Xurl+0.5*grad2linfactor*sizeH*scalefactor;
			bbox[1][1]=Yurl+0.5*grad2linfactor*sizeW*scalefactor;
	
//		outputurl=prefix+"?"+
//			"REQUEST="+request+"&"+
//			"SERVICE="+service+"&"+
//			"VERSION="+version+"&"+
//			"LAYERS="+layers+"&"+
//			"STYLES="+styles+"&"+
//			"FORMAT="+format+"&"+
//			"BGCOLOR="+bgcolor+"&"+
//			"TRANSPARENT="+transparent+"&"+
//			"SRS="+srs+"&"+
//			"BBOX="+ 
//					Double.toString(bbox[1][0])+","+
//					Double.toString(bbox[0][0])+","+
//					Double.toString(bbox[1][1])+","+
//					Double.toString(bbox[0][1])+"&"+
//			"WIDTH="+wmsW+"&"+
//			"HEIGHT="+wmsH;
//			; 
	
	
	}
	
	
			
	public UrlParamsGeoConversionEPSG102175() {
	}

	
	
	/**
	 * returns BBOX parameter to URL
	 * @return converted url part
	 */
	public String getConvertedBboxUrlPart() {
		return "BBOX="+ 
				Double.toString(bbox[1][0])+","+
				Double.toString(bbox[0][0])+","+
				Double.toString(bbox[1][1])+","+
				Double.toString(bbox[0][1]);
	}
	
	/**
	 * retruns WDTH parameter to URL
	 * @return converted width
	 */
	public String getConvertedWidth() {
		return "WIDTH="+wmsW;
	}
	
	/**
	 * retruns HEIGHT parameter to URL
	 * @return converted height
	 */
	public String getConvertedHeight() {
		return "HEIGHT="+wmsH;
	}
	
	/**
	 * returns URL SRS param
	 * @return converted srs
	 */
	public String getSrsPart() {
		return "SRS="+srs;
	}
	
	/**
	 * Sets params for Geoconversion when external method parsed given URL 
	 * @param centerB
	 * @param centerL
	 * @param zoom
	 * @param pictWidth
	 * @param pictLength
	 */
	public void setUrlGivenParams(double centerB, double centerL, int zoom, int pictWidth, int pictLength ) {
		this.Burl = centerB;
		this.Lurl = centerL;
		this.zoom = zoom;
		this.sizeW = pictWidth;
		this.sizeH = pictLength;
		this.converturl();
	}

}
