package klient;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;

public class Gruppe {
	
	private String gruppenavn;
	private String gruppeid;
	private Bruker admin;
	
	public Gruppe() {
		
	}
	
	public Gruppe(String navn, ArrayList<Bruker> medlemmer, String gruppeid, Bruker admin) throws IOException {
		this.gruppenavn = navn;
		setGruppeid(gruppeid);
		setAdmin(admin);
		setMedlemmer(medlemmer);
	}
	
	public void setGruppeid(String gruppeid) {
		this.gruppeid = gruppeid;
	}
	
	public String getGruppeid() {
		return gruppeid;
	}
	
	public void setAdmin(Bruker admin) {
		this.admin = admin;
	}
	
	public Bruker getAdmin() {
		return admin;
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
				if (medlem.getEmail().equals(medlemListeProperty.getValue().get(i).getEmail())) {
					duplikat = true;
				}
			}
			if (duplikat) {
				medlemListeProperty.getValue().remove(medlem);
			}
			else {
			}
		}
		catch (NullPointerException e) {
		}
	}
	
	public String toString() {
		return gruppenavn;
	}
	
}
