/**********************************************************************************************
 *  PL2000toWGS84FileConverter narzêdzie, którego celem jest konwersja zawartoœci  plików    
 *  tekstowych i konwersja wspó³rzêdnych w nich zawartych z uk³adu PL2000 do WGS84 
 *   
 *  @author Jaros³aw Legierski Centrum Badawczo - Rozwojowe  Orange Labs / Orange Polska S.A.
 *  copyright (c) 2014-2015 Orange Polska S.A. niniejszy kod jest otwarty i dystrybuowany 
 *  na licencji:   GNU GENERAL PUBLIC LICENSE   Version 3, której  pe³ny tekst mo¿na
 *  znale¿æ pod adresem:  http://www.gnu.org/licenses/gpl-3.0.txt
 * 
 *  oprogramowanie stworzone w ramach Projektu  MUNDO - Dane po Warszawsku http://danepowarszawsku.pl/ 
 *  Projekt wspó³finansowany przez Narodowe Centrum Badañ i Rozwoju w ramach Programu Innowacje Spo³eczne
 *
/**********************************************************************************************/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class GUI extends JFrame
implements ActionListener {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextArea outputfile; 
	private JButton bOtworz;
	private JButton bZamknij;
	private JButton bFileConversion;
	private JButton bAbout;
	public JTextArea inputfile;
	public JTextArea xwgs84desc;
	public JTextArea ywgs84desc;
	public JTextArea xpl2000desc;
	public JTextArea ypl2000desc;
	public JTextArea separatordesc;
    public 	Image image = null;
    private JLabel labelBL;
    private JLabel labelXY;
    private JLabel labelseparator;
    public JCheckBox checkboxobrys; 




	public GUI()
	{ 
		//poczatek tworzenia GUI
		
		String x_description = "x";  
 		String y_description = "y";
 		String x_description_wgs84 ="x_wgs84";
 		String y_description_wgs84 ="y_wgs84";
 		String separator_desc = ";" ;  		 				 		
 		
		
		JFrame frame = new JFrame("TXT"); //
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //
		frame.setPreferredSize(new Dimension(800, 400));	
		JPanel contentPane = new JPanel(new BorderLayout());
		frame.setTitle("PL2000->WGS84 file converter");
		       
        
        xpl2000desc= new JTextArea(x_description);
 		xpl2000desc.setBounds(380,165,150,20);
 		xpl2000desc.setLineWrap(true);
 		xpl2000desc.setVisible(true);
 		
 		ypl2000desc= new JTextArea(y_description);
 		ypl2000desc.setBounds(550,165,150,20);
 		ypl2000desc.setLineWrap(true);
 		
 		xwgs84desc = new JTextArea(x_description_wgs84);
 		xwgs84desc.setBounds(380,200,150,20);
 		xwgs84desc.setLineWrap(true);
 		
 		ywgs84desc = new JTextArea(y_description_wgs84);
 		ywgs84desc.setBounds(550,200,150,20);
 		ywgs84desc.setLineWrap(true);
 		
 		separatordesc = new JTextArea(separator_desc);
 		separatordesc.setBounds(550,230,150,20);
 		separatordesc.setLineWrap(true);
 		
 		inputfile = new JTextArea();
 		inputfile.setBounds(30,25,700,30);
 		inputfile.setLineWrap(true);
 		
 	    outputfile = new JTextArea();
 	    outputfile.setBounds(30,78,700,30);

        labelBL  = new JLabel("", JLabel.CENTER);        
        labelBL.setText("X,Y (PL2000)");
        labelBL.setBounds(690, 165, 100, 30);
        labelBL.setVisible(true);
        
        labelXY  = new JLabel("", JLabel.CENTER);        
        labelXY.setText("B,L (WGS84)");
        labelXY.setBounds(690, 200, 100, 30);
        labelXY.setVisible(true);

        labelseparator  = new JLabel("", JLabel.CENTER);        
        labelseparator.setText("separator");
        labelseparator.setBounds(380, 230, 100, 30);
        labelseparator.setVisible(true);
       
        checkboxobrys = new JCheckBox("Plik zawiera obrysy");
        checkboxobrys.setSelected(true);
        checkboxobrys.setBounds(380, 260, 300, 20);
 	    checkboxobrys.setVisible(true);
 	   
 		bOtworz = new JButton("Otwórz plik");
 		bOtworz.setBounds(30, 165, 150, 30);
 		bOtworz.addActionListener(this);
 	
 		
 		bZamknij = new JButton("Zamknij");
 		bZamknij.setBounds(30,200,150,30);
 		bZamknij.addActionListener(this);
	
 		
 		bFileConversion = new JButton("Konwersja pliku");
 		bFileConversion.setBounds(200,165,160,30);
 		bFileConversion.addActionListener(this);
 		
 		bAbout = new JButton("O programie");
 		bAbout.setBounds(200,200,160,30);
 		frame.add(bAbout);
 		bAbout.addActionListener(this);
 				
 		
 		
 		contentPane.setSize(frame.getSize());
 		contentPane.setLayout(null);
 		contentPane.setBorder(BorderFactory.createLineBorder(Color.black));
 		contentPane.setVisible(true);
 		contentPane.add(xpl2000desc);
 		contentPane.add(ypl2000desc);
 		contentPane.add(xwgs84desc);
 		contentPane.add(ywgs84desc);
 		contentPane.add(labelBL);
 		contentPane.add(labelXY);
 		contentPane.add(labelseparator);
 		contentPane.add(separatordesc);
 		contentPane.add(checkboxobrys);
 		contentPane.add(inputfile);
 		contentPane.add(outputfile);
 		contentPane.add(bOtworz);
 		contentPane.add(bZamknij);
 		contentPane.add(bAbout);
 		contentPane.add(bFileConversion);
 		
 		frame.setContentPane(contentPane); 	
 	    frame.setLocationRelativeTo(null);  // centrujemy  na œrodku okna
	 	frame.setBounds(300, 350,800, 400);
	    frame.setLayout(null);
		
			
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
    
	 	frame.pack(); 		
	 	frame.setVisible(true);
		 		
		 		//koniec GUI
		 
		 	 	
	}
	
 


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		
		Object z = e.getSource();
		//obsluga przyciskow znajdujacych sie na konsoli glownej
		if(z==bOtworz)
		{
			 Application.Start();
		}

		
		if(z==bFileConversion)
		{
			Application.Conversion();
		}
		
		if(z==bAbout)
		{

			Application.About();
		}
		
		if(z==bZamknij)
		{
			
			
			System.exit(0);
			
		}
		
	
		
		
		
	}
	
	
}