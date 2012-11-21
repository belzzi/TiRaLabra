package com.mtspelto.pakkaus;

import java.io.*;

/** Luokka joka kirjoittaa bittej� tiedostoon.
 * 
 * @author mikkop
 *
 */
public class BittiOutputStream extends OutputStream {

	/** Virheidenetsint�tila. Tulostaa jokaisen kirjoitetun tavun System.outiin
	 * 
	 */
	private static final boolean DEBUG = false;
	
	/**
	 * Kohdetiedosto
	 */
    private OutputStream  kohde;
    
    /** 32 bitin puskuri.
     * 
     */
    private int           bittiPuskuri;
    /** BittiPuskurin nykyinen koko bittein�.
     * 
     */
    private int           puskurinKoko;
    
    /* Bittimaskit jokaiselle 32-bittisen kokonaisluvun bitille.
     * N�it� k�ytet��n nollataessa ei-halutut bitit pois.
     *  
     */
    static final int BITTIMASKIT[] = {
        0x00, 0x01, 0x03, 0x07, 0x0f, 0x1f, 0x3f, 0x7f, 0xff,
        0x1ff,0x3ff,0x7ff,0xfff,0x1fff,0x3fff,0x7fff,0xffff,
        0x1ffff,0x3ffff,0x7ffff,0xfffff,0x1fffff,0x3fffff,
        0x7fffff,0xffffff,0x1ffffff,0x3ffffff,0x7ffffff,
        0xfffffff,0x1fffffff,0x3fffffff,0x7fffffff,0xffffffff
    };

    /** Kirjoittaa 8 alinta tavua annetusta kokonaisluvusta.
     * 
     * @param luku 
     */
    public void write(int luku) throws IOException {
        write(8, luku);
    }
    
    /** Luo uuden BittiOutputStreamin annettuun OutputStreamiin.
     * 
     * @param out OutputStream jonne bittej� kirjoitetaan
     */
    public BittiOutputStream(OutputStream out){
        kohde = out;
        bittiPuskuri = 0;
        puskurinKoko = 0;
    }
    
    /**
     * Kirjoittaa bitit joita ei viel� ole kirjoitettu levylle t�ytt�en tavun nollabiteill�.
     *
     */
    public void flush() {
        if (puskurinKoko > 0) {
        	int kirjoitettava = (bittiPuskuri << 8-puskurinKoko);
       		try{
       			kohde.write( (kirjoitettava) );
       			if (DEBUG)
       				System.out.println("  Flush: Levylle: " + annaBittiStringi(kirjoitettava, 32));
       		}
           	catch (java.io.IOException ioe){
           		ioe.printStackTrace();
           	}
            bittiPuskuri = 0;
            puskurinKoko -= 8;
        }
                
        try{
            kohde.flush();    
        }
        catch (java.io.IOException ioe){
        	ioe.printStackTrace();
        }
    }

    /** Sulkee t�m�n BittiOutputStreamin.
     * 
     */
    public void close() {
    	try {
    		flush();
    		kohde.close();
    	} catch (IOException ioe) {
    		ioe.printStackTrace();
    	}
    }

    /** Apurimetodi joka tulostaa luvun bitit.
     * 
     * @param luku
     * @return kokonaisluvun bittien esityksen merkkijonona (<code>bittiMaara</code> alinta bitti�) 
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
    
    /**
     * Kirjoittaa pyydetyn m��r�n bittej� kohdetiedostoon.
     * 
     * @param bittiMaara montako alinta bitti� bitit-muuttujasta kirjoitetaan
     * @param bitit kokonaisluku josta kirjoitetaan <code>bittiMaara</code> alinta bitti�
     */    
    public void write(int bittiMaara, int bitit) throws IOException {    	
    	if (DEBUG)
    		System.out.println(" Pyydetyt bitit: " + annaBittiStringi(bitit, bittiMaara));
    	if (DEBUG)
    		System.out.println(" BittiPuskuri ennen kirjoitusta: " + annaBittiStringi(bittiPuskuri, puskurinKoko));
    	
    	int vanhaPuskurinKoko = puskurinKoko == 0?bittiMaara-8:puskurinKoko;
    	puskurinKoko += bittiMaara;
        
        while (puskurinKoko >= 8){
        	//Nyt pit�� saada bittiPuskurin vanha sis�lt� bittiPuskurin alkuun, ja sen pituuden verran vasemmalle siirrettyn� uudet bitit liitetty� per��n
        	
        	bittiPuskuri = (bittiPuskuri << (puskurinKoko - vanhaPuskurinKoko)) | bitit;
        	bittiPuskuri = (bittiPuskuri >> (puskurinKoko - 8));
        	
        	bittiMaara = bittiMaara -8;
        	if (DEBUG)
        		System.out.println(" Levylle: " + annaBittiStringi(bittiPuskuri, 8));
			kohde.write(bittiPuskuri);
			puskurinKoko -= 8;
        	bittiPuskuri = bittiPuskuri << 8;
        }
		//Jos bittej� j�i yli, j�tet��n ne puskuriin
        if (bittiMaara > 0) {
        	bittiPuskuri = (bitit & BITTIMASKIT[bittiMaara]);
            if (DEBUG)
            	System.out.println(" Bittipuskuriin j�i: " + annaBittiStringi(bittiPuskuri, 31));
        }
    }      
}        
        
