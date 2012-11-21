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
public class HuffmanPakkaus implements PakkausRajapinta {
	
	
	/**
	 * Lippu jolla kytket‰‰n virheenetsint‰tila p‰‰lle / pois.
	 */
	public static final boolean DEBUG = false;
	
	/**
	 * Lippu, jonka ollessa p‰‰ll‰, pakatessa kohdetiedostoon kirjoitetaan
	 * Huffman-koodit raakana (ei siis bittein‰).
	 * 
	 * Helpottaa vianetsint‰‰ koodausvaiheessa.
	 */
	public static final boolean DUMMY_PAKKAUS = false;
	
	/**
	 * Pakattaessa l‰hdetiedostosta kerralla luettavan lohkon vakiokoko.
	 */
	public static final int VAKIO_PAKKAUSBLOKKIKOKO = 1024;
	
	/**
	 *  Purettavasta tiedostosta kerralla luettavan bin‰‰rilohkon vakiokoko
	 *  Lohkon koon pit‰‰ aina olla v‰hint‰‰n pisimm‰n mahdollisen Huffman-koodin
	 *  pituus, eli ainakin 16 tavua.
	 */
	public static final int VAKIO_PURKUBLOKKIKOKO = 128;
	
	/** Pakattaessa l‰hdetiedostosta kerralla luettavan lohkon koko.
	 * 
	 */
	private int pakkausBlokkiKoko;
	
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
	public HuffmanPakkaus(File lahde, File kohde) {
		this.purkuBlokkiKoko = VAKIO_PURKUBLOKKIKOKO;
		this.pakkausBlokkiKoko = VAKIO_PAKKAUSBLOKKIKOKO;
		merkistaKoodi = new HajautusTaulukko();
		koodistaMerkki = new HajautusTaulukko();
		this.lahde = lahde;
		this.kohde = kohde;
	}
	
	
	/**
	 * Rakentaa HuffmanPakkaus-luokan.
	 * 
	 * @param lahde L‰hdetiedoston nimi
	 * @param kohde Kohdetiedoston nimi
	 * @param pakkausBlokkiKoko Pakattaessa k‰ytett‰v‰ blokkikoko
	 * @param purkuBlokkiKoko Purettaessa k‰ytett‰v‰ blokkikoko
	 */
	public HuffmanPakkaus(File lahde, File kohde, int pakkausBlokkiKoko, int purkuBlokkiKoko) {
		this.purkuBlokkiKoko = purkuBlokkiKoko;
		this.pakkausBlokkiKoko = pakkausBlokkiKoko;
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
		
		// Sanaston luku erillisest‰ .ser tiedostosta 
		//ObjectInputStream ois = new ObjectInputStream(new FileInputStream(lahde.getAbsoluteFile() + ".ser"));  
		
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
	
	/**
	 * Tiedoston pakkauksen p‰‰metodi. 
	 * 
	 * @return true mik‰li pakkaus onnistui, muutoin false
	 * 
	 */
	@Override
	public boolean pakkaaTiedosto() throws FileNotFoundException, IOException {
		BufferedReader r = new BufferedReader(new FileReader(lahde));
		//BufferedWriter w = new BufferedWriter(new FileWriter(kohde));
		FileOutputStream fos = new FileOutputStream(kohde);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		
		//FileOutputStream fos2 = new FileOutputStream(kohde.getAbsoluteFile() + ".ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		try {
			HajautusTaulukko esiintyvyydet = annaEsiintyvyydet(r);
			r.close();
			
			//J‰rjestet‰‰n merkit esiintyvyyden perusteella PrioriteettiJonoa k‰ytt‰en ##TODO: Tee oma PrioriteettiJono
			//PriorityQueue<HuffmanPuuSisaSolmu> jono = priorisoiAvaimet(avaimet.iterator(), arvot.iterator());
			
			MinimiKeko jono = priorisoiAvaimet(esiintyvyydet.avaimet(), esiintyvyydet.arvot());
			HuffmanPuuSisaSolmu juuri = luoHuffmanPuu(jono);
			luoMerkkiKoodiTaulukot(juuri, "");
			System.out.println("Merkkitaulukot luotu");
			
			if (DEBUG)
				tulostaTaulukot();

			//kirjoitaSanasto(oos);
			oos.writeObject(koodistaMerkki);
			
			//Alustetaan r uudestaan lukemaan l‰hdetiedosto alusta
			r = new BufferedReader(new FileReader(lahde));
								
			kirjoitaPakattuTiedosto(r,bos);
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
	 *  Kirjoittaa kohdetiedostoon pakatun tiedoston.
	 *  
	 * @param r BufferedReader Reader joka osoittaa l‰hdetiedostoon
	 * @param bos BufferedOutputStream OutputStream joka osoittaa kohdetiedostoon
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void kirjoitaPakattuTiedosto(BufferedReader r, BufferedOutputStream bos) throws FileNotFoundException, IOException {
		char[] lahdeTaulukko = new char[pakkausBlokkiKoko];
		int yhteensaTavujaLuettu = 0;
		int loopissaTavujaLuettu = 0;
		int yhteensaTavujaKirjoitettu = 0;
			
		byte tavu = 0;
		int bittiLaskuri = 0;
		int tavuLaskuri = 0;
		
		byte[] jaljelleJaaneetMerkit = new byte[10];
		while ((loopissaTavujaLuettu = r.read(lahdeTaulukko))>0) {
			//Kirjoitetaan jokainen merkki Huffman-koodina StringBufferiin...
			
			for (int i = 0; i < loopissaTavujaLuettu; i++) {
				//sb.append((String)merkistaKoodi.get(lahdeTaulukko[i]));
				byte[] koodi = ((String)merkistaKoodi.annaArvo(lahdeTaulukko[i])).getBytes();
				for (int j = 0; j < koodi.length; j++) {
					bittiLaskuri++;
					tavu = (byte)(tavu << 1);
					if (koodi[j] == '1')
						tavu = (byte)(tavu | 1);
					if (bittiLaskuri == 8) {
						bos.write(tavu);
						tavuLaskuri++;
						bittiLaskuri = 0;
					}
				}
			}
			yhteensaTavujaLuettu += loopissaTavujaLuettu;
		}
		System.out.println("Yhteens‰ " + yhteensaTavujaLuettu + " tavua luettu l‰hdetiedostosta");
		System.out.println("Yhteens‰ " + tavuLaskuri + " tavua dataa kirjoitettu kohdetiedostoon");
		double teho = 100-(double)tavuLaskuri / (double)yhteensaTavujaLuettu * 100;
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("Pakkaustehokkuus: " + df.format(teho) + " % (ei ota huomioon sanaston viem‰‰ tilaa)");
	}
	
	
	/**
	 * Metodi joka skannaa l‰hdetiedoston ensimm‰isen kerran l‰pi, ja palauttaa merkkien esiintyvyydet HashMapissa.
	 * @param r BufferedReader - lukija l‰hdetiedostoon
	 * @return HajautusTaulukko joka sis‰lt‰‰ kaikki l‰hdetekstiss‰ esiintyv‰t merkit ja niiden lasketut esiintyvyydet
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public HajautusTaulukko annaEsiintyvyydet(BufferedReader r) throws FileNotFoundException, IOException {
		HajautusTaulukko esiintyvyydet = new HajautusTaulukko();
		
		char[] lahdeTaulukko = new char[256];
		int yhteensaTavujaLuettu = 0;
		int loopissaTavujaLuettu = 0;
		
		while ((loopissaTavujaLuettu = r.read(lahdeTaulukko))>0) {
			yhteensaTavujaLuettu += loopissaTavujaLuettu;
			
			for (int i = 0; i < lahdeTaulukko.length; i++) {
				Character merkki = new Character(lahdeTaulukko[i]);
				if (esiintyvyydet.annaArvo(merkki) != null) {
					esiintyvyydet.korvaaElementti(merkki, ((Integer)esiintyvyydet.annaArvo(merkki)).intValue()+1);
				} else {
					esiintyvyydet.lisaaElementti(merkki, 1);
				}
			}
		}
		System.out.print("Ensimm‰inen skannaus valmis, yhteens‰ luettu " + yhteensaTavujaLuettu + " tavua... ");
		return esiintyvyydet;
	}
	
	/** Metodi joka generoi Huffman-puurakenteen annetulle prioriteettijonon merkeille
	 * 
	 * @param jono L‰hdetekstiss‰ esiintyv‰t merkit esiintyvyysj‰rjestyksess‰
	 * @return HuffmanPuuSisaSolmu Huffman-puurakenteen juuri
	 */
	public HuffmanPuuSisaSolmu luoHuffmanPuu(MinimiKeko jono) {
		HuffmanPuuSisaSolmu juuri;
		HuffmanPuuSisaSolmu pieninFrekvenssi = null;
		HuffmanPuuSisaSolmu toiseksiPienin = null;
		while (jono.koko() > 1) {
			//Tehd‰‰n puu
			pieninFrekvenssi = jono.annaPienin();
			toiseksiPienin = jono.annaPienin();
			HuffmanPuuSisaSolmu s = new HuffmanPuuSisaSolmu(pieninFrekvenssi.annaFrekvenssi() + toiseksiPienin.annaFrekvenssi());
			pieninFrekvenssi.asetaVanhempi(s);
			s.asetaVasen(pieninFrekvenssi);
			s.asetaOikea(toiseksiPienin);
			jono.lisaa(s);
		}
		//juuri = (HuffmanPuuSisaSolmu)jono.remove();
		juuri = (HuffmanPuuSisaSolmu)jono.annaPienin();
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
	public MinimiKeko priorisoiAvaimet(Iterator avaimet, Iterator arvot) {
		MinimiKeko jono = new MinimiKeko();
		while (avaimet.hasNext()) {
			char merkki = ((Character)avaimet.next()).charValue();
			int esiintyvyys = ((Integer)arvot.next()).intValue(); 
			HuffmanPuuLehti h = new HuffmanPuuLehti(merkki, esiintyvyys);
			jono.lisaa(h);
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
				merkistaKoodi.lisaaElementti(merkki, koodi);
				koodistaMerkki.lisaaElementti(koodi, merkki);
		} else if (s instanceof HuffmanPuuSisaSolmu) {
			if (s.annaVasenLapsi() != null)
				luoMerkkiKoodiTaulukot(s.annaVasenLapsi(),koodi + "0");
			if (s.annaOikeaLapsi() != null)
				luoMerkkiKoodiTaulukot(s.annaOikeaLapsi(),koodi + "1");
		}
	}

}	