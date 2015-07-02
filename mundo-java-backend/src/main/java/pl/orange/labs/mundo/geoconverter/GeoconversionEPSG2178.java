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
 * @author Jarosław Legierski
 *
 */
public class GeoconversionEPSG2178 {
	public double Bwgs84; 	// szerokość geodezyjna punktu 
	public double Lwgs84; 	// długość geodezyjna punktu 
	public double Hwgs84; 	// wysokości geometryczna (elipsoidalna)
	public double  X;    	// współrzędna kartezjańska X 
	public double  Y;    	// współrzędna kartezjańska Y
	public double  H;	   	// współrzędna kartezjańska H
	
	public double  Xgk;    	// współrzędna X Gaussa-Krugera 
	public double  Ygk;    	// współrzędna Y Gaussa-Krugera
	public double  Hgk;	   	// współrzędna H Gaussa-Krugera
	
	public double  Xpl1992;  // współrzędna kartezjańska X w układzie 1992 
	public double  Ypl1992;  // współrzędna kartezjańska Y w układzie 1992
	
	public double  Xpl2000; 	// współrzędna kartezjańska X w układzie 2000
	public double  Ypl2000;		// współrzędna kartezjańska Y w układzie 2000
	
	private double a = 6378137; 		//Długoć dużej półosi elipsoidy WGS84
	private double b = 6356752.31414; 	//Długoć małej półosi elipsoidy WGS84   
	private double e; 					// mimosród elipsoidy
	private double N;					// promień przekroju elipsoidy w pierwszym wertykale
	
	private double 	m0 = 0.999923; 		//skala odwzorowania w układzie pl2000
	private Integer L0 = 21; 			//długoć południka osiowego wybranej strefy (w stopniach) - w układzie pl2000
	private Integer c = L0/3;  			// - cecha strefy - w układzie pl2000
	
	private double Fi; 	  				// szerokoć sferyczna (odwzorowanie Lagrange'a)
	private double lambda;  			// długoć sferyczna (odwzorowanie Lagrange'a)
	private double k; 	  				// wielkoć pomocnicza (odwzorowanie Lagrange'a)
	private double alfamerc; 			//  współrzędna katowa zespolona  w odwzorowaniu Mercatora
	private double betamerc; 			//  współrzędna katowa zespolona w odwzorowaniu Mercatora
	private double xmerc; 				//  współrzędna kartezjanska X w odwzorowaniu Mercatora
	private double ymerc; 				//  współrzędna kartezjanska Y w odwzorowaniu Mercatora
	private double Ro;  				// promień strefy odwzorowania Lagrange'a
	
	private double a2 = 0.8377318247344E-3; // współczynnik wielomianu aproksymujšcago w odwzorowaniu Gaussa-Kruegera 
	private double a4 = 0.7608527788826E-6;	// współczynnik wielomianu aproksymujšcago w odwzorowaniu Gaussa-Kruegera
	private double a6 = 0.1197638019173E-8; // współczynnik wielomianu aproksymujšcago w odwzorowaniu Gaussa-Kruegera
	private double a8 = 0.2443376242510E-11; // współczynnik wielomianu aproksymujšcago w odwzorowaniu Gaussa-Kruegera
	
	public GeoconversionEPSG2178 (double B, double L, double H){
	
		Bwgs84 =B;
		Lwgs84 =L;
		Hwgs84=H;
		 
	// Przeliczenie wielkoci kštowych BLH na kartezjanskie XYZ dla elipsoiry GRS80 (WGS84)
		
	e=Math.sqrt((Math.pow(a,2)-Math.pow(b,2))/Math.pow(a,2));  
	N = (a)/(Math.sqrt((1-(Math.pow(e, 2)*(Math.sin((Bwgs84*Math.PI)/180)*Math.sin((Bwgs84*Math.PI)/180) ))))) ; 
	X =  ((N+Hwgs84)* Math.cos((Bwgs84*Math.PI)/180)*Math.cos((Lwgs84*Math.PI)/180));
	Y =  ((N+Hwgs84)* Math.cos((Bwgs84*Math.PI)/180)*Math.sin((Lwgs84*Math.PI)/180));
	H =  ((N*(1-Math.pow(e,2))+Hwgs84)*Math.sin((Bwgs84*Math.PI)/180));
	
	// odwzorowanie Lagrange'a B,L-> Fi,lambda
	
	k= Math.pow((1-e*Math.sin((B*Math.PI)/180))/((1+e*Math.sin((B*Math.PI)/180))) , e/2);
	Fi = 2*(Math.atan(k*Math.tan(((B*Math.PI)/180)/2+Math.PI/4))-Math.PI/4); 
	lambda =L;
	Ro=6367449.14577105;

	//Fi=186527.772995787/3600; //51.8132702766075 //dane testowe
	//Fi=(Fi*Math.PI)/180; //w radianach //dane testowe
	
	
	// odwzorowanie Mercatora Fi,lambda -> alfamerc, betamerc lub Fi,lambda -> xmerc, ymerc 
	
	
	double deltalambda=lambda-L0; //w stopniach
	
	//deltaalfa=-14400/3600; //test 
	
	deltalambda=(deltalambda*Math.PI)/180; //w radianach
			
	
	xmerc= Ro* Math.atan((Math.sin(Fi))/(Math.cos(Fi)*Math.cos(deltalambda)));
	ymerc= 0.5*Ro* Math.log((1+Math.cos(Fi)*Math.sin(deltalambda))/((1-Math.cos(Fi)*Math.sin(deltalambda))));
	
	alfamerc=  Math.atan((Math.sin(Fi))/(Math.cos(Fi)*Math.cos(deltalambda)));
	betamerc= 0.5* Math.log((1+Math.cos(Fi)*Math.sin(deltalambda))/((1-Math.cos(Fi)*Math.sin(deltalambda))));
	
	
	//  odwzorowanie Gaussa-Kruegera 
	
	Xgk= Ro*(alfamerc+ 
			(
			(a2*Math.sin(2*alfamerc)*Math.cosh(2*betamerc))+ 
			(a4*Math.sin(4*alfamerc)*Math.cosh(4*betamerc))+
			(a6*Math.sin(6*alfamerc)*Math.cosh(6*betamerc))+
			(a8*Math.sin(8*alfamerc)*Math.cosh(8*betamerc))
			)
			); 
	
	Ygk= Ro*(betamerc+ 
			(
			(a2*Math.cos(2*alfamerc)*Math.sinh(2*betamerc))+ 
			(a4*Math.cos(4*alfamerc)*Math.sinh(4*betamerc))+
			(a6*Math.cos(6*alfamerc)*Math.sinh(6*betamerc))+
			(a8*Math.cos(8*alfamerc)*Math.sinh(8*betamerc))
			)
			); 
	
	 // obliczenie XY w układzie 1992
	
	Xpl1992 =  0.9993*Xgk -5300000; 		//Xpl1992
	Ypl1992 =  0.9993*Ygk + 500000;			//Ypl1992

	 // obliczenie XY w układzie 2000
	
	Xpl2000 =  m0*Xgk; 							// Xpl2000
	Ypl2000 =  m0*Ygk + 500000 + c* 1000000; 	// Ypl2000
	} 
	
	
}
