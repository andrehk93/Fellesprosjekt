package klient;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class grupper_controller {
	
	private ArrayList<Bruker> medlemmer;
	private ArrayList<Gruppe> dine_grupper;
	private ArrayList<Bruker> brukere;
	private ArrayList<Bruker> newArrayList;
	private ArrayList<Bruker> s�kBrukere;
	private ObservableList<Gruppe> gruppe_observable;
	private Gruppe currentChanging;
	private Gruppe gruppe;
	private String s�k;
	private String gruppeNavn;
    
    @FXML
    TextField gruppenavn, brukers�k;
    @FXML
    ListView<Bruker> brukerliste, gruppemedlemmer_liste;
    @FXML
    Label antall_gruppemedlemmer, legg_til_lbl;
    @FXML
    Button legg_til_knapp, fjern_knapp, endre, slett, forlat, lagre_gruppe_knapp;
    @FXML
    ComboBox<Gruppe> gruppe_combobox;
    
    public void initialize() throws IOException {
    	addListner();
    	loadGrupper();
		medlemmer = new ArrayList<Bruker>();
		s�kBrukere = new ArrayList<Bruker>();
		antall_gruppemedlemmer.setText(""+medlemmer.size());
		getUsers();
		s�kBrukere = brukere;
		brukerliste(s�kBrukere);
		gruppemedlemmer_liste(medlemmer);
    }
    
    private void loadGrupper() throws IOException {
    	dine_grupper = new ArrayList<Gruppe>();
    	for (Gruppe gruppe : Klienten.grupper) {
   			dine_grupper.add(gruppe);
    	}
    	gruppe_observable = FXCollections.observableList(dine_grupper);
    	gruppe_combobox.setItems(gruppe_observable);
    	FxUtil.autoCompleteComboBox(gruppe_combobox, FxUtil.AutoCompleteMode.CONTAINING);
    	
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
		ChangeListener<Gruppe> valgt_gruppe = new ChangeListener<Gruppe>() {

			@Override
			public void changed(ObservableValue<? extends Gruppe> arg0,
					Gruppe arg1, Gruppe arg2) {
				sjekkDisable();
			}
			
		};
		gruppe_combobox.getSelectionModel().selectedItemProperty().addListener(valgt_gruppe);
		
		ChangeListener<String> s�ker= new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				s�k = newValue;
				
				s�kBrukere = newArrayList();
		    	if (s�k != ""){
		    		for (int i = 0; i < brukere.size(); i++){
		    			if (s�k.length() < brukere.get(i).getNavn().length()+1){
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
    	brukere = Klienten.getBrukereArray();
    	for (Bruker bruker : brukere) {
    		if (bruker.getEmail().equals(Klienten.bruker.getEmail())) {
    			brukere.remove(bruker);
    			break;
    		}
    	}
    	brukerliste(brukere);
    }
    
    public ArrayList<Bruker> newArrayList(){
    	newArrayList = new ArrayList<Bruker>();
    	return newArrayList;
    }
    
    @FXML
	public void lagre() throws IOException{											// Kan ikke bli ferdig med f�r grupper-klassen er endret
    	gruppeNavn = gruppenavn.getText();
		if (medlemmer.size() <2){
			legg_til_lbl.setText("*Du kan ikke lage en gruppe med mindre enn to personer.");
		}
		else {
			if (currentChanging == null) {
				String id = Klienten.addGruppe(gruppeNavn, medlemmer);
				gruppe = new Gruppe(gruppeNavn, medlemmer, id, Klienten.bruker);
				Klienten.grupper.add(gruppe);
				
			}
			else {
				Klienten.grupper.remove(currentChanging);
				Klienten.removeGruppe(currentChanging.getGruppeid());
				String id = Klienten.addGruppe(gruppeNavn, medlemmer);
				gruppe = new Gruppe(gruppeNavn, medlemmer, id, Klienten.bruker);
				Klienten.grupper.add(gruppe);
			}
			ScreenNavigator.loadScreen(ScreenNavigator.getForrigeScreen());
		}
	}
    
    @FXML
    private void slett_gruppe(ActionEvent event) throws IOException {
    	Gruppe valgt_gruppe = FxUtil.getComboBoxValue(gruppe_combobox);
    	if (valgt_gruppe == null || ! valgt_gruppe.getAdmin().getEmail().equals(Klienten.bruker.getEmail())) {
    	}
    	else {
    		Klienten.grupper.remove(valgt_gruppe);
			Klienten.removeGruppe(valgt_gruppe.getGruppeid());
			gruppe_observable.remove(valgt_gruppe);
			for (Bruker bruker : medlemmer) {
				s�kBrukere.add(bruker);
			}
			medlemmer.clear();
			gruppemedlemmer_liste(medlemmer);
    		brukerliste(s�kBrukere);
    		gruppenavn.setText(null);
    		currentChanging = null;
    	}
    }
    
    @FXML
    private void endre_gruppe(ActionEvent event) {
    	Gruppe valgt_gruppe = FxUtil.getComboBoxValue(gruppe_combobox);
    	if (valgt_gruppe != null && valgt_gruppe.getAdmin().getEmail().equals(Klienten.bruker.getEmail())) {
    		if (! medlemmer.isEmpty()) {
    			for (Bruker bruker : medlemmer) {
        			s�kBrukere.add(bruker);
        		}
    			medlemmer.clear();
    		}
    		for (Bruker bruker : valgt_gruppe.getMedlemmer()) {
    			if (! bruker.getEmail().equals(Klienten.bruker.getEmail())) {
    				s�kBrukere.remove(bruker);
    				medlemmer.add(bruker);
    			}
    		}
    		gruppemedlemmer_liste(medlemmer);
    		brukerliste(s�kBrukere);
    		gruppenavn.setText(valgt_gruppe.getNavn());
    		currentChanging = valgt_gruppe;
    	}
    }
    
    @FXML
    private void forlat_gruppe(ActionEvent event) throws IOException {
    	Gruppe valgt_gruppe = FxUtil.getComboBoxValue(gruppe_combobox);
    	if (valgt_gruppe != null) {
    		Klienten.removeGroupMember(valgt_gruppe.getGruppeid(), Klienten.bruker);
    		Klienten.grupper.remove(valgt_gruppe);
    		valgt_gruppe.removeMedlem(Klienten.bruker);
    		loadGrupper();
    	}
    }
    
    private void sjekkDisable() {
    	Gruppe valgt_gruppe = FxUtil.getComboBoxValue(gruppe_combobox);
    	if (valgt_gruppe != null) {
	    	if (valgt_gruppe.getAdmin().getEmail().equals(Klienten.bruker.getEmail())) {
	    		forlat.setDisable(true);
	    	}
	    	else {
	    		forlat.setDisable(false);
	    	}
    	}
    }
    
    @FXML
    public void forkast(){
    	ScreenNavigator.loadScreen(ScreenNavigator.getForrigeScreen());  // M� endres etter grupper-klasse fix
    }
    
    @FXML
	public void avbryt(){
		ScreenNavigator.loadScreen(ScreenNavigator.getForrigeScreen());
	}
   
    
    
}

