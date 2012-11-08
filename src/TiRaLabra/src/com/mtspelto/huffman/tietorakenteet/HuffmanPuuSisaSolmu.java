/**
 * 
 */
package com.mtspelto.huffman.tietorakenteet;

/**
 * T�m� luokka toteuttaa Huffman-puun sis�solmun. T�st� my�s periytyy lehti-luokka (HuffmanPuuLehti).
 *
 * @author mikkop
 *
 */
public class HuffmanPuuSisaSolmu implements Comparable {
	private HuffmanPuuSisaSolmu vasenLapsi;
	private HuffmanPuuSisaSolmu oikeaLapsi;
	private HuffmanPuuSisaSolmu vanhempi;
	/**
	 * Solmun esiintyvyys (sis�solmussa lapsien esiintyvyyksien summa)
	 */
	protected int frekvenssi;
	/**
	 * Solmun nimi
	 */
	public String nimi;
	
	/**
	 * Vakiokonstruktori. Sis�solmun jota ei ole alustettu tunnistaa frekvenssist� -1.
	 */
	public HuffmanPuuSisaSolmu() {
		frekvenssi = -1;
	}
	/**
	 * Luo uuden HuffmanPuuSisaSolmun annetulla esiiintyvyydell�.
	*/
	public HuffmanPuuSisaSolmu(int frekvenssi) {
		this.frekvenssi = frekvenssi;
	}
	
	/**
	 * Vertailija. Kun kahta HuffmanPuuSisaSolmua vertaillaan < ja > operaattoreilla,
	 * Java kutsuu t�t� metodia suuruusj�rjestyksen selvitt�miseksi.
	 *
	 */
	public int compareTo(Object o) {
		if (((HuffmanPuuSisaSolmu)o).annaFrekvenssi() > this.annaFrekvenssi())
			return -1;
		else
			if (((HuffmanPuuSisaSolmu)o).annaFrekvenssi() == this.annaFrekvenssi())
				return 0;
		return 1;	
	}
	
	public HuffmanPuuSisaSolmu(int frekvenssi, HuffmanPuuSisaSolmu vanhempi, HuffmanPuuSisaSolmu vasenLapsi, HuffmanPuuSisaSolmu oikeaLapsi) {
		this.frekvenssi = frekvenssi;
		this.vanhempi = vanhempi;
		this.vasenLapsi = vasenLapsi;
		this.oikeaLapsi = oikeaLapsi;
	}
	
	/** Palauttaa frekvenssin (esiintyvyyden)
	 * 
	 * @return int Frekvenssi (esiintyvyys)
	 */
	public int annaFrekvenssi() {
		return frekvenssi;
	}
	
	/** Asettaa frekvenssin (esiintyvyyden)
	 * 
	 * @param frekvenssi
	 */
	public void asetaFrekvenssi(int frekvenssi) {
		this.frekvenssi = frekvenssi;
	}
	
	/**
	 * Palauttaa vasemman lapsen
	 * @return HuffmanPuuSisaSolmu vasen lapsi
	 */
	public HuffmanPuuSisaSolmu annaVasenLapsi() {
		return this.vasenLapsi;
	}
	
	/** Palauttaa oikean lapsen
	 * 
	 * @return HuffmanPuuSisaSolmu oikea lapsi
	 */
	public HuffmanPuuSisaSolmu annaOikeaLapsi() {
		return this.oikeaLapsi;
	}
	
	/**
	 * Palauttaa t�m�n elementin vanhemman.
	 * @return HuffmanPuuSisaSolmu elementin vanhempi
	 */
	public HuffmanPuuSisaSolmu annaVanhempi() {
		return this.vanhempi;
	}
		
	/** Asettaa vasemman lapsen
	 * 
	 * @param HuffmanPuuSisaSolmu vasen lapsi
	 */
	public void asetaVasen(HuffmanPuuSisaSolmu h) {
		this.vasenLapsi = h;
	}
	
	/** Asettaa oikean lapsen
	 * 
	 * @param HuffmanPuuSisaSolmu oikea lapsi
	 */
	public void asetaOikea(HuffmanPuuSisaSolmu h) {
		this.oikeaLapsi= h;
	}
	
	/** Asettaa vanhemman
	 * 
	 * @param HuffmanPuuSisaSolmu vanhempi
	 */
	public boolean asetaVanhempi(HuffmanPuuSisaSolmu h) {
		this.vanhempi = h;
		return true;
	}
	
}