package com.mtspelto.huffman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/** Rajapinta, jonka kaikki pakkausalgoritmit toteuttaa. Vaikka tällä hetkellä ainoa
 * toteutusluokka on HuffmanPakkaus, rajapinnan käyttö mahdollistaa uusien algoritmien lisäämisen helposti.
 * 
 *  
 * @author mikkop
 * @see com.mtspelto.huffman.HuffmanPakkaus
 */
public interface PakkausRajapinta {
	
	public boolean pakkaaTiedosto() throws FileNotFoundException, IOException;
	public boolean puraTiedosto() throws FileNotFoundException, IOException, ClassNotFoundException;
		
}
