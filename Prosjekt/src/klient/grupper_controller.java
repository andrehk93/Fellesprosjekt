package klient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
	private String søk;
	private ArrayList<String> søk_liste;
	private ArrayList<String> søk_liste2;
	private ArrayList<String> testliste;
	private ArrayList<String> søkBruker_liste;
	private Scene scene;
    public void setScene(Scene scene) { this.scene = scene; }
    
    @FXML
    TextField gruppenavn = new TextField();;
    @FXML
    TextField brukersøk = new TextField();
    @FXML
    ListView<String> brukerliste = new ListView<>();
    @FXML
	ListView<String> gruppemedlemmer_liste = new ListView<>();

    @FXML
    Slider gruppemedlemmer_slider = new Slider();
    @FXML
    TextField antall_gruppemedlemmer = new TextField();
    @FXML
    Button forkast_gruppe_knapp = new Button();
    @FXML
    Button lagre_gruppe_knapp = new Button();
    
    
	
	public grupper_controller(){
		medlemmer = new ArrayList<Bruker>();
		brukere = new ArrayList<Bruker>();
		testliste = new ArrayList<String>();
		søkBruker_liste = new ArrayList<String>();
		søk_liste = new ArrayList<String>();
		søk_liste2 = new ArrayList<String>();
		testlisteAdd();
		brukerliste(testliste);
		
	}
	
	public void testlisteAdd(){
		testliste.add("Andreas");
		testliste.add("Christoffer");
		testliste.add("Lars");
		testliste.add("Martin");
		testliste.add("My");
	}
	
	public void brukerliste(ArrayList SøkBrukere){
    	ObservableList<String> liste_brukere = FXCollections.observableList(SøkBrukere);
		brukerliste.setItems(liste_brukere);
	}
    
    @FXML
    public void handleGruppesøk(KeyEvent event) throws IOException {
    	søkBruker_liste.clear();
    	søk = brukersøk.getText();
    	
    	for (int i = 0; i < testliste.size(); i++){
    		søkBruker_liste.add(testliste.get(i));
    	}
    	
/*		søk_liste = søk.split("");
    	for (int i = 0; i < testliste.size(); i++){
    		if (søk.length() < testliste.get(i).length()){
    			for (int j = 0; j < søk.length(); j++){
    				søk_liste2 = testliste.get(i).split("");
    				if (søk_liste[j] == søk_liste2[j]){
    					søkBruker_liste.add(testliste.get(i));
    				}
    			}
    		}
    	}
    	System.out.println(søk_liste[1]);
    	System.out.println(søk_liste2[1]);*/
    	
    	System.out.println(søk);
    	brukerliste(søkBruker_liste);
    }
    
    
    public void getUsers() throws IOException{
    	brukere = Klienten.getAllUserDetails();
    	brukerliste.setItems((ObservableList) brukere);
    }
    
    
    
    
}
