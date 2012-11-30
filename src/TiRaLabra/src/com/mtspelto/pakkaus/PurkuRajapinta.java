package com.mtspelto.pakkaus;

import java.io.FileNotFoundException;
import java.io.IOException;

/** Rajapinta, jonka kaikki purkualgoritmit toteuttaa. T�ll� hetkell� t�m�n 
 * rajapinnan toteutusluokkia ovat HuffmanPurku ja LZWPurku. Rajapinnan
 * k�ytt� mahdollistaa uusien algoritmien lis��misen helposti.
 *  
 * @author mikkop
 * @see com.mtspelto.pakkaus.HuffmanPurku
 * @see com.mtspelto.pakkaus.LZWPurku
 */
public interface PurkuRajapinta {
	
	/** Purkaa tiedoston.
	 * 
	 * @return true mik�li purku onnistui, muutoin false
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean puraTiedosto() throws FileNotFoundException, IOException, ClassNotFoundException;

}
