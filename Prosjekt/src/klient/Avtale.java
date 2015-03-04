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
	
	public Avtale(Bruker eier, ArrayList<Bruker> deltakere, TidsIntervall tid, M�terom rom) throws IOException{
		setEier(eier);
		//settOppVarsel(this);
		setDeltakere(deltakere);
		setTid(tid);
		setRom(rom);
		lagVarsel(this);
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
	public void addDeltakere(Bruker deltaker) {
		try {
			for (Bruker bruker : gruppeProperty.getValue().getMedlemmer()) {
				gruppeProperty.getValue().addMedlem(deltaker);
				if (! deltaker.equals(eierProperty.getValue())) {
					Klienten.leggTilAvtale(bruker.getEmail(), avtaleid);
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
			return gruppeProperty.getValue().getMedlemmer();
		}
		catch (NullPointerException e) {
			System.out.println("LOL tom liste");
			ArrayList<Bruker> nan = new ArrayList<Bruker>();
			nan.add(new Bruker("Andreas", "ahk9339@gmail.com"));
			return nan;
		}
	}
	
	
	// Antar at det fjernes �n om gangen
	public void removeDeltakere(Bruker deltaker) {
		try {
			gruppeProperty.getValue().removeMedlem(deltaker);
		}
		catch (NullPointerException e) {
		}
		
	}
	
	
	//Antar at det ikke er noen her fra f�r av
	public void setDeltakere(ArrayList<Bruker> deltakere) {
		try {
			for (int i = 0; i < deltakere.size(); i++) {
				gruppeProperty.getValue().addMedlem(deltakere.get(i));
			}
		}
		catch (NullPointerException e) {
			Gruppe gruppa = new Gruppe(this, deltakere);
			gruppeProperty.setValue(gruppa);
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
		
		//Lager listeners for M�terom-klassen
		ChangeListener<M�terom> rom = new ChangeListener<M�terom>() {

			@Override
			public void changed(ObservableValue<? extends M�terom> observable,
					M�terom oldValue, M�terom newValue) {
				try {
					if (! oldValue.equals(newValue)) {
						String melding = "En avtale er endret: \nKlokken: " + avtale.getTid().getStart()
								+ " - " + avtale.getTid().getSlutt() + "\nDato: " + avtale.getTid().getDato()
								+ " \nAdmin: " + avtale.getAvtaleAdmin().getNavn() + 
								" \nTidligere m�terom: " + oldValue.getNavn() + "\nNytt m�terom: " + avtale.getRom().getNavn();
						for (int i = 1; i < avtale.getDeltakere().size(); i++) {
							Varsel avtVarsel = new Varsel(melding, avtale.getDeltakere().get(i), avtale.getAvtaleAdmin(), false, avtale);
							avtale.getDeltakere().get(i).giVarsel(avtVarsel);
						}
					}
					
				}
				catch (NullPointerException | IOException e) {
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
								" \nM�terom: " + avtale.getRom().getNavn();
						for (int i = 1; i < avtale.getDeltakere().size(); i++) {
							Varsel avtVarsel = new Varsel(melding, avtale.getDeltakere().get(i), avtale.getAvtaleAdmin(), false, avtale);
							avtale.getDeltakere().get(i).giVarsel(avtVarsel);
						}
					}
					
				}
				catch (NullPointerException | IOException e) {
				}
			}
		};
		tidsProperty.addListener(tid);
	}
	
	public void lagVarsel(Avtale avtale) throws IOException {
		String melding = "En avtale er opprettet: klokken: " + avtale.getTid().getStart()
				+ " - " + avtale.getTid().getSlutt() + "dato: " + avtale.getTid().getDato()
				+ " av: " + avtale.getAvtaleAdmin().getNavn() + 
				" m�terom: " + avtale.getRom().getNavn();
		for (int i = 1; i < avtale.getDeltakere().size(); i++) {
			Varsel avtVarsel = new Varsel(melding, avtale.getDeltakere().get(i), avtale.getAvtaleAdmin(), false, this);
			//avtale.getDeltakere().get(i).giVarsel(avtVarsel);
		}
	}
	
	
	
}
