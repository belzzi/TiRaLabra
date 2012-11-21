package com.mtspelto.pakkaus.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mtspelto.pakkaus.BittiInputStream;

/** Yksikkötesti BittiInputStream-luokalle.
 * 
 * @author mikkop
 * @see com.mtspelto.pakkaus#BittiInputStream
 */
public class BittiInputStreamTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/** Rakentaa uuden BittiInputStreamin, ja lukee 2 12 bitin kokonaislukua.
	 * 
	 */
	@Test
	public void testBittiInputStream() {
		try {
			BittiInputStream bis = new BittiInputStream(new FileInputStream("C:/Huffman-tmp/BittiOutputStream-test.out"));
			int a = bis.read(12);
			int b = bis.read(12);
			int c = bis.read(12);
			int d = bis.read(12);
			int e = bis.read(12);
			int f = bis.read(12);
			int g = bis.read(12);
			int h = bis.read(12);
			int i = bis.read(12);
		
			assertEquals(97, a);
			assertEquals(256, b);
			assertEquals(98, c);
			assertEquals(259, d);
			assertEquals(259, e);
			assertEquals(259, f);
			assertEquals(259, g);
			assertEquals(260, h);
			assertEquals(-1, i);
			bis.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("IOException");
		}
	}

}
