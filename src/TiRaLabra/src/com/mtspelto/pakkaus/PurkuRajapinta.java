package com.mtspelto.pakkaus;

import java.io.FileNotFoundException;
import java.io.IOException;

/** Rajapinta, jonka kaikki purkualgoritmit toteuttaa. Tällä hetkellä tämän 
 * rajapinnan toteutusluokkia ovat HuffmanPurku ja LZWPurku. Rajapinnan
 * käyttö mahdollistaa uusien algoritmien lisäämisen helposti.
 *  
 * @author mikkop
 * @see com.mtspelto.pakkaus.HuffmanPurku
 * @see com.mtspelto.pakkaus.LZWPurku
 */
public interface PurkuRajapinta {
	
	/** Purkaa tiedoston.
	 * 
	 * @return true mikäli purku onnistui, muutoin false
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean puraTiedosto() throws FileNotFoundException, IOException, ClassNotFoundException;

}
