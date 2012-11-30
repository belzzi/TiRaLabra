package com.mtspelto.pakkaus;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Iterator;

import com.mtspelto.pakkaus.tietorakenteet.HajautusTaulukko;
import com.mtspelto.pakkaus.tietorakenteet.HuffmanPuuLehti;
import com.mtspelto.pakkaus.tietorakenteet.HuffmanPuuSisaSolmu;
import com.mtspelto.pakkaus.tietorakenteet.MinimiKeko;


/**
 * Huffman-algoritmin totetuttava pakkaus- ja purkuohjelma.
 * 
 * @see com.mtspelto.pakkaus.Pakkaaja
 * @author mikkop
 *
 */
public class HuffmanPurku implements PurkuRajapinta {
	
	
	/**
	 * Lippu jolla kytket‰‰n virheenetsint‰tila p‰‰lle / pois.
	 */
	public static final boolean DEBUG = false;
			
	/**
	 *  Purettavasta tiedostosta kerralla luettavan bin‰‰rilohkon vakiokoko
	 *  Lohkon koon pit‰‰ aina olla v‰hint‰‰n pisimm‰n mahdollisen Huffman-koodin
	 *  pituus, eli ainakin 16 tavua.
	 */
	public static final int VAKIO_PURKUBLOKKIKOKO = 128;
	
	/** Purettavasta tiedostosta kerralla luettavan bin‰‰rilohkon vakiokoko
	 * 
	 */
	private int purkuBlokkiKoko;
	
	/** L‰hdetiedosto
	 * 
	 */
	private File lahde;
	
	/** Kohdetiedosto
	 * 
	 */
	private File kohde;
	
	/** Hajautustaulukko johon tallennetaan kaikki Huffman-koodit merkill‰ avainnettuna 
	 *  T‰t‰ k‰ytet‰‰n kun tiedet‰‰n merkki ja halutaan tiet‰‰ Huffman-koodi.
	 */
	HajautusTaulukko merkistaKoodi;
	
	/** Hajautustaulukko johon tallennetaan kaikki merkit Huffman-koodilla avainnettuna.
	 * T‰t‰ k‰ytet‰‰n kun tiedet‰‰n merkin Huffman-koodi ja halutaan tiet‰‰ mit‰ merkki‰
	 * se vastaa.
	 */
	HajautusTaulukko koodistaMerkki;
	
	/**
	 * Rakentaa HuffmanPakkaus-luokan.
	 * 
	 * @param lahde L‰hdetiedoston nimi
	 * @param kohde Kohdetiedoston nimi
	 */
	public HuffmanPurku(File lahde, File kohde) {
		this.purkuBlokkiKoko = VAKIO_PURKUBLOKKIKOKO;
		merkistaKoodi = new HajautusTaulukko();
		koodistaMerkki = new HajautusTaulukko();
		this.lahde = lahde;
		this.kohde = kohde;
	}
	
	
	/**
	 * Rakentaa HuffmanPurku-luokan.
	 * 
	 * @param lahde L‰hdetiedoston nimi
	 * @param kohde Kohdetiedoston nimi
	 * @param purkuBlokkiKoko Purettaessa k‰ytett‰v‰ blokkikoko
	 */
	public HuffmanPurku(File lahde, File kohde, int purkuBlokkiKoko) {
		this.purkuBlokkiKoko = purkuBlokkiKoko;
		merkistaKoodi = new HajautusTaulukko();
		koodistaMerkki = new HajautusTaulukko();
		this.lahde = lahde;
		this.kohde = kohde;
	}
	
	/**
	 * Metodi joka tulostaa molemmat kooditaulukot System.out:iin.
	 * 
	 */
	public void tulostaTaulukot() {
		Iterator arvot = merkistaKoodi.arvot();
		Iterator avaimet = merkistaKoodi.avaimet();
		System.out.println("##DEBUG: Yhteens‰ " + merkistaKoodi.annaKoko() + " koodia tallennettu.\n\nMerkist‰ koodi- taulukko:");
		System.out.println("MERKKI     KOODI\n----------------");
		while (arvot.hasNext()) {
			String arvo = (String)arvot.next();
			Character avain = (Character)avaimet.next();
			System.out.println(avain + "          " + arvo);
		}
		arvot = koodistaMerkki.arvot();
		avaimet = koodistaMerkki.avaimet();
		System.out.println("\n\n##DEBUG: Koodista merkki- taulukko:");
		System.out.println("KOODI      MERKKI\n----------------");
		while (arvot.hasNext()) {
			Character arvo = (Character)arvot.next();
			String avain = (String)avaimet.next();
			System.out.println(avain + "          " + arvo);
		}
	}
	
	/**
	 * Pakatun tiedoston purkamisen p‰‰metodi. T‰ss‰ vaiheessa tyhj‰ toteutus rajapinnan toteuttamiseksi.
	 * @throws ClassNotFoundException 
	 * 
	 */
	public boolean puraTiedosto() throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(lahde);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ObjectInputStream ois = new ObjectInputStream(bis);
		byte[] luetutKoodiTavut = new byte[purkuBlokkiKoko];
				
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(kohde));
		
		koodistaMerkki = (HajautusTaulukko)ois.readObject();
		
		if (DEBUG) {
			Iterator it = koodistaMerkki.avaimet();
			Iterator it2 = koodistaMerkki.arvot();
			while (it.hasNext()) {
				char c = (char)it2.next();
				System.out.println(it.next().toString() + ": " + c);
			}
		}
		
		StringBuilder lohkonKoodiMerkit = new StringBuilder();
		int tavujaLuettu = 0;
		
		StringBuilder lohkonSelkoTeksti;
		StringBuilder nykyisenMerkinKoodi = new StringBuilder();

		// Luetaan pakattua dataa tiedostosta lohkon koon verran kerrallaan
		while ((tavujaLuettu = bis.read(luetutKoodiTavut))>0) {
			lohkonSelkoTeksti = new StringBuilder();
			lohkonKoodiMerkit = nykyisenMerkinKoodi;
			nykyisenMerkinKoodi = new StringBuilder();
			//K‰yd‰‰n lohkon jokainen tavu l‰pi
			for (int i = 0; i < tavujaLuettu; i++) {
				// k‰yd‰‰n jokaisen tavun jokainen bitti l‰pi, ja lis‰t‰‰n puskuriin
				for (int j = 7; j>=0; j--) {
					if ((luetutKoodiTavut[i] & (1 << j)) != 0)
						lohkonKoodiMerkit.append("1");
					else
						lohkonKoodiMerkit.append("0");
				}
			}
			char[] lohkonKoodiMerkitTaulukko = lohkonKoodiMerkit.toString().toCharArray();
	
			for (int k = 0; k<lohkonKoodiMerkitTaulukko.length; k++) {
				nykyisenMerkinKoodi.append(lohkonKoodiMerkitTaulukko[k]);
				Object o = koodistaMerkki.annaArvo(nykyisenMerkinKoodi.toString());
				if (o != null) {
					lohkonSelkoTeksti.append(o.toString());
					nykyisenMerkinKoodi = new StringBuilder();
				} 
			}
			bos.write(lohkonSelkoTeksti.toString().getBytes());
		}
		bos.flush();
		bis.close();
		fis.close();
		bos.close();
		return false;
	}

}	