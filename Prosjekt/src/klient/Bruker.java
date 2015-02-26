package klient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Bruker {
	
	public Bruker(){
		
	}
	
	public Bruker(String navn, String email){
		setNavn(navn);
		setEmail(email);
		addListeners();
	}
	
	private Property<ArrayList<Avtale>> avtaleListeProperty = new ObjectPropertyBase<ArrayList<Avtale>>(null) {

		@Override
		public Object getBean() {
			return this;
		}

		@Override
		public String getName() {
			return "Avtaleliste";
		}
		
	};
	
	private Property<String> nameProperty = new SimpleStringProperty();
	private Property<String> emailProperty = new SimpleStringProperty();
	
	public String getNavn(){
		return nameProperty.getValue();
	}
	
	public void setNavn(String name) {
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
	
	public ArrayList<Avtale> getAvtaler() {
		return avtaleListeProperty.getValue();
	}
	
	public void addAvtale(Avtale avtale) {
		boolean duplikat = false;
		if (avtaleListeProperty.getValue() == null) {
			ArrayList<Avtale> denne_avtalen = new ArrayList<Avtale>();
			denne_avtalen.add(avtale);
			avtaleListeProperty.setValue(denne_avtalen);
		}
		else {
			for (Avtale avtaler : avtaleListeProperty.getValue()) {
				if (avtale.equals(avtaler)) {
					duplikat = true;
				}
			}
			if (duplikat) {
				avtaleListeProperty.getValue().add(avtale);
			}
			else {
				System.out.println("avtalen finnes");
			}
			
		}
	}
	
	public void removeAvtale(Avtale avtale) {
		if (avtaleListeProperty.getValue() == null) {
			System.out.println("Du har ingen avtaler");
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
	
	public void addListeners() {
		ChangeListener<ArrayList<Avtale>> listener = new ChangeListener<ArrayList<Avtale>>() {

			@Override
			public void changed(
					ObservableValue<? extends ArrayList<Avtale>> arg0,
					ArrayList<Avtale> arg1, ArrayList<Avtale> arg2) {
				System.out.println("nå skjedde det noe!");
				for (int i = 0; i < arg2.get(0).getDeltakere().size(); i++) {
					arg2.get(0).getDeltakere().get(i).addAvtale(arg2.get(0));
				}
			}
		};
		avtaleListeProperty.addListener(listener);
	
	}
	
	public static void main(String[] args) {
		Bruker Andreas = new Bruker("Andreas", "a");
		Bruker Martin = new Bruker("Martin", "m");
		Bruker Ch = new Bruker("Ch", "c");
		ArrayList<Bruker> delt = new ArrayList<Bruker>();
		delt.add(Martin);
		delt.add(Ch);
		Møterom rom = new Møterom(20, "GOBI");
		Møterom rom_2 = new Møterom(20, "MOKI");
		TidsIntervall tid = new TidsIntervall(LocalTime.of(12, 15), LocalTime.of(12, 45), LocalDate.of(2015,12,12));
		Avtale avtale_1 = new Avtale(Andreas, delt, tid, rom, null);
		System.out.println(avtale_1);
		Andreas.addAvtale(avtale_1);
		Andreas.getAvtaler().get(0).setRom(rom_2);
		System.out.println(Andreas.getAvtaler());
		System.out.println("Martins avtaler: " + Martin.getAvtaler());
		System.out.println("Chs avtaler: " + Ch.getAvtaler());
		Andreas.removeAvtale(avtale_1);
	}
	
	
	
}
