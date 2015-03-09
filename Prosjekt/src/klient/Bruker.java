package klient;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;

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
	
	
	public void addAvtale(Avtale avtale) throws IOException {
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
	
	public void inviterTilNyAvtale(Avtale avtale) throws IOException {
		Klienten.inviterDeltaker(this.getEmail(), avtale.getAvtaleid());
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
			}
			else {
				System.out.println("Varselet er der fra før av.");
			}
		}
		catch (NullPointerException e) {
			ArrayList<Varsel> varselet = new ArrayList<Varsel>();
			varselet.add(avtVarsel);
			varselListeProperty.setValue(varselet);
		}
		
	}
	
	public void deleteAvtale(Avtale avtale) {
		System.out.println("SLETTER DENNE BRUKEREN: " + this.getNavn());
		System.out.println("FØRSTE BRUKER: " + avtale.getDeltakere().get(0).getNavn());
		avtale.removeDeltakere(this);
	}
	
	public void addListeners() {
	}
	
	
	
}
