/************************************************************************************************
 *   Klasa której zadaniem jest dokonywanie konwersji wspó³rzêdnych z url zawierajacych wspó³rzedne WGS84
 *   na url WMS (PL2000)
 *    
 *  @author Jaros³aw Legierski Orange Labs / Centrum Badawczo - Rozwojowe 
 *  copyright (c) 2014-2015 Orange Polska S.A. niniejszy kod jest otwarty i dystrybuowany 
 *  na licencji:   Lesser General Public License v2.1 (LGPLv2.1), której  pe³ny tekst mo¿na 
 *  znaleŸæ pod adresem:  https://www.gnu.org/licenses/lgpl-2.1.html 
 * 
 * 
 *  oprogramowanie stworzone w ramach Projektu  MUNDO - Dane po Warszawsku http://danepowarszawsku.pl/ 
 *  Projekt wspó³finansowany przez Narodowe Centrum Badañ i Rozwoju w ramach Programu Innowacje Spo³eczne
 *
/**********************************************************************************************/


public class Urlconversion {

	
	public String inputurl;
	public String outputurl;
	
	public double Burl;
	public double Lurl;
	public double Xurl;
	public double Yurl;
	
	private int zoom;
	private int sizeW;
	private int sizeH;
	private String centerparname="center";
	private String zoomparname="zoom";
	private String sizeparname="size";
	
	private String prefix = "http://wms.um.warszawa.pl/serwis"; //WMS Warszawa
	private String request = "GetMap";
	private String service = "WMS";
	private String version = "1.1.1";
	private String layers  =  "WMS/ENOM_Ulice" ; //W-wa
	
	
	private String styles = "";
	private String format = "image/png";
	private String bgcolor = "0xFFFFFF";
	private String transparent ="TRUE";
	private String srs = "EPSG:2178";  
	private String srsWGS84 = "EPSG:4326";
	private double[][] bbox = new double[2][2];
	private int wmsW;
	private int wmsH;
	
	
	public static Geoconversion  convert;
		
	public void parseurl() {
		
		// wyszukujemy x i y z input url
			
		int index1=0;
		int index2=0;
		
		index1=inputurl.indexOf(centerparname)+centerparname.length()+1;
		index2=inputurl.indexOf(",");
		
		Burl= Double.parseDouble(inputurl.substring(index1,index2));
		
		index1=inputurl.indexOf("&");
		
		Lurl= Double.parseDouble(inputurl.substring(index2+1,index1));
		
		// wyszukujemy wartocs zoom  eg- zoom=10
		
		index1=inputurl.indexOf(zoomparname)+zoomparname.length()+1;
		index2=inputurl.indexOf("&",index1);
		
		
		zoom=  Integer.parseInt(inputurl.substring(index1,index2));
		
		//wyszukujemy warosc size=200x200
		
		index1=inputurl.indexOf(sizeparname)+sizeparname.length()+1;
		index2=inputurl.indexOf("x");
		
		sizeW= Integer.parseInt(inputurl.substring(index1,index2));
		
		index1=inputurl.length();
		
		sizeH= Integer.parseInt(inputurl.substring(index2+1,index1));
				
		
	}
	
	public void converturlWGS84()
	{
		
		wmsW=sizeW;
		wmsH=sizeH;
		
	    
		double scalefactorWGS84 =360/(Math.pow(2,zoom)*256); //wspolczynnik skalowania
				
		bbox[0][0]=Burl-0.5*sizeH*scalefactorWGS84;
		bbox[1][0]=Lurl-0.5*sizeW*scalefactorWGS84;
		bbox[0][1]=Burl+0.5*sizeH*scalefactorWGS84;
		bbox[1][1]=Lurl+0.5*sizeW*scalefactorWGS84;

		
		outputurl=prefix+"?"+
				"REQUEST="+request+"&"+
				"SERVICE="+service+"&"+
				"VERSION="+version+"&"+
				"LAYERS="+layers+"&"+
				"STYLES="+styles+"&"+
				"FORMAT="+format+"&"+
				"BGCOLOR="+bgcolor+"&"+
				"TRANSPARENT="+transparent+"&"+
				"SRS="+srsWGS84+"&"+
				"BBOX="+ 
						Double.toString(bbox[1][0])+","+
						Double.toString(bbox[0][0])+","+
						Double.toString(bbox[1][1])+","+
						Double.toString(bbox[0][1])+"&"+
				"WIDTH="+wmsW+"&"+
				"HEIGHT="+wmsH;
				; 
		
	}
	
	
	
	
	
	public void converturl() {
	
		wmsW=sizeW;
		wmsH=sizeH;
		
		convert = new Geoconversion(Burl, Lurl, 200);
		Xurl=convert.Xpl2000; 
		Yurl=convert.Ypl2000; 
				
		
		double grad2linfactor =(40075704/(2*Math.PI)*Math.cos(Burl*Math.PI/180)*2*Math.PI)/360; //km na grad
		
		double scalefactor =360/(Math.pow(2,zoom)*256); //wspolczynnik skalowania 

		
		    bbox[0][0]=Xurl-0.5*grad2linfactor*sizeH*scalefactor;
			bbox[1][0]=Yurl-0.5*grad2linfactor*sizeW*scalefactor;
			bbox[0][1]=Xurl+0.5*grad2linfactor*sizeH*scalefactor;
			bbox[1][1]=Yurl+0.5*grad2linfactor*sizeW*scalefactor;
	
	
		outputurl=prefix+"?"+
			"REQUEST="+request+"&"+
			"SERVICE="+service+"&"+
			"VERSION="+version+"&"+
			"LAYERS="+layers+"&"+
			"STYLES="+styles+"&"+
			"FORMAT="+format+"&"+
			"BGCOLOR="+bgcolor+"&"+
			"TRANSPARENT="+transparent+"&"+
			"SRS="+srs+"&"+
			"BBOX="+ 
					Double.toString(bbox[1][0])+","+
					Double.toString(bbox[0][0])+","+
					Double.toString(bbox[1][1])+","+
					Double.toString(bbox[0][1])+"&"+
			"WIDTH="+wmsW+"&"+
			"HEIGHT="+wmsH;
			; 
	
	
	}
	
	
			
	public Urlconversion() {
		
		
	
	
	
	}

}
