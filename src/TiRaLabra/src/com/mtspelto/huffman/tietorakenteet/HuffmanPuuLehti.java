/**
 * 
 */
package com.mtspelto.huffman.tietorakenteet;

/**
 * @author mikkop
 *
 */

public class HuffmanPuuLehti extends HuffmanPuuSisaSolmu {

	public HuffmanPuuLehti(char arvo, int frekvenssi) {
		this.arvo = arvo;
		this.frekvenssi = frekvenssi;
	}

	private char arvo;
	
	public char annaArvo() {
		return arvo;
	}

	public void asetaArvo(char arvo) {
		this.arvo = arvo;
	}

}
