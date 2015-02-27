package klient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Bruker {
	
	public Bruker(){
		
	}
	
	public Bruker(String navn, String email){
		setNavn(navn);
		setEmail(email);
		addListeners();
	}
	
	private Property<ArrayList<Avtale>> avtaleListeProperty = new ObjectPropertyBase<ArrayList<Avtale>>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "Avtaleliste";
		}
		
	};
	
	private Property<ArrayList<Varsel>> varselListeProperty = new ObjectPropertyBase<ArrayList<Varsel>>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "VarselListe";
		}
		
	};
	
	private Property<String> nameProperty = new SimpleStringProperty();
	private Property<String> emailProperty = new SimpleStringProperty();
	
	public String getNavn(){
		return nameProperty.getValue();
	}
	
	public void setNavn(String name) {
		nameProperty.setValue(name);
	}
	
	public Property<String> nameProperty(){
		return nameProperty;
	}
	
	public String getEmail(){
		return emailProperty.getValue();
	}
	
	public void setEmail(String email){
		emailProperty.setValue(email);
	}
	
	public ArrayList<Avtale> getAvtaler() {
		return avtaleListeProperty.getValue();
	}
	
	public void addAvtale(Avtale avtale) {
		boolean duplikat = false;
		if (avtaleListeProperty.getValue() == null) {
			ArrayList<Avtale> denne_avtalen = new ArrayList<Avtale>();
			denne_avtalen.add(avtale);
			avtaleListeProperty.setValue(denne_avtalen);
		}
		else {
			for (Avtale avtaler : avtaleListeProperty.getValue()) {
				if (avtale.equals(avtaler)) {
					duplikat = true;
				}
			}
			if (duplikat) {
				System.out.println("avtalen finnes allerede");
			}
			else {
				avtaleListeProperty.getValue().add(avtale);
			}
			
		}
	}
	
	public void removeAvtale(Avtale avtale) {
		if (avtaleListeProperty.getValue() == null) {
			System.out.println("Du har ingen avtaler");
		}
		else if (! avtale.getAvtaleAdmin().equals(this)) {
			this.deleteAvtale(avtale);
		}
		else {
			try {
				for (int i = 0; i < avtale.getDeltakere().size(); i++) {
					avtale.getDeltakere().get(i).deleteAvtale(avtale);
				}
				avtaleListeProperty.getValue().remove(avtale);
				System.out.println("Avtalen ble fjernet");
			}
			catch (NullPointerException e) {
				System.out.println("Avtalen finnes ikke");
			}
		}
	}
	
	public void giVarsel(Varsel avtVarsel) {
		try {
			boolean duplikat = false;
			for (int i = 0; i < varselListeProperty.getValue().size(); i++) {
				if (varselListeProperty.getValue().get(i).equals(avtVarsel)) {
					duplikat = true;
				}
			}
			if (! duplikat) {
				varselListeProperty.getValue().add(avtVarsel);
				System.out.println("\n" + avtVarsel.getBrukerSendtTil().getNavn() + 
						" har fått et varsel: \n" + avtVarsel.getMelding() +
						"\nSignatur: " + avtVarsel.getBrukerSendtFra().getNavn() + "\n");
			}
			else {
				System.out.println("Varselet er der fra før av.");
			}
		}
		catch (NullPointerException e) {
			ArrayList<Varsel> varselet = new ArrayList<Varsel>();
			varselet.add(avtVarsel);
			varselListeProperty.setValue(varselet);
			System.out.println("\n" + avtVarsel.getBrukerSendtTil().getNavn() + 
					" har fått et varsel: \n" + avtVarsel.getMelding() +
					"\nSignatur: " + avtVarsel.getBrukerSendtFra().getNavn() + "\n");
		}
		
	}
	
	public void deleteAvtale(Avtale avtale) {
		System.out.println("SLETTER DENNE BRUKEREN: " + this.getNavn());
		System.out.println("FØRSTE BRUKER: " + avtale.getDeltakere().get(0).getNavn());
		avtale.removeDeltakere(this);
	}
	
	public void addListeners() {
		ChangeListener<ArrayList<Avtale>> removeAvtaleListener = new ChangeListener<ArrayList<Avtale>>() {

			@Override
			public void changed(
					ObservableValue<? extends ArrayList<Avtale>> arg0,
					ArrayList<Avtale> arg1, ArrayList<Avtale> arg2) {
				if (! arg2.isEmpty() && ! arg2.get(0).getDeltakere().isEmpty()) {
					if (arg1 != null) {
						if (arg1.size() > arg2.size()) {
							System.out.println("En avtale ble slettet");
							for (int i = 0; i < arg2.get(0).getDeltakere().size(); i++) {
								arg2.get(0).getDeltakere().get(i).removeAvtale(arg2.get(0));
							}
						}
						else {
							System.out.println("Det ble lagt til en avtale");
							for (int i = 0; i < arg2.get(0).getDeltakere().size(); i++) {
								arg2.get(0).getDeltakere().get(i).addAvtale(arg2.get(0));
							}
						}
					}
				}
			}
		};
		avtaleListeProperty.addListener(removeAvtaleListener);
	}
	
	
	public static void main(String[] args) {
		Bruker Andreas = new Bruker("Andreas", "a");
		Bruker Martin = new Bruker("Martin", "m");
		Bruker Ch = new Bruker("Ch", "c");
		ArrayList<Bruker> delt = new ArrayList<Bruker>();
		delt.add(Martin);
		delt.add(Ch);
		Møterom rom = new Møterom(20, "GOBI");
		Møterom rom_2 = new Møterom(20, "MOKI");
		TidsIntervall tid = new TidsIntervall(LocalTime.of(12, 15), LocalTime.of(12, 45), LocalDate.of(2015,12,12));
		Avtale avtale_1 = new Avtale(Andreas, delt, tid, rom);
		System.out.println(avtale_1);
		
		
		//TESTER:
		Andreas.addAvtale(avtale_1);
		Andreas.getAvtaler().get(0).setRom(rom_2);
		System.out.println(Andreas.getAvtaler());
		System.out.println("Martins avtaler: " + Martin.getAvtaler());
		System.out.println("Chs avtaler: " + Ch.getAvtaler());
		Ch.removeAvtale(avtale_1);
		System.out.println(avtale_1);
		Martin.removeAvtale(avtale_1);
		System.out.println("Chs avtaler: " + Ch.getAvtaler());
		System.out.println("ANDREAS Avtaler: " + Andreas.getAvtaler());
		System.out.println("MARTINS AVTALER: " + Martin.getAvtaler());
	}
	
	
	
}
