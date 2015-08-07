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

import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;




public class GUI  extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextArea url; //input url for WMS testing
	public JTextArea xml; //xml from WMS service
	private JButton bStart;
	private JButton bZamknij;
	private JButton bPrzelicz;
	private JButton bKonwersjaurl;
	private JButton bKonwersjaurlWGS84;
	public JTextArea wmsurl;
	public JTextArea xpl2000;
	public JTextArea ypl2000;
	public JTextArea bwgs84;
	public JTextArea lwgs84;
	public JLabel imageGMaps;
    public JFrame frameGMaps ;
	public JLabel imageWMS;
    public JFrame frameWMS ;
    public 	Image image = null;
    private JLabel labelBL;
    private JLabel labelXY;




	public GUI()
	{ 
		//poczatek tworzenia GUI
		setTitle("WMSconversionTool");
	
		setBounds(300, 350,800, 400);
		setLayout(null);
		
			
		
		
		 try {
	           

			    File input =  new File("staticmap.png") ;
			    
			     image =  ImageIO.read(input);
			     
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }

		
		
		
		try {      //deklaracja wygladu konsoli 
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		
			try {
		        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		    } catch (Exception ex) {
		        // error
		    }
		 		}
		 		new Container();
		 		
		 		

		 		double Htest = 52.00 + 13.00/60+ 56.00/3600; //dane testowe 
		 		double Ltest = 21.00 + 0+ 30.00/3600;
		 		
		 				 		
		 		
		 		bwgs84= new JTextArea(Double.toString(Htest));
		 		bwgs84.setBounds(380,165,150,30);
		 		bwgs84.setLineWrap(true);
		 		add(bwgs84);
		 		
		 		lwgs84= new JTextArea(Double.toString(Ltest));
		 		lwgs84.setBounds(550,165,150,30);
		 		lwgs84.setLineWrap(true);
		 		add(lwgs84);
		 		
		 		xpl2000 = new JTextArea();
		 		xpl2000.setBounds(380,200,150,30);
		 		xpl2000.setLineWrap(true);
		 		add(xpl2000);
		 		
		 		ypl2000 = new JTextArea();
		 		ypl2000.setBounds(550,200,150,30);
		 		ypl2000.setLineWrap(true);
		 		add(ypl2000);
		 		

		 		
		 		wmsurl = new JTextArea();
		 		wmsurl.setBounds(30,78,700,60);
		 		wmsurl.setLineWrap(true);
		 		add(wmsurl);
		 		
		 		
		 	    url = new JTextArea();
		 	    url.setBounds(30,25,700,30);
		 	    add(url);
		 	    
		        frameGMaps = new JFrame();
		        frameGMaps.setSize(600, 300);
		        frameGMaps.setResizable(isResizable());
		        frameGMaps.setName("GoogleMaps");
	     	    imageGMaps = new  JLabel(new ImageIcon(image));
		 	    imageGMaps.setBounds(400,50,500,500);
		 	    frameGMaps.add(imageGMaps);
		        frameGMaps.setVisible(true);
		        frameGMaps.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		        
		        frameWMS = new JFrame();
		        frameWMS.setSize(600, 300);
		        frameWMS.setResizable(isResizable());
		        frameWMS.setName("WMS");
		        frameWMS.setBounds(650, 0, 600, 300);
	     	    imageWMS = new  JLabel(new ImageIcon(image));
		 	    imageWMS.setBounds(400,50,500,500);
		 	    frameWMS.add(imageWMS);
		        frameWMS.setVisible(true);
		        frameWMS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		        labelBL  = new JLabel("", JLabel.CENTER);        
		        labelBL.setText("B,L (WGS84)");
		        labelBL.setBounds(690, 165, 100, 30);
		        labelBL.setVisible(true);
		        add(labelBL) ; 
		        
		        labelXY  = new JLabel("", JLabel.CENTER);        
		        labelXY.setText("X,Y (PL2000)");
		        labelXY.setBounds(690, 200, 100, 30);
		        labelXY.setVisible(true);
		        add(labelXY) ; 

	    
		 	    
		 		bStart = new JButton("Wyœwietl mapy");
		 		bStart.setBounds(30, 165, 150, 30);
		 		add(bStart);
		 		bStart.addActionListener(this);
		 	
		 		
		 		bZamknij = new JButton("Zamknij");
		 		bZamknij.setBounds(30,200,150,30);
		 		add(bZamknij);
		 		bZamknij.addActionListener(this);
		 		
		 		bPrzelicz = new JButton("Przelicz WGS84 -> PL2000");
		 		bPrzelicz.setBounds(380,240,320,30);
		 		add(bPrzelicz);
		 		bPrzelicz.addActionListener(this);
		 		
		 		
		 		bKonwersjaurl = new JButton("Konwersja url (PL2000)");
		 		bKonwersjaurl.setBounds(200,165,160,30);
		 		add(bKonwersjaurl);
		 		bKonwersjaurl.addActionListener(this);
		 		
		 		bKonwersjaurlWGS84 = new JButton("Konwersja url (WGS84)");
		 		bKonwersjaurlWGS84.setBounds(200,200,160,30);
		 		add(bKonwersjaurlWGS84);
		 		bKonwersjaurlWGS84.addActionListener(this);
		 		//koniec GUI
		 		
		 
		 	
	}
	
 


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		this.addWindowListener(new WindowAdapter() {
              @Override
              public void windowClosing(WindowEvent e) {
                  super.windowClosing(e);
                  System.exit(0);
                 
              }
          });
		
		Object z = e.getSource();
		//obsluga przyciskow znajdujacych sie na konsoli glownej
		if(z==bStart)
		{
			 MainWindow.Start();
		}

		if(z==bPrzelicz)
		{
			 MainWindow.Przelicz();
		}

		if(z==bKonwersjaurl)
		{
			MainWindow.Konwersjaurl();
		}
		
		if(z==bKonwersjaurlWGS84)
		{
			MainWindow.KonwersjaurlWGS84();
		}
		
		if(z==bZamknij)
		{
			
			
			System.exit(0);
			
		}
		
	
		
		
		
	}
	
	
}
