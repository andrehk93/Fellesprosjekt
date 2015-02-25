package application.klient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;

public class Møterom {
	
	private ArrayList<TidsIntervall> opptatteTider;
	
	public Møterom() {
		
	}
	
	public Møterom(int kapasitet, String navn){
		opptatteTider = new ArrayList<TidsIntervall>();
		setKapasitet(kapasitet);
	}
	
	private Property<Number> kapasitetProperty = new SimpleIntegerProperty();
	
	
	public int getKapasitet(){
		return kapasitetProperty.getValue().intValue();
	}
	
	public void setKapasitet(int kapasitet){
		kapasitetProperty.setValue(kapasitet);
	}
	
	public void setOpptatt(TidsIntervall forespørsel) {
		boolean booket = false;
		if (opptatteTider.isEmpty()) {
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
			opptatteTider.add(forespørsel);
		}
	}
	
	public ArrayList<TidsIntervall> getOpptatt() {
		return opptatteTider;
	}
	
	public static void main(String[] args) {
		TidsIntervall test1_start = new TidsIntervall(LocalTime.of(10, 20),LocalTime.of(11, 20), LocalDate.of(2015, 2, 25));
		TidsIntervall test1_slutt = new TidsIntervall(LocalTime.of(9, 20),LocalTime.of(11, 20), LocalDate.of(2015, 3, 25));
		Møterom rom1 = new Møterom(20, "Gobi");
		rom1.setOpptatt(test1_slutt);
		System.out.println(rom1);
	}
	
	
	
}
