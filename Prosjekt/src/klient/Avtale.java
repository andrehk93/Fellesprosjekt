package klient;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;

public class Avtale {
	
	public Avtale() {
		
	}
	
	public Avtale(Bruker eier, ArrayList<Bruker> deltakere, TidsIntervall tid, Møterom rom, Gruppe gruppe){
		setEier(eier);
		setDeltakere(deltakere);
		setTid(tid);
		setRom(rom);
		setGruppe(gruppe);
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
	
	public void setEier(Bruker eier) {
		eierProperty.setValue(eier);
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
	
	private Property<ArrayList<Bruker>> deltakerProperty = new ObjectPropertyBase<ArrayList<Bruker>>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "deltaker";
		}
		
	};
	
	
	//Antar at det legges til én
	public void addDeltakere(Bruker deltaker) {
		boolean duplikat = false;
		if (deltakerProperty.getValue() != null) {
			for (Bruker deltakere : deltakerProperty.getValue()) {
				if (deltaker.equals(deltakere)) {
					duplikat = true;
				}
			}
			if (duplikat) {
				System.out.println("brukeren er i avtalen fra før");
			}
			else {
				deltakerProperty.getValue().add(deltaker);
			}
		}
		else {
			deltakerProperty.getValue().add(deltaker);
			System.out.println("Legger ikke til deltakere");
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
	
	
	// Dette vil være den såkalte "reservere møterom"-klassen
	public void setRom(Møterom møterom) {
		//finne ledige møterom i database
		romProperty.setValue(møterom);
	}
	
	public ArrayList<Bruker> getDeltakere() {
		return deltakerProperty.getValue();
	}
	
	
	// Antar at det fjernes én om gangen
	public void removeDeltakere(Bruker deltaker) {
		boolean eksisterer = false;
		if (deltakerProperty.getValue() != null) {
			for (Bruker deltakerne : deltakerProperty.getValue()) {
				if (! deltakerne.equals(deltaker)){
					eksisterer = true;
				}
			}
			if (eksisterer) {
				deltakerProperty.getValue().remove(deltaker);
				System.out.println("Brukeren er fjernet.");
			}
			else {
				System.out.println("Brukeren er ikke deltakende.");
			}
		}
		else {
			System.out.println("Det er ingen deltakere for øyeblikket.");
		}
	}
	
	
	//Antar at det ikke er noen her fra før av
	public void setDeltakere(ArrayList<Bruker> deltakere) {
		deltakerProperty.setValue(deltakere);
	}
	
	public String toString() {
		String streng = "";
		streng += "Admin: " + this.getAvtaleAdmin().getName() + "\n";
		streng += "Deltakere: \n\n";
		for (int i = 0; i < this.getDeltakere().size(); i++) {
			streng += this.getDeltakere().get(i).getName() + "\n";
		}
		streng += "\nRom: " + this.getRom().getNavn() + "\n";
		streng += "Tid: " + this.getTid().getStart() + " - " + this.getTid().getSlutt() + "\n";
		return streng;
	}
	
	public static void main(String[] args) {
		Bruker Andreas = new Bruker("Andreas", "ahk9339@gmail.com");
		Bruker Lars = new Bruker("lars", "l@hotmail.com");
		Bruker jens = new Bruker("jens", "j@hotmail.com");
		Bruker ivar = new Bruker("ivar", "i@hotmail.com");
		ArrayList<Bruker> brukere = new ArrayList<Bruker>();
		brukere.add(Lars);
		brukere.add(jens);
		brukere.add(ivar);
		TidsIntervall tiden = new TidsIntervall(LocalTime.of(10, 15), LocalTime.of(11, 15), LocalDate.of(2015, 12, 12));
		Møterom rommet = new Møterom(20, "Gobi");
		rommet.setOpptatt(tiden);
		TidsIntervall tid = new TidsIntervall(LocalTime.of(10, 15), LocalTime.of(11, 15), LocalDate.of(2015, 12, 12));
		Avtale møte = new Avtale(Andreas, brukere, tid, rommet, new Gruppe());
		System.out.println(møte.toString());
	}
	
	
}
