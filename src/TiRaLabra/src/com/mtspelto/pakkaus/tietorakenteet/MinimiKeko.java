/**
 * 
 */
package com.mtspelto.pakkaus.tietorakenteet;

/** Bin‰‰ripuuna toteutettu minimikeko. Tallentaa tiedot taulukkoon.
 * 
 * 
 * @author mikkop
 */
public class MinimiKeko {
	
	/** Keon tiedot tallennetaan t‰h‰n taulukkoon.
	 * 
	 * Koon t‰ytyy olla 2:n potenssi.
	 */
	private HuffmanPuuSisaSolmu taulukko[];
	
	/** Taulukon nykyinen koko
	 * 
	 */
	private int koko;

	/** Taulukon nykyinen maksimikoko
	 * 
	 */
	private int maksimiKoko;
	
	/** Keon maksimikoko jota k‰ytet‰‰n jos muuta arvoa ei ole annettu konstrukrille.
	 * 
	 */
	private static final int VAKIO_MAKSIMIKOKO = 1024;
	
	
	/** Rakentaa tyhj‰n minimikeon.
	 * 
	 */
	public MinimiKeko() {
		maksimiKoko = VAKIO_MAKSIMIKOKO;
		taulukko = new HuffmanPuuSisaSolmu[maksimiKoko];
		koko = 0;
	}
	
	/** Rakentaa minimikeon annetulla taulukolla.
	 * 
	 */
	public MinimiKeko(HuffmanPuuSisaSolmu[] taulukko) {
		this.taulukko = taulukko;
		koko = 0;
		maksimiKoko = VAKIO_MAKSIMIKOKO;
		for (int i = 0; i < taulukko.length; i++) {
			if (taulukko[i] != null)
				koko++;
		}
		heapifyAlaspain(0);
	}
	
	/** Lis‰‰ annetun elementin minimikekoon.
	 * 
	 * @param elementti Lis‰tt‰v‰ elementti
	 */
	public void lisaa(HuffmanPuuSisaSolmu elementti) {
		if (koko < maksimiKoko) {
			taulukko[koko] = elementti;
			koko++;
			heapifyYlospain(koko-1);
		} else {
			System.out.println("Taulukko t‰ynn‰! Elementtej‰ t‰ll‰ hetkell‰ " + koko);
		}
	}
	

	/** Palauttaa keon pienimm‰n elementin samalla poistaen sen.
	 * 
	 * @return Keon pienin elementti.
	 */
	public HuffmanPuuSisaSolmu annaPienin() {
		if (koko == 0) {
			return null;
		}
		HuffmanPuuSisaSolmu palautettava = taulukko[0];
		
		taulukko[0] = taulukko[koko-1];
		taulukko[--koko] = null;
        if(koko > 0)
        	heapifyAlaspain(0);
		return palautettava;
	}
	
	/** K‰y keon l‰pi alhaalta ylˆsp‰in keko-ominaisuuden palauttamiseksi
	 * elementin lis‰yksen j‰lkeen.
	 * 
	 * @param aloitusSolmu
	 */
	private void heapifyYlospain(int aloitusSolmu) {
        if( aloitusSolmu > 0 ) {
            int vanhempi = vanhempi(aloitusSolmu);
            if (taulukko[vanhempi] == null) return;
            if (taulukko[vanhempi].compareTo(taulukko[aloitusSolmu]) > 0) {
                vaihdaElementit(vanhempi, aloitusSolmu);
                heapifyYlospain(vanhempi);
            }
        }
    }
	
	/** K‰y keon l‰pi ylh‰‰lt‰ alasp‰in keko-ominaisuuden palauttamiseksi 
	 * elementin poiston j‰lkeen.
	 * .
	 * @param aloitusSolmu Solmu josta alasp‰in keko-ominaisuus validoidaan.
	 */
	private void heapifyAlaspain(int aloitusSolmu) {
		int vasen = vasen(aloitusSolmu);
		int oikea = oikea(aloitusSolmu);
 
		if (taulukko[aloitusSolmu] == null) return;
        if( oikea >= taulukko.length && vasen >= taulukko.length )
        	return;
        if (taulukko[oikea] == null && taulukko[vasen] == null) 
        	return;
       
        int oikeaFrekvenssi = Integer.MAX_VALUE;
        int vasenFrekvenssi = Integer.MAX_VALUE;
        if (taulukko[oikea] != null)
        	oikeaFrekvenssi = taulukko[oikea].annaFrekvenssi();
        if (taulukko[vasen] != null)
        	vasenFrekvenssi = taulukko[vasen].annaFrekvenssi();
        
        int pienempiLapsi = oikeaFrekvenssi > vasenFrekvenssi ? vasen : oikea;
        
        if (taulukko[aloitusSolmu].compareTo(taulukko[pienempiLapsi]) > 0) {
            vaihdaElementit(pienempiLapsi, aloitusSolmu);
            heapifyAlaspain(pienempiLapsi);
        }
	}
	
	/** Palauttaa taulukon nykyisen elementtien m‰‰r‰n.
	 * 
	 * @return hajautustaulukon nykyinen elementtien m‰‰r‰
	 */
	public int koko() {
		return koko; 
	}
	
	/** Palauttaa annetun solmun vanhemman indeksin.
	 * 
	 * @param solmu
	 * @return vanhempi
	 */
	public int vanhempi(int solmu) {
		if (solmu == 0)
			return -1;
		if (solmu == 1)
			return 0;
		else
			return ((++solmu)/2)-1;
	}
	
	/** Palauttaa annetun solmun vasemman lapsen.
	 * 
	 * @param solmu
	 * @return annetun solmun vasen lapsi
	 */
	public int vasen(int solmu) {
		if (solmu == 0)
			return 1;
		else
			return ((++solmu)*2)-1;
	}
	
	/** Palauttaa annetun solmun oikean lapsen.
	 * 
	 * @param solmu
	 * @return annetun solmun oikea lapsi
	 */
	public int oikea(int solmu) {
		if (solmu == 0)
			return 2;
		return ((++solmu)*2);
	}

	/** Apumetodi joka vaihtaa taulukon 2 solmua kesken‰‰n
	 * 
	 * @param e1
	 * @param e2
	 */
	private void vaihdaElementit(int e1, int e2) {
		HuffmanPuuSisaSolmu apu = taulukko[e1];
		taulukko[e1] = taulukko[e2];
		taulukko[e2] = apu;
	}
}


