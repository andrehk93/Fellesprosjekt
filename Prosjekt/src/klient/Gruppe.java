package klient;

import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;

public class Gruppe {
	
	public Gruppe() {
		
	}
	
	public Gruppe(Avtale avtale, ArrayList<Bruker> medlemmer) {
		setAvtale(avtale);
		setMedlemmer(medlemmer);
	}
	
	public void setMedlemmer(ArrayList<Bruker> medlemmer) {
		medlemListeProperty.setValue(medlemmer);
	}
	
	private Property<ArrayList<Bruker>> medlemListeProperty = new ObjectPropertyBase<ArrayList<Bruker>>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "Medlemliste";
		}
		
	};
	
	private Property<Avtale> avtaleProperty = new ObjectPropertyBase<Avtale>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "Avtale";
		}
		
	};
	
	public ArrayList<Bruker> getMedlemmer() {
		return medlemListeProperty.getValue();
	}
	
	public Property<ArrayList<Bruker>> getMedlemListeProperty() {
		return medlemListeProperty;
	}
	
	public void addMedlem(Bruker medlem) {
		boolean duplikat = false;
		try {
			for (int i = 0; i < medlemListeProperty.getValue().size(); i++) {
				if (medlem.equals(medlemListeProperty.getValue().get(i))) {
					duplikat = true;
				}
			}
			if (duplikat) {
				System.out.println("Brukeren: " + medlem.getNavn() + " er allerede medlem av gruppen");
			}
			else {
				medlemListeProperty.getValue().add(medlem);
				medlem.addAvtale(avtaleProperty.getValue());
			}
		}
		catch (NullPointerException e) {
			ArrayList<Bruker> medlemmet = new ArrayList<Bruker>();
			medlemmet.add(medlem);
			medlemListeProperty.setValue(medlemmet);
		}
		medlem.addAvtale(avtaleProperty.getValue());
	}
	
	public void removeMedlem(Bruker medlem) {
		boolean duplikat = false;
		try {
			for (int i = 0; i < medlemListeProperty.getValue().size(); i++) {
				if (medlem.equals(medlemListeProperty.getValue().get(i))) {
					duplikat = true;
				}
			}
			if (duplikat) {
				medlemListeProperty.getValue().remove(medlem);
				medlem.deleteAvtale(avtaleProperty.getValue());
			}
			else {
				System.out.println("Brukeren er ikke medlem av gruppen");
			}
		}
		catch (NullPointerException e) {
			System.out.println("Det er ingen medlemmer i gruppen");
		}
	}
	
	public Avtale getAvtale() {
		return avtaleProperty.getValue();
	}
	
	public void setAvtale(Avtale avtale) {
		avtaleProperty.setValue(avtale);
	}

}
