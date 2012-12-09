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

/** Lempel-Ziv-Welch algoritmin pakkausohjelma.
 * 
 * @author mikkop
 * @see com.mtspelto.pakkaus.LZWPurku
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
				merkkijono.deleteCharAt(merkkijono.length()-1);
				int kirjoitettavaInt = (int)pakkausSanasto.annaArvo("" + merkkijono);
				bos.write(12, kirjoitettavaInt);
				bittiLaskuri += 12;
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
		System.out.println("Yhteens‰ " + luettuTavuja + " tavua luettu l‰hdetiedostosta");
		System.out.println("Yhteens‰ " + bittiLaskuri/8  + " tavua dataa kirjoitettu kohdetiedostoon");
		double teho = (double)(bittiLaskuri/8) / (double)luettuTavuja * 100;
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("Pakkaustehokkuus: " + df.format(teho) + " %");
		return true;
	}
}
