package klient;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Varsel {
	
	public Varsel() {
		
	}
	
	public Varsel(String melding, Bruker sendtTil, Bruker sendtFra, boolean lest) {
		setMelding(melding);
		setBrukerSendtFra(sendtFra);
		setBrukerSendtTil(sendtTil);
		setLest(lest);
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
	
	private Property<Bruker> brukerTilProperty = new ObjectPropertyBase<Bruker>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "BrukerTil";
		}
	};
	
	public Bruker getBrukerSendtTil() {
		return brukerTilProperty.getValue();
	}
	
	public void setBrukerSendtTil(Bruker sendtTil) {
		
		brukerTilProperty.setValue(sendtTil);
	}
	
	private Property<Bruker> brukerFraProperty = new ObjectPropertyBase<Bruker>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "BrukerFra";
		}
	};
	
	public Bruker getBrukerSendtFra() {
		return brukerFraProperty.getValue();
	}
	
	public void setBrukerSendtFra(Bruker sendtFra) {
		brukerFraProperty.setValue(sendtFra);
	}

}
