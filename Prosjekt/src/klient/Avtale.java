package klient;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Avtale {
	
	public Avtale() {
		
	}
	
	public Avtale(Bruker eier, ArrayList<Bruker> deltakere, TidsIntervall tid, M�terom rom, Gruppe gruppe){
		setEier(eier);
		ArrayList<Bruker> lagtTilDeltakere = new ArrayList<Bruker>();
		ChangeListener<ArrayList<Bruker>> deltakerListener = new ChangeListener<ArrayList<Bruker>>() {

			@Override
			public void changed(
					ObservableValue<? extends ArrayList<Bruker>> observable,
					ArrayList<Bruker> oldValue, ArrayList<Bruker> newValue) {
				for (int i = 0; i < newValue.size(); i++) {
					System.out.println("brukere: " + newValue.get(i).getNavn());
					addDeltakere(newValue.get(i));
				}
			}
		};
		deltakerProperty.addListener(deltakerListener);
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
		ChangeListener<Bruker> eierListener = new ChangeListener<Bruker>() {

			@Override
			public void changed(ObservableValue<? extends Bruker> observable,
					Bruker oldValue, Bruker newValue) {
				System.out.println(newValue.getNavn() + " er n� admin");
			}
			
		};
		eierProperty.addListener(eierListener);
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
	
	
	//Antar at det legges til �n
	public void addDeltakere(Bruker deltaker) {
		boolean duplikat = false;
		if (deltakerProperty.getValue() != null) {
			for (Bruker deltakere : deltakerProperty.getValue()) {
				if (deltaker.equals(deltakere)) {
					duplikat = true;
				}
			}
			if (duplikat) {
				System.out.println("brukeren er i avtalen fra f�r");
			}
			else {
				deltakerProperty.getValue().add(deltaker);
				deltaker.addAvtale(this);
			}
		}
		else {
			ArrayList<Bruker> deltakere = new ArrayList<Bruker>();
			deltakere.add(deltaker);
			deltakerProperty.setValue(deltakere);
			deltaker.addAvtale(this);
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
	
	public Gruppe getGruppe() {
		return gruppeProperty.getValue();
	}
	
	public void setGruppe(Gruppe gruppe) {
		gruppeProperty.setValue(gruppe);
	}
	
	public M�terom getRom() {
		return romProperty.getValue();
	}
	
	
	// Dette vil v�re den s�kalte "reservere m�terom"-klassen
	public void setRom(M�terom m�terom) {
		//finne ledige m�terom i database
		romProperty.setValue(m�terom);
	}
	
	public ArrayList<Bruker> getDeltakere() {
		return deltakerProperty.getValue();
	}
	
	
	// Antar at det fjernes �n om gangen
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
			System.out.println("Det er ingen deltakere for �yeblikket.");
		}
	}
	
	
	//Antar at det ikke er noen her fra f�r av
	public void setDeltakere(ArrayList<Bruker> deltakere) {
		for (Bruker deltaker : deltakere) {
			addDeltakere(deltaker);
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
		M�terom rommet = new M�terom(20, "Gobi");
		rommet.setOpptatt(tiden);
		TidsIntervall tid = new TidsIntervall(LocalTime.of(10, 15), LocalTime.of(11, 15), LocalDate.of(2015, 12, 12));
		Avtale m�te = new Avtale(Andreas, brukere, tid, rommet, new Gruppe());
		m�te.setEier(Lars);
		m�te.setEier(jens);
		m�te.addDeltakere(ivar);
		System.out.println("LARS SINE AVTALER: " + Lars.getAvtaler());
	}
	
	
}
