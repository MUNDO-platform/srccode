import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**********************************************************************************************
 *  PL2000toWGS84FileConverter narzêdzie, którego celem jest konwersja zawartoœcici  plików    
 *  tekstwych i konwersja wspó³rzêdnych w nich zawartych z uk³adu PL2000 do WGS84 
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



public class Application {

	
	
	public static GUI app;
	public static File input_file;
	public static File output_file;
	public static String x_description ="x";
	public static String y_description ="y";
	public static String x_description_pl2000 ="x_pl2000";
	public static String y_description_pl2000 ="y_pl2000";
	
	public static Geoconversion convert;
	
	public Application() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		app = new GUI();
		System.out.println("GUI loaded...");
		
	}
	
	
	public static void Start()  {
		
		 
	
		
		JFileChooser fc = new JFileChooser();
		if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
		input_file = fc.getSelectedFile();


		app.inputfile.setText(input_file.getPath());
		String outpath= ((input_file.getPath())+" "+".csv");
		output_file= new File(outpath); 
		app.outputfile.setText(output_file.getPath());
	
		
	}
	
	public static void Conversion(){
		

		double BWGS84 =0.0;
		double LWGS84 = 0.0;
		double XPL2000=0.0;
		double YPL2000=0.0;
		BufferedReader br = null;
		BufferedWriter bw = null;		
		x_description = app.xpl2000desc.getText();
		y_description = app.ypl2000desc.getText();
		x_description_pl2000 = app.xwgs84desc.getText();
		y_description_pl2000 =app.ywgs84desc.getText();
		
		
		
		
		
		
			// BufferedReader & FileInputStream
		try {
	
			try {
				
			
				br= new BufferedReader(
				 new InputStreamReader(
				         new FileInputStream(input_file ), "UTF8"));
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// BufferedWriter 
		
		try {
			bw =	new BufferedWriter(new FileWriter(output_file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		
		
		String line;
		String newline = null;
		String[] header = null ;
		String[] line_elements ;
		String[] line_elements_full = null ;
		String separator =";";
		int linenumber=0;
		int x_position = 0;		
		int y_position = 0;
		
		
	
		
	    try {
			while ((line = br.readLine()) != null) {
			//odczyt nagÅ‚Ã³Å‚wka
			
				if (linenumber==0){
				header = line.split(separator);
				header[0]=header[0].replace("\uFEFF", ""); //USUNIECIE BOM z UTF-8
				x_position=Arrays.asList(header).indexOf(x_description);
				y_position=Arrays.asList(header).indexOf(y_description);
				
				// dopisujemy przed X wspó³rzêdne w WGS 84
				header[x_position]=  x_description_pl2000+ separator+ y_description_pl2000+ separator+ header[x_position];

				StringBuilder newlinesb= new StringBuilder();
				//zapis do nowej linii
				for (String lineelement : header ) {
					newlinesb.append(lineelement);
					newlinesb.append(separator);
					}
				
				newline=newlinesb.toString();
				linenumber++;
				}
					
				
			else{
				linenumber++;
				line_elements = line.split(separator);
				
				BWGS84= Double.parseDouble(line_elements[x_position]);
				LWGS84= Double.parseDouble(line_elements[y_position]);
				convert = new Geoconversion(LWGS84,BWGS84,0.00); //Bwgs84
				YPL2000 = convert.Xpl2000;   
				XPL2000 = convert.Ypl2000;
				
				XPL2000=(double)Math.round(XPL2000 * 1000000000) / 1000000000; //aproksymacja 9 miejsc po przecinku
				YPL2000=(double)Math.round(YPL2000 * 1000000000) / 1000000000; //aproksymacja 9 miejsc po przecinku
				
				line_elements[x_position]= Double.toString(XPL2000)+ separator+ Double.toString(YPL2000) + separator+ line_elements[x_position];
				
				
				if (app.checkboxobrys.isSelected()&&header.length!=line_elements.length)
				{ 
				//plik z obrysem 
				

					int  line_elements_length=line_elements .length;  
					
					for (int i =1 ; i<header.length-line_elements_length+1; i++ ) {
						
						
						line_elements = Arrays.copyOf(line_elements, line_elements.length +1);
						
						line_elements [line_elements .length-1 ] = line_elements_full[line_elements_length+i-1];
						  
							
						};
					
					
					
					
					
					StringBuilder newlinesb= new StringBuilder();
					for (String lineelement : line_elements ) {
						
						newlinesb.append(lineelement);
						newlinesb.append(separator);
						}
					
					
					
					
					newline=newlinesb.toString();
					linenumber++;
					
					
				
				}
				else{
					
			    //plik bez obrysu 
				//zapis do nowej linii
				line_elements_full=line_elements;
				StringBuilder newlinesb= new StringBuilder();
				for (String lineelement : line_elements ) {
					
					newlinesb.append(lineelement);
					newlinesb.append(separator);
					}
				
				
				
				
				newline=newlinesb.toString();
				linenumber++;
				}
				
				}
				
				if (linenumber!=1)
				{
				bw.newLine();
				}
				
				bw.write(newline);
				BWGS84 =0.0;
				LWGS84 =0.0;

				
				
				
				// process the line.
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		try {
			bw.close();	
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void About(){
	
		JOptionPane.showMessageDialog(app, 
				 "WGS84toPL2000FileConverter narzêdzie, którego celem jest konwersja zawartoœci  plików"+""
				+ "\n"+    
				 "tekstowych i konwersja wspó³rzêdnych w nich zawartych z WGS84 do PL2000 " 
				 + "\n"+
				 "@author Jaros³aw Legierski Centrum Badawczo - Rozwojowe  / Orange Labs / Orange Polska S.A."
				 + "\n"+
				 ""
				 + "\n"+
				 "Oprogramowanie stworzone w ramach Projektu : MUNDO Miejskie Us³ugi Na Danych Oparte"
				 + "\n"+
				 ""
				 +"\n"+
				 "Projekt wspó³finansowany przez Narodowe Centrum Badañ i Rozwoju w ramach" 
				 +"\n"+
				 "Programu Innowacje Spo³eczne"
		);
	}
	
	
}
