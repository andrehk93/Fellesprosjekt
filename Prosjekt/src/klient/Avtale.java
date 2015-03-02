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
	
	public Avtale() {
		
	}
	
	public Avtale(Bruker eier, ArrayList<Bruker> deltakere, TidsIntervall tid, Møterom rom) throws IOException{
		setEier(eier);
		settOppVarsel(this);
		setDeltakere(deltakere);
		setTid(tid);
		setRom(rom);
		lagVarsel(this);
		Klienten.lagAvtale(tid, rom);
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
		if (gruppeProperty.getValue() == null || eierProperty.getValue() == null) {
			addDeltakere(eier);
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
	
	//Antar at det legges til én
	public void addDeltakere(Bruker deltaker) {
		boolean duplikat = false;
		if (gruppeProperty.getValue() != null) {
			for (Bruker deltakere : gruppeProperty.getValue().getMedlemmer()) {
				if (deltaker.equals(deltakere)) {
					duplikat = true;
				}
			}
			if (duplikat) {
				System.out.println("brukeren: " + deltaker.getNavn() + " er i avtalen fra før");
			}
			else {
				gruppeProperty.getValue().addMedlem(deltaker);
				deltaker.addAvtale(this);
			}
		}
		else {
			ArrayList<Bruker> deltakeren = new ArrayList<Bruker>();
			deltakeren.add(deltaker);
			Gruppe gruppen = new Gruppe(this, deltakeren);
			gruppeProperty.setValue(gruppen);
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
		if (romProperty.getValue() == null) {
			romProperty.setValue(møterom);
		}
		else {
			System.out.println("For å endre møterom må funksjonen ENDREROM benyttes");
		}
	}
	
	public ArrayList<Bruker> getDeltakere() {
		return gruppeProperty.getValue().getMedlemmer();
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
	public void setDeltakere(ArrayList<Bruker> deltakere) {
		try {
			for (int i = 0; i < deltakere.size(); i++) {
				gruppeProperty.getValue().addMedlem(deltakere.get(i));
			}
		}
		catch (NullPointerException e) {
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
		
		//Lager listeners for Møterom-klassen
		ChangeListener<Møterom> rom = new ChangeListener<Møterom>() {

			@Override
			public void changed(ObservableValue<? extends Møterom> observable,
					Møterom oldValue, Møterom newValue) {
				try {
					if (! oldValue.equals(newValue)) {
						String melding = "En avtale er endret: \nKlokken: " + avtale.getTid().getStart()
								+ " - " + avtale.getTid().getSlutt() + "\nDato: " + avtale.getTid().getDato()
								+ " \nAdmin: " + avtale.getAvtaleAdmin().getNavn() + 
								" \nTidligere møterom: " + oldValue.getNavn() + "\nNytt møterom: " + avtale.getRom().getNavn();
						for (int i = 1; i < avtale.getDeltakere().size(); i++) {
							Varsel avtVarsel = new Varsel(melding, avtale.getDeltakere().get(i), avtale.getAvtaleAdmin(), false);
							avtale.getDeltakere().get(i).giVarsel(avtVarsel);
						}
					}
					
				}
				catch (NullPointerException e) {
				}
			}
		};
		romProperty.addListener(rom);
		
		
		//Lager varsel for endring/oppretting av tidspunkt
		ChangeListener<TidsIntervall> tid = new ChangeListener<TidsIntervall>() {

			@Override
			public void changed(ObservableValue<? extends TidsIntervall> observable,
					TidsIntervall oldValue, TidsIntervall newValue) {
				try {
					if (! oldValue.equals(newValue)) {
						//Hvis den nye eventen ikke er det samme som forrige
						String melding = "En avtale er endret: \nTidligere klokken: " + oldValue.getStart()
								+ " - " + oldValue.getSlutt() + "\nNytt tidspunkt: " + avtale.getTid().getStart()
								+ " - " + avtale.getTid().getSlutt() + "\nDato: " + avtale.getTid().getDato()
								+ " \nAdmin: " + avtale.getAvtaleAdmin().getNavn() + 
								" \nMøterom: " + avtale.getRom().getNavn();
						for (int i = 1; i < avtale.getDeltakere().size(); i++) {
							Varsel avtVarsel = new Varsel(melding, avtale.getDeltakere().get(i), avtale.getAvtaleAdmin(), false);
							avtale.getDeltakere().get(i).giVarsel(avtVarsel);
						}
					}
					
				}
				catch (NullPointerException e) {
				}
			}
		};
		tidsProperty.addListener(tid);
	}
	
	public void lagVarsel(Avtale avtale) {
		String melding = "En avtale er opprettet: \nklokken: " + avtale.getTid().getStart()
				+ " - " + avtale.getTid().getSlutt() + "\ndato: " + avtale.getTid().getDato()
				+ " \nav: " + avtale.getAvtaleAdmin().getNavn() + 
				" \nmøterom: " + avtale.getRom().getNavn();
		for (int i = 1; i < avtale.getDeltakere().size(); i++) {
			Varsel avtVarsel = new Varsel(melding, avtale.getDeltakere().get(i), avtale.getAvtaleAdmin(), false);
			avtale.getDeltakere().get(i).giVarsel(avtVarsel);
		}
	}
	
	
	
}
