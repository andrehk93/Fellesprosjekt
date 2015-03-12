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
	
	public Avtale(Bruker eier, ArrayList<Bruker> deltakere, TidsIntervall tid, Møterom rom, String avtaleid) throws IOException{
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
	
	public void endreRom(Møterom rom) {
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
	
	//Antar at det legges til én
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
	
	private Property<Møterom> romProperty = new ObjectPropertyBase<Møterom>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "møterom";
		}
		
	};
	
	public Møterom getRom() {
		return romProperty.getValue();
	}
	
	public Møterom finnRom() {
		//Skal spørre databasen om ledige rom i tidspunktet angitt.
		
		
		//Midlertidig løsning
		return romProperty.getValue();
	}
	
	
	public void setRom(Møterom møterom) {
		if (romProperty.getValue() == null) {
			romProperty.setValue(møterom);
		}
		else {
			System.out.println("For å endre møterom må funksjonen ENDREROM benyttes");
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
	
	
	// Antar at det fjernes én om gangen
	public void removeDeltakere(Bruker deltaker) {
		try {
			deltakerProperty.getValue().remove(deltaker);
			//SKAL FJERNE BRUKER FRA AVTALEN I DATABASEN HER
		}
		catch (NullPointerException e) {
		}
		
	}
	
	
	//Antar at det ikke er noen her fra før av
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
