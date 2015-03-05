package klient;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class grupper_controller {
	
	private ArrayList<String> medlemmer;					// M� endres til bruker n�r jeg f�r inn innhold i listen brukere
	private ArrayList<Bruker> brukere;
	private String s�k;
	private ArrayList<String> splitList;
	private ArrayList<String> s�k_liste;
	private ArrayList<String> s�k_liste2;
	private ArrayList<String> testliste;
	private Scene scene;
    public void setScene(Scene scene) { this.scene = scene; }
    
    @FXML
    TextField gruppenavn = new TextField();;
    @FXML
    TextField brukers�k = new TextField();
    @FXML
    ListView<String> brukerliste = new ListView<>();
    @FXML
	ListView<String> gruppemedlemmer_liste = new ListView<>();
    @FXML
    Slider gruppemedlemmer_slider = new Slider();
    @FXML
    TextField antall_gruppemedlemmer = new TextField();
    @FXML
    Button legg_til_knapp = new Button();
    @FXML
    Button fjern_knapp = new Button();
    @FXML
    Button forkast_gruppe_knapp = new Button();
    @FXML
    Button lagre_gruppe_knapp = new Button();
    
    public void initialize() {
    	addListner();
		medlemmer = new ArrayList<String>();			// M� endres til bruker n�r jeg f�r inn innhold i listen brukere
		brukere = new ArrayList<Bruker>();
		testliste = new ArrayList<String>();			// M� endre alle testliste til brukere n�r jeg f�r listen fra databasen
		testliste = new ArrayList<String>();
		splitList = new ArrayList<String>();
		s�k_liste = new ArrayList<String>();
		s�k_liste2 = new ArrayList<String>();
		testlisteAdd();
		brukerliste(testliste);
		gruppemedlemmer_liste(medlemmer);
    }
	
	
	public void testlisteAdd(){
		testliste.add("Andreas");
		testliste.add("Christoffer");
		testliste.add("Lars");
		testliste.add("Martin");
		testliste.add("My");
	}
//-------------------------------------------------------------------------------------------------------------------------------------	
	public void brukerliste(ArrayList<String> S�kBrukere){
    	ObservableList<String> liste_brukere = FXCollections.observableList(S�kBrukere);
		brukerliste.setItems(liste_brukere);
	}
	
	public void gruppemedlemmer_liste(ArrayList<String> GruppeMedlemmer){
    	ObservableList<String> liste_gruppeMedlemmer = FXCollections.observableList(GruppeMedlemmer);
		gruppemedlemmer_liste.setItems(liste_gruppeMedlemmer);
	}
	
	public void leggeTilIGruppe(){
		brukerliste.getSelectionModel().getSelectedItem();
		medlemmer.add(brukerliste.getSelectionModel().getSelectedItem());
		testliste.remove(brukerliste.getSelectionModel().getSelectedItem());
	}
	
	public void addListner(){
		ChangeListener<String> s�ker= new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				s�k = newValue;
				if (testliste.isEmpty() && s�k.length()<1){
		    		brukerliste(testliste);
		    	} 
		    	else{
		            brukerliste(testliste);
		    	}
				if (!medlemmer.isEmpty()){
					gruppemedlemmer_liste(medlemmer);
				}
				
		    	testliste.clear();
		    	if (s�k != null){
		    		for (int i = 0; i < testliste.size(); i++){
		    			s�k_liste2 = splitList(testliste.get(i));
	    				System.out.println(s�k_liste2);
				    	s�k_liste = splitList(s�k);
	    				System.out.println(s�k_liste);
	    				if (s�k.length() < testliste.get(i).length()){
		    				for (int j = 0; j < s�k.length(); j++){
		    					if (s�k_liste.get(j).equals(s�k_liste2.get(j))){
		    						if (!testliste.contains(testliste.get(i))){
		    							testliste.add(testliste.get(i));
		    						}
		    					}
		    				}
	    				}
		    		}
		    	}

			}
		};
		brukers�k.textProperty().addListener(s�ker);
	}
    
	
	public ArrayList<String> splitList(String e){
		splitList.clear();
		for (int i = 0; i < e.length(); i++){
			splitList.add(Character.toString(e.charAt(i)));
		}
		return splitList;
	}
	
    @FXML
    public void handleGruppes�k(KeyEvent event) throws IOException {
    	
    }
    
    @FXML
    private void handleLeggTil(ActionEvent event) throws IOException, NoSuchAlgorithmException {
    	if (brukerliste.getSelectionModel().getSelectedItem() != null){
    		brukerliste.getSelectionModel().getSelectedItem();
			medlemmer.add(brukerliste.getSelectionModel().getSelectedItem());
			testliste.remove(brukerliste.getSelectionModel().getSelectedItem());
    	}
		if (testliste.isEmpty() && s�k == null){
    		brukerliste(testliste);
    	} 
    	else{
            brukerliste(testliste);
    	}
		gruppemedlemmer_liste(medlemmer);

	}
    
    @FXML
    private void handleFjern(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		if (gruppemedlemmer_liste.getSelectionModel().getSelectedItem() != null){
	    	gruppemedlemmer_liste.getSelectionModel().getSelectedItem();
			testliste.add(gruppemedlemmer_liste.getSelectionModel().getSelectedItem());
			medlemmer.remove(gruppemedlemmer_liste.getSelectionModel().getSelectedItem());
		}
		
		if (testliste.isEmpty() && s�k == null){
    		brukerliste(testliste);
    	} 
    	else{
            brukerliste(testliste);
    	}
		gruppemedlemmer_liste(medlemmer);
		
	}
    
    
    public void getUsers() throws IOException{
    	brukere = Klienten.getAllUserDetails();
    	brukerliste.setItems((ObservableList) brukere);
    	brukerliste(testliste);
    }
    
    
    
    
}
