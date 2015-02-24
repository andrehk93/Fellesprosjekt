 package application.klient;

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
	
	
	
	
	
}
