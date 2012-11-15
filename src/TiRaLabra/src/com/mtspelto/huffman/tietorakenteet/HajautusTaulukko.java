package com.mtspelto.huffman.tietorakenteet;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.lang.model.element.ElementVisitor;

/**
 * T‰m‰ luokka toteuttaa hajautustaulukon jonka avaimet ovat joko kokonaislukuja tai merkkijonoja.
 * Luokka k‰ytt‰‰ tiedon tallentamiseen HajautusTaulukonKetjutettuLista-luokkaa.
 * 
 * @author mikkop
 * @see HajautusTaulukonKetjutettuLista
 */
public class HajautusTaulukko implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7428885402030893011L;
	
	/** 
	 * Mik‰li muuta taulukon kokoa ei anneta, k‰ytet‰‰n t‰t‰ arvoa.
	 */
	private static final int VAKIOKOKO = 32;
	
	/**
	 * Tietorakenne tiedon s‰ilytt‰miseeen.
	 */
	private HajautusTaulukonKetjutettuLista[] listat;
	
	/** Listan k‰ytt‰m‰n taulukon koko.
	 *  
	 */
	private int listanKoko;
	
	/** Koko hajautustaulukon t‰m‰nhetkinen elementtien m‰‰r‰.
	 * 
	 */
	private int nykyinenKoko;
	
	/**
	 * Rakentaa uuden Hajautustaulukon.
	 */
	public HajautusTaulukko() {
		listanKoko = VAKIOKOKO;
		listat = new HajautusTaulukonKetjutettuLista[listanKoko];
		for (int i = 0; i < listanKoko; i++) {
			listat[i] = new HajautusTaulukonKetjutettuLista();
		}
	}
	
	/**
	 * Apumetodi joka valitsee oikean listan avaimelle
	 * 
	 * @param avain
	 * @return HajautusTaulukonKetjutettuLista Ketjutettu lista johon avain tallennetaan
	 */
	public HajautusTaulukonKetjutettuLista valitseLista(Object avain) {
		HajautusTaulukonKetjutettuLista palautettava = null;

		if (avain instanceof Integer)
			palautettava = listat[intHashCode(((Integer)avain).intValue())];
		if (avain instanceof Character)
			palautettava =  listat[intHashCode(((Character)avain).charValue())];
		if (avain instanceof Byte)
			palautettava =  listat[((Byte)avain).byteValue()];
		if (avain instanceof String) {
			//System.out.println("Stringin " + (String)avain + "  hashCode=" + ((String)avain).hashCode() + "; stringHashCode=" + stringHashCode((String)avain));
			palautettava =  listat[stringHashCode((String)avain)];
		}
		return palautettava;
	}
	
	/**
	 * Metodi joka lis‰‰ elementin hajautustaulukkoon.
	 * 
	 * @param avain
	 * @param arvo
	 * @return True mik‰li elementin lis‰ys onnistui, muutoin false,
	 */
	public boolean lisaaElementti(Object avain, Object arvo) {
		if (valitseLista(avain).lisaaElementti(avain, arvo)) {
			nykyinenKoko++;
			return true;
		} 
		return false;
	}
	
	/**
	 * Metodi joka korvaa olemassaolevan elementin hajautustaulukossa,
	 * tai jos elementti‰ ei lˆydy, lis‰‰ uuden.
	 * 
	 * @param avain
	 * @param arvo
	 * @return True mik‰li elementin lis‰ys onnistui, muutoin false,
	 */
	public boolean korvaaElementti(Object avain, Object arvo) {
		//Korvaa..
		if (annaArvo(avain) != null) {
			valitseLista(avain).korvaaTaiLisaa(avain, arvo);
			return true;
		} else { 
			// tai lis‰‰:
			return lisaaElementti(avain, arvo);
		}
	}

	/**
	 * Merkkijonoavaimille k‰ytett‰v‰ hajautusalgoritmi.
	 * Laskee hajautuspaikan annetulle merkkijonoavaimelle.
	 * @param s Merkkijono
	 * @return int Annetun merkkijonon sijainti hajautustaulukossa.
	 */
	public int stringHashCode(String s) {
		int palautettava = Math.abs(s.hashCode()) % listanKoko;
		return palautettava;
	}
	
	/** Kokonaislukuavaimimlle k‰ytett‰v‰ hajautusalgoritmi.
	 * Laskee hajautuspaikan annetulle kokonaislukuavaimelle.
	 * 
	 * @param x
	 * @return annetun kokonaislukuavaimen sijainnin taulukossa
	 */
	public int intHashCode(int x) {
		return Math.abs(x) % listanKoko;
	}
	
	/** Antaa hajautustaulukon nykyisen koon.
	 * 
	 * @return Nykyisten elementtien m‰‰r‰.
	 */
	public int annaKoko() {
		return nykyinenKoko;
	}
	
	/** Palauttaa annettua avainta vastaavan arvon.
	 * 
	 * @param avain
	 * @return annettua avainta vastaavan arvon, tai null, mik‰li avainta ei lˆydy taulukosta.
	 */
	public Object annaArvo(Object avain) {
		if (avain == null)
			return null;
		HajautusTaulukonKetjutettuLista lista = valitseLista(avain);
		if (lista != null)
			return valitseLista(avain).annaElementtiAvaimella(avain);
		return null;
	}
	
	/** Elementin poisto taulukosta.
	 * 
	 * @param avain
	 * @return true jos elementti oli olemassa ja poisto onnistui, false jos elementti‰ ei lˆytynyt tai poisto ep‰onnistui.
	 */
	public boolean poistaElementti(Object avain) {
		boolean poistoOnnistui = valitseLista(avain).poistaElementti(avain);
		if (poistoOnnistui)
			nykyinenKoko--;
		return poistoOnnistui;
	}
	
	/** Palauttaa Iterator-luokan instanssin kaikista taulukon arvoista.
	 * 
	 * @return Iterator-olio joka sis‰lt‰‰ hajautustaulukon kaikki arvot.
	 */
	public Iterator arvot() {
		return this.new ArvotIterator();
	}
	
	/** Palauttaa Iterator-luokan instanssin kaikista taulukon avaimista.
	 * 
	 * @return Iterator-olio joka sis‰lt‰‰ hajautustaulukon kaikki avaimet.
	 */
	public Iterator avaimet() {
		return this.new AvaimetIterator();
	}

	/**
	 * Toteuttaa Iterator-rajapinnan joka palauttaa hajautustaulukon sis‰lt‰m‰t arvot.
	 * 
	 * @author mikkop
	 *
	 */
	private class ArvotIterator implements Iterator {
        // start stepping through the array from the beginning
        private Elementti seuraava;
        private int taulukonPositio;
        
        public ArvotIterator() {
        	int hakuPositio = 0;
        	boolean ensimmainenLoytynyt = false;
        	Elementti e = null;
        	while (!ensimmainenLoytynyt && hakuPositio < listat.length) {
        		e = listat[hakuPositio].annaAlku();
        		if (e != null) {
        			ensimmainenLoytynyt = true;
        			taulukonPositio = hakuPositio;
        			seuraava = e;
        		} else {
        			hakuPositio++;
        		}
        	}
        	System.out.println("Konstruktoitu arvot");
        }
        
        /** Metodi jolla tarkistetaan onko iteratorissa viel‰ j‰ljell‰ yksi elementti.
         * 
         * @return true mik‰li Iteratorissa on viel‰ yksi elementti noudettavissa, muussa tapauksessa false.
         */
        public boolean hasNext() {
        	if (seuraava != null)
        		return true;
        	return false;
        }
        
        /** Metodi joka palauttaa seuraavan elementin.
         * 
         * @return Seuraava elementti
         * @throws NoSuchElementException Jos metodia kutsutaan kun hasNext() == false
         */
        public Object next() throws NoSuchElementException {
	    	if (seuraava == null)
	    		throw new NoSuchElementException();
	    	
	    	// Palautetaan seuraava elementti
	    	Elementti palautettava = seuraava;
	    	boolean seuraavaLoytynyt = false;
	    	
	    	// Sitten etsit‰‰n onko sit‰ seuraavaa elementti‰ olemassa
	    	if (seuraava.annaSeuraava() != null) {
	    		seuraava = seuraava.annaSeuraava();
	    	}else {
	    		seuraava = null;
	    		while (taulukonPositio < listat.length -1 && !seuraavaLoytynyt) {
	    			taulukonPositio++;
	    			if (listat[taulukonPositio].annaAlku() != null) {
	    				seuraava = listat[taulukonPositio].annaAlku();
	    				seuraavaLoytynyt = true;
	    				break;
	    			}
	    			if (taulukonPositio >= listat.length) {
	    				seuraavaLoytynyt = true;
	    				seuraava = null;
	    			}
	    		}
	    	}        	
	        return palautettava.annaArvo();
        }
        
        /** Metodi joka poistaa seuraavan elementin palauttamatta sit‰.
         * 
         * @throws NoSuchElementException Jos metodia kutsutaan kun hasNext() == false
         */
        public void remove() {
        	Object o = next();
        }
	}
	
	/**
	 * Toteuttaa Iterator-rajapinnan joka palauttaa hajautustaulukon sis‰lt‰m‰t avaimet.
	 * 
	 * @author mikkop
	 *
	 */
	private class AvaimetIterator implements Iterator  {
        // start stepping through the array from the beginning
		private Elementti seuraava;
        private int taulukonPositio;

		public AvaimetIterator() {
        	int hakuPositio = 0;
        	boolean ensimmainenLoytynyt = false;
        	Elementti e = null;
        	while (!ensimmainenLoytynyt && hakuPositio < listat.length) {
        		e = listat[hakuPositio].annaAlku();
        		if (e != null) {
        			ensimmainenLoytynyt = true;
        			taulukonPositio = hakuPositio;
        			seuraava = e;
        		} else {
        			hakuPositio++;
        		}
        	}
        	System.out.println("Konstruktoitu");
        }
        
        /** Metodi jolla tarkistetaan onko iteratorissa viel‰ j‰ljell‰ yksi elementti.
         * 
         * @return true mik‰li Iteratorissa on viel‰ yksi elementti noudettavissa, muussa tapauksessa false.
         */
        public boolean hasNext() {
        	if (seuraava != null)
        		return true;
        	return false;
        }
        
        /** Metodi joka palauttaa seuraavan elementin.
         * 
         * @return Seuraava elementti
         * @throws NoSuchElementException Jos metodia kutsutaan kun hasNext() == false
         */
        public Object next() throws NoSuchElementException {
        	if (this.seuraava == null)
        		throw new NoSuchElementException();
        	
        	// Palautetaan seuraava elementti
        	Elementti palautettava = seuraava;
        	boolean seuraavaLoytynyt = false;
        	
        	// Sitten etsit‰‰n onko sit‰ seuraavaa elementti‰ olemassa
        	if (seuraava.annaSeuraava() != null) {
        		seuraava = seuraava.annaSeuraava();
        	}else {
        		seuraava = null;
        		while (taulukonPositio < listat.length -1 && !seuraavaLoytynyt) {
        			taulukonPositio++;
        			if (listat[taulukonPositio].annaAlku() == null) {
            			continue;
        			} else {
        				seuraava = listat[taulukonPositio].annaAlku();
        				seuraavaLoytynyt = true;
        			}
        			if (taulukonPositio >= listat.length) {
        				seuraavaLoytynyt = true;
        			}
        		}
        	}        	
            return palautettava.annaAvain();
        }

        /** Metodi joka poistaa seuraavan elementin palauttamatta sit‰.
         * 
         * @throws NoSuchElementException Jos metodia kutsutaan kun hasNext() == false
         */
        public void remove() throws NoSuchElementException {
        	Object o = next();
        }
    }
}
