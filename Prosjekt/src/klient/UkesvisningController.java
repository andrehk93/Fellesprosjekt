package klient;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class UkesvisningController {
	
	@FXML private GridPane ruter;
	@FXML private ScrollPane scroll;
	@FXML private Label ukeNr;
	
	private ArrayList<Avtale> avtaleListe;
	private LocalDate firstDayOfWeek;
	private int weekNumber;
	
	public void initialize() {
		scroll.setVvalue(scroll.getVmin());
		firstDayOfWeek = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()-1);
		weekNumber = firstDayOfWeek.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		ukeNr.setText(String.valueOf(weekNumber));
	}
	
	private void loadGrid() {
		
	}
	
	@FXML
	private void nextPaneMonthView(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
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
	private void nextPaneEditGroups(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.GRUPPER);
	}
	
	@FXML
	private void nextPaneLogOut(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.INNLOGGING);
	}

}
