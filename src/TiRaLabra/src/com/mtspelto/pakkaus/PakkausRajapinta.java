package com.mtspelto.pakkaus;

import java.io.FileNotFoundException;
import java.io.IOException;

/** Rajapinta, jonka kaikki pakkausalgoritmit toteuttaa. Tällä hetkellä tämän 
 * rajapinnan toteutusluokkia ovat HuffmanPakkaus ja LZWPakkaus. Rajapinnan
 * käyttö mahdollistaa uusien algoritmien lisäämisen helposti.
 *  
 * @author mikkop
 * @see com.mtspelto.pakkaus.HuffmanPakkaus
 * @see com.mtspelto.pakkaus.LZWPakkaus
 */
public interface PakkausRajapinta {
	
	/** Pakkaa annetun tiedoston.
	 * 
	 * @return true mikäli pakkaus onnistui, muutoin false 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean pakkaaTiedosto() throws FileNotFoundException, IOException;
	
	/** Purkaa tiedoston.
	 * 
	 * @return true mikäli purku onnistui, muutoin false
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean puraTiedosto() throws FileNotFoundException, IOException, ClassNotFoundException;
		
}
