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
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class grupper_controller {
	
	private ArrayList<Bruker> medlemmer;					
	private ArrayList<Bruker> brukere;
	private String søk;
	private ArrayList<Bruker> søkBruker_liste;
	private Scene scene;
    public void setScene(Scene scene) { this.scene = scene; }
    
    @FXML
    TextField gruppenavn = new TextField();;
    @FXML
    TextField brukersøk = new TextField();
    @FXML
    ListView<Bruker> brukerliste = new ListView<>();
    @FXML
	ListView<Bruker> gruppemedlemmer_liste = new ListView<>();
    @FXML
    Slider gruppemedlemmer_slider = new Slider();
    @FXML
    TextField antall_gruppemedlemmer = new TextField();
    @FXML 
    Label legg_til_lbl = new Label();
    @FXML
    Button legg_til_knapp = new Button();
    @FXML
    Button fjern_knapp = new Button();
    @FXML
    Button forkast_gruppe_knapp = new Button();
    @FXML
    Button lagre_gruppe_knapp = new Button();
    
    public void initialize() throws IOException {
    	addListner();
		medlemmer = new ArrayList<Bruker>();			
		brukere = new ArrayList<Bruker>();
		brukere = new ArrayList<Bruker>();			
		søkBruker_liste = new ArrayList<Bruker>();
		getUsers();
		brukerliste(brukere);
		gruppemedlemmer_liste(medlemmer);
    }
	
	
    

	public void brukerliste(ArrayList<Bruker> SøkBrukere){
    	ObservableList<Bruker> liste_brukere = FXCollections.observableList(SøkBrukere);
		brukerliste.setItems(liste_brukere);
	}
	
	public void gruppemedlemmer_liste(ArrayList<Bruker> GruppeMedlemmer){
    	ObservableList<Bruker> liste_gruppeMedlemmer = FXCollections.observableList(GruppeMedlemmer);
		gruppemedlemmer_liste.setItems(liste_gruppeMedlemmer);
	}
	
	public void leggeTilIGruppe(){
		brukerliste.getSelectionModel().getSelectedItem();
		medlemmer.add(brukerliste.getSelectionModel().getSelectedItem());
		brukere.remove(brukerliste.getSelectionModel().getSelectedItem());
	}
	
	public void addListner(){
		ChangeListener<String> søker= new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				søk = newValue;
				if (søkBruker_liste.isEmpty() && søk.length()<1){
		    		brukerliste(brukere);
		    	} 
		    	else{
		            brukerliste(søkBruker_liste);
		    	}
				if (!medlemmer.isEmpty()){
					gruppemedlemmer_liste(medlemmer);
				}
				
		    	søkBruker_liste.clear();
		    	if (søk != null){
		    		for (int i = 0; i < brukere.size(); i++){
	    				if (søk.length() < brukere.get(i).getNavn().length()+1){
	    					String j = brukere.get(i).getNavn().substring(0, søk.length());
	    					if (søk.toLowerCase().equals(j.toLowerCase())){
		    					if (!søkBruker_liste.contains(brukere.get(i))){
		    						søkBruker_liste.add(brukere.get(i));
		    					}
		    					
		    				}
	    				}
		    		}
		    	}

			}
		};
		brukersøk.textProperty().addListener(søker);
	}
    
	
	
    @FXML
    public void handleGruppesøk(KeyEvent event) throws IOException {
    	
    }
    
    @FXML
    private void handleLeggTil(ActionEvent event) throws IOException, NoSuchAlgorithmException {
    	if (medlemmer.contains(brukerliste.getSelectionModel().getSelectedItem())){
    		legg_til_lbl.setText("Brukeren er allerede i gruppen");
		}else {
			legg_til_lbl.setText("");
		}
    	
    	if (brukerliste.getSelectionModel().getSelectedItem() != null && søkBruker_liste.isEmpty() && søk == null && !medlemmer.contains(brukerliste.getSelectionModel().getSelectedItem())){;
	    	brukerliste.getSelectionModel().getSelectedItem();
			medlemmer.add(brukerliste.getSelectionModel().getSelectedItem());
			søkBruker_liste.remove(brukerliste.getSelectionModel().getSelectedItem());
		}
		
		if (søkBruker_liste.isEmpty() && søk == null){
    		brukerliste(brukere);
    	} 
    	else{
            brukerliste(søkBruker_liste);
    	}
		gruppemedlemmer_liste(medlemmer);

	}
    
    @FXML
    private void handleFjern(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		if (gruppemedlemmer_liste.getSelectionModel().getSelectedItem() != null && søkBruker_liste.isEmpty() && søk == null){
			gruppemedlemmer_liste.getSelectionModel().getSelectedItem();
			søkBruker_liste.add(gruppemedlemmer_liste.getSelectionModel().getSelectedItem());
			medlemmer.remove(gruppemedlemmer_liste.getSelectionModel().getSelectedItem());
		}

		if (søkBruker_liste.isEmpty() && søk == null){
    		brukerliste(brukere);
    	} 
    	else{
            brukerliste(søkBruker_liste);
    	}
		gruppemedlemmer_liste(medlemmer);
		
	}
    
    
    public void getUsers() throws IOException{
    	brukere = Klienten.getAllUserDetails();
    	brukerliste(søkBruker_liste);
    }
    
    
    @FXML
	public void avbryt(){
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
	}
    
    
}
