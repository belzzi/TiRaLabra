package com.mtspelto.pakkaus.tietorakenteet;

/** Luokka joka toteuttaa Huffman-puun lehden. Huffman-puussa vain lehdiss‰ s‰ilytet‰‰n dataa,
 * joten t‰m‰ luokka lis‰‰ HuffmanPuuSisaSolmuun kent‰n s‰ilytett‰v‰n merkin arvolle.
 * 
 * @author mikkop
 * @see HuffmanPuuSisaSolmu
 */

public class HuffmanPuuLehti extends HuffmanPuuSisaSolmu {

	/** Luo HuffmanPuuLehden annetulla merkill‰ ja esiintyvyydell‰.
	 * 
	 * @param merkki Merkki
	 * @param frekvenssi
	 */
	public HuffmanPuuLehti(char merkki, int frekvenssi) {
		this.merkki = merkki;
		this.frekvenssi = frekvenssi;
	}

	private char merkki;
	
	/** Palauttaa t‰m‰n Huffman-lehden sis‰lt‰m‰n merkin.
	 * 
	 * @return T‰m‰n huffman-lehden sis‰lt‰m‰ merkki.
	 */
	public char annaMerkki() {
		return merkki;
	}

	/** Asettaa t‰m‰n Huffman-lehden sis‰lt‰m‰n merkin.
	 * 
	 * @param merkki
	 */
	public void asetaMerkki(char merkki) {
		this.merkki = merkki;
	}
	
}
