package klient;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class OpprettingController {

	private String fornavnText;
    @FXML private TextField fornavn, etternavn, epost;
    @FXML private Label fornavn_lbl, etternavn_lbl, epost_lbl,passord_lbl, passord2_lbl;
    @FXML private PasswordField passord, passord2;

    
    @FXML private Button lagre;

    //Gå tilbake til Innloggingsiden
    
    public void initialize() {
    	addListner();
    }
    
    
    @FXML
    void previousPane(ActionEvent event) {
        ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
    }
	@FXML// OPPRETT BRUKER - Gå til neste screen
	private void nextPane(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		if (isValidFornavn() && isValidEtternavn() && isValidEpost() && isPassordTheSame() && isPassordFilled()){
			lagBruker(event);
			Klienten.login(epost.getText(), passord.getText());
			ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
		}else{
			isValidFornavn();
			isValidEtternavn();
			isValidEpost();
			isPassordFilled();
			isPassordTheSame();
			
		}
		
		
	}
	
	public void lagBruker(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		Klienten.createUser(epost.getText(), fornavn.getText(), etternavn.getText(), passord.getText());
	}
 
	public void addListner(){
		ChangeListener<String> fornavnInnput= new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				isValidFornavn();
			}
			
		};
		ChangeListener<String> etternavnInnput= new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				isValidEtternavn();
			}
		};
		
		ChangeListener<String> passordInnput= new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				isPassordTheSame();
			}			
			
			
		};
		
		ChangeListener<String> epostInnput= new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				isValidEpost();
			}			
			
			
		};		
		
		fornavn.textProperty().addListener(fornavnInnput);
		etternavn.textProperty().addListener(etternavnInnput);
		epost.textProperty().addListener(epostInnput);
		passord.textProperty().addListener(passordInnput);
		passord2.textProperty().addListener(passordInnput);
	} 

	
	public boolean isValidFornavn(){ // Validerer fornavn
    	String regex = "^[a-zA-Z'-]+$";
    	if (!fornavn.getText().matches(regex)){
    		fornavn.setStyle("-fx-text-box-border : red");
    		fornavn_lbl.setText("*Kan kun bestå av bokstaver.");
    		return false;
    	}else if (fornavn.getText().equals("")) {
    		fornavn.setStyle("-fx-text-box-border : red");
        	fornavn_lbl.setText("* Feltet må fylles.");
        	return false;
    	}else {
    		fornavn.setStyle("-fx-text-box-border : white");
        	fornavn_lbl.setText("Kun bokstaver");
        	return true;
    	}
    	 	
   
    }
    	
	public boolean isValidEtternavn(){ // Validerer etternavn
		String regex = "^[a-zA-Z'-]+$";
    	if(!etternavn.getText().matches(regex)){
    		etternavn.setStyle("-fx-text-box-border : red");
    		etternavn_lbl.setText("*Kan kun bestå av bokstaver.");
    		return false;
    	}else if (etternavn.getText().equals("")) {
    		etternavn.setStyle("-fx-text-box-border : red");
        	etternavn_lbl.setText("* Feltet må fylles.");
        	return false;
    	}
    	etternavn.setStyle("-fx-text-box-border : white");
        etternavn_lbl.setText("Kun bokstaver");
        return true;
    	
    	}
	
    public boolean isValidEpost() { // Validerer epost
    	boolean stricterFilter = true; 
        String stricterFilterString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        String laxString = ".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
        String emailRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailRegex);
        java.util.regex.Matcher m = p.matcher(epost.getText());
        boolean valid = m.matches();
        if (valid == true){
        	epost.setStyle("-fx-text-box-border : white");
        	epost_lbl.setText("");
        	return true;
        }else{
        	epost.setStyle("-fx-text-box-border : red");
        	epost_lbl.setText("* Epost ikke gyldig");
        	return false;
        }
 }
	
	//Validere og sjekke om passord er lik passord2 
    public boolean isPassordTheSame() {
		if(!passord.getText().equals(passord2.getText())){
   			passord.setStyle("-fx-text-box-border : red");
   			passord2.setStyle("-fx-text-box-border : red");
   			passord2_lbl.setText("*Første passord er ikke det samme som andre passord");
   			return false;
   		}else{
   			passord.setStyle("-fx-text-box-border : white");
   			passord2.setStyle("-fx-text-box-border : white");
   			passord2_lbl.setText("");
   			isPassordFilled();
   			return true;
   		}	
    }
    
    public boolean isPassordFilled() {
    	if (passord.getText().equals("") && passord2.getText().equals("")){
    		passord.setStyle("-fx-text-box-border : red");
   			passord_lbl.setText("* Feltet må fylles.");
    		passord2.setStyle("-fx-text-box-border : red");
   			passord2_lbl.setText("* Feltet må fylles.");
   			return false;
   			
    	}
    	else if (passord.getText().equals("")){
   			passord.setStyle("-fx-text-box-border : red");
   			passord_lbl.setText("* Feltet må fylles.");
   			return false;
   			
   		}else if (passord2.getText().equals("")){
   			passord2.setStyle("-fx-text-box-border : red");
   			passord2_lbl.setText("* Feltet må fylles.");
   			return false;
   			
   		}else{
   			return true;
   		}
    }

    
}

	


