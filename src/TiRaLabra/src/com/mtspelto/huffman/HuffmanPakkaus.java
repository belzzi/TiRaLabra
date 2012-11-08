package com.mtspelto.huffman;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.PriorityQueue;

import com.mtspelto.huffman.tietorakenteet.HuffmanPuuLehti;
import com.mtspelto.huffman.tietorakenteet.HuffmanPuuSisaSolmu;

/**
 * Pakkaus- ja purkuohjelman "p‰‰ohjelma".
 * 
 * @see main()
 * @author mikkop
 *
 */
public class HuffmanPakkaus {
	/**
	 * Luettavan tiedoston lohkon koko.
	 */
	public static final int BLOKKIKOKO = 64;
	
	private int blokkiKoko;
	private File lahde;
	private File kohde;
	
	/** Hajautustaulukko johon tallennetaan kaikki Huffman-koodit merkill‰ avainnettuna 
	 *  T‰t‰ k‰ytet‰‰n kun tiedet‰‰n merkki ja halutaan tiet‰‰ Huffman-koodi.
	 */
	private HashMap merkistaKoodi;
	/** Hajautustaulukko johon tallennetaan kaikki merkit Huffman-koodilla avainnettuna.
	 * T‰t‰ k‰ytet‰‰n kun tiedet‰‰n merkin Huffman-koodi ja halutaan tiet‰‰ mit‰ merkki‰
	 * se vastaa.
	 */
	private HashMap koodistaMerkki;
	
	public HuffmanPakkaus(File lahde, File kohde, int blokkiKoko) {
		this.blokkiKoko = blokkiKoko;
		merkistaKoodi = new HashMap();
		koodistaMerkki = new HashMap();
		this.lahde = lahde;
		this.kohde = kohde;
	}
	
	public void tulostaTaulukot() {
		Iterator arvot = merkistaKoodi.values().iterator();
		Iterator avaimet = merkistaKoodi.keySet().iterator();
		System.out.println("Yhteens‰ " + merkistaKoodi.size() + " koodia tallennettu.\n\nMerkist‰ koodi- taulukko:");
		System.out.println("MERKKI     KOODI\n----------------");
		while (arvot.hasNext()) {
			String arvo = (String)arvot.next();
			Character avain = (Character)avaimet.next();
			System.out.println(avain + "          " + arvo);
		}
		arvot = koodistaMerkki.values().iterator();
		avaimet = koodistaMerkki.keySet().iterator();
		System.out.println("\n\nKoodista merkki- taulukko:");
		System.out.println("KOODI      MERKKI\n----------------");
		while (arvot.hasNext()) {
			Character arvo = (Character)arvot.next();
			String avain = (String)avaimet.next();
			System.out.println(avain + "          " + arvo);
		}
	}
	
