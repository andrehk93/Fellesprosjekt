package klient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Møterom {
	
	Property<ArrayList<TidsIntervall>> opptatteTiderProperty = new ObjectPropertyBase<ArrayList<TidsIntervall>>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "Opptatte tider";
		}
	};
	
	public Møterom() {
		
	}
	
	public Møterom(int kapasitet, String navn){
		setKapasitet(kapasitet);
		setNavn(navn);
	}
	
	private Property<Number> kapasitetProperty = new SimpleIntegerProperty();
	private Property<String> navnProperty = new SimpleStringProperty();
	
	
	public int getKapasitet(){
		return kapasitetProperty.getValue().intValue();
	}
	
	public void setKapasitet(int kapasitet){
		kapasitetProperty.setValue(kapasitet);
	}
	
	public void setOpptatt(TidsIntervall forespørsel) {
		boolean booket = false;
		if (opptatteTiderProperty.getValue() == null) {
			ArrayList<TidsIntervall> tiden = new ArrayList<TidsIntervall>();
			tiden.add(forespørsel);
			opptatteTiderProperty.setValue(tiden);
			booket = true;
			System.out.println("Er ikke booket denne dagen");
		}
		else {
			System.out.println("sjekker om er ledig...");
			for (TidsIntervall tidspunkt : this.getOpptatt()) {
				if (forespørsel.isIn(tidspunkt)) {
					System.out.println("Rommet er allerede booket");
					booket = true;
					break;
				}
			}
		}
		if (! booket) {
			System.out.println("rommet er ledig");
			opptatteTiderProperty.getValue().add(forespørsel);
		}
	}
	
	public ArrayList<TidsIntervall> getOpptatt() {
		return opptatteTiderProperty.getValue();
	}
	
	public void setNavn(String navn) {
		navnProperty.setValue(navn);
	}
	
	
	public String getNavn() {
		return navnProperty.getValue();
	}
	
	public static void main(String[] args) {
		TidsIntervall test1_slutt = new TidsIntervall(LocalTime.of(9, 20),LocalTime.of(11, 20), LocalDate.of(2015, 3, 25));
		TidsIntervall test1_start = new TidsIntervall(LocalTime.of(9, 20),LocalTime.of(11, 20), LocalDate.of(2015, 3, 25));
		Møterom rom1 = new Møterom(20, "Gobi");
		rom1.setOpptatt(test1_slutt);
		Møterom rom2 = new Møterom(20, "Moki");
		rom2.setOpptatt(test1_start);
		System.out.println("FEILTESTER:");
		rom1.setOpptatt(test1_start);
		System.out.println("rom1:");
		System.out.println(rom1.getOpptatt().get(0).getStart() + " - " + rom1.getOpptatt().get(0).getSlutt());
		System.out.println("rom2:");
		System.out.println(rom2.getOpptatt().get(0).getStart() + " - " + rom2.getOpptatt().get(0).getSlutt());
	}
	
	
	
}
