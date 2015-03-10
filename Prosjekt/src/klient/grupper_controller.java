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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class grupper_controller {
	
	private ArrayList<Bruker> medlemmer;					
	private ArrayList<Bruker> brukere;
	private ArrayList<Bruker> newArrayList;
	private ArrayList<Bruker> s�kBrukere;
	private Gruppe gruppe;
	private String s�k;
	private Scene scene;
	private String gruppeNavn;
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
    Label antall_gruppemedlemmer = new Label();
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
		s�kBrukere = new ArrayList<Bruker>();
		antall_gruppemedlemmer.setText(""+medlemmer.size());
		getUsers();
		s�kBrukere = brukere;
		brukerliste(s�kBrukere);
		gruppemedlemmer_liste(medlemmer);
    }
	
    @FXML
	public void getGruppenavn(){
    	gruppeNavn = gruppenavn.getText();
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
		s�kBrukere.remove(brukerliste.getSelectionModel().getSelectedItem());
	}
	
	public void fjerneFraGruppe(){
		gruppemedlemmer_liste.getSelectionModel().getSelectedItem();
		s�kBrukere.add(gruppemedlemmer_liste.getSelectionModel().getSelectedItem());
		medlemmer.remove(gruppemedlemmer_liste.getSelectionModel().getSelectedItem());
	}
	
	public void addListner(){
		ChangeListener<String> s�ker= new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				s�k = newValue;
				
				s�kBrukere = newArrayList();
		    	if (s�k != ""){
		    		for (int i = 0; i < brukere.size(); i++){
		    			if (s�k.length() < brukere.get(i).getNavn().length()){
		    				String j = brukere.get(i).getNavn().substring(0, s�k.length());
		    				if (s�k.toLowerCase().equals(j.toLowerCase())){
			    				if (!s�kBrukere.contains(brukere.get(i)) && !medlemmer.contains(brukere.get(i))){
			    					s�kBrukere.add(brukere.get(i));
			    				}
			    					
			    			}
		    			}
		    		}
		    		brukerliste(s�kBrukere);
		    	}
		    	else {
		    		brukerliste(s�kBrukere);
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
    	if (!s�kBrukere.contains(brukerliste.getSelectionModel().getSelectedItem())){
    		
    	}
    	
    	if (brukerliste.getSelectionModel().getSelectedItem() != null && s�kBrukere.isEmpty() && s�k == "" && !medlemmer.contains(brukerliste.getSelectionModel().getSelectedItem())){;
    		leggeTilIGruppe();
			brukerliste(s�kBrukere);
		}if (s�kBrukere.isEmpty() && s�k == "" && !medlemmer.contains(brukerliste.getSelectionModel().getSelectedItem()) && !brukerliste.getSelectionModel().getSelectedItem().equals(null)){
			leggeTilIGruppe();
    	} else if (s�kBrukere.isEmpty()){
    		leggeTilIGruppe();
    	}
    	else if (!medlemmer.contains(brukerliste.getSelectionModel().getSelectedItem()) && s�kBrukere.contains(brukerliste.getSelectionModel().getSelectedItem())){
    		leggeTilIGruppe();
    		brukerliste(s�kBrukere);
    	}
		gruppemedlemmer_liste(medlemmer);
		antall_gruppemedlemmer.setText(""+medlemmer.size());

	}
    
    @FXML
    private void handleFjern(ActionEvent event) throws IOException, NoSuchAlgorithmException {
    	if (!medlemmer.contains(gruppemedlemmer_liste.getSelectionModel().getSelectedItem())){
    		
    	}
		 if (medlemmer.contains(gruppemedlemmer_liste.getSelectionModel().getSelectedItem())){
			fjerneFraGruppe();
			brukerliste(s�kBrukere);
		} else if(medlemmer.size() == 1 ){
    		gruppemedlemmer_liste.getSelectionModel().getSelectedItem();
			s�kBrukere.add(medlemmer.get(0));
			medlemmer.remove(medlemmer.get(0));  
		}	else {
            brukerliste(s�kBrukere);
    	} 
		gruppemedlemmer_liste(medlemmer);
		antall_gruppemedlemmer.setText(""+medlemmer.size());
		
	}
    
    
    public void getUsers() throws IOException{
		brukere = new ArrayList<Bruker>();
    	brukere = Klienten.getAllUserDetails();
    	brukerliste(brukere);
    }
    
    public ArrayList<Bruker> newArrayList(){
    	newArrayList = new ArrayList<Bruker>();
    	return newArrayList;
    }
    
    @FXML
	public void lagre(){											// Kan ikke bli ferdig med f�r grupper-klassen er endret
    	gruppeNavn = gruppenavn.getText();
		System.out.println(gruppeNavn);
		if (medlemmer.size() <1){
			legg_til_lbl.setText("*Du kan ikke lage en gruppe uten medlemmer.");
		}
	}
    
    @FXML
    public void forkast(){
    	ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);  // M� endres etter grupper-klasse fix
    }
    
    @FXML
	public void avbryt(){
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
	}
   
    
    
}
