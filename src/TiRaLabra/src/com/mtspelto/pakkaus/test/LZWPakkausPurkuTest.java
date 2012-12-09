/**
 * 
 */
package com.mtspelto.pakkaus.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mtspelto.pakkaus.LZWPakkaus;
import com.mtspelto.pakkaus.Pakkaaja;

/** Yksikkötesti LZWPakkaus-luokalle. Pakkaa ja purkaa 5 erikokoista tiedostoa.
 * 
 * @author mikkop
 * @see LZWPakkaus
 */
public class LZWPakkausPurkuTest {

	/** Lähdetiedostojen nimet
	 * 
	 */
	String fileName1 = "C:/Huffman-tmp/testitiedosto1_180k.txt";
	String fileName2 = "C:/Huffman-tmp/testitiedosto2_500k.xml";
	String fileName3 = "C:/Huffman-tmp/testitiedosto3_13M.txt";
	String fileName4 = "C:/Huffman-tmp/testitiedosto4_20M.xml";
	String fileName5 = "C:/Huffman-tmp/testitiedosto5_115M.xml";

/** Pakattujen tiedostojen nimet
 * 	
 */
	String fileName1Pak = "C:/Huffman-tmp/testitiedosto1_180k.lzw";
	String fileName2Pak = "C:/Huffman-tmp/testitiedosto2_500k.lzw";
	String fileName3Pak = "C:/Huffman-tmp/testitiedosto3_13M.lzw";
	String fileName4Pak = "C:/Huffman-tmp/testitiedosto4_20M.lzw";
	String fileName5Pak = "C:/Huffman-tmp/testitiedosto5_115M.lzw";
	
	/** Purettujen tiedostojen nimet
	 * 
	 */
	String fileName1Pur = "C:/Huffman-tmp/testitiedosto1_180k.lpur";
	String fileName2Pur = "C:/Huffman-tmp/testitiedosto2_500k.lpur";
	String fileName3Pur = "C:/Huffman-tmp/testitiedosto3_13M.lpur";
	String fileName4Pur = "C:/Huffman-tmp/testitiedosto4_20M.lpur";
	String fileName5Pur = "C:/Huffman-tmp/testitiedosto5_115M.lpur";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/** Olettaa että seuraava tekstitiedostot ovat olemassa:
	 * 	C:/Huffman-tmp/testitiedosto_pieni.txt
	 * 	C:/Huffman-tmp/testitiedosto2_keski.txt
	 *  C:/Huffman-tmp/testitiedosto3_suuri.xml
	 * 
	 */
	@Test
	public void testLZWPakkaus() {
		try { 
			System.setProperty("pakkausalgoritmi", "lzw");
			Pakkaaja.main(new String[]{"-pakkaa",fileName1, fileName1Pak});
			Pakkaaja.main(new String[]{"-pakkaa",fileName2, fileName2Pak});
			Pakkaaja.main(new String[]{"-pakkaa",fileName3, fileName3Pak});
			Pakkaaja.main(new String[]{"-pakkaa",fileName4, fileName4Pak});
			Pakkaaja.main(new String[]{"-pakkaa",fileName5, fileName5Pak});
			//Pakkaaja.main(new String[]{"-pakkaa",fileName6, fileName6Pak});
			
			Pakkaaja.main(new String[]{"-pura",fileName1Pak, fileName1Pur});
			Pakkaaja.main(new String[]{"-pura",fileName2Pak, fileName2Pur});
			Pakkaaja.main(new String[]{"-pura",fileName3Pak, fileName3Pur});
			Pakkaaja.main(new String[]{"-pura",fileName4Pak, fileName4Pur});
			Pakkaaja.main(new String[]{"-pura",fileName5Pak, fileName5Pur});
			//Pakkaaja.main(new String[]{"-pura",fileName6Pak, fileName6Pur});
		} catch (Exception e) {
			e.printStackTrace();
			fail("Poikkeus");
		}
	}

}
