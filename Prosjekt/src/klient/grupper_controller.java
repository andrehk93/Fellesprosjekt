package klient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class grupper_controller {
	
	private ArrayList<Bruker> medlemmer;
	private ArrayList<Bruker> brukere;
	private Bruker Andreas;
	private Bruker Christoffer;
	private Bruker Lars;
	private Bruker Martin;
	private Bruker My;
	
	public grupper_controller(){
		medlemmer = new ArrayList<Bruker>();
		brukere = new ArrayList<Bruker>();		
	}
	

	private Scene scene;
    public void setScene(Scene scene) { this.scene = scene; }
    
    @FXML
    TextField gruppenavn = new TextField();;
    @FXML
    TextField brukersøk = new TextField();
    @FXML
    ListView<Bruker> brukerliste = new ListView<Bruker>();
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
	
    public void handleGruppenavn(KeyEvent event) throws IOException {
    	System.out.println("Hei");
    }
    
    public void getUsers() throws IOException{
    	brukere = Klienten.getAllUserDetails();
    	brukerliste.setItems((ObservableList) brukere);
    }
    
    
    
    
}
