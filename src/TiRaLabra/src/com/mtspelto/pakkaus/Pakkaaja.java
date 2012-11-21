/**
 * 
 */
package com.mtspelto.pakkaus;

import java.io.File;

/** Luokka jota k‰ytet‰‰n pakkaus / purkuoperaation k‰ynnist‰miseen.
 * 
 * @author mikkop
 *
 */
public class Pakkaaja {

	/** P‰‰ohjelma. Lukee komentoriviparametrein‰ operaation (joko -pakkaa tai -pura) sek‰ pakattavan / purettavan
	 * tiedoston sek‰ kohdetiedoston nimet t‰ss‰ j‰rjestyksess‰.
	 * 
	 * Vakiona ohjelma k‰ytt‰‰ pakkausalgoritmina Huffman-koodausta. Pakkausalgoritmi voidaan asettaa komentorivilt‰ 
	 * system propertyll‰ "pakkausalgoritmi", ja tuntee arvot "lzw" tai "huffman". Esim.
	 * java -jar Pakkaaja.jar -Dpakkausalgoritmi=lzw -pakkaa <l‰hdetiedosto> <kohdetiedosto>
	 * 
	 * @param args Ensimm‰inen parametri t‰ytyy olla operaation nimi (joko -pakkaa tai -pura), toinen l‰hdetiedoston ja kolmas kohdetiedoston nimi.
	 * @see com.mtspelto.pakkaus#HuffmanPakkaus
	 * @see com.mtspelto.pakkaus#LZWPakkaus
	 */
	public static void main(String[] args) {

		//System.out.println("Pakkausalgoritmi: " + System.getProperty("pakkausalgoritmi"));
		long aloitusAika = System.currentTimeMillis();
		if (args.length != 3) {
			System.out.println("V‰‰r‰ m‰‰r‰ argumentteja. Anna argumenttina operaatio (joko -pakkaa tai -pura), pakattavan tiedoston ja kohdetiedoston nimi.");
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
			System.out.println("Kohdetiedosto " + args[2] + " on jo olemassa, poistetaan..");
			kohdeTiedostoNimi.delete();
		}
		PakkausRajapinta pakkaaja;
		if (System.getProperty("pakkausalgoritmi").toLowerCase().equals("lzw")) {
			pakkaaja = new LZWPakkaus(lahdeTiedostoNimi, kohdeTiedostoNimi);
		} else {
			pakkaaja = new HuffmanPakkaus(lahdeTiedostoNimi, kohdeTiedostoNimi);
		}
		try {
			if (operaatio.equals("-pakkaa")) {
				System.out.println("Aloitetaan tiedoston " + args[1] + " pakkaus...");
				System.out.println("K‰ytett‰v‰ pakkausalgoritmi: " + System.getProperty("pakkausalgoritmi"));
				pakkaaja.pakkaaTiedosto();
			} else {
				if (operaatio.equals("-pura")) {
					System.out.println("Aloitetaan tiedoston " + args[1] + " purku...");
					System.out.println("Pakkausalgoritmi: " + System.getProperty("pakkausalgoritmi"));
					pakkaaja.puraTiedosto();
				}
			}
			long kesto = System.currentTimeMillis() - aloitusAika;
			System.out.println("Valmis! Suoritus kesti " + kesto + " ms.");
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