	/*
	 * Palauttaa l‰hdetiedoston tavujen lukum‰‰r‰n.
	 * Huom! Ei t‰ss‰ vaiheessa viel‰ tee muuta kuin skannaa tiedoston kertaalleen
	 * l‰pi ja etsii Huffman-koodit.
	 */
	public int pakkaaTiedosto() throws FileNotFoundException, IOException {
		HashMap esiintyvyydet = new HashMap();
		
		char[] lahdeTaulukko = new char[blokkiKoko];
		//for (int i = 0; i < lahdeTaulukko.length; i++) {
		BufferedReader r = new BufferedReader(new FileReader(lahde));
		int yhteensaTavujaLuettu = 0;
		int loopissaTavujaLuettu = 0;
		
		while ((loopissaTavujaLuettu = r.read(lahdeTaulukko))>0) {
			System.out.println("Luettu " + loopissaTavujaLuettu + " tavua...");
			yhteensaTavujaLuettu += loopissaTavujaLuettu;
			
			for (int i = 0; i < lahdeTaulukko.length; i++) {
				Character merkki = new Character(lahdeTaulukko[i]);
				if (esiintyvyydet.containsKey(merkki)) {
					esiintyvyydet.put(merkki, ((Integer)esiintyvyydet.get(merkki)).intValue()+1);
				} else {
					esiintyvyydet.put(merkki, new Integer(1));
				}
			}
		}
		r.close();
		System.out.println("Skannaus valmis, yhteens‰ luettu " + yhteensaTavujaLuettu + " tavua");
	
		Set<Character> avaimet = esiintyvyydet.keySet();
		Collection arvot = esiintyvyydet.values();

		//J‰rjestet‰‰n merkit esiintyvyyden perusteella PrioriteettiJonoa k‰ytt‰en
		//ToDo: Tee oma PrioriteettiJono
		PriorityQueue<HuffmanPuuSisaSolmu> jono = new PriorityQueue<HuffmanPuuSisaSolmu>();

		Iterator avaimetIterator = avaimet.iterator();
		Iterator arvotIterator = arvot.iterator();
		while (avaimetIterator.hasNext()) {
			char merkki = ((Character)avaimetIterator.next()).charValue();
			int esiintyvyys = ((Integer)arvotIterator.next()).intValue(); 
			HuffmanPuuLehti h = new HuffmanPuuLehti(merkki, esiintyvyys);
			h.nimi = merkki + "!";
			jono.offer(h);
		}
		
		HuffmanPuuSisaSolmu juuri;
		HuffmanPuuSisaSolmu valiaikainen;
		HuffmanPuuSisaSolmu pieninFrekvenssi = null;
		HuffmanPuuSisaSolmu toiseksiPienin = null;
		while (jono.size() > 1) {
			//Tehd‰‰n puu
			pieninFrekvenssi = jono.poll();
			toiseksiPienin = jono.poll();
			//System.out.println(pieninFrekvenssi.annaArvo() + ": " + pieninFrekvenssi.annaFrekvenssi());
			//System.out.println(toiseksiPienin.annaArvo() + ": " + toiseksiPienin.annaFrekvenssi());
			HuffmanPuuSisaSolmu s = new HuffmanPuuSisaSolmu(pieninFrekvenssi.annaFrekvenssi() + toiseksiPienin.annaFrekvenssi());
			pieninFrekvenssi.asetaVanhempi(s);
			s.asetaVasen(pieninFrekvenssi);
			s.asetaOikea(toiseksiPienin);
			jono.offer(s);
			//valiaikainen = s;
			//if (valiaikainen != null && valiaikainen instanceof HuffmanPuuSolmu)
			//	s.aseta
		}
		juuri = (HuffmanPuuSisaSolmu)jono.remove();
		pieninFrekvenssi.asetaVanhempi(juuri);
		toiseksiPienin.asetaVanhempi(juuri);
		juuri.asetaVasen(pieninFrekvenssi);
		juuri.asetaOikea(toiseksiPienin);
	
		luoKoodit(juuri, "");
		tulostaTaulukot();
		return yhteensaTavujaLuettu;
	}

	/** Metodi joka luo huffman-koodit annetusta puun juurisolmusta alkaen. 
	 * Kutsuu itse‰‰n rekursiivisesti kunnes koko puun kaikki merkit on l‰pik‰yty.
	 * 
	 * @param s
	 * @param koodi
	 */
	public void luoKoodit(HuffmanPuuSisaSolmu s, String koodi) {
		if (s instanceof HuffmanPuuLehti) {
				char merkki = ((HuffmanPuuLehti)s).annaArvo();
				merkistaKoodi.put(merkki, koodi);
				koodistaMerkki.put(koodi, merkki);
				//System.out.println(h.annaArvo() + ": " + h.annaFrekvenssi() + ":: " + koodi);
		} else if (s instanceof HuffmanPuuSisaSolmu) {
			if (s.annaVasenLapsi() != null)
				luoKoodit(s.annaVasenLapsi(),koodi + "0");
			if (s.annaOikeaLapsi() != null)
				luoKoodit(s.annaOikeaLapsi(),koodi + "1");
		}
	}

	/**
	 * "P‰‰ohjelma" jolla pakkaus- tai purkuoperaatio aloitetaan.
	 *  
	 * @param args Argumentteina tulee antaa operaatio (-pakkaa tai -pura), l‰hde- ja kohdetiedostojen nimet
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
//		HuffmanPuuSisaSolmu juuri = new HuffmanPuuSisaSolmu();
		if (args.length != 3) {
			System.out.println("V‰‰r‰ m‰‰r‰ argumentteja. Anna argumenttina operaatio (joko -pakkaa tai -pura) pakattava tiedosto ja kohdetiedoston nimi.");
			System.exit(1);
		}
		String operaatio = args[0];
		File lahdeTiedostoNimi = new File(args[1]);
		if (!lahdeTiedostoNimi.exists()) {
			System.out.println("L‰hdetiedostoa " + args[1] + " ei ole olemassa.");
			System.exit(2);
		}
			
		File kohdeTiedostoNimi = new File(args[2]);
		if (kohdeTiedostoNimi.exists()) {
			System.out.println("Kohdetiedosto on jo olemassa.");
			System.exit(3);
		}
		
		HuffmanPakkaus hc = new HuffmanPakkaus(lahdeTiedostoNimi,kohdeTiedostoNimi,BLOKKIKOKO);
		try {
			hc.pakkaaTiedosto();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	
		
}	