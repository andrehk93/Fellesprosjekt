package klient;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class KalenderController {
	
	private ArrayList<Label> labels;
	@FXML private GridPane ruter;
	
	public void initialize(){
		loadLabels();
	}
	
	private void loadLabels(){
		for(int i=0;i<7;i++){
			for(int j=0;j<8;j++){
				ruter.add(new Label(i+" "+j), i, j);
			}
		}
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
