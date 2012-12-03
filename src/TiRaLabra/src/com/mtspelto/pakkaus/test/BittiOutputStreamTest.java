package com.mtspelto.pakkaus.test;

import static org.junit.Assert.*;

import java.io.FileOutputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mtspelto.pakkaus.BittiOutputStream;

/** Yksikkötesti BittiOutputStream-luokalle.
 * 
 * @author mikkop
 * @see com.mtspelto.pakkaus#BittiOutputStream
 */
public class BittiOutputStreamTest {

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

	@Test
	public void testBittiOutputStream() {
		try {
			FileOutputStream fos = new FileOutputStream("C:/Huffman-tmp/BittiOutputStream-test.out");
			BittiOutputStream bos = new BittiOutputStream(fos);

			//bos.write('a');
			//bos.write('b');
//			bos.write(8,6);
//			bos.write(4,1);
			bos.write(12,97); //000001100001
			bos.write(12,256); //000100000000
			bos.write(12,98);
			bos.write(12,259);
			bos.write(12,259);
			bos.write(12,259);
			bos.write(12,259);
			bos.write(12,260);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception");
		}
		//fail("Not yet implemented"); // TODO
	}

}
