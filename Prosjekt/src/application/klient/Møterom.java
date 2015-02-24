package application.klient;

import java.util.ArrayList;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;

public class Møterom {
	
	public Møterom(){
		
	}
	
	public Møterom(int kapasitet, ArrayList<TidsIntervall> opptatt){
		setKapasitet(kapasitet);
		setOpptatt(opptatt);
	}
	
	private Property<Integer> kapasitetProperty = new SimpleIntegerProperty();
	
	
	public int getKapasitet(){
		return kapasitetProperty.getValue();
	}
	
	public void setKapasitet(int kapasitet){
		kapasitetProperty.setValue(kapasitet);
	}
	
	
	
}
