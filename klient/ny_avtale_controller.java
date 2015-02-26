package klient;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ny_avtale_controller {
	private Scene scene;
    public void setScene(Scene scene) { this.scene = scene; }

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


