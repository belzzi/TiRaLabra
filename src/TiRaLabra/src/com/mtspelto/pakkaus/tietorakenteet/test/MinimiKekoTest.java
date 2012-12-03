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

/** Yksikkötesti Minimithis-luokalle.
 * 
 * @author mikkop
 *
 */
public class MinimiKekoTest extends MinimiKeko {

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
			
		//Minimithis this = new Minimithis();
		
		assertTrue(this.vanhempi(0) == -1);
		assertTrue(this.vanhempi(1) == 0);
		assertTrue(this.vanhempi(2) == 0);
		assertTrue(this.vanhempi(3) == 1);
		assertTrue(this.vanhempi(4) == 1);
		assertTrue(this.vanhempi(5) == 2);
		assertTrue(this.vanhempi(6) == 2);
		
		
		assertTrue(this.vasen(0) == 1);
		assertTrue(this.vasen(1) == 3);
		assertTrue(this.vasen(2) == 5);
		assertTrue(this.vasen(5) == 11);
		assertTrue(this.vasen(8) == 17);
		assertTrue(this.oikea(0) == 2);
		assertTrue(this.oikea(1) == 4);
		assertTrue(this.oikea(2) == 6);
		assertTrue(this.oikea(5) == 12);
		assertTrue(this.oikea(8) == 18);
		this.lisaa(new HuffmanPuuLehti('a',10));
		this.lisaa(new HuffmanPuuLehti('b', 5));
		this.lisaa(new HuffmanPuuLehti('ö', 21));
		this.lisaa(new HuffmanPuuLehti('c', 7));
		this.lisaa(new HuffmanPuuLehti('d', 6));
		this.lisaa(new HuffmanPuuLehti('f', 1));
		this.lisaa(new HuffmanPuuLehti('h', 66));
		
		HuffmanPuuLehti merkki;
		System.out.println("Merkki\tEsiintyvyys");
		while ((merkki = (HuffmanPuuLehti)this.annaPienin()) != null)
			System.out.println(merkki.annaMerkki() + "\t" + merkki.annaFrekvenssi());
		System.out.println("\n...Toinen iteraatio\n");

		System.out.println("Merkki\tEsiintyvyys");

		this.lisaa(new HuffmanPuuLehti('a',10));
		this.lisaa(new HuffmanPuuLehti('b', 5));
		this.lisaa(new HuffmanPuuLehti('ö', 21));
		this.lisaa(new HuffmanPuuLehti('c', 7));
		this.lisaa(new HuffmanPuuLehti('d', 6));
		this.lisaa(new HuffmanPuuLehti('f', 1));
		this.lisaa(new HuffmanPuuLehti('h', 66));
		while ((merkki = (HuffmanPuuLehti)this.annaPienin()) != null)
			System.out.println(merkki.annaMerkki() + "\t" + merkki.annaFrekvenssi());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

}
