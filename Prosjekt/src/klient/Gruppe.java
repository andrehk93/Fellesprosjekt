package klient;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;

public class Gruppe {
	
	private String gruppenavn;
	
	public Gruppe() {
		
	}
	
	public Gruppe(String navn, ArrayList<Bruker> medlemmer) {
		this.gruppenavn = navn;
		setMedlemmer(medlemmer);
	}
	
	public void setNavn(String navn){
		this.gruppenavn = navn;
	}
	
	public String getNavn(){
		return this.gruppenavn;
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
	
	public ArrayList<Bruker> getMedlemmer() {
		return medlemListeProperty.getValue();
	}
	
	public Property<ArrayList<Bruker>> getMedlemListeProperty() {
		return medlemListeProperty;
	}
	
	public void addMedlem(Bruker medlem) throws IOException {
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
			}
		}
		catch (NullPointerException e) {
			ArrayList<Bruker> medlemmet = new ArrayList<Bruker>();
			medlemmet.add(medlem);
			medlemListeProperty.setValue(medlemmet);
		}
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
			}
			else {
				System.out.println("Brukeren er ikke medlem av gruppen");
			}
		}
		catch (NullPointerException e) {
			System.out.println("Det er ingen medlemmer i gruppen");
		}
	}
	
	public String toString() {
		return gruppenavn;
	}
	
}
