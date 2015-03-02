package klient;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class OpprettingController {

    @FXML private TextField fornavn, etternavn, epost;
    @FXML private Label fornavn_lbl, etternavn_lbl, epost_lbl,passord_lbl, passord2_lbl;
    @FXML private PasswordField passord, passord2;

    @FXML private Button lagre;

    //Gå tilbake til Innloggingsiden
    @FXML
    void previousPane(ActionEvent event) {
        ScreenNavigator.loadScreen(ScreenNavigator.INNLOGGING);
    }
	@FXML// OPPRETT BRUKER - Gå til neste screen
	private void nextPane(ActionEvent event) throws IOException {
		lagBruker(event);
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
	}
	
	public void lagBruker(ActionEvent event) throws IOException {
		Klienten.createUser(epost.getText(), fornavn.getText(), etternavn.getText(), passord.getText());
	}
 
    //Validere og sjekke om navn er gyldig, hver gang man trykker på en key
   
 /**   
    @FXML
    private void nameCheck(KeyEvent event) {
    	if (isValidName()){
    		
    	}
    }
  
    @FXML//Validere og sjekke om epost er gyldig, hver gang man trykker på en key
    void 948c8c(ActionEvent event) {

    }

    @FXML //Validere og sjekke om passord er likt med passord 2 
    void 948c8c(ActionEvent event) {

    }

    @FXML
    void 9a9797(ActionEvent event) {

    }
    
    public boolean isValidName(){
    	String regex = "^[a-zA-Z]+$";
    	if (fornavn.getText().matches(regex)){
    		fornavn.setStyle("-fx-text-box-border : red");
    		fornavn_lbl.setText("*Kan kun bestå av bokstaver.");
    	}else if(etternavn.getText().matches(regex)){
    		etternavn.setStyle("-fx-text-box-border : red");
    		etternavn.setText("*Kan kun bestå av bokstaver.");
    	}else if (etternavn.getText().isEmpty()){
    		etternavn.setStyle("-fx-text-box-border : red");
    		etternavn_lbl.setText("* Feltet må fylles.");
    	}else if(fornavn.getText().isEmpty()){
    		fornavn.setStyle("-fx-text-box-border : red");
    		fornavn_lbl.setText("* Feltet må fylles.");
    	}else{
    		fornavn.setStyle("-fx-text-box-border : white");
    		fornavn_lbl.setText("");
    		etternavn.setStyle("-fx-text-box-border : white");
    		etternavn_lbl.setText("");
    	}
    	
    }*/

}
