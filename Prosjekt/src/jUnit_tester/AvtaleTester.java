package jUnit_tester;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import klient.Avtale;
import klient.Bruker;

import org.junit.Test;

public class AvtaleTester {

	@Test
	public void test() {
	}
	
	
	//Funker bra denne klassen
	@Test
	public void startAvtale() throws IOException {
		Avtale avtale = new Avtale();
		Bruker bruker = new Bruker();
		bruker.setNavn("Andreas");
		bruker.setEmail("ahk9339@gmail.com");
		bruker.setRights(1);
		avtale.setId("23");
		assertEquals("Skal v�re 23 begge", "23", avtale.getId());
		avtale.setEier(bruker);
		assertEquals("Skal ha samme bruker", bruker, avtale.getEier());
		avtale.setAvtaleNavn("Min avtale");
		assertEquals("Skal v�re samme navn", "Min avtale", avtale.getAvtaleNavn());
		ArrayList<Bruker> deltakere = new ArrayList<Bruker>();
		avtale.setDeltakere(deltakere);
		avtale.addDeltakere(bruker);
		deltakere.add(bruker);
		assertEquals("Skal v�re like", deltakere, avtale.getDeltakere());
		Bruker adminsjekk = new Bruker();
		adminsjekk.setEmail("ADMIN");
		adminsjekk.setNavn("adminis");
		avtale.setEier(adminsjekk);
		assertEquals(bruker, avtale.getEier());
		avtale.addDeltakere(adminsjekk);
		deltakere.add(adminsjekk);
		assertEquals("SKAL V�RE LIKE", deltakere, avtale.getDeltakere());
		avtale.addDeltakere(adminsjekk);
		assertEquals("SKAL V�RE LIKE", deltakere, avtale.getDeltakere());
		avtale.removeDeltakere(adminsjekk);
		deltakere.remove(adminsjekk);
		assertEquals(deltakere, avtale.getDeltakere());
		avtale.removeDeltakere(adminsjekk);
		assertEquals(deltakere, avtale.getDeltakere());
		avtale.removeDeltakere(bruker);
		deltakere.remove(bruker);
		assertEquals(deltakere, avtale.getDeltakere());
		assertEquals(null, avtale.getEier());
	}

}
