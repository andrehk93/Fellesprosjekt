package klient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Avtale {
	
	
	private String avtaleid;
	private String avtalenavn;
	private boolean stranger;
	
	public Avtale() {
		
	}
	
	public Avtale(Bruker eier, ArrayList<Bruker> deltakere, TidsIntervall tid, Møterom rom, String avtaleid) throws IOException{
		this.avtaleid = avtaleid;
		getAvtaleNavn();
		setEier(eier);
		setDeltakere(deltakere);
		setTid(tid);
		setRom(rom);
		setStranger(false);
	}
	
	private void removeEier() {
		eierProperty.setValue(null);
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
		if (eierProperty.getValue() == null) {
			eierProperty.setValue(eier);
			addDeltakere(eier);
			eier.addAvtale(this);
		}
	}
	
	public void endreRom(Møterom rom) {
		romProperty.setValue(rom);
	}
	
	public void endreTid(TidsIntervall tid) {
		tidsProperty.setValue(tid);
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
			if (! deltaker.equals(eierProperty.getValue())) {
				for (Bruker delt : deltakerProperty.getValue()) {
					if (delt.getEmail().equals(deltaker.getEmail())) {
						return;
					}
				}
				deltakerProperty.getValue().add(deltaker);
				Klienten.leggTilAvtale(deltaker.getEmail(), avtaleid);
				deltaker.addAvtale(this);
			}
			else {
				for (Bruker delt : deltakerProperty.getValue()) {
					if (delt.getEmail().equals(deltaker.getEmail())) {
						System.out.println("ikke legg til");
						return;
					}
				}
				System.out.println("dessverre legg til");
				deltakerProperty.getValue().add(deltaker);
			}
		}
		catch (NullPointerException e) {
			ArrayList<Bruker> deltakeren = new ArrayList<Bruker>();
			if (! deltaker.equals(eierProperty.getValue())) {
				deltakeren.add(deltaker);
				deltakerProperty.setValue(deltakeren);
				Klienten.leggTilAvtale(deltaker.getEmail(), avtaleid);
				deltaker.addAvtale(this);
			}
			else {
				deltakeren.add(deltaker);
				deltakerProperty.setValue(deltakeren);
			}
		}
	}
	
	public void setTid(TidsIntervall tid) {
		tidsProperty.setValue(tid);
	}
	
	public TidsIntervall getTid() {
		return tidsProperty.getValue();
	}
	
	public Bruker getEier() {
		return eierProperty.getValue();
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
	
	public void setRom(Møterom møterom) {
		if (romProperty.getValue() == null) {
			romProperty.setValue(møterom);
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
	public void removeDeltakere(Bruker deltaker) throws IOException {
		try {
			deltakerProperty.getValue().remove(deltaker);
			Klienten.deleteAttendant(avtaleid, deltaker.getEmail());
			if (deltakerProperty.getValue().isEmpty()) {
				removeEier();
			}
		}
		catch (NullPointerException e) {
			System.out.println("NULLPOINTER");
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
	
	public boolean getStranger(){
		return this.stranger;
	}
	
	public void setStranger(boolean b){
		this.stranger = b;
	}
	
	public String toString() {
		String streng = avtaleid.trim();
		return streng;
	}
	
	
}
