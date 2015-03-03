package klient;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class KalenderController {
	
	private ArrayList<Dag> dager;
	@FXML private GridPane ruter;
	@FXML private Button forrigeManed;
	@FXML private Button nesteManed;
	@FXML private Label manedLabel;
	@FXML private Label arLabel;
	private int maned;
	private int aar;
	
	public void initialize(){
		setMonth(LocalDate.now().getMonthValue());
		setYear(LocalDate.now().getYear());
		flushView();
	}
	
	private void flushView(){
		dager = new ArrayList<Dag>();
		ruter.getChildren().clear();
		getDays();
		loadGrid();	
	}
	
	private void setMonth(int month){
		maned = month;
		manedLabel.setText(LocalDate.of(2015, month, 01).getMonth().toString());
	}
	
	private int getMonth(){
		return maned;
	}
	
	private void setYear(int year){
		aar = year;
		arLabel.setText(String.valueOf(year));
	}
	
	private int getYear(){
		return aar;
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
		catch (DateTimeException e){}
	}
	
	
	
	private void loadGrid(){
		int lengde = dager.size();
		LocalDate fysteDag = dager.get(0).getDato();
		int firstDiM = fysteDag.getDayOfWeek().getValue()-1;
		int t=0;
		for(int j=firstDiM;j<7;j++){
			Label label = new Label(dager.get(t).getDayinMonth());
			ruter.add(label, j, 0);
			ruter.setValignment(label, VPos.TOP);
			setTexts(j,0,t);
			t++;
		}
		for(int i=1;i<8;i++){
			for(int j=0;j<7;j++){
				Label label = new Label(dager.get(t).getDayinMonth());
				ruter.add(label, j, i);
				ruter.setValignment(label, VPos.TOP);
				setTexts(j,i,t);
				t++;
				if(t>=lengde){
					break;
				}
			}
			if(t>=lengde){
				break;
			}
		}
	}
	
	private void setTexts(int j,int i,int t){
		Text text = new Text();
		ArrayList<Avtale> avtaler = new ArrayList<Avtale>();
		avtaler = dager.get(t).getAvtaleListe();
		String temp = "";
		if(avtaler!=null){
			for(Avtale app : avtaler){
				temp += app.getTid().getStart().toString()+"\n";		
			}
		}
		text.setText(temp);
		ruter.add(text, j, i);
	}
	
	@FXML
	private void nextPaneDayView(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.DAGSVISNING);
	}
	
	@FXML
	private void nextPaneMakeAppointment(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.AVTALE);
	}
	
	@FXML
	private void nextMonth(ActionEvent event) {
		if(getMonth()+1>=13){
			setMonth(1);
			setYear(getYear()+1);
		}
		else{
			setMonth(getMonth()+1);
		}
		flushView();
	}
	
	@FXML
	private void previousMonth(ActionEvent event) {
		if(getMonth()-1<=0){
			setMonth(12);
			setYear(getYear()-1);
		}
		else{
			setMonth(getMonth()-1);
		}
		flushView();
	}
}
