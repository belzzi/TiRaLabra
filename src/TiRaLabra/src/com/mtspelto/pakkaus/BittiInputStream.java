package com.mtspelto.pakkaus;
import java.io.*;

/** Luokka jolla voi lukea InputStreamista bittej‰ halutun m‰‰r‰n kerrallaan.
 * 
 * @author mikkop
 *
 */
public class BittiInputStream extends InputStream {
	
	/** L‰hdetiedoston InputStream
	 * 
	 */
    private InputStream     lahde;
    
    /** 32 bitin puskuri
     * 
     */
    private int bittiPuskuri;
	
    /** bittiPuskurin nykyinen koko bittein‰
     * 
     */
    private int puskurinKoko;
    
    /** Rakentaa uuden BittiInputStreamin l‰hdetiedostoon
     * 
     * @param lahde L‰hdetiedosto
     */
	public BittiInputStream(InputStream lahde) {
		this.lahde = lahde;
		bittiPuskuri = 0;
		puskurinKoko = 0;
	}
	
	
	/** Sulkee t‰m‰n BittiInputStreamin
	 *     
	 */
    public void close() {
    	try {
    		if (lahde != null) {
    			lahde.close();
    		}
    	} catch (java.io.IOException ioe){
    		ioe.printStackTrace();
    	}
    }

    
    /** Lukee 8 bitti‰.
     * 
     */
	@Override
	public int read() throws IOException {
		return read(8);
	}
	
    /** Apurimetodi joka tulostaa luvun bitit.
     * 
     * @param luku
     * @return kokonaisluvun bittien esityksen merkkijonona (<code>bittiMaara</code> alinta bitti‰) 
     */
    private String annaBittiStringi(int luku, int bittiMaara) {
    	StringBuilder sb = new StringBuilder();
    	for (int i = bittiMaara; i > 0; i--) {
    		if ((luku & (1 << i-1)) != 0)
    			sb.append("1");
    		else
    			sb.append("0");
    	}
    	return sb.toString();
    }


    /** Lukee pyydetyn m‰‰r‰n bittej‰ l‰hdetiedostosta.
     * 
     * @param bitteja
     * @return kokonaisluvun jossa alimmat <code>bitteja</code> m‰‰r‰ bittej‰ luettu l‰hdetiedostosta 
     */
 	public int read(int bitteja) {
		int palautettava = 0;

		// Mik‰li puskurissa on t‰ll‰ hetkell‰ v‰hemm‰n 
		if (bitteja > puskurinKoko) {
			while (bitteja > puskurinKoko) {
				try {
					int luettu = lahde.read();
					// Mik‰li pyydetty‰ bittim‰‰r‰‰ ei ole j‰ljell‰ palautetaan -1
					if (luettu == -1) {
						return -1;
					}
					bittiPuskuri = bittiPuskuri << 8 | luettu;
					puskurinKoko += 8;
				} catch (IOException ioe) {
					ioe.printStackTrace();
					return -1;
				}
			}
		} else {
			//Muuten palautetaan bittipuskurista suoraan n bitti‰
			palautettava = bittiPuskuri & BittiOutputStream.BITTIMASKIT[bitteja];
			puskurinKoko -= bitteja;
		}
		palautettava = bittiPuskuri >> puskurinKoko - bitteja;
		puskurinKoko -= bitteja;
		bittiPuskuri = bittiPuskuri & BittiOutputStream.BITTIMASKIT[puskurinKoko];

		return palautettava;
 	}
		
 	/** Alustaa t‰m‰n BittiInputStreamin uudestaan
 	 * 
 	 */
    public void reset() {
    	bittiPuskuri = puskurinKoko = 0;
    }
}


