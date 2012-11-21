package com.mtspelto.pakkaus;

import java.io.FileNotFoundException;
import java.io.IOException;

/** Rajapinta, jonka kaikki pakkausalgoritmit toteuttaa. T�ll� hetkell� t�m�n 
 * rajapinnan toteutusluokkia ovat HuffmanPakkaus ja LZWPakkaus. Rajapinnan
 * k�ytt� mahdollistaa uusien algoritmien lis��misen helposti.
 *  
 * @author mikkop
 * @see com.mtspelto.pakkaus.HuffmanPakkaus
 * @see com.mtspelto.pakkaus.LZWPakkaus
 */
public interface PakkausRajapinta {
	
	/** Pakkaa annetun tiedoston.
	 * 
	 * @return true mik�li pakkaus onnistui, muutoin false 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean pakkaaTiedosto() throws FileNotFoundException, IOException;
	
	/** Purkaa tiedoston.
	 * 
	 * @return true mik�li purku onnistui, muutoin false
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean puraTiedosto() throws FileNotFoundException, IOException, ClassNotFoundException;
		
}
