/**
 * 
 */
package com.mtspelto.huffman.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mtspelto.huffman.HuffmanPakkaus;

/** Yksikkötesti HuffmanPakkaus-luokalle.
 * 
 * @author mikkop
 * @see HuffmanPakkaus
 */
public class HuffmanPakkausTest {

	String fileName1 = "C:/Huffman-tmp/testitiedosto1_4k.txt";
	String fileName2 = "C:/Huffman-tmp/testitiedosto2_100k.txt";
	String fileName3 = "C:/Huffman-tmp/testitiedosto3_500k.xml";
	String fileName4 = "C:/Huffman-tmp/testitiedosto4_20M.xml";
	String fileName5 = "C:/Huffman-tmp/testitiedosto5_115M.xml";

	
	String fileName1Pak = "C:/Huffman-tmp/testitiedosto1_4k.pak";
	String fileName2Pak = "C:/Huffman-tmp/testitiedosto2_100k.pak";
	String fileName3Pak = "C:/Huffman-tmp/testitiedosto3_500k.pak";
	String fileName4Pak = "C:/Huffman-tmp/testitiedosto4_20M.pak";
	String fileName5Pak = "C:/Huffman-tmp/testitiedosto5_115M.pak";

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
	public void testHuffmanPakkaus() {
		int retVal = 0;
		try { 
			HuffmanPakkaus.main(new String[]{"-pakkaa",fileName1, fileName1Pak});
			HuffmanPakkaus.main(new String[]{"-pakkaa",fileName2, fileName2Pak});
			HuffmanPakkaus.main(new String[]{"-pakkaa",fileName3, fileName3Pak});
			HuffmanPakkaus.main(new String[]{"-pakkaa",fileName4, fileName4Pak});
			HuffmanPakkaus.main(new String[]{"-pakkaa",fileName5, fileName5Pak});
		} catch (Exception e) {
			
			e.printStackTrace();
			fail("Poikkeus");
		}
	}

}
