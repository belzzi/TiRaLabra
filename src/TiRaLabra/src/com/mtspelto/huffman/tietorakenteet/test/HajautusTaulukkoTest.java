package com.mtspelto.huffman.tietorakenteet.test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mtspelto.huffman.tietorakenteet.HajautusTaulukko;

public class HajautusTaulukkoTest {

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
	public void testVakioKonstruktoriJaOlioidenLisays() {
		HajautusTaulukko ht = new HajautusTaulukko();
		ht.lisaaElementti(new String("yksi"), new String("yksiArvo"));
		ht.lisaaElementti(new String("kaksi"), new String("kaksiArvo"));
		assertEquals("yksiArvo", (String)ht.annaArvo("yksi"));
		assertEquals("kaksiArvo", (String)ht.annaArvo("kaksi"));
		assertTrue(ht.annaKoko() == 2);
	}
	
	@Test
	public void testKeskeltaElementinPoisto() {
		HajautusTaulukko ht = new HajautusTaulukko();
		ht.lisaaElementti(new String("yksi"), new String("yksiArvo"));
		ht.lisaaElementti(new String("kaksi"), new String("kaksiArvo"));
		ht.lisaaElementti(new String("kolme"), new String("kolmeVanhaArvo"));
		ht.korvaaElementti(new String("kolme"), new String("kolmeUusiArvo"));
		assertTrue(ht.annaKoko() == 3);
		ht.poistaElementti("kaksi");
		assertEquals("yksiArvo", (String)ht.annaArvo("yksi"));
		assertEquals("kolmeUusiArvo", (String)ht.annaArvo("kolme"));
		System.out.println(ht.annaKoko());
		assertTrue(ht.annaKoko() == 2);
		ht.lisaaElementti(1,"yksiIntArvo");
		ht.lisaaElementti(2,"kaksiIntArvo");
		ht.lisaaElementti(3,"kolmeIntArvo");
		assertTrue(ht.annaKoko() == 5);
	}
	
	@Test
	public void testIteraattorit() {
		HajautusTaulukko ht = new HajautusTaulukko();
		ht.lisaaElementti("yksi", "yksiArvo");
		ht.lisaaElementti(2, "kaksiArvo");
		ht.lisaaElementti(3, "kolmeArvo");
		try {
			Iterator i1 = ht.avaimet();
			
			Iterator i2 = ht.arvot();
			assertTrue(i1 != null);
			assertTrue(i2 != null);
			int elementtejaI1= 0;
			int elementtejaI2= 0;
			
			while (i1.hasNext()) {
				elementtejaI1++;
				Object o = i1.next();
				System.out.println("Iteraattori #1 (avaimet) Elementti #" + elementtejaI1 + ": " + o.toString());
			}
			System.out.println("===================");
			while (i2.hasNext()) {
				elementtejaI2++;
				Object o = i2.next();
				System.out.println("Iteraattori #2 (arvot) Elementti #" + elementtejaI2 + ": " + o.toString());
			}
			assertTrue(ht.annaKoko() == 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
