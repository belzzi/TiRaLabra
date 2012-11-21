/**
 * 
 */
package com.mtspelto.pakkaus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import com.mtspelto.pakkaus.tietorakenteet.HajautusTaulukko;

/** T‰m‰ luokka toteuttaa Lempel-Ziv-Welch algoritmiin perustuvan tiedon pakkaus- ja purkuohjelman.
 * 
 * @author mikkop
 *
 */
public class LZWPakkaus implements PakkausRajapinta {

	/** L‰hdetiedosto
	 * 
	 */
	private File lahde;
	
	/** Kohdetiedosto
	 * 
	 */
	private File kohde;
	
	/** Virheidenetsint‰tila p‰‰lle (true) / pois (false)
	 * 
	 */
	private static final boolean DEBUG = false;
		
	/** Sanasto t‰lle pakkaus / purkuoperaatiolle.
	 *  Avaimena k‰ytet‰‰n merkkijonoa ja arvona kokonaislukua.
	 */	
	private HajautusTaulukko pakkausSanasto;
	
	/** Luo uuden LZWPakkaus-instanssin.
	 * 
	 * @param lahdeTiedostoNimi
	 * @param kohdeTiedostoNimi
	 */
	public LZWPakkaus(File lahdeTiedostoNimi, File kohdeTiedostoNimi) {
		lahde = lahdeTiedostoNimi;
		kohde = kohdeTiedostoNimi;
		pakkausSanasto = new HajautusTaulukko();
	}
	

	/** Pakkaa l‰hdetiedoston LZW:ll‰ ja kirjoittaa pakatun datan kohdetiedostoon.
	 * 
	 * @return true mik‰li pakkaus onnistui, muutoin false
	 * @see com.mtspelto.pakkaus.PakkausRajapinta#pakkaaTiedosto()
	 */
	@Override
	public boolean pakkaaTiedosto() throws FileNotFoundException, IOException {
		System.out.println("LZW pakkaus aloitetaan");
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(lahde));
		BittiOutputStream bos = new BittiOutputStream (new BufferedOutputStream(new FileOutputStream(kohde)));
		
		int c;
		int seuraavaKoodi = 256;
		
		int luettuTavuja = 0;
		StringBuilder merkkijono = new StringBuilder(); //prefix
		String prefix = "";
		
