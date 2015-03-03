package klient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
	private ArrayList<String> brukere;
	private Bruker Andreas;
	private Bruker Christoffer;
	private Bruker Lars;
	private Bruker Martin;
	private Bruker My;
	
	public grupper_controller(){
		medlemmer = new ArrayList<Bruker>();
		brukere = new ArrayList<String>();		
	}
	

	private Scene scene;
    public void setScene(Scene scene) { this.scene = scene; }
    
    @FXML
    TextField gruppenavn = new TextField();;
    @FXML
    TextField brukers�k = new TextField();
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
	
    public void handleGruppenavn(KeyEvent event) {
    	System.out.println("Hei");
    }
    
    public void getUsers() throws IOException{
    	brukere = (ArrayList<String>) Arrays.asList(Klienten.getAllUsers().split(" "));
    }
    
    
    
    
}