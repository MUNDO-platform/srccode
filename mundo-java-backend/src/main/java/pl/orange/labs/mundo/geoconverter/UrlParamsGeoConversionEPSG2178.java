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
 * Class provides WMS URL on the basis of parameters in Google format
 * 	http://maps.googleapis.com/maps/api/staticmap?center=52.232936,21.061194&zoom=10&size=200x200
 * is converted to 
 * 	http://wms.um.warszawa.pl/serwis?REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1&LAYERS=WMS/Raster_orto2010_8m&STYLES=&FORMAT=image/png&BGCOLOR=0xFFFFFF&TRANSPARENT=TRUE&SRS=EPSG:2178&BBOX=7497257.45418737,5784605.02953798,7509135.16707077,5794666.15103922&WIDTH=1020&HEIGHT=864
 * 
 * @author Jarosław Legierski
 *
 */
public class UrlParamsGeoConversionEPSG2178 implements GeoConversion {
	
	//public String inputurl;
	//public String outputurl;
	
	private double Burl;
	private double Lurl;
	private double Xurl;
	private double Yurl;
	
	private int zoom;
	private int sizeW;
	private int sizeH;
//	private String centerparname="center";
//	private String zoomparname="zoom";
//	private String sizeparname="size";
	
//	private String prefix = "http://wms.um.warszawa.pl/serwis";
//	private String request = "GetMap";
//	private String service = "WMS";
//	private String version = "1.1.1";
//	private String layers  = "WMS/ENOM_Ulice";
	//private String layers  = "WMS/Raster_orto2010_8m";
	//private String layers  = "WMS/Raster_plan_39";
	
	
//	private String styles = "";
//	private String format = "image/png";
//	private String bgcolor = "0xFFFFFF";
//	private String transparent ="TRUE";
	private String srs = "EPSG:2178";
	private double[][] bbox = new double[2][2];
	private int wmsW;
	private int wmsH;
	
	private static GeoconversionEPSG2178  convert;
	
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

//	/**
//	 * Optionally may be used for initiation the object by parsing input URL
//	 * @param inputurl
//	 */
//	public void parseurl(String inputurl) {
//		// wyszukujemy x i y z input url
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
//		// wyszukujemy wartoć zoom  eg- zoom=10
//		
//		index1=inputurl.indexOf(zoomparname)+zoomparname.length()+1;
//		index2=inputurl.indexOf("&",index1);
//		
//		
//		zoom=  Integer.parseInt(inputurl.substring(index1,index2));
//		
//		//wyszukujemy waroć size=200x200
//		
//		index1=inputurl.indexOf(sizeparname)+sizeparname.length()+1;
//		index2=inputurl.indexOf("x");
//		
//		sizeW= Integer.parseInt(inputurl.substring(index1,index2));
//		
//		index1=inputurl.length();
//		
//		sizeH= Integer.parseInt(inputurl.substring(index2+1,index1));
//		this.converturl();
//	}
	
	/**
	 * does the hard work
	 */
	private void converturl() {
	
		wmsW=sizeW;
		wmsH=sizeH;
		convert = new GeoconversionEPSG2178(Burl, Lurl, 200);
		Xurl=convert.Xpl2000; //52-  57
		Yurl=convert.Ypl2000; // 21  75 
//		double 	Wmapy=7519616.26268-7488477.25192; // bazowe wartoci z WMS UM Warszawa
//		double 	Hmapy=5805199.299715-5772296.739585; // bazowe wartoci z WMS UM Warszawa
		
		//int  korekcja=76;	// współczynnik korekcji skali ustalany empirycznie 	
		
		//double scalefactor =2* (256- korekcja)/Math.pow(2,zoom); //skalowanie WG google http://pastebin.com/2hjwWUcN
		
//		double scalefactor =2*180/Math.pow(2,zoom)/256; //współczynnik skalowania 
		
		double grad2linfactor = (40075704/(2*Math.PI)*Math.cos(Burl*Math.PI/180)*2*Math.PI)/360; // km na grad
		double scalefactor =360/(Math.pow(2,zoom)*256); //współczynnik skalowania 
		 
		    bbox[0][0]=Xurl-0.5*grad2linfactor*sizeH*scalefactor;
			bbox[1][0]=Yurl-0.5*grad2linfactor*sizeW*scalefactor;
			bbox[0][1]=Xurl+0.5*grad2linfactor*sizeH*scalefactor;
			bbox[1][1]=Yurl+0.5*grad2linfactor*sizeW*scalefactor;
			
		
	//bbox[0][0]=7497257.45418737; //x1
	//bbox[1][0]=5784605.02953798; //y1
	//bbox[0][1]=7509135.16707077; //x2
	//bbox[1][1]=5794666.15103922; //y2
	
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
	
	/**
	 * returns BBOX parameter to URL
	 * @return converted url bbox part
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
	
}
