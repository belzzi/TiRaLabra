/**
 * 
 */
package com.mtspelto.pakkaus.tietorakenteet.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mtspelto.pakkaus.tietorakenteet.HuffmanPuuLehti;
import com.mtspelto.pakkaus.tietorakenteet.HuffmanPuuSisaSolmu;


/** Yksikkötesti Huffman-puulle (testaa luokat HuffmanPuuSisaSolmu ja HuffmanPuuLehti).
 * 
 * @author mikkop
 *
 */
public class HuffmanPuuSisaSolmuTest {

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

	/**
	 * Test method for {@link com.mtspelto.pakkaus.tietorakenteet.HuffmanPuuSisaSolmu#HuffmanPuuSisaSolmu(int, com.mtspelto.pakkaus.tietorakenteet.HuffmanPuuSisaSolmu, com.mtspelto.pakkaus.tietorakenteet.HuffmanPuuSisaSolmu, com.mtspelto.pakkaus.tietorakenteet.HuffmanPuuSisaSolmu)}.
	 */
	@Test
	public void testHuffmanPuuSisaSolmuIntHuffmanPuuSisaSolmuHuffmanPuuSisaSolmuHuffmanPuuSisaSolmu() {
		HuffmanPuuLehti hplA5 = new HuffmanPuuLehti('a',5);
		HuffmanPuuLehti hplB3 = new HuffmanPuuLehti('b',3);
		HuffmanPuuSisaSolmu ab8 = new HuffmanPuuSisaSolmu(8);
		ab8.asetaVasen(hplA5);
		ab8.asetaOikea(hplB3);
		hplA5.asetaVanhempi(ab8);
		hplB3.asetaVanhempi(ab8);	
		assertEquals(hplA5, ab8.annaVasenLapsi());
		assertEquals(hplB3, ab8.annaOikeaLapsi());
		assertEquals(ab8, hplB3.annaVanhempi());
		assertTrue((hplA5.annaFrekvenssi() == 5));
		assertTrue((ab8.annaFrekvenssi() == 8));

	}

}
