package com.mtspelto.huffman;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.PriorityQueue;

import com.mtspelto.huffman.tietorakenteet.HuffmanPuuLehti;
import com.mtspelto.huffman.tietorakenteet.HuffmanPuuSisaSolmu;


/**
 * Huffman-algoritmin pakkaus- ja purkuohjelman "p‰‰ohjelma".
 * 
 * @see com.mtspelto.huffman.HuffmanPakkaus#main(String[])
 * @author mikkop
 *
 */
public class HuffmanPakkaus implements PakkausRajapinta {
	
	/**
	 * Lippu, jonka ollessa p‰‰ll‰, pakatessa kohdetiedostoon kirjoitetaan
	 * Huffman-koodit raakana (ei siis bittein‰).
	 * 
	 * Helpottaa vianetsint‰‰ koodausvaiheessa.
	 */
	public static final boolean DUMMY_PAKKAUS = false;
	
	/**
	 * Luettavan tiedoston lohkon koko.
	 */
	public static final int PAKKAUSBLOKKIKOKO = 256;
	
	/**
	 *  Purettavasta tiedostosta luettavan lohkon koko
	 */
	public static final int PURKUBLOKKIKOKO = 4096;
	
	public static final short[] BITTIMASKIT = new short[]{1,2,4,8,16,32,64,128};
	public static final short[] KAANTEISET_BITTIMASKIT = new short[]{128,64,32,16,8,5,2,1};
	
	/**
	 * Lippu jolla kytket‰‰n virheenetsint‰tila p‰‰lle / pois.
	 */
	public static final boolean DEBUG = false;
	
	private int pakkausBlokkiKoko;
	private int purkuBlokkiKoko;
	private File lahde;
	private File kohde;
	
	/** Hajautustaulukko johon tallennetaan kaikki Huffman-koodit merkill‰ avainnettuna 
	 *  T‰t‰ k‰ytet‰‰n kun tiedet‰‰n merkki ja halutaan tiet‰‰ Huffman-koodi.
	 */
	HashMap merkistaKoodi;
	
	/** Hajautustaulukko johon tallennetaan kaikki merkit Huffman-koodilla avainnettuna.
	 * T‰t‰ k‰ytet‰‰n kun tiedet‰‰n merkin Huffman-koodi ja halutaan tiet‰‰ mit‰ merkki‰
	 * se vastaa.
	 */
	HashMap koodistaMerkki;
	
	/**
	 * Rakentaa HuffmanPakkaus-luokan.
	 * 
	 * @param lahde
	 * @param kohde
	 * @param pakkausBlokkiKoko Pakattaessa k‰ytett‰v‰ blokkikoko
	 * @param purkuBlokkiKoko Purettaessa k‰ytett‰v‰ blokkikoko
	 */
	public HuffmanPakkaus(File lahde, File kohde, int pakkausBlokkiKoko, int purkuBlokkiKoko) {
		this.purkuBlokkiKoko = purkuBlokkiKoko;
		this.pakkausBlokkiKoko = pakkausBlokkiKoko;
		merkistaKoodi = new HashMap();
		koodistaMerkki = new HashMap();
		this.lahde = lahde;
		this.kohde = kohde;
	}
	
	/**
	 * Metodi joka tulostaa molemmat kooditaulukot System.out:iin.
	 * 
	 */
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
	
	/**
	 * Pakatun tiedoston purkamisen p‰‰metodi. T‰ss‰ vaiheessa tyhj‰ toteutus rajapinnan toteuttamiseksi.
	 * @throws ClassNotFoundException 
	 * 
	 */
	public boolean puraTiedosto() throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(lahde);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ObjectInputStream ois = new ObjectInputStream(bis);
		byte[] luetutKoodiTavut = new byte[PURKUBLOKKIKOKO];
		
