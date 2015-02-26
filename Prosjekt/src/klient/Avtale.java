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
	
	public Avtale(Bruker eier, ArrayList<Bruker> deltakere, TidsIntervall tid, Møterom rom, Gruppe gruppe){
		setEier(eier);
		settOppVarsel(this);
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
		if (deltakerProperty.getValue() == null || eierProperty.getValue() == null) {
			ArrayList<Bruker> eieren = new ArrayList<Bruker>();
			eieren.add(eier);
			deltakerProperty.setValue(eieren);
			eierProperty.setValue(eier);
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
				System.out.println("brukeren: " + deltaker.getNavn() + " er i avtalen fra før");
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
				if (deltakerne.equals(deltaker)){
					eksisterer = true;
				}
			}
			if (eksisterer) {
				deltaker.getAvtaler().remove(this);
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
	
	public void settOppVarsel(Avtale avtale) {
		ChangeListener<ArrayList<Bruker>> deltakerVarsel = new ChangeListener<ArrayList<Bruker>>() {

			@Override
			public void changed(
					ObservableValue<? extends ArrayList<Bruker>> observable,
					ArrayList<Bruker> oldValue, ArrayList<Bruker> newValue) {
				System.out.println("Det har skjedd en endring i gruppen: " + newValue.get(0).getNavn());
			}
		};
		deltakerProperty.addListener(deltakerVarsel);
		
		ChangeListener<Møterom> rom = new ChangeListener<Møterom>() {

			@Override
			public void changed(ObservableValue<? extends Møterom> observable,
					Møterom oldValue, Møterom newValue) {
				try {
					String melding = "Avtalen klokken: " + avtale.getTid().getStart()
							+ " - " + avtale.getTid().getSlutt()
							+ " er endret: " + avtale.getAvtaleAdmin().getNavn() + " har endret møterom fra: "
									+ oldValue.getNavn() + " til: " + newValue.getNavn();
					for (int i = 1; i < avtale.getDeltakere().size(); i++) {
						Varsel avtVarsel = new Varsel(melding, avtale.getDeltakere().get(i), avtale.getAvtaleAdmin(), false);
						avtale.getDeltakere().get(i).giVarsel(avtVarsel);
					}
					
				}
				catch (NullPointerException e) {
					String melding = "En avtale er opprettet klokken: " + avtale.getTid().getStart()
							+ " - " + avtale.getTid().getSlutt()
							+ " av: " + avtale.getAvtaleAdmin().getNavn() + 
							" og møterom er satt til: " + newValue.getNavn();
					for (int i = 1; i < avtale.getDeltakere().size(); i++) {
						Varsel avtVarsel = new Varsel(melding, avtale.getDeltakere().get(i), avtale.getAvtaleAdmin(), false);
						avtale.getDeltakere().get(i).giVarsel(avtVarsel);
					}
				}
			}
		};
		romProperty.addListener(rom);
		
		ChangeListener<TidsIntervall> tid = new ChangeListener<TidsIntervall>() {

			@Override
			public void changed(ObservableValue<? extends TidsIntervall> observable,
					TidsIntervall oldValue, TidsIntervall newValue) {
				try {
					String melding = "Avtalen klokken: " + oldValue.getStart() + oldValue.getSlutt()
							+ " er endret til klokkken: " + avtale.getTid().getStart()
							+ " - " + avtale.getTid().getSlutt() + avtale.getAvtaleAdmin().getNavn() + " møterom: "
									+ avtale.getRom().getNavn();
					for (int i = 1; i < avtale.getDeltakere().size(); i++) {
						Varsel avtVarsel = new Varsel(melding, avtale.getDeltakere().get(i), avtale.getAvtaleAdmin(), false);
						avtale.getDeltakere().get(i).giVarsel(avtVarsel);
					}
					
				}
				catch (NullPointerException e) {
					String melding = "En avtale er opprettet klokken: " + avtale.getTid().getStart()
							+ " - " + avtale.getTid().getSlutt()
							+ " av: " + avtale.getAvtaleAdmin().getNavn() + 
							" og møterom er satt til: " + avtale.getRom().getNavn();
					for (int i = 1; i < avtale.getDeltakere().size(); i++) {
						Varsel avtVarsel = new Varsel(melding, avtale.getDeltakere().get(i), avtale.getAvtaleAdmin(), false);
						avtale.getDeltakere().get(i).giVarsel(avtVarsel);
					}
				}
			}
		};
		romProperty.addListener(rom);
		
		
	}
	
	
	public static void main(String[] args) {
		Bruker Andreas = new Bruker("Andreas", "ahk9339@gmail.com");
		Bruker Lars = new Bruker("lars", "l@hotmail.com");
		Bruker jens = new Bruker("jens", "j@hotmail.com");
		Bruker ivar = new Bruker("ivar", "i@hotmail.com");
		ArrayList<Bruker> brukere = new ArrayList<Bruker>();
		brukere.add(Lars);
		brukere.add(jens);
		TidsIntervall tiden = new TidsIntervall(LocalTime.of(10, 15), LocalTime.of(11, 15), LocalDate.of(2015, 12, 12));
		Møterom rommet = new Møterom(20, "Gobi");
		Møterom rom_2 = new Møterom(20, "Moki");
		rommet.setOpptatt(tiden);
		TidsIntervall tid = new TidsIntervall(LocalTime.of(10, 15), LocalTime.of(11, 15), LocalDate.of(2015, 12, 12));
		TidsIntervall tid_2 = new TidsIntervall(LocalTime.of(11, 15), LocalTime.of(12, 15), LocalDate.of(2015, 12, 12));
		Avtale møte = new Avtale(Andreas, brukere, tid, rommet, new Gruppe());
		Andreas.addAvtale(møte);
		
		//Tester:
		System.out.println("ANDREAS SINE AVTALER: " + Andreas.getAvtaler());
		møte.setEier(jens);
		møte.removeDeltakere(jens);
		møte.setTid(tid_2);
		møte.setRom(rom_2);
		møte.endreRom(rommet);
		System.out.println("LARS SINE AVTALER: " + Lars.getAvtaler());
		System.out.println("Andreas: " + Andreas.getAvtaler());
		System.out.println("IVAR SINE AVTALER: " + ivar.getAvtaler());
	}
	
	
}
