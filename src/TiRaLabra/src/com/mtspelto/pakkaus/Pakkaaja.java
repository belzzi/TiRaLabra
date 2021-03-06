/**
 * 
 */
package com.mtspelto.pakkaus;

import java.io.File;

/** Luokka jota käytetään pakkaus / purkuoperaation käynnistämiseen.
 * 
 * @author mikkop
 *
 */
public class Pakkaaja {

	/** Pääohjelma. Lukee komentoriviparametreinä operaation (joko -pakkaa tai -pura) sekä pakattavan / purettavan
	 * tiedoston sekä kohdetiedoston nimet tässä järjestyksessä.
	 * 
	 * Vakiona ohjelma käyttää pakkausalgoritmina Huffman-koodausta. Pakkausalgoritmi voidaan asettaa komentoriviltä 
	 * system propertyllä "pakkausalgoritmi", ja tuntee arvot "lzw" tai "huffman". Esim.
	 * java -Dpakkausalgoritmi=lzw -jar Pakkaaja.jar -pakkaa <lähdetiedosto> <kohdetiedosto>
	 * 
	 * @param args Ensimmäinen parametri täytyy olla operaation nimi (joko -pakkaa tai -pura), toinen lähdetiedoston ja kolmas kohdetiedoston nimi.
	 * @see com.mtspelto.pakkaus#HuffmanPakkaus
	 * @see com.mtspelto.pakkaus#LZWPakkaus
	 */
	public static void main(String[] args) {

		//System.out.println("Pakkausalgoritmi: " + );
		long aloitusAika = System.currentTimeMillis();
		String algoritmi = System.getProperty("pakkausalgoritmi");
		if (algoritmi == null)
			algoritmi = "huffman";
		if (args.length != 3) {
			System.out.println("Väärä määrä argumentteja. Anna argumenttina operaatio (joko -pakkaa tai -pura), pakattavan tiedoston ja kohdetiedoston nimi.");
			System.exit(1);
		}
		String operaatio = args[0];
		File lahdeTiedostoNimi = new File(args[1]);
		if (!lahdeTiedostoNimi.exists()) {
			System.out.println("Lähdetiedostoa " + args[1] + " ei ole olemassa.");
			System.exit(2);
		}
		File kohdeTiedostoNimi = new File(args[2]);
		if (kohdeTiedostoNimi.exists()) {
			System.out.println("Kohdetiedosto " + args[2] + " on jo olemassa, poistetaan..");
			kohdeTiedostoNimi.delete();
		}

		try {
			PakkausRajapinta pakkaaja;
			if (operaatio.equals("-pakkaa")) {
				if (algoritmi.toLowerCase().equals("lzw")) {
					pakkaaja = new LZWPakkaus(lahdeTiedostoNimi, kohdeTiedostoNimi);
				} else {
					pakkaaja = new HuffmanPakkaus(lahdeTiedostoNimi, kohdeTiedostoNimi);
				}
				System.out.println("Aloitetaan tiedoston " + args[1] + " pakkaus...");
				System.out.println("Käytettävä pakkausalgoritmi: " + System.getProperty("pakkausalgoritmi"));
				pakkaaja.pakkaaTiedosto();
			} else {
				if (operaatio.equals("-pura")) {
					PurkuRajapinta purkaja;
					if (algoritmi.toLowerCase().equals("lzw")) {
						purkaja = new LZWPurku(lahdeTiedostoNimi, kohdeTiedostoNimi);
					} else {
						purkaja = new HuffmanPurku(lahdeTiedostoNimi, kohdeTiedostoNimi);
					}
					System.out.println("Aloitetaan tiedoston " + args[1] + " purku...");
					
					System.out.println("Pakkausalgoritmi: " + System.getProperty("pakkausalgoritmi"));
					purkaja.puraTiedosto();
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
