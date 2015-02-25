package klient;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;

public class Bruker {
	
	public Bruker(){
		
	}
	
	public Bruker(String name, String email){
		setName(name);
		setEmail(email);
	}
	
	private Property<String> nameProperty = new SimpleStringProperty();
	private Property<String> emailProperty = new SimpleStringProperty();
	
	public String getName(){
		return nameProperty.getValue();
	}
	
	public void setName(String name) {
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
	
	
	
}
