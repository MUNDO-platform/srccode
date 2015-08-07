/************************************************************************************************
 *   Klasa której zadaniem jest dokonywanie konwersji współrzędnych z układu PL2000
 *   na WGS84
 * 
 *  @author Jarosław Legierski Centrum Badawczo - Rozwojowe  Orange Labs / Orange Polska S.A.
 *  copyright (c) 2014-2015 Orange Polska S.A. niniejszy kod jest otwarty i dystrybuowany 
 *  na licencji:   Lesser General Public License v2.1 (LGPLv2.1), której  pełny tekst można
 *  znaleźć pod adresem:  https://www.gnu.org/licenses/lgpl-2.1.html 
 * 
 *  oprogramowanie stworzone w ramach Projektu  MUNDO - Dane po Warszawsku http://danepowarszawsku.pl/ 
 *  Projekt współfinansowany przez Narodowe Centrum Badań i Rozwoju w ramach Programu Innowacje Społeczne
 *
/**********************************************************************************************/

public class ReverseGeoconversion {
	
	private double  Xpl2000; 	// współrzędna kartezjaęska X w układzie 2000
	private double  Ypl2000;	//współrzędna kartezjaęska Y w układzie 2000
	public double  Xgk;    	// współrzędna X Gaussa-Krugera 
	public double  Ygk;    	// współrzędna Y Gaussa-Krugera
	public double  Hgk;	   	// współrzędna H Gaussa-Krugera
	private double  u;    	// unormowana współrzędna X Gaussa-Krugera 
	private double  v;    	// unormowana współrzędna Y Gaussa-Krugera
	private double 	m0 = 0.999923; 		//skala odwzorowania w układzie  pl2000
	private Integer L0 = 21; 			//długość  południka osiowego wybranej strefy (w stopniach) - w układzie pl2000 Warszawa

	private Integer c = L0/3;  			// - cecha strefy - w układzie pl2000
	private double Ro;  				// promień strefy odwzorowania Lagrange'a
	private double alfamerc; 			//  współrzędna katowa zespolona  w odwzorowaniu Mercatora
	private double betamerc; 			//  współrzędna katowa zespolona w odwzorowaniu Mercatora
	private double b2 = -0.8377321681641E-3; // wspóczynnik wielomianu aproksymujęcago w odwzorowaniu odwrotnym Gaussa-Kruegera 
	private double b4 = -0.5905869626083E-7;	// współczynnik  wielomianu aproksymujęcago w odwzorowaniu odwrotnym Gaussa-Kruegera
	private double b6 = -0.1673488904988E-9; // wspóczynnik  wielomianu aproksymujęcago w odwzorowaniu odwrotnym Gaussa-Kruegera
	private double b8 = -0.2167737805597E-12; // wspóczynnik  wielomianu aproksymujęcago w odwzorowaniu odwrotnym Gaussa-Kruegera
	private double Fi; 	  				// szerokość sferyczna (odwzorowanie Lagrange'a)
	@SuppressWarnings("unused")
	private double lambda;  			// długość sferyczna (odwzorowanie Lagrange'a)
	private double deltalambda;			//deltalambda=lambda-L0
	//private double c2 = 0.3356551485597E-12; // wspóczynnik  wielomianu aproksymujęcago w odwzorowaniu odwrotnym Lagrange'a  
	//private double c4 = 0.6571873148459E-5;	// wspóczynnik  wielomianu aproksymujęcago w odwzorowaniu odwrotnym Lagrange'a 
	//private double c6 = 0.1764656426454E-7; // wspóczynnik  wielomianu aproksymujęcago w odwzorowaniu odwrotnym Lagrange'a 
	//private double c8 = 0.5400482187760E-10; // wspóczynnik  wielomianu aproksymujęcago w odwzorowaniu odwrotnym Lagrange'a
	
	private double c2 =  0.003356551485597 ; // wspóczynnik  wielomianu aproksymujęcago w odwzorowaniu odwrotnym Lagrange'a  
	private double c4 =  0.000006571873148459;	// wspóczynnik  wielomianu aproksymujęcago w odwzorowaniu odwrotnym Lagrange'a 
	private double c6 =  0.00000001764656426454; // wspóczynnik  wielomianu aproksymujęcago w odwzorowaniu odwrotnym Lagrange'a 
	private double c8 =  0.00000000005400482187760; // wspóczynnik  wielomianu aproksymujęcago w odwzorowaniu odwrotnym Lagrange'a
	
	
	private double Bwgs84rad; 	// szerokość geodezyjna punktu 
	@SuppressWarnings("unused")
	private double Lwgs84rad; 	// długość geodezyjna punktu 
	public double Bwgs84; 	// szerokość geodezyjna punktu 
	public double Lwgs84; 	// długość geodezyjna punktu 
	public double Hwgs84; 	// wysokość geometryczna (elipsoidalna)

	
	
	public ReverseGeoconversion(double Y, double X) {
			
		Xpl2000=X;
		Ypl2000=Y;
		

		
    // obliczenie Xgk Ygk w z XY układzie 2000 - przesunięcie 
	
		Xgk =Xpl2000/m0; 							// Xgk
		Ygk=(Ypl2000 - 500000 - c*1000000)/m0; 		//Ygk
		
	 // Przekształcenie odwrotne do odwzorowania Gaussa-Kruegera
		
		Ro=6367449.14577105;
		u=Xgk/Ro;
		v=Ygk/Ro;
		


		alfamerc=u+ 
				(
				(b2*Math.sin(2*u)*Math.cosh(2*v))+
				(b4*Math.sin(4*u)*Math.cosh(4*v))+
				(b6*Math.sin(6*u)*Math.cosh(6*v))+
				(b8*Math.sin(8*u)*Math.cosh(8*v))
				);
	    betamerc=v+
	    		(
	    		(b2*Math.cos(2*u)*Math.sinh(2*v))+
	    		(b4*Math.cos(4*u)*Math.sinh(4*v))+
	    		(b6*Math.cos(6*u)*Math.sinh(6*v))+
	    		(b8*Math.cos(8*u)*Math.sinh(8*v))
	    		);
				

    
	double w= 2.00*  Math.atan(Math.exp(betamerc))-(Math.PI)/2.00; // ?
			
		Fi=Math.asin(Math.cos(w)*Math.sin(alfamerc)) ; //rad !
		deltalambda =  Math.atan(Math.tan(w)/Math.cos(alfamerc)); //rad !  			


	    
		Bwgs84rad = Fi + c2 * Math.sin(2*Fi)+ c4*Math.sin(4*Fi)+c6*Math.sin(6*Fi)+c8*Math.sin(8*Fi);
		
		Lwgs84rad=deltalambda;
		Lwgs84=deltalambda*180/Math.PI+L0;
				
		Bwgs84 =Bwgs84rad*180/Math.PI; 
		
		
	}

}
