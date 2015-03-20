package jUnit_tester;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import klient.Avtale;
import klient.Bruker;

import org.junit.Test;

public class BrukerTester {

	
	//fungerer også greit
	@Test
	public void test() throws IOException {
		Bruker bruker = new Bruker();
		bruker.setEmail("ahk9339@gmail.com");
		bruker.setNavn("Andreas");
		bruker.setRights(1);
		assertEquals("ahk9339@gmail.com", bruker.getEmail());
		assertEquals("Andreas", bruker.getNavn());
		assertEquals(1, bruker.getRights());
		assertEquals(null, bruker.getAvtaler());
		Avtale avt = new Avtale();
		bruker.addAvtale(avt);
		assertEquals(avt, bruker.getAvtaler().get(0));
		bruker.removeAvtale(avt);
		assertEquals(new ArrayList<Avtale>(), bruker.getAvtaler());
	}

}
