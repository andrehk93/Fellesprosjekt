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

		System.out.println("\"" + brukernavn + "\"" + "\"" + passord + "\"");
		if (svar.equals("OK")) {
			ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);

			//IF SETNINGENE - satt i slik rekkefølge sånn at de ikke overrider hverandre (labelet)
		}else if(passord.getText().equals("") && brukernavn.getText().equals("")){
			brukernavn.setStyle("-fx-text-box-border : red ");
			passord.setStyle("-fx-text-box-border : red ");
			msg.setText("Skriv inn brukernavn og passord.");

		}else if (brukernavn.getText().isEmpty()){
			brukernavn.setStyle("-fx-text-box-border : red ");
			msg.setText("Skriv inn brukernavn (e-postadresse).");
		}else if(!(brukernavn.getText().matches(regex))){
			brukernavn.setStyle("-fx-text-box-border : red ");
			msg.setText("Brukernavn er på ugyldig format!");
		}else if(passord.getText().equals("")){		
			passord.setStyle("-fx-text-box-border : red ");
			msg.setText("Skriv inn passord.");

		}else if(svar.equals("NO SUCH USER")){
			msg.setText("Brukernavnet eksisterer ikke!");
		}else {
			msg.setText("Feil passord!");
		}
	}

	@FXML
	private void enterKeyPress(KeyEvent event) throws NoSuchAlgorithmException, IOException{
		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)"
				+ "*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		
		brukernavn.setStyle("-fx-text-box-border : white ");
		passord.setStyle("-fx-text-box-border : white ");
		msg.setText(" ");

		if(event.getCode() == KeyCode.ENTER){
			String svar = Klienten.login(brukernavn.getText(), passord.getText()); 
			System.out.println("\"" + brukernavn + "\"" + "\"" + passord + "\"");
			if (svar.equals("OK")) {
				ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);

				//IF SETNINGENE - satt i slik rekkefølge sånn at de ikke overrider hverandre (labelet)
			}else if(passord.getText().equals("") && brukernavn.getText().equals("")){
				brukernavn.setStyle("-fx-text-box-border : red ");
				passord.setStyle("-fx-text-box-border : red ");
				msg.setText("Skriv inn brukernavn og passord.");

			}else if (brukernavn.getText().isEmpty()){
				brukernavn.setStyle("-fx-text-box-border : red ");
				msg.setText("Skriv inn brukernavn (e-postadresse).");
			}else if(!(brukernavn.getText().matches(regex))){
				brukernavn.setStyle("-fx-text-box-border : red ");
				msg.setText("Brukernavn er på ugyldig format!");
			}else if(passord.getText().equals("")){		
				passord.setStyle("-fx-text-box-border : red ");
				msg.setText("Skriv inn passord.");

			}else if(svar.equals("NO SUCH USER")){
				msg.setText("Brukernavnet eksisterer ikke!");
			}else {
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


		//************VALIDERINGSMETODER******************
		//	public void checkInput(){
		//		String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)"
		//				+ "*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		//
		//		if (brukernavn.getText().equals("")){
		//			brukernavn.setStyle("-fx-text-box-border : red ");
		//			msg.setText("Skriv inn brukernavn (e-postadresse).");
		//		}else if(passord.getText().equals("")){		
		//			passord.setStyle("-fx-text-box-border : red ");
		//			msg.setText("Skriv inn passord.");
		//		}else if(passord.getText().equals("") && brukernavn.getText().equals("")){
		//			passord.setStyle("-fx-text-box-border : red ");
		//			msg.setText("Skriv inn brukernavn og passord.");
		//		}else if(!(brukernavn.getText().matches(regex))){
		//			brukernavn.setStyle("-fx-text-box-border : red ");
		//			msg.setText("Brukernavn er på ugyldig format!");
		//		}else{
		//			brukernavn.setStyle("-fx-text-box-border : white ");
		//			passord.setStyle("-fx-text-box-border : white ");
		//			msg.setText(" ");
		//
		//		}
		//
		//		//Sjekk om brukernavnet finnes, sjekk mot db liste av brukernavn. Gi tilbakemld = "Brukernavn eksisterer ikke"
		//	}

	}








