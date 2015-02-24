package application.klient;


import java.util.ArrayList;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;

public class Avtale {
	
	public Avtale(){
		
	}
	
	public Avtale(Bruker eier, ArrayList<Bruker> deltakere, TidsIntervall tid, Møterom rom){
		setEier(eier);
		setDeltakere(deltakere);
	}
	
	private Property<String> emailProperty = new SimpleStringProperty();
	
	
	
	
}
