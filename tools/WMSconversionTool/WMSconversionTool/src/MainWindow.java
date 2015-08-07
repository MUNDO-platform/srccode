/************************************************************************************************
 *  WMSconversionTool narzêdzie,  którego celem jest porównanie map pochodz¹cych z dwóch 
 *  Ÿróde³ oraz dokonywania konwersji wspó³rzêdnych z uk³adu WGS84  na PL 2000 
 * 
 *  @author Jaros³aw Legierski Centrum Badawczo - Rozwojowe  Orange Labs / Orange Polska S.A.
 *  copyright (c) 2014-2015 Orange Polska S.A. niniejszy kod jest otwarty i dystrybuowany 
 *  na licencji:   GNU GENERAL PUBLIC LICENSE   Version 3 , której  pe³nyny tekst mo¿na
 *  znaleŸæ pod adresem: http://www.gnu.org/licenses/gpl-3.0.txt 
 * 
 *  oprogramowanie stworzone w ramach Projektu  MUNDO – Dane po Warszawsku http://danepowarszawsku.pl/ 
 *  Projekt wspó³finansowany przez Narodowe Centrum Badañ i Rozwoju w ramach Programu Innowacje Spo³eczne
 *
/**********************************************************************************************/

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;





public class MainWindow {


	
	public static String path = "http://maps.googleapis.com/maps/api/staticmap?center=52.232936,21.061194&zoom=10&size=200x200";  //warszawa
	
	public static String pathwms = "http://wms.um.warszawa.pl/serwis?REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1&LAYERS=WMS/ENOM_Ulice&STYLES=&FORMAT=image/png&BGCOLOR=0xFFFFFF&TRANSPARENT=TRUE&SRS=EPSG:2178&BBOX=7497257.45418737,5784605.02953798,7509135.16707077,5794666.15103922&WIDTH=1020&HEIGHT=864";
	
	
	
			public static GUI app;
			public static Geoconversion  convert;
			public static Urlconversion  urlconversion;
			
			
	public  BufferedImage bimg ;
	
	
	
	public static void main(String[] args) throws IOException {

		app = new GUI();
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		app.url.setText(path);
		app.wmsurl.setText(pathwms);
		System.out.println("GUI loaded...");
              
		
	}
	
	
		public static  Image readImage(String fn) throws IOException {
		Image img = null;
		URL url = new URL(fn);
		img = ImageIO.read(url);
		
		return img;
		}
	
	public static void Start()  {
		// TODO Auto-generated method stub
		Image img1 = null;
		Image imgwms = null;
		 
	     
				
		
		try {
			String path1=app.url.getText(); 
			URL url = new URL(path1);
			img1 = ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			pathwms=app.wmsurl.getText(); 
			URL urlwms = new URL(pathwms);
			imgwms = ImageIO.read(urlwms);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
		
		
		ImageIcon icon = new ImageIcon(img1);
		ImageIcon iconwms = new ImageIcon(imgwms);
		
		app.imageGMaps.setIcon(icon); 
		 
		app.imageGMaps.repaint();
		
		app.imageWMS.setIcon(iconwms);
		
		
		
		
		
	}
	
	
	public static void Konwersjaurl()  {
		
		urlconversion = new Urlconversion();
		
		urlconversion.inputurl=app.url.getText(); 
		
		
		urlconversion.parseurl();
		
		urlconversion.converturl();
		
		app.wmsurl.setText(urlconversion.outputurl );
			
		
	}
	
		public static void KonwersjaurlWGS84()  {
		
		urlconversion = new Urlconversion();
		
		urlconversion.inputurl=app.url.getText(); 
		
		
		urlconversion.parseurl();
		
		urlconversion.converturlWGS84();
		
		app.wmsurl.setText(urlconversion.outputurl );
		
		
			
	
		
		
		
		
		
	}
	
	public static void Przelicz()  {
		
		
		
	
		
		double B = Double.parseDouble(app.bwgs84.getText());
		double L = Double.parseDouble(app.lwgs84.getText());
		double H = 200; 
		
		convert = new Geoconversion(B,L,H);
		
		
		double  X = convert.Xpl2000;
		double  Y = convert.Ypl2000;
		
		app.xpl2000.setText(Double.toString(X)); 
		app.ypl2000.setText(Double.toString(Y));
		
	}
	
	

	

}