		// Sanaston luku erillisest‰ .ser tiedostosta 
		//ObjectInputStream ois = new ObjectInputStream(new FileInputStream(lahde.getAbsoluteFile() + ".ser"));  
		
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(kohde));
		
		koodistaMerkki = (HashMap)ois.readObject();
		
		if (DEBUG) {
			Iterator it = koodistaMerkki.keySet().iterator();
			Iterator it2 = koodistaMerkki.values().iterator();
			while (it.hasNext()) {
				char c = (char)it2.next();
				System.out.println(it.next().toString() + ": " + c);
			}
		}
		
		StringBuilder lohkonKoodiMerkit = new StringBuilder();
		StringBuffer loputMerkit;
		int tavujaLuettu = 0;
		StringBuilder lohkonSelkoTeksti = new StringBuilder();
		StringBuilder nykyisenMerkinKoodi = new StringBuilder();

		while ((tavujaLuettu = bis.read(luetutKoodiTavut))>0) {
			lohkonKoodiMerkit = new StringBuilder();
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
			char[] lohkonKoodiMerkitTaulukko = (nykyisenMerkinKoodi.toString() + lohkonKoodiMerkit.toString()).toCharArray();
	
			for (int k = 0; k<lohkonKoodiMerkitTaulukko.length; k++) {
				nykyisenMerkinKoodi.append(lohkonKoodiMerkitTaulukko[k]);
				Object o = koodistaMerkki.get(nykyisenMerkinKoodi.toString());
				if (o != null) {
					lohkonSelkoTeksti.append(o.toString());
					nykyisenMerkinKoodi = new StringBuilder();
				} 
			}
		}
		bos.write(lohkonSelkoTeksti.toString().getBytes());
		bos.flush();
		bis.close();
		fis.close();
		bos.close();
		return false;
	}
	
	/**
	 * 
	 * Tiedoston pakkauksen p‰‰metodi.
	 * 
	 * Huom! Ei t‰ss‰ vaiheessa viel‰ tee muuta kuin skannaa tiedoston kertaalleen
	 * l‰pi ja etsii Huffman-koodit.
	 */
	public boolean pakkaaTiedosto() throws FileNotFoundException, IOException {
		BufferedReader r = new BufferedReader(new FileReader(lahde));
		//BufferedWriter w = new BufferedWriter(new FileWriter(kohde));
		FileOutputStream fos = new FileOutputStream(kohde);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		
		//FileOutputStream fos2 = new FileOutputStream(kohde.getAbsoluteFile() + ".ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		try {
			HashMap esiintyvyydet = annaEsiintyvyydet(r);
			r.close();
			
			Set<Character> avaimet = esiintyvyydet.keySet();
			Collection arvot = esiintyvyydet.values();
	
			//J‰rjestet‰‰n merkit esiintyvyyden perusteella PrioriteettiJonoa k‰ytt‰en ##TODO: Tee oma PrioriteettiJono
			PriorityQueue<HuffmanPuuSisaSolmu> jono = priorisoiAvaimet(avaimet.iterator(), arvot.iterator());
			HuffmanPuuSisaSolmu juuri = luoHuffmanPuu(jono);
			luoMerkkiKoodiTaulukot(juuri, "");
			tulostaTaulukot();
			
			//kirjoitaSanasto(oos);
			oos.writeObject(koodistaMerkki);
			
			//Alustetaan r uudestaan lukemaan l‰hdetiedosto alusta
			r = new BufferedReader(new FileReader(lahde));
								
			kirjoitaLahteestaKohde(r,bos);
			r.close();
			bos.flush();
			bos.close();
			fos.close();
			oos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 *  Kirjoittaa kohdetiedostoon 
	 * @param r BufferedReader Reader joka osoittaa l‰hdetiedostoon
	 * @param bos BufferedOutputStream OutputStream joka osoittaa kohdetiedostoon
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void kirjoitaLahteestaKohde(BufferedReader r, BufferedOutputStream bos) throws FileNotFoundException, IOException {
		char[] lahdeTaulukko = new char[purkuBlokkiKoko];
		int yhteensaTavujaLuettu = 0;
		int loopissaTavujaLuettu = 0;
		/** T‰m‰ taulukko tarvitaan s‰ilytt‰m‰‰n lohkon viimeisen tavun bitit joita ei kirjoitettu edellisell‰ kerralla. annaKoodattuMerkkiJono() asettaa t‰h‰n
		 * taulukkoon bittejen arvoiksi -1, 0 tai 1, jossa -1 tarkoittaa ett‰ bitti ei ole k‰ytˆss‰.
		 * 
		 */
		short[] ylijaamaBitit = new short[8];
		for (int i = 0; i<ylijaamaBitit.length;i++)
			ylijaamaBitit[i]=-1;

		/** T‰ss‰ on bugi. Lohkon vaihtuessa, edellisen lohkon viimeinen tavu on t‰ytetty nollilla :(
		 * 
		 */
		while ((loopissaTavujaLuettu = r.read(lahdeTaulukko))>0) {
			byte[] taulukko = annaKoodattuMerkkiJono(lahdeTaulukko, loopissaTavujaLuettu, ylijaamaBitit);
			bos.write(taulukko);
			//bos.write(annaKoodattuMerkkiJono(lahdeTaulukko, loopissaTavujaLuettu, ylijaamaBitit));
			System.out.println();
			//"Kirjoitettu " + loopissaTavujaLuettu + " tavua...");
		}
		System.out.println("Yhteens‰ " + yhteensaTavujaLuettu + " tavua kirjoitettu");
	}
	
	/** Apumetodi joka palauttaa annetun merkkitaulukon  
	 * 
	 * @param lahdeTaulukko
	 * @return byte[] l‰hdetaulukko Huffman-koodattuna (tai mik‰li Dummy-koodaus on kytketty p‰‰lle, Huffman-koodit merkkijonona)
	 */
	public byte[] annaKoodattuMerkkiJono(char[] lahdeTaulukko, int tavujaDataa, short[] ylijaamaBitit) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tavujaDataa; i++) {
			sb.append(merkistaKoodi.get(lahdeTaulukko[i]));
		}
		if (DUMMY_PAKKAUS) {
			return sb.toString().getBytes();
		} else {
			return pakkaaLohko(sb.toString().getBytes(), ylijaamaBitit);
		}
	}
	
	/**
	 * Metodi joka ottaa syˆtteekseen Huffman-koodimerkkijonon, ja palauttaa
	 * koodin pakattuna biteiksi.
	 * 
	 * @param huffmanMerkkiLohko Tavutaulukko joka sis‰lt‰‰ Huffman-merkkijonon
	 * @param ylijaamaBitit Taulukko, johon metodi sijoittaa lopuksi yli j‰‰neet bitit
	 * @return Huffman-koodattu lohko.
	 * 
	 */
	public byte[] pakkaaLohko(byte[] huffmanMerkkiLohko, short[] ylijaamaBitit) {
		byte tavu = 0;
		byte bittiLaskuri = 0;
				
		ByteBuffer bb = ByteBuffer.allocateDirect(purkuBlokkiKoko*2);
		
		byte[] pakattuTavu = new byte[1];
		int tavuja = 0;
		//Syotet‰‰n ensin edellisest‰ lohkosta j‰‰neet ylij‰‰m‰bitit ensimm‰isen tavun alkuun...
		for (int i = ylijaamaBitit.length-1; i>-1; i--) {
			if (ylijaamaBitit[i]>-1) {
				tavu = (byte)(tavu <<1);
				if (ylijaamaBitit[i] == 1)
					tavu = (byte)(tavu | 1);
				//Sitten merkit‰‰n ylijaamabitti k‰ytetyksi alustamalla se takaisin arvoon -1
				ylijaamaBitit[i] = -1;
				bittiLaskuri++;
			}
		}
		boolean taydellinenTavu = false;
		//Sitten pakataan itse annetun lohkon sis‰ltˆ
		for (int i = 0; i < huffmanMerkkiLohko.length; i++) {
			taydellinenTavu = false;
			tavu = (byte)(tavu << 1);
			if (huffmanMerkkiLohko[i] != '0') {
				tavu = (byte)(tavu | 1);
			}
			if (bittiLaskuri < 7) {
				bittiLaskuri++;
			} else {
				bb.put(tavu);
				tavuja++;
				bittiLaskuri = 0;
				tavu = 0;
				taydellinenTavu = true;
			}
		}
		//Jos lohko l‰pik‰ynti ei p‰‰ttynyt tasam‰‰r‰‰n (=8) bittej‰, otetaan talteen bitit v‰lilt‰ 0->bittiLaskuri:
		if (bittiLaskuri<7) {
			//..sitten otetaan talteen bitit v‰lilt‰ 0->bittiLaskuri:
			for (int i = bittiLaskuri -1; i>-1; i--)
				if ((tavu & BITTIMASKIT[i]) == BITTIMASKIT[i])
					ylijaamaBitit[i] = 1;
				else
					ylijaamaBitit[i] = 0;
		}
		byte[] pakattuLohko = new byte[tavuja];
		bb.rewind();
		bb = bb.get(pakattuLohko);
		return pakattuLohko;
	}
	
	/**
	 * Metodi joka skannaa l‰hdetiedoston ensimm‰isen kerran l‰pi, ja palauttaa merkkien esiintyvyydet HashMapissa.
	 * @param r BufferedReader - lukija l‰hdetiedostoon
	 * @return HashMap Hajautustaulukko joka sis‰lt‰‰ kaikki l‰hdetekstiss‰ esiintyv‰t merkit ja niiden lasketut esiintyvyydet
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public HashMap annaEsiintyvyydet(BufferedReader r) throws FileNotFoundException, IOException {
		HashMap esiintyvyydet = new HashMap();
		
		char[] lahdeTaulukko = new char[purkuBlokkiKoko];
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
		System.out.println("Skannaus valmis, yhteens‰ luettu " + yhteensaTavujaLuettu + " tavua");
		return esiintyvyydet;
	}
	
	/** Metodi joka generoi Huffman-puurakenteen annetulle prioriteettijonon merkeille
	 * 
	 * @param jono L‰hdetekstiss‰ esiintyv‰t merkit esiintyvyysj‰rjestyksess‰
	 * @return HuffmanPuuSisaSolmu Huffman-puurakenteen juuri
	 */
	public HuffmanPuuSisaSolmu luoHuffmanPuu(PriorityQueue<HuffmanPuuSisaSolmu> jono) {
		HuffmanPuuSisaSolmu juuri;
		HuffmanPuuSisaSolmu pieninFrekvenssi = null;
		HuffmanPuuSisaSolmu toiseksiPienin = null;
		while (jono.size() > 1) {
			//Tehd‰‰n puu
			pieninFrekvenssi = jono.poll();
			toiseksiPienin = jono.poll();
			HuffmanPuuSisaSolmu s = new HuffmanPuuSisaSolmu(pieninFrekvenssi.annaFrekvenssi() + toiseksiPienin.annaFrekvenssi());
			pieninFrekvenssi.asetaVanhempi(s);
			s.asetaVasen(pieninFrekvenssi);
			s.asetaOikea(toiseksiPienin);
			jono.offer(s);
		}
		juuri = (HuffmanPuuSisaSolmu)jono.remove();
		pieninFrekvenssi.asetaVanhempi(juuri);
		toiseksiPienin.asetaVanhempi(juuri);
		juuri.asetaVasen(pieninFrekvenssi);
		juuri.asetaOikea(toiseksiPienin);
		return juuri;
	}
	
	/**
	 * Metodi Tekee listan merkeist‰ esiintyvyyden perusteella j‰rjestettyn‰ (pienin esiintyvyys ansin),
	 * @param avaimet
	 * @param arvot
	 * @return PriorityQueue J‰rjestetty lista esiintyvyyksist‰
	 */
	public PriorityQueue<HuffmanPuuSisaSolmu> priorisoiAvaimet(Iterator avaimet, Iterator arvot) {
		PriorityQueue<HuffmanPuuSisaSolmu> jono = new PriorityQueue<HuffmanPuuSisaSolmu>();

		while (avaimet.hasNext()) {
			char merkki = ((Character)avaimet.next()).charValue();
			int esiintyvyys = ((Integer)arvot.next()).intValue(); 
			HuffmanPuuLehti h = new HuffmanPuuLehti(merkki, esiintyvyys);
			h.nimi = merkki + "!";
			jono.offer(h);
		}
		return jono;
	}
	
	/** Metodi joka luo huffman-koodit annetusta puun juurisolmusta alkaen. 
	 * Kutsuu itse‰‰n rekursiivisesti kunnes koko puun kaikki merkit on l‰pik‰yty 
	 * ja kirjoitettu sek‰ merkistaKoodi ett‰ koodistaMerkki taulukoihin.
	 * 
	 * @param s Huffman-puun solmu (sis‰solmu tai lehti)
	 * @param koodi
	 */
	public void luoMerkkiKoodiTaulukot(HuffmanPuuSisaSolmu s, String koodi) {
		if (s instanceof HuffmanPuuLehti) {
				char merkki = ((HuffmanPuuLehti)s).annaMerkki();
				merkistaKoodi.put(merkki, koodi);
				koodistaMerkki.put(koodi, merkki);
				//System.out.println(h.annaArvo() + ": " + h.annaFrekvenssi() + ":: " + koodi);
		} else if (s instanceof HuffmanPuuSisaSolmu) {
			if (s.annaVasenLapsi() != null)
				luoMerkkiKoodiTaulukot(s.annaVasenLapsi(),koodi + "0");
			if (s.annaOikeaLapsi() != null)
				luoMerkkiKoodiTaulukot(s.annaOikeaLapsi(),koodi + "1");
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
		long startTime = System.currentTimeMillis();
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
			System.out.println("Kohdetiedosto on jo olemassa, poistetaan..");
			kohdeTiedostoNimi.delete();
			//System.exit(3);
		}
		
		HuffmanPakkaus hc = new HuffmanPakkaus(lahdeTiedostoNimi,kohdeTiedostoNimi,PAKKAUSBLOKKIKOKO, PURKUBLOKKIKOKO);
		try {
			if (operaatio.equals("-pakkaa")) {
				hc.pakkaaTiedosto();
			} else {
				if (operaatio.equals("-pura")) {
					hc.puraTiedosto();
				}
			}
			long duration = System.currentTimeMillis() - startTime;
			System.out.println("Valmis! Suoritus kesti " + duration + " ms.");
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	
		
}	