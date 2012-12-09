package com.mtspelto.pakkaus;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Iterator;

import com.mtspelto.pakkaus.tietorakenteet.HajautusTaulukko;
import com.mtspelto.pakkaus.tietorakenteet.HuffmanPuuLehti;
import com.mtspelto.pakkaus.tietorakenteet.HuffmanPuuSisaSolmu;
import com.mtspelto.pakkaus.tietorakenteet.MinimiKeko;


/**
 * Huffman-algoritmin pakkausohjelma.
 * 
 * @see com.mtspelto.pakkaus.Pakkaaja
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
	 * Pakattaessa l‰hdetiedostosta kerralla luettavan lohkon vakiokoko.
	 */
	public static final int VAKIO_PAKKAUSBLOKKIKOKO = 4096;
		
	/** Pakattaessa l‰hdetiedostosta kerralla luettavan lohkon koko.
	 * 
	 */
	private int pakkausBlokkiKoko;
		
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
	 */
	public HuffmanPakkaus(File lahde, File kohde, int pakkausBlokkiKoko) {
		this.pakkausBlokkiKoko = pakkausBlokkiKoko;
		merkistaKoodi = new HajautusTaulukko();
		koodistaMerkki = new HajautusTaulukko();
		this.lahde = lahde;
		this.kohde = kohde;
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
		FileOutputStream fos = new FileOutputStream(kohde);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		try {
			long lahdePituus = lahde.length();
			HajautusTaulukko esiintyvyydet = annaEsiintyvyydet(r);
			r.close();
			
			//J‰rjestet‰‰n merkit esiintyvyyden perusteella MinimiKekoa k‰ytt‰en
			MinimiKeko jono = priorisoiAvaimet(esiintyvyydet.avaimet(), esiintyvyydet.arvot());
			HuffmanPuuSisaSolmu juuri = luoHuffmanPuu(jono);
			luoMerkkiKoodiTaulukot(juuri, "");
			System.out.println("Merkkitaulukot luotu");
			
			//kirjoitetaan sanasto objektina:
			oos.writeObject(koodistaMerkki);
			//kirjoitetaan l‰hdetiedoston merkkien m‰‰r‰ Longina (vaaditaan viimeisen merkin katkaisua varten purussa!)
			oos.writeObject(new Long(lahdePituus));
			//Alustetaan r uudestaan lukemaan l‰hdetiedosto alusta
			r = new BufferedReader(new FileReader(lahde));
			BittiOutputStream bios = new BittiOutputStream(bos);
			kirjoitaPakattuTiedosto(r,bios);
			bios.close();
			r.close();
			bos.flush();
			bos.close();
			fos.close();
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
	private void kirjoitaPakattuTiedosto(BufferedReader r, BittiOutputStream bos) throws FileNotFoundException, IOException {
		
		char[] lahdeTaulukko = new char[pakkausBlokkiKoko];
		int yhteensaTavujaLuettu = 0;
		int loopissaTavujaLuettu = 0;
		int yhteensaTavujaKirjoitettu = 0;
			
		byte tavu = 0;
		int yhteensaBittiLaskuri = 0;
		int tavuLaskuri = 0;
		
		while ((loopissaTavujaLuettu = r.read(lahdeTaulukko))>0) {
			//Kirjoitetaan jokainen merkki Huffman-koodina StringBufferiin...
			yhteensaTavujaLuettu += loopissaTavujaLuettu;
			for (int i = 0; i < loopissaTavujaLuettu; i++) {
				byte[] koodi = ((String)merkistaKoodi.annaArvo(lahdeTaulukko[i])).getBytes();
				int koodiBitit = 0;
				for (int j = 0; j < koodi.length; j++) {
					koodiBitit = koodiBitit << 1;
					if (koodi[j] == '1')
						koodiBitit = koodiBitit | 1;
				}
				bos.write(koodi.length, koodiBitit);
				yhteensaBittiLaskuri += koodi.length;
			}
		}
		//Lasketaan biteist‰ kirjoitut tavut:
		tavuLaskuri = yhteensaBittiLaskuri/8;
		//..ja lis‰t‰‰n viimeinen tavu jos niit‰ ei ollut tasam‰‰r‰:
		if ((yhteensaBittiLaskuri % 8) == 0) {
			tavuLaskuri++;
		}
		System.out.println("Yhteens‰ " + yhteensaTavujaLuettu + " tavua luettu l‰hdetiedostosta");
		System.out.println("Yhteens‰ " + tavuLaskuri + " tavua dataa kirjoitettu kohdetiedostoon");
		double teho = (double)tavuLaskuri / (double)yhteensaTavujaLuettu * 100;
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
	private HajautusTaulukko annaEsiintyvyydet(BufferedReader r) throws FileNotFoundException, IOException {
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
	private HuffmanPuuSisaSolmu luoHuffmanPuu(MinimiKeko jono) {
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
	private MinimiKeko priorisoiAvaimet(Iterator avaimet, Iterator arvot) {
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
	private void luoMerkkiKoodiTaulukot(HuffmanPuuSisaSolmu s, String koodi) {
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