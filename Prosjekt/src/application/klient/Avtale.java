package application.klient;


import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;

public class Avtale {
	
	public Avtale() {
		
	}
	
	public Avtale(Bruker eier, ArrayList<Bruker> deltakere, TidsIntervall tid, Møterom rom, Gruppe gruppe){
		setEier(eier);
		setDeltakere(deltakere);
	}
	
	private Property<String> emailProperty = new SimpleStringProperty();
	
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
		eierProperty.setValue(eier);
	}
	
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
	
	public void setDeltakere(ArrayList<Bruker> deltakere) {
		if (! deltakere.isEmpty()) {
			ArrayList<Bruker> deltakerListe = new ArrayList<Bruker>();
			for (Bruker deltaker : deltakere) {
				deltakerListe.add(deltaker);
			}
			deltakerProperty.setValue(deltakerListe);
		}
		else {
			System.out.println("Legger ikke til deltakere");
		}
	}
	
	
}
