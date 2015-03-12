package klient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;

public class Avtale {
	
	
	private String avtaleid;
	private String avtalenavn;
	
	public Avtale() {
		
	}
	
	public Avtale(Bruker eier, ArrayList<Bruker> deltakere, TidsIntervall tid, M�terom rom, String avtaleid) throws IOException{
		this.avtaleid = avtaleid;
		getAvtaleNavn();
		setEier(eier);
		setDeltakere(deltakere);
		setTid(tid);
		setRom(rom);
	}
	
	public String getAvtaleid() {
		return avtaleid;
	}
	
	public void setAvtaleNavn() throws IOException {
		avtalenavn = Klienten.getAppNavn(avtaleid);
	}
	
	public void setAvtaleNavn(String navn) throws IOException {
		avtalenavn = navn;
	}
	
	public String getAvtaleNavn() {
		return avtalenavn;
	}
	
	private Property<String> idProperty = new ObjectPropertyBase<String>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "id";
		}
		
	};
	
	public void setId(String id){
		idProperty.setValue(id);
	}
	
	public String getId() {
		return idProperty.getValue();
	}
	
	private Property<Bruker> eierProperty = new ObjectPropertyBase<Bruker>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "eier";
		}
		
	};
	
	public void setEier(Bruker eier) throws IOException {
		if (deltakerProperty.getValue() == null || eierProperty.getValue() == null) {
			eierProperty.setValue(eier);
			addDeltakere(eier);
			eier.addAvtale(this);
		}
		else {
			System.out.println("Denne avtalen har en eier allerede");
		}
	}
	
	public void endreRom(M�terom rom) {
		romProperty.setValue(rom);
	}
	
	public void endreTid(TidsIntervall tid) {
		tidsProperty.setValue(tid);
		finnRom();
	}
	
	private Property<TidsIntervall> tidsProperty = new ObjectPropertyBase<TidsIntervall>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "tid";
		}
		
	};
	
	//Antar at det legges til �n
	public void addDeltakere(Bruker deltaker) throws IOException {
		try {
			deltakerProperty.getValue().add(deltaker);
			/*for (Bruker bruker : deltakerProperty.getValue()) {
				deltakerProperty.getValue().add(deltaker);
				if (! deltaker.equals(eierProperty.getValue())) {
					Klienten.leggTilAvtale(bruker.getEmail(), avtaleid);
					deltaker.addAvtale(this);
				}
			}*/
		}
		catch (NullPointerException e) {
			
		}
	}
	
	public void setTid(TidsIntervall tid) {
		tidsProperty.setValue(tid);
	}
	
	public TidsIntervall getTid() {
		return tidsProperty.getValue();
	}
	
	public Bruker getAvtaleAdmin() {
		return eierProperty.getValue();
	}
	
	public void setAvtaleAdmin(Bruker eier) {
		eierProperty.setValue(eier);
	}
	
	private Property<ArrayList<Bruker>> deltakerProperty = new ObjectPropertyBase<ArrayList<Bruker>>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "deltakere";
		}
		
	};
	
	private Property<M�terom> romProperty = new ObjectPropertyBase<M�terom>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "m�terom";
		}
		
	};
	
	public M�terom getRom() {
		return romProperty.getValue();
	}
	
	public M�terom finnRom() {
		//Skal sp�rre databasen om ledige rom i tidspunktet angitt.
		
		
		//Midlertidig l�sning
		return romProperty.getValue();
	}
	
	
	public void setRom(M�terom m�terom) {
		if (romProperty.getValue() == null) {
			romProperty.setValue(m�terom);
		}
		else {
			System.out.println("For � endre m�terom m� funksjonen ENDREROM benyttes");
		}
	}
	
	public ArrayList<Bruker> getDeltakere() {
		try {
			return deltakerProperty.getValue();
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	
	// Antar at det fjernes �n om gangen
	public void removeDeltakere(Bruker deltaker) {
		try {
			deltakerProperty.getValue().remove(deltaker);
			//SKAL FJERNE BRUKER FRA AVTALEN I DATABASEN HER
		}
		catch (NullPointerException e) {
		}
		
	}
	
	
	//Antar at det ikke er noen her fra f�r av
	public void setDeltakere(ArrayList<Bruker> deltakere) throws IOException {
		try {
			for (int i = 0; i < deltakere.size(); i++) {
				deltakerProperty.getValue().add(deltakere.get(i));
				deltakere.get(i).addAvtale(this);
			}
		}
		catch (NullPointerException e) {
			deltakerProperty.setValue(deltakere);
			for (int i = 0; i < deltakere.size(); i++) {
				deltakere.get(i).addAvtale(this);
			}
		}
	}
	
	public String toString() {
		String streng = avtaleid;
		return streng;
	}
	
	
}
