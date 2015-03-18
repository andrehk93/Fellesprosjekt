package klient;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EPopController {
	
	@FXML private TextField brukernavn;
	@FXML private Button leggtil, removeCal;
	@FXML private Label msg;
	
	void initialize(){}
	
	private void sjekkBrukernavn() throws IOException{
		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)"
				+ "*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		brukernavn.setStyle("-fx-text-box-border : white ");
		msg.setText(" ");
		String svar = brukernavn.getText(); 
		if(brukernavn.getText().equals("")){
			brukernavn.setStyle("-fx-text-box-border : red ");
			msg.setText("Skriv inn epost.");
		}
		else if (!svar.equals("no Name")){
			Klienten.addEkstraBruker(svar);
			if (ScreenNavigator.getLoadScreen().equals("Ukesvisning.fxml")) {
				ScreenNavigator.loadScreen(ScreenNavigator.UKESVISNING);
			}
			else if (ScreenNavigator.getLoadScreen().equals("Kalender_månedsvisning.fxml")) {
				ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
			}
			else {
				ScreenNavigator.loadScreen(ScreenNavigator.DAGSVISNING);
			}
			KalenderController.newStranger();
			Popup.exit();
		}
		else if(svar.equals("no Name")){
			msg.setText("Brukernavnet eksisterer ikke!");
		}
		else if(!(brukernavn.getText().matches(regex))){
			brukernavn.setStyle("-fx-text-box-border : red ");
			msg.setText("Ugyldig format!");
		}
	}
	
	@FXML
	public void handleButtonAction(ActionEvent event) throws IOException{
		sjekkBrukernavn();
	}
	
	@FXML
	private void enterKeyPress(KeyEvent event) throws NoSuchAlgorithmException, IOException{
		if(event.getCode() == KeyCode.ENTER){
			sjekkBrukernavn();
		}
	}
	
	@FXML private void handleButtonRemove(ActionEvent event) throws IOException{
		Klienten.removeStrangers();
	}

}
