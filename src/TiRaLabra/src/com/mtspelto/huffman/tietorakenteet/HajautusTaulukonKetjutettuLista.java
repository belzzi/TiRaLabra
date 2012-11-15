package com.mtspelto.huffman.tietorakenteet;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Ketjutetun listan elementti
 * 
 * @author mikkop
 *
 */
class Elementti implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7473990639480453826L;
	private Object avain;
	private Object arvo;
	
	private Elementti seuraava;
	
	/**
	 * Luo uuden elementin annetulla avaimella ja arvolla.
	 * @param avain
	 * @param arvo
	 */
	public Elementti(Object avain, Object arvo) {
		this.avain = avain;
		this.arvo = arvo;
	}
	
	/**
	 * Palauttaa elementin arvon.
	 * 
	 * @return Object Arvo
	 */
	public Object annaArvo() {
		return arvo;
	}
	
	/** Palauttaa elementin avaimen.
	 * 
	 * @return Object avain
	 */
	public Object annaAvain() {
		return avain;
	}
	
	/** Asettaa elementin arvon.
	 * 
	 * @param arvo
	 */
	public void asetaArvo(Object arvo) {
		this.arvo = arvo;
	}
	
	/** Asettaa elementin avaimen.
	 * 
	 * @param avain
	 */
	public void asetaAvain(Object avain) {
		this.avain= avain;
	}
	
	/**
	 * Palauttaa ketjutetun listan seuraavan elementin.
	 * @return Elementti seuraava elementti
	 */
	public Elementti annaSeuraava() {
		return seuraava;
	}
	
	/** Asettaa ketjutetun listan seuraavan elementin
	 * 
	 * @param e seuraava elementti
	 */
	public void asetaSeuraava(Elementti e) {
		this.seuraava = e;
	}
	
}

/**
 *  HajautusTaulukko-luokan käyttämä tietorakenne tiedon tallentamiseen. Tämä tietorakenne on 
 *  perinteinen yksinkertaisesti linkitetty lista.
 *  
 * @author mikkop
 *
 */
public class HajautusTaulukonKetjutettuLista implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -564568914156412874L;
	
	/**
	 * Viite listan ensimmäiseen elementtiin. 
	 */
	private Elementti alku;
	//private Object edellinen;
	
	/** Lisää uuden elementin annetulla avaimella ja arvolla.
	 * 
	 * @param avain
	 * @param arvo
	 * @return true mikäli lisäys onnistui, muutoin false.
	 */
	public boolean lisaaElementti(Object avain, Object arvo) {
		if (alku == null) {
			alku = new Elementti(avain,arvo);
			return true;
		}
		boolean paikkaLoytynyt = false;
		Elementti valiaikainen = alku;
		while (!paikkaLoytynyt) {
			if (valiaikainen.annaSeuraava() == null) {
				valiaikainen.asetaSeuraava(new Elementti(avain,arvo));
				paikkaLoytynyt = true;
			} else {
				valiaikainen = valiaikainen.annaSeuraava();
			}
		}
		return true;
	}
	
	/** Lisää uuden elementin annetulla avaimella ja arvolla.
	 * 
	 * @param avain
	 * @param arvo
	 * @return true mikäli lisäys onnistui, muutoin false.
	 */
	public boolean korvaaTaiLisaa(Object avain, Object arvo) {
		Elementti valiaikainen = alku;
		boolean paikkaLoytynyt = false;
		
		while (!paikkaLoytynyt) {
			if (valiaikainen.annaAvain().equals(avain)) {
				valiaikainen.asetaArvo(arvo);
				paikkaLoytynyt = true;
				return true;
			}
			if (valiaikainen.annaSeuraava() == null) {
				valiaikainen.asetaSeuraava(new Elementti(avain,arvo));
				paikkaLoytynyt = true;
			} else {
				valiaikainen = valiaikainen.annaSeuraava();
			}
		}
		return true;
	}
	
	/** Poistaa annettua avainta vastaavan elementin linkitetystä listasta.
	 * 
	 * @param o
	 * @return true mikäli elementin poisto onnistui, muutoin false.
	 */
	public boolean poistaElementti(Object o) {
		Elementti edellinen = null;
		Elementti valiaikainen = alku;
		boolean elementtiLoytynyt = false;
		while (valiaikainen != null) {
			if (valiaikainen.annaAvain().equals(o)) {
				if (edellinen == null) {
					alku = valiaikainen.annaSeuraava();
					valiaikainen = null;
					return true;
				} else { 
					edellinen.asetaSeuraava(valiaikainen.annaSeuraava());
					return true;
				}
			} else {
				edellinen = valiaikainen;
				valiaikainen = valiaikainen.annaSeuraava();
			}
		}
		return false;
	}
		
	/** Palauttaa annettua avainta vastaavan elementin.
	 * 
	 * @param o Haetun elementin avain
	 * @return Mikäli elementti löytyy, elementin, muussa tapauksessa null
	 */
	public Object annaElementtiAvaimella(Object o) {
		Elementti valiaikainen = alku;
		boolean elementtiLoytynyt = false;
		if (valiaikainen == null)
			return null;
		while (!elementtiLoytynyt) {
			if (valiaikainen.annaAvain().equals(o))
				return valiaikainen.annaArvo();
			if (valiaikainen.annaSeuraava() != null) {
				if (valiaikainen.annaSeuraava().annaAvain().equals(o))
					return valiaikainen.annaSeuraava().annaArvo();
				else
					valiaikainen = valiaikainen.annaSeuraava();
			} else {
				return null;
			}
		}
		return null;
	}
	
	/** Palauttaa ensimmäisen elementin
	 *  
	 * @return ensimmäinen elementti
	 */
	public Elementti annaAlku() {
		return alku;
	}
	
	/** Asettaa ensimmäisen elementin.
	 * 
	 * @param alku ensimmäinen elementti
	 */
	public void asetaAlku(Elementti alku) {
		this.alku = alku;
	}
	
	
}
 