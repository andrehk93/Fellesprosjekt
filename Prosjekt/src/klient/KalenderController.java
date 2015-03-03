package klient;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class KalenderController {
	
	private ArrayList<Dag> dager;
	@FXML private GridPane ruter;
	private int maned;
	private int aar;
	
	public void initialize(){
		dager = new ArrayList<Dag>();
		setMonth(5);
		setYear(2015);
		getDays();
		loadLabels();
	}
	
	private void setMonth(int month){
		maned = month;
	}
	
	private void setYear(int year){
		aar = year;
	}
	
	private void getDays(){
		try{
			int i=1;
			while(true){
				LocalDate dato = LocalDate.of(aar, maned, i);
				Dag dag = new Dag(dato);
				dager.add(dag);
				i++;
			}
		}
		catch (DateTimeException e){
			
		 }
	}
	
	private void loadLabels(){
		LocalDate fysteDag = dager.get(0).getDato();
		int firstDiM = fysteDag.getDayOfWeek().getValue()-1;
		System.out.println(firstDiM);
		int t=0;
		for(int j=firstDiM;j<7;j++){
			ruter.add(new Label(dager.get(t).getDayinMonth()), j, 0);
			t++;
		}
		for(int i=1;i<8;i++){
			for(int j=0;j<7;j++){
				ruter.add(new Label(dager.get(t).getDayinMonth()), j, i);
				t++;
			}
		}
	}
	
	private String getNumberofDiM(int row, int col){
		LocalDate fysteDag = LocalDate.of(2015, maned, 1);
		int firstDiM = fysteDag.getDayOfWeek().getValue();
		return "";
	}
	
	@FXML
	private void nextPaneDayView(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.DAGSVISNING);
	}
	
	@FXML
	private void nextPaneMakeAppointment(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.AVTALE);
	}


}
