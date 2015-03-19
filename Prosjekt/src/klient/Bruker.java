package klient;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Bruker {
	
	public Bruker(){
		
	}
	
	
	public Bruker(String navn, String email, Number rights){
		setNavn(navn.trim());
		setEmail(email.trim());
		setRights(rights);
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
	private Property<Number> rightsProperty = new SimpleIntegerProperty();
	
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
	
	public Number getRighs(){
		return rightsProperty.getValue();
	}
	
	public void setRights(Number rights){
		rightsProperty.setValue(rights);
	}
	
	
	public void addAvtale(Avtale avtale) throws IOException {
		if (avtaleListeProperty.getValue() == null) {
			ArrayList<Avtale> denne_avtalen = new ArrayList<Avtale>();
			denne_avtalen.add(avtale);
			avtaleListeProperty.setValue(denne_avtalen);
		}
		else {
			avtaleListeProperty.getValue().add(avtale);
		}
	}
	
	public void inviterTilNyAvtale(Avtale avtale) throws IOException {
		Klienten.inviterDeltaker(this.getEmail(), avtale.getAvtaleid());
		avtale.addDeltakere(this);
	}
	
	public void removeAvtale(Avtale avtale) throws IOException {
		if (avtaleListeProperty.getValue() == null) {
		}
		else {
			try {
				if (! avtale.getEier().equals(this)) {
					deleteAvtale(avtale);
				}
			}
			catch (NullPointerException e) {
				
			}
			try {
				for (int i = 0; i < avtale.getDeltakere().size(); i++) {
					avtale.getDeltakere().get(i).deleteAvtale(avtale);
				}
				avtaleListeProperty.getValue().remove(avtale);
			}
			catch (NullPointerException e) {
				avtaleListeProperty.getValue().remove(avtale);
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
	
	public void deleteAvtale(Avtale avtale) throws IOException {
		avtale.removeDeltakere(this);
	}
	
	public void addListeners() {
	}
	
	public String toString(){
		return getNavn() + " (" + getEmail().trim() + ")";
	}
	
	
}
