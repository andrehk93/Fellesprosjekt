package klient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ny_avtale_controller {
	private Scene scene;
    public void setScene(Scene scene) { this.scene = scene; }
    
    @FXML// FORKAST - Går tilbake til kalenderviewet all informasjon skrevet inn i feltene skl ignoreres
	private void handleForkastButton(ActionEvent event) {
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
	}
    
    @FXML// LAGRE - Går tilbake til kalenderviewet. 
    //All informasjon skal lagres som en ny avtale og settes i kalenderen
   	private void handleLagreButton(ActionEvent event) {
    	//FYLL INN KODE
   		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
   	}
       

	public void handleavtalenavn(KeyEvent event){
		TextField avtalenavn = (TextField) scene.lookup("#avtalenavn");
		String s = avtalenavn.getText();
		for (int i = 0; i < s.length(); i++) {
			String ch = s.charAt(i) + "";
			if(ch.charAt(0) != '-' && ch.charAt(0) != ' ' && !isInteger(ch) && !Character.isLetter(s.charAt(i))){
				avtalenavn.setStyle("-fx-border-color:red;");
				System.out.println("String is now Illegal");
				return;
				}
			}
		}
	
		public static boolean isInteger(String s) {
		    try { 
		        Integer.parseInt(s);
		        if(Integer.parseInt(s) < 0){
		        	return false;
		        }
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		    // only got here if we didn't return false
		    return true;
		}
		
		
}


