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


public class InnloggingController {//implements Initializable

	//Koble innlogging med DB (brukernavn og passord)

	@FXML private TextField brukernavn;
	@FXML private Label msg;
	@FXML private PasswordField passord;
	@FXML private Button logginn, opprettbruker;
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
			ScreenNavigator.loadScreen(ScreenNavigator.AVTALE);
		}
	}

	@FXML// OPPRETT BRUKER - G� til neste screen
	private void nextPane(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.OPPRETTING);
	}


	//************VALIDERINGSMETODER******************
	public void checkInput(){
		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)"
				+ "*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

		if (brukernavn.getText().equals("") || passord.getText().equals("") ){ 
			brukernavn.setStyle("-fx-text-box-border : red ");
			msg.setText("Manglende brukernavn og/eller passord.");
		}else if(!(brukernavn.getText().matches(regex))){
			brukernavn.setStyle("-fx-text-box-border : red ");
			msg.setText("Brukernavn/epostadresse er p� ugyldig format!");

			//Sjekk om brukernavnet finnes, sjekk mot db liste av brukernavn. Gi tilbakemld = "Brukernavn eksisterer ikke"

			//Sjekk om brukernavner riktig, hvis riktig sjekk om det tilh�rende pw er identisk til d skrevet inn. 
		}else{
			brukernavn.setStyle("-fx-text-box-border : white ");
			passord.setStyle("-fx-text-box-border : white ");
			msg.setText(" ");
		}

	}
}







