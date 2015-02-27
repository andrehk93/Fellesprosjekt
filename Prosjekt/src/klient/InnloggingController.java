package klient;


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


	@FXML// LOGG INN KNAPPEN
	private void handleButtonAction(ActionEvent event) {
		checkInput();
	}

	@FXML// OPPRETT BRUKER - Gå til neste screen
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
			msg.setText("Brukernavn/epostadresse er på ugyldig format!");

			//Sjekk om brukernavnet finnes, sjekk mot db liste av brukernavn. Gi tilbakemld = "Brukernavn eksisterer ikke"

			//Sjekk om brukernavner riktig, hvis riktig sjekk om det tilhørende pw er identisk til d skrevet inn. 
		}else{
			brukernavn.setStyle("-fx-text-box-border : white ");
			passord.setStyle("-fx-text-box-border : white ");
			msg.setText(" ");
		}

	}
}







