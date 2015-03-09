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
	private String s�k;
	private ArrayList<Bruker> s�kBruker_liste;
	private Scene scene;
    public void setScene(Scene scene) { this.scene = scene; }
    
    @FXML
    TextField gruppenavn = new TextField();;
    @FXML
    TextField brukers�k = new TextField();
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
		s�kBruker_liste = new ArrayList<Bruker>();
		getUsers();
		brukerliste(brukere);
		gruppemedlemmer_liste(medlemmer);
    }
	
	
    

	public void brukerliste(ArrayList<Bruker> S�kBrukere){
    	ObservableList<Bruker> liste_brukere = FXCollections.observableList(S�kBrukere);
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
		ChangeListener<String> s�ker= new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				s�k = newValue;
				if (s�kBruker_liste.isEmpty() && s�k.length()<1){
		    		brukerliste(brukere);
		    	} 
		    	else{
		            brukerliste(s�kBruker_liste);
		    	}
				if (!medlemmer.isEmpty()){
					gruppemedlemmer_liste(medlemmer);
				}
				
		    	s�kBruker_liste.clear();
		    	if (s�k != null){
		    		for (int i = 0; i < brukere.size(); i++){
	    				if (s�k.length() < brukere.get(i).getNavn().length()+1){
	    					String j = brukere.get(i).getNavn().substring(0, s�k.length());
	    					if (s�k.toLowerCase().equals(j.toLowerCase())){
		    					if (!s�kBruker_liste.contains(brukere.get(i))){
		    						s�kBruker_liste.add(brukere.get(i));
		    					}
		    					
		    				}
	    				}
		    		}
		    	}

			}
		};
		brukers�k.textProperty().addListener(s�ker);
	}
    
	
	
    @FXML
    public void handleGruppes�k(KeyEvent event) throws IOException {
    	
    }
    
    @FXML
    private void handleLeggTil(ActionEvent event) throws IOException, NoSuchAlgorithmException {
    	if (medlemmer.contains(brukerliste.getSelectionModel().getSelectedItem())){
    		legg_til_lbl.setText("Brukeren er allerede i gruppen");
		}else {
			legg_til_lbl.setText("");
		}
    	
    	if (brukerliste.getSelectionModel().getSelectedItem() != null && s�kBruker_liste.isEmpty() && s�k == null && !medlemmer.contains(brukerliste.getSelectionModel().getSelectedItem())){;
	    	brukerliste.getSelectionModel().getSelectedItem();
			medlemmer.add(brukerliste.getSelectionModel().getSelectedItem());
			s�kBruker_liste.remove(brukerliste.getSelectionModel().getSelectedItem());
		}
		
		if (s�kBruker_liste.isEmpty() && s�k == null){
    		brukerliste(brukere);
    	} 
    	else{
            brukerliste(s�kBruker_liste);
    	}
		gruppemedlemmer_liste(medlemmer);

	}
    
    @FXML
    private void handleFjern(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		if (gruppemedlemmer_liste.getSelectionModel().getSelectedItem() != null && s�kBruker_liste.isEmpty() && s�k == null){
			gruppemedlemmer_liste.getSelectionModel().getSelectedItem();
			s�kBruker_liste.add(gruppemedlemmer_liste.getSelectionModel().getSelectedItem());
			medlemmer.remove(gruppemedlemmer_liste.getSelectionModel().getSelectedItem());
		}

		if (s�kBruker_liste.isEmpty() && s�k == null){
    		brukerliste(brukere);
    	} 
    	else{
            brukerliste(s�kBruker_liste);
    	}
		gruppemedlemmer_liste(medlemmer);
		
	}
    
    
    public void getUsers() throws IOException{
    	brukere = Klienten.getAllUserDetails();
    	brukerliste(s�kBruker_liste);
    }
    
    
    @FXML
	public void avbryt(){
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
	}
    
    
}
