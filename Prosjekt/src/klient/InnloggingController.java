package klient;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import klient.Klienten;
import klient.ScreenNavigator;


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
		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)"
				+ "*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		brukernavn.setStyle("-fx-text-box-border : white ");
		passord.setStyle("-fx-text-box-border : white ");
		msg.setText(" ");

		String svar = Klienten.login(brukernavn.getText(), passord.getText()); 
		if (brukernavn.getText().equals("")&&passord.getText().equals("")){
			brukernavn.setStyle("-fx-text-box-border : red ");
			passord.setStyle("-fx-text-box-border : red ");
			msg.setText("Skriv inn brukernavn og passord");
		}else if(brukernavn.getText().equals("")){
			brukernavn.setStyle("-fx-text-box-border : red ");
			msg.setText("Skriv inn passord.");

		}else if(passord.getText().equals("")){
			passord.setStyle("-fx-text-box-border : red ");
			msg.setText("Skriv inn passord.");
		}
		else if (svar.equals("OK")){
			ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
		} else if(svar.equals("NO SUCH USER")){
			msg.setText("Brukernavnet eksisterer ikke!");
			
		}else if(!(brukernavn.getText().matches(regex))){
			brukernavn.setStyle("-fx-text-box-border : red ");
			msg.setText("Ugyldig format!");
		}
		else {
			msg.setText("Feil passord!");
		}
	}

	@FXML
	private void enterKeyPress(KeyEvent event) throws NoSuchAlgorithmException, IOException{
		if(event.getCode() == KeyCode.ENTER){

			String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)"
					+ "*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
			brukernavn.setStyle("-fx-text-box-border : white ");
			passord.setStyle("-fx-text-box-border : white ");
			msg.setText(" ");

			String svar = Klienten.login(brukernavn.getText(), passord.getText()); 
			if (brukernavn.getText().equals("") && passord.getText().equals("")){
				brukernavn.setStyle("-fx-text-box-border : red ");
				passord.setStyle("-fx-text-box-border : red ");
				msg.setText("Skriv inn brukernavn og passord");
			}else if(brukernavn.getText().equals("")){
				brukernavn.setStyle("-fx-text-box-border : red ");
				msg.setText("Skriv inn brukernavn");

			}else if(passord.getText().equals("")){
				passord.setStyle("-fx-text-box-border : red ");
				msg.setText("Skriv inn passord.");
			}
			else if (svar.equals("OK")){
				ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
			} else if(svar.equals("NO SUCH USER")){
				msg.setText("Brukernavnet eksisterer ikke!");
			}else if(!(brukernavn.getText().matches(regex))){
				brukernavn.setStyle("-fx-text-box-border : red ");
				msg.setText("Ugyldig format!");
			}
			else {
				msg.setText("Feil passord!");
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
}


	//************VALIDERINGSMETODER******************
	//	public void checkInput(){
	//		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)"
	//				+ "*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	//		brukernavn.setStyle("-fx-text-box-border : white ");
	//		passord.setStyle("-fx-text-box-border : white ");
	//		msg.setText(" ");
	//
	//		if (brukernavn.getText().equals("")){
	//			brukernavn.setStyle("-fx-text-box-border : red ");
	//			msg.setText("Skriv inn brukernavn (e-postadresse).");
	//		}
	//		if(passord.getText().equals("")){		
	//			passord.setStyle("-fx-text-box-border : red ");
	//			msg.setText("Skriv inn passord.");
	//		}
	//		if(!(brukernavn.getText().matches(regex))){
	//			brukernavn.setStyle("-fx-text-box-border : red ");
	//			msg.setText("Brukernavn/epostadresse er på ugyldig format!");
	//
	//		}

