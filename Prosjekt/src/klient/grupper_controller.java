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
	
	private ArrayList<String> medlemmer;					// Må endres til bruker når jeg før inn innhold i listen brukere
	private ArrayList<Bruker> brukere;
	private String søk;
	private ArrayList<String> splitList;
	private ArrayList<String> søk_liste;
	private ArrayList<String> søk_liste2;
	private ArrayList<String> testliste;
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
    Button legg_til_knapp = new Button();
    @FXML
    Button fjern_knapp = new Button();
    @FXML
    Button forkast_gruppe_knapp = new Button();
    @FXML
    Button lagre_gruppe_knapp = new Button();
    
    public void initialize() {
    	addListner();
		medlemmer = new ArrayList<String>();			// Må endres til bruker når jeg før inn innhold i listen brukere
		brukere = new ArrayList<Bruker>();
		testliste = new ArrayList<String>();			// Må endre alle testliste til brukere når jeg får listen fra databasen
		testliste = new ArrayList<String>();
		splitList = new ArrayList<String>();
		søk_liste = new ArrayList<String>();
		søk_liste2 = new ArrayList<String>();
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
	public void brukerliste(ArrayList<String> SøkBrukere){
    	ObservableList<String> liste_brukere = FXCollections.observableList(SøkBrukere);
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
		ChangeListener<String> søker= new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				søk = newValue;
				if (testliste.isEmpty() && søk.length()<1){
		    		brukerliste(testliste);
		    	} 
		    	else{
		            brukerliste(testliste);
		    	}
				if (!medlemmer.isEmpty()){
					gruppemedlemmer_liste(medlemmer);
				}
				
		    	testliste.clear();
		    	if (søk != null){
		    		for (int i = 0; i < testliste.size(); i++){
		    			søk_liste2 = splitList(testliste.get(i));
	    				System.out.println(søk_liste2);
				    	søk_liste = splitList(søk);
	    				System.out.println(søk_liste);
	    				if (søk.length() < testliste.get(i).length()){
		    				for (int j = 0; j < søk.length(); j++){
		    					if (søk_liste.get(j).equals(søk_liste2.get(j))){
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
		brukersøk.textProperty().addListener(søker);
	}
    
	
	public ArrayList<String> splitList(String e){
		splitList.clear();
		for (int i = 0; i < e.length(); i++){
			splitList.add(Character.toString(e.charAt(i)));
		}
		return splitList;
	}
	
    @FXML
    public void handleGruppesøk(KeyEvent event) throws IOException {
    	
    }
    
    @FXML
    private void handleLeggTil(ActionEvent event) throws IOException, NoSuchAlgorithmException {
    	if (brukerliste.getSelectionModel().getSelectedItem() != null){
    		brukerliste.getSelectionModel().getSelectedItem();
			medlemmer.add(brukerliste.getSelectionModel().getSelectedItem());
			testliste.remove(brukerliste.getSelectionModel().getSelectedItem());
    	}
		if (testliste.isEmpty() && søk == null){
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
		
		if (testliste.isEmpty() && søk == null){
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
