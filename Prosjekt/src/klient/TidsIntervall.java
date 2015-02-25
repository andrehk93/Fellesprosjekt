 package klient;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;

public class TidsIntervall {
	
	public TidsIntervall(){
		
	}
	
	public TidsIntervall(LocalTime start, LocalTime slutt, LocalDate dato){
		setStart(start);
		setSlutt(slutt);
		setDato(dato);		
	}
	
	private Property<LocalTime> startProperty = new ObjectPropertyBase<LocalTime>(null){
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "start";
		}
	};
	
	private Property<LocalTime> sluttProperty = new ObjectPropertyBase<LocalTime>(null){
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "slutt";
		}
	};
	
	private Property<LocalDate> datoProperty = new ObjectPropertyBase<LocalDate>(null){
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "dato";
		}
	};
	
	public LocalTime getStart(){
		return startProperty.getValue();
	}
	
	public void setStart(LocalTime start){
		startProperty.setValue(start);
	}
	
	public Property<LocalTime> startProperty(){
		return startProperty;
	}
	
	public LocalTime getSlutt(){
		return sluttProperty.getValue();
	}
	
	public void setSlutt(LocalTime slutt){
		sluttProperty.setValue(slutt);
	}
	
	public Property<LocalTime> sluttProperty(){
		return sluttProperty;
	}
	
	public LocalDate getDato(){
		return datoProperty.getValue();
	}
	
	public void setDato(LocalDate dato){
		datoProperty.setValue(dato);
	}
	
	public Property<LocalDate> datoProperty(){
		return datoProperty;
	}
	
	//sjekker om DET GITTE tidsintervallobjektet er i DETTE (this) objektet (f.eks 10:20 - 11:20):
	
	public boolean isIn(TidsIntervall gittTid) {
		System.out.println(gittTid.getDato());
		System.out.println(this.getDato().compareTo(gittTid.getDato()));
		if (this.getDato().equals(gittTid.getDato())) {
			if (this.getStart().isAfter(gittTid.getSlutt())) {
				return false;
			}
			else if (this.getSlutt().isBefore(gittTid.getStart())) {
				return false;
			}
			else if (this.getSlutt().compareTo(gittTid.getStart()) == 0){
				return false;
			}
			else if (this.getStart().compareTo(gittTid.getSlutt()) == 0){
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return false;
		}
	}
	
	
	
	
	
}