		//Lis‰t‰‰n vakiosanasto
		for (int i = 0; i < 256; i++) {
			String merkkiString = new String("" + (char)i);
			pakkausSanasto.lisaaElementti(merkkiString, i);
		}
		int bittiLaskuri = 0 ;
		boolean loppu = false;
		while (!loppu) {
			c = bis.read();
			if (c == -1)
				loppu = true;
			luettuTavuja++;
			merkkijono.append((char)c);

			//Luetaan merkkej‰ sis‰‰n l‰hdetiedostosta kunnes sanastosta ei en‰‰ lˆydy merkkijono + c.
			//Siin‰ vaiheessa kirjoitetaan kohdetiedostoon merkkijono
			if (!pakkausSanasto.sisaltaaAvaimen(merkkijono.toString())) {
				int koodi = seuraavaKoodi++;
				pakkausSanasto.lisaaElementti(merkkijono.toString(), koodi);
				if (DEBUG)
					System.out.println("Lis‰tty koodi " + koodi + ": " + merkkijono.toString());
				merkkijono.deleteCharAt(merkkijono.length()-1);
				int kirjoitettavaInt = (int)pakkausSanasto.annaArvo("" + merkkijono);
				bos.write(12, kirjoitettavaInt);
				bittiLaskuri += 12;
				if (DEBUG)
					System.out.println("  -> Kirjoitettu koodi " + kirjoitettavaInt + "(" + merkkijono.toString() + ")");
				merkkijono = new StringBuilder();
				merkkijono.append((char)c);
			}
			// jos sanasto on t‰ynn‰, hyl‰t‰‰n se:
			if (seuraavaKoodi == 4096) {
				pakkausSanasto = new HajautusTaulukko();
				for (int i = 0; i < 256; i++) {
					String merkkiString = new String("" + (char)i);
					pakkausSanasto.lisaaElementti(merkkiString, i);
				}
				seuraavaKoodi = 256;
			}
		}
		bos.flush();
		bos.close();
		bis.close();
		System.out.println("Sanaston koko lopussa: " + seuraavaKoodi);
		System.out.println("Yhteens‰ " + luettuTavuja + " tavua luettu l‰hdetiedostosta");
		System.out.println("Yhteens‰ " + bittiLaskuri/8  + " tavua dataa kirjoitettu kohdetiedostoon");
		double teho = (double)(bittiLaskuri/8) / (double)luettuTavuja * 100;
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("Pakkaustehokkuus: " + df.format(teho) + " %");
		return true;
	}

	/** Purkaa l‰hdetiedoston ja kirjoittaa puretun datan kohdetiedostoon.
	 * 
	 * @return true mik‰li pakkaus onnistui, muutoin false
	 * @see com.mtspelto.pakkaus.PakkausRajapinta#puraTiedosto()
	 */
	@Override
	public boolean puraTiedosto() throws FileNotFoundException, IOException {
		System.out.println("LZW purku aloitetaan");
		//BufferedInputStream bis = new BufferedInputStream(new FileInputStream(lahde));
		BittiInputStream bis = new BittiInputStream(new BufferedInputStream(new FileInputStream(lahde)));
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(kohde));

		 String[] sanasto = new String[4096];
		 int i; // next available codeword value
		 int bittiLaskuri = 0;
		 int tavuLaskuri = 0;
	     int kierrosLaskuri = 0;

	     // Laitetaan kaikki 8-bittiset merkit sanastoon valmiiksi
	     for (i = 0; i < 256; i++)
	            sanasto[i] = "" + (char) i;
	     
	     int koodiSana = 0;
	     String edellinenMerkkijono = "";
	     int suurinKoodi = i;
	     int seuraavaKoodi = 256;
	     
	     while ((koodiSana = bis.read(12)) != -1) {
	    	 kierrosLaskuri++;
	    	 bittiLaskuri += 12;
	    	 if (DEBUG)
	    		 System.out.println("Kierros " + kierrosLaskuri + ": Luettu koodi " + koodiSana + "("+ sanasto[koodiSana] + ")");
	    	 if (sanasto[koodiSana] == null) {
	    		 try {
	    			 sanasto[koodiSana] = edellinenMerkkijono + edellinenMerkkijono.charAt(0);
	    		 } catch (StringIndexOutOfBoundsException siobe) {
	    			 //siobe.printStackTrace();
	    			 System.out.println("Kierroksella " + kierrosLaskuri + " poikkeus, koodisana " + koodiSana);
	    			 //throw new Exception();
	    		 }
	    		 if (DEBUG)
	    			 System.out.println("  -> Kirjoitettu koodi " + koodiSana + ": " + sanasto[koodiSana] + "(suurin koodi oli " + suurinKoodi + ")");
	    		 suurinKoodi = koodiSana;
	    	 }
    		 bos.write(sanasto[koodiSana].getBytes());
	    	 tavuLaskuri += sanasto[koodiSana].length();
	    	 if ( edellinenMerkkijono != null && edellinenMerkkijono.length() > 0) {
	    		 sanasto[seuraavaKoodi] = edellinenMerkkijono + sanasto[koodiSana].charAt(0);
	    		 if (DEBUG)
	    			 System.out.println("  -> Kirjoitettu koodi " + seuraavaKoodi + ": " + sanasto[seuraavaKoodi]);
	    		 if (suurinKoodi < seuraavaKoodi)
	    			 suurinKoodi = seuraavaKoodi;
	    		 seuraavaKoodi++;
	    	 }
	    	 edellinenMerkkijono = sanasto[koodiSana];
	    	 //Jos sanasto on t‰ynn‰, hyl‰t‰‰n se ja alustetaan uudestaan:
	    	 if (seuraavaKoodi == 4096) {
	    		 sanasto = new String[4096];
	    	     for (i = 0; i < 256; i++)
	 	            sanasto[i] = "" + (char) i;
	    	    seuraavaKoodi = 256;
	    	 }
	     }
	     if (DEBUG) {
	    	 System.out.println("Yhteens‰ " + bittiLaskuri/8+ " tavua pakattua dataa luettu");
	    	 System.out.println("Yhteens‰ " + tavuLaskuri + " tavua purettua dataa kirjoitettu");	     
	     }
	     System.out.println("Sanaston koko lopussa: " + suurinKoodi);
	     bis.close();
	     bos.flush();
	     bos.close();
	     return true;
	}
}
