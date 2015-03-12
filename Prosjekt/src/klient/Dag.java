package klient;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;

public class Dag {
	
	public Dag() {
		
	}
	
	public Dag(LocalDate dato) {
		setDato(dato);
	}
	
	Property<LocalDate> datoProperty = new ObjectPropertyBase<LocalDate>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "dato";
		}
	};
	
	Property<ArrayList<Avtale>> avtaleListeProperty = new ObjectPropertyBase<ArrayList<Avtale>>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "AvtaleListe";
		}
	};
	
	public void setDato(LocalDate dato) {
		datoProperty.setValue(dato);
	}
	
	public LocalDate getDato() {
		return datoProperty.getValue();
	}
	
	public String getDayinMonth() {
		return (datoProperty.getValue().toString().split("-"))[2];
	}
	
	public ArrayList<Avtale> getAvtaleListe() {
		return avtaleListeProperty.getValue();
	}
	
	public void addAvtale(Avtale avtale) {
		if (avtaleListeProperty.getValue() == null) {
			ArrayList<Avtale> denne_avtalen = new ArrayList<Avtale>();
			denne_avtalen.add(avtale);
			avtaleListeProperty.setValue(denne_avtalen);
		}
		else {
			avtaleListeProperty.getValue().add(avtale);
		}
	}
	
	public void removeAvtale(Avtale avtale) {
		if (avtaleListeProperty.getValue() == null) {
			System.out.println("Du har ingen avtaler denne dagen");
		}
		else {
			try {
				avtaleListeProperty.getValue().remove(avtale);
				System.out.println("Avtalen ble fjernet");
			}
			catch (NullPointerException e) {
				System.out.println("Avtalen finnes ikke");
			}
		}
	}

}
