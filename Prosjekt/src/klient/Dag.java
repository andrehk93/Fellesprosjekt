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
	
	public String getManedNavn() {
		int i = this.getDato().getMonthValue();
		switch(i){
			case 1:
				return "januar";
			case 2:
				return "februar";
			case 3:
				return "mars";
			case 4:
				return "april";
			case 5:
				return "mai";
			case 6:
				return "juni";
			case 7:
				return "juli";
			case 8:
				return "august";
			case 9:
				return "september";
			case 10:
				return "oktober";
			case 11:
				return "november";
			case 12:
				return "desember";
		}
		return null;
	}
	
	public String getDagNavn(){
		int i = this.getDato().getDayOfWeek().getValue();
		switch(i){
			case 1:
				return "mandag";
			case 2:
				return "tirsdag";
			case 3:
				return "onsdag";
			case 4:
				return "torsdag";
			case 5:
				return "fredag";
			case 6:
				return "lørdag";
			case 7:
				return "søndag";
		}
		return null;
	}

}
