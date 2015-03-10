package klient;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class InnloggingController {//implements Initializable

	//Koble innlogging med DB (brukernavn og passord)

	@FXML private TextField brukernavn;
	@FXML private Label msg;
	@FXML private PasswordField passord;
	@FXML private Button logginn, opprettbruker;
	@FXML private Button retryButton;
	public Socket socket;
	public DataOutputStream outToServer;
	public BufferedReader inFromServer;
	private static String modifiedSentence;

	public void initialize() {
	}


	@FXML// LOGG INN KNAPPEN
	private void handleButtonAction(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		checkInput();
		if (Klienten.login(brukernavn.getText(), passord.getText())) {
			ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
		}
	}

	@FXML
	private void enterKeyPress(KeyEvent event) throws NoSuchAlgorithmException, IOException{
		if(event.getCode() == KeyCode.ENTER){
			checkInput();
			if (Klienten.login(brukernavn.getText(), passord.getText())){
				ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
			}
		}
	}


	@FXML
	private void retryCon(ActionEvent event) throws IOException {
		Klienten klienten = new Klienten();
		if(klienten.getTilkobling()){
			ScreenNavigator.loadScreen(ScreenNavigator.INNLOGGING);
		}
	}


	//************VALIDERINGSMETODER******************
	public void checkInput(){
		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)"
				+ "*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		brukernavn.setStyle("-fx-text-box-border : white ");
		passord.setStyle("-fx-text-box-border : white ");
		msg.setText(" ");

		if (brukernavn.getText().equals("")){
			brukernavn.setStyle("-fx-text-box-border : red ");
			msg.setText("Skriv inn brukernavn (e-postadresse).");
		}
		if(passord.getText().equals("")){		
			passord.setStyle("-fx-text-box-border : red ");
			msg.setText("Skriv inn passord.");
		}
		if(!(brukernavn.getText().matches(regex))){
			brukernavn.setStyle("-fx-text-box-border : red ");
			msg.setText("Brukernavn/epostadresse er på ugyldig format!");

		}

		//Sjekk om brukernavnet finnes, sjekk mot db liste av brukernavn. Gi tilbakemld = "Brukernavn eksisterer ikke"
	}

}








