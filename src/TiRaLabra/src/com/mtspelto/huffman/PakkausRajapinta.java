package com.mtspelto.huffman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/** Rajapinta, jonka kaikki pakkausalgoritmit toteuttaa. Vaikka t�ll� hetkell� ainoa
 * toteutusluokka on HuffmanPakkaus, rajapinnan k�ytt� mahdollistaa uusien algoritmien lis��misen helposti.
 * 
 *  
 * @author mikkop
 * @see com.mtspelto.huffman.HuffmanPakkaus
 */
public interface PakkausRajapinta {
	
	public boolean pakkaaTiedosto() throws FileNotFoundException, IOException;
	public boolean puraTiedosto() throws FileNotFoundException, IOException, ClassNotFoundException;
		
}
