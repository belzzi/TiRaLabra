package com.mtspelto.pakkaus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mtspelto.pakkaus.tietorakenteet.HajautusTaulukko;

/** LZW-algoritmin purkuohjelma.
 * 
 * @author mpeltonen
 * @see com.mtspelto.pakkaus.LZWPakkaus
 * @see com.mtspelto.pakkaus.PurkuRajapinta
 */
public class LZWPurku implements PurkuRajapinta {

	/** L‰hdetiedosto
	 * 
	 */
	private File lahde;
	
	/** Kohdetiedosto
	 * 
	 */
	private File kohde;
		
	/** Sanasto t‰lle purkuoperaatiolle.
	 *  Avaimena k‰ytet‰‰n merkkijonoa ja arvona kokonaislukua.
	 */	
	private HajautusTaulukko pakkausSanasto;
	
	/** Luo uuden LZWPakkaus-instanssin.
	 * 
	 * @param lahdeTiedostoNimi
	 * @param kohdeTiedostoNimi
	 */
	public LZWPurku(File lahdeTiedostoNimi, File kohdeTiedostoNimi) {
		lahde = lahdeTiedostoNimi;
		kohde = kohdeTiedostoNimi;
		pakkausSanasto = new HajautusTaulukko();
	}
	
	/** Purkaa l‰hdetiedoston ja kirjoittaa puretun datan kohdetiedostoon.
	 * 
	 * @return true mik‰li pakkaus onnistui, muutoin false
	 * @see com.mtspelto.pakkaus.PurkuRajapinta#puraTiedosto()
	 */
	@Override
	public boolean puraTiedosto() throws FileNotFoundException, IOException {
		System.out.println("LZW purku aloitetaan");
		//BufferedInputStream bis = new BufferedInputStream(new FileInputStream(lahde));
		BittiInputStream bis = new BittiInputStream(new BufferedInputStream(new FileInputStream(lahde)));
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(kohde));

		 String[] sanasto = new String[4096];
		 int i;
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
	    	 if (sanasto[koodiSana] == null) {
	    		sanasto[koodiSana] = edellinenMerkkijono + edellinenMerkkijono.charAt(0);
	    		 suurinKoodi = koodiSana;
	    	 }
    		 bos.write(sanasto[koodiSana].getBytes());
	    	 tavuLaskuri += sanasto[koodiSana].length();
	    	 if ( edellinenMerkkijono != null && edellinenMerkkijono.length() > 0) {
	    		 sanasto[seuraavaKoodi] = edellinenMerkkijono + sanasto[koodiSana].charAt(0);
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
	     bis.close();
	     bos.flush();
	     bos.close();
	     return true;
	}

}
