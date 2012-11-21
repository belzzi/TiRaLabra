package com.mtspelto.pakkaus.tietorakenteet;
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
	 * Vertailija. Palauttaa 1 jos t�m� olio on suurempi kuin parametrina annettu, -1 jos se on pienempi, 0 jos oliot ovat yht� suuria.
	 *
	 */
	public int compareTo(Object o) {
		if ( this.annaFrekvenssi() > ((HuffmanPuuSisaSolmu)o).annaFrekvenssi())
			return 1;
		else
			if (((HuffmanPuuSisaSolmu)o).annaFrekvenssi() == this.annaFrekvenssi())
				return 0;
		return -1;	
	}
	
	/**
	 * Luo annetuilla parametreill� HuffmanPuuSisaSolmu-olion.
	 * 
	 * @param frekvenssi
	 * @param vanhempi
	 * @param vasenLapsi
	 * @param oikeaLapsi
	 */
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
	 * @param h vasen lapsi
	 */
	public void asetaVasen(HuffmanPuuSisaSolmu h) {
		this.vasenLapsi = h;
	}
	
	/** Asettaa oikean lapsen
	 * 
	 * @param h oikea lapsi
	 */
	public void asetaOikea(HuffmanPuuSisaSolmu h) {
		this.oikeaLapsi= h;
	}
	
	/** Asettaa vanhemman
	 * 
	 * @param h vanhempi
	 */
	public boolean asetaVanhempi(HuffmanPuuSisaSolmu h) {
		this.vanhempi = h;
		return true;
	}
	
}