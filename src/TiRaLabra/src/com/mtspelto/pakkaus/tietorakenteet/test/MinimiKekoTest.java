package com.mtspelto.pakkaus.tietorakenteet.test;
import static org.junit.Assert.*;

import javax.swing.text.html.MinimalHTMLWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mtspelto.pakkaus.tietorakenteet.HuffmanPuuLehti;
import com.mtspelto.pakkaus.tietorakenteet.MinimiKeko;

/** Yksikkötesti MinimiKeko-luokalle.
 * 
 * @author mikkop
 *
 */
public class MinimiKekoTest {

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
	public void test() {
		try {
			
		MinimiKeko keko = new MinimiKeko();
		
		assertTrue(keko.vanhempi(0) == -1);
		assertTrue(keko.vanhempi(1) == 0);
		assertTrue(keko.vanhempi(2) == 0);
		assertTrue(keko.vanhempi(3) == 1);
		assertTrue(keko.vanhempi(4) == 1);
		assertTrue(keko.vanhempi(5) == 2);
		assertTrue(keko.vanhempi(6) == 2);
		
		
		assertTrue(keko.vasen(0) == 1);
		assertTrue(keko.vasen(1) == 3);
		assertTrue(keko.vasen(2) == 5);
		assertTrue(keko.vasen(5) == 11);
		assertTrue(keko.vasen(8) == 17);
		assertTrue(keko.oikea(0) == 2);
		assertTrue(keko.oikea(1) == 4);
		assertTrue(keko.oikea(2) == 6);
		assertTrue(keko.oikea(5) == 12);
		assertTrue(keko.oikea(8) == 18);
		keko.lisaa(new HuffmanPuuLehti('a',10));
		keko.lisaa(new HuffmanPuuLehti('b', 5));
		keko.lisaa(new HuffmanPuuLehti('ö', 21));
		keko.lisaa(new HuffmanPuuLehti('c', 7));
		keko.lisaa(new HuffmanPuuLehti('d', 6));
		keko.lisaa(new HuffmanPuuLehti('f', 1));
		keko.lisaa(new HuffmanPuuLehti('h', 66));
		
		HuffmanPuuLehti merkki;
		System.out.println("Merkki\tEsiintyvyys");
		while ((merkki = (HuffmanPuuLehti)keko.annaPienin()) != null)
			System.out.println(merkki.annaMerkki() + "\t" + merkki.annaFrekvenssi());
		System.out.println("\n...Toinen iteraatio\n");

		System.out.println("Merkki\tEsiintyvyys");

		keko.lisaa(new HuffmanPuuLehti('a',10));
		keko.lisaa(new HuffmanPuuLehti('b', 5));
		keko.lisaa(new HuffmanPuuLehti('ö', 21));
		keko.lisaa(new HuffmanPuuLehti('c', 7));
		keko.lisaa(new HuffmanPuuLehti('d', 6));
		keko.lisaa(new HuffmanPuuLehti('f', 1));
		keko.lisaa(new HuffmanPuuLehti('h', 66));
		while ((merkki = (HuffmanPuuLehti)keko.annaPienin()) != null)
			System.out.println(merkki.annaMerkki() + "\t" + merkki.annaFrekvenssi());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
