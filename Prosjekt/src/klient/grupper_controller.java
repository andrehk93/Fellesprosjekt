package klient;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class grupper_controller {
	
	private ArrayList<Bruker> medlemmer;
	
	public void grupper_controller(){
		medlemmer = new ArrayList<Bruker>();
	}

	private Scene scene;
    public void setScene(Scene scene) { this.scene = scene; }
    
    @FXML
    TextField gruppenavn = new TextField();;
    @FXML
    TextField brukersøk = new TextField();
    @FXML
    ListView brukerliste = new ListView();
    @FXML
    ListView gruppemedlemmer_liste = new ListView();
    @FXML
    Slider gruppemedlemmer_slider = new Slider();
    @FXML
    TextField antall_gruppemedlemmer = new TextField();
    @FXML
    Button forkast_gruppe_knapp = new Button();
    @FXML
    Button lagre_gruppe_knapp = new Button();
	
}
