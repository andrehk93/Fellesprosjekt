package klient;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Avtale {
	
	
	String avtaleid;
	
	public Avtale() {
		
	}
	
	public Avtale(Bruker eier, ArrayList<Bruker> deltakere, TidsIntervall tid, Møterom rom, String avtaleid) throws IOException{
		System.out.println("Gjestene FORTSATT: " + deltakere);
		this.avtaleid = avtaleid;
		setEier(eier);
		setDeltakere(deltakere);
		setTid(tid);
		setRom(rom);
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
		if (gruppeProperty.getValue() == null || eierProperty.getValue() == null) {
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
			for (Bruker bruker : gruppeProperty.getValue().getMedlemmer()) {
				gruppeProperty.getValue().addMedlem(deltaker);
				if (! deltaker.equals(eierProperty.getValue())) {
					Klienten.leggTilAvtale(bruker.getEmail(), avtaleid);
					deltaker.addAvtale(this);
				}
			}
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
	
	private Property<Gruppe> gruppeProperty = new ObjectPropertyBase<Gruppe>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "gruppe";
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
	
	public Gruppe getGruppe() {
		return gruppeProperty.getValue();
	}
	
	public void setGruppe(Gruppe gruppe) {
		gruppeProperty.setValue(gruppe);
	}
	
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
			return gruppeProperty.getValue().getMedlemmer();
		}
		catch (NullPointerException e) {
			System.out.println("LOL tom liste");
			ArrayList<Bruker> nan = new ArrayList<Bruker>();
			nan.add(new Bruker("Andreas", "ahk9339@gmail.com"));
			return nan;
		}
	}
	
	
	// Antar at det fjernes én om gangen
	public void removeDeltakere(Bruker deltaker) {
		try {
			gruppeProperty.getValue().removeMedlem(deltaker);
		}
		catch (NullPointerException e) {
		}
		
	}
	
	
	//Antar at det ikke er noen her fra før av
	public void setDeltakere(ArrayList<Bruker> deltakere) throws IOException {
		try {
			for (int i = 0; i < deltakere.size(); i++) {
				gruppeProperty.getValue().addMedlem(deltakere.get(i));
				deltakere.get(i).addAvtale(this);
			}
		}
		catch (NullPointerException e) {
			Gruppe gruppa = new Gruppe(this, deltakere);
			gruppeProperty.setValue(gruppa);
			for (int i = 0; i < deltakere.size(); i++) {
				deltakere.get(i).addAvtale(this);
			}
		}
	}
	
	public String toString() {
		String streng = "";
		streng += "Admin: " + this.getAvtaleAdmin().getNavn() + "\n";
		streng += "Deltakere: \n\n";
		for (int i = 0; i < this.getDeltakere().size(); i++) {
			streng += this.getDeltakere().get(i).getNavn() + "\n";
		}
		streng += "\nRom: " + this.getRom().getNavn() + "\n";
		streng += "Tid: " + this.getTid().getStart() + " - " + this.getTid().getSlutt() + "\n";
		return streng;
	}
	
	
}
