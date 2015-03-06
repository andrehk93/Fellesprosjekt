package klient;

import java.io.IOException;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Varsel {
	
	public Varsel() {
		
	}
	
	public Varsel(String melding, String sendtFra, boolean lest, String avtaleid, String tidspunkt) throws IOException {
		setMelding(melding);
		setBrukerSendtFra(sendtFra);
		setLest(lest);
		setAvtaleid(avtaleid);
		setTidspunkt(tidspunkt);
	}
	
	private Property<Boolean> lestProperty = new SimpleBooleanProperty();
	
	public boolean getLest() {
		return lestProperty.getValue();
	}
	
	public void setLest(boolean lest) {
		lestProperty.setValue(lest);
	}
	
	private Property<String> meldingProperty = new SimpleStringProperty();
	
	public String getMelding() {
		return meldingProperty.getValue();
	}
	
	public void setMelding(String melding) {
		meldingProperty.setValue(melding);
	}
	
	private Property<String> brukerFraProperty = new SimpleStringProperty();
	
	public String getBrukerSendtFra() {
		return brukerFraProperty.getValue();
	}
	
	public void setBrukerSendtFra(String fra) {
		brukerFraProperty.setValue(fra);
	}
	
	Property<String> tidProperty = new SimpleStringProperty();
	
	public void setTidspunkt(String tid) {
		tidProperty.setValue(tid);
	}
	
	Property<String> avtaleidProperty = new SimpleStringProperty();
	
	public void setAvtaleid(String avtaleid) {
		avtaleidProperty.setValue(avtaleid);
	}
	
	public String getAvtaleid()  {
		return avtaleidProperty.getValue();
	}
	
	public String getTid() {
		return tidProperty.getValue();
	}
	
}
