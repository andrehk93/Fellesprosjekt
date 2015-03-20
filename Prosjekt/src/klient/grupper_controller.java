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
	private ArrayList<Bruker> søkBrukere;
	private ObservableList<Gruppe> gruppe_observable;
	private Gruppe currentChanging;
	private Gruppe gruppe;
	private String søk;
	private String gruppeNavn;
    
    @FXML
    TextField gruppenavn, brukersøk;
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
		søkBrukere = new ArrayList<Bruker>();
		antall_gruppemedlemmer.setText(""+medlemmer.size());
		getUsers();
		søkBrukere = brukere;
		brukerliste(søkBrukere);
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
		søkBrukere.remove(brukerliste.getSelectionModel().getSelectedItem());
	}
	
	public void fjerneFraGruppe(){
		gruppemedlemmer_liste.getSelectionModel().getSelectedItem();
		søkBrukere.add(gruppemedlemmer_liste.getSelectionModel().getSelectedItem());
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
		
		ChangeListener<String> søker= new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				søk = newValue;
				
				søkBrukere = newArrayList();
		    	if (søk != ""){
		    		for (int i = 0; i < brukere.size(); i++){
		    			if (søk.length() < brukere.get(i).getNavn().length()+1){
		    				String j = brukere.get(i).getNavn().substring(0, søk.length());
		    				if (søk.toLowerCase().equals(j.toLowerCase())){
			    				if (!søkBrukere.contains(brukere.get(i)) && !medlemmer.contains(brukere.get(i))){
			    					søkBrukere.add(brukere.get(i));
			    				}
			    					
			    			}
		    			}
		    		}
		    		brukerliste(søkBrukere);
		    	}
		    	else {
		    		brukerliste(søkBrukere);
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
    	if (!søkBrukere.contains(brukerliste.getSelectionModel().getSelectedItem())){
    		
    	}
    	
    	if (brukerliste.getSelectionModel().getSelectedItem() != null && søkBrukere.isEmpty() && søk == "" && !medlemmer.contains(brukerliste.getSelectionModel().getSelectedItem())){;
    		leggeTilIGruppe();
			brukerliste(søkBrukere);
		}if (søkBrukere.isEmpty() && søk == "" && !medlemmer.contains(brukerliste.getSelectionModel().getSelectedItem()) && !brukerliste.getSelectionModel().getSelectedItem().equals(null)){
			leggeTilIGruppe();
    	} else if (søkBrukere.isEmpty()){
    		leggeTilIGruppe();
    	}
    	else if (!medlemmer.contains(brukerliste.getSelectionModel().getSelectedItem()) && søkBrukere.contains(brukerliste.getSelectionModel().getSelectedItem())){
    		leggeTilIGruppe();
    		brukerliste(søkBrukere);
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
			brukerliste(søkBrukere);
		} else if(medlemmer.size() == 1 ){
    		gruppemedlemmer_liste.getSelectionModel().getSelectedItem();
			søkBrukere.add(medlemmer.get(0));
			medlemmer.remove(medlemmer.get(0));  
		}	else {
            brukerliste(søkBrukere);
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
	public void lagre() throws IOException{											// Kan ikke bli ferdig med før grupper-klassen er endret
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
				søkBrukere.add(bruker);
			}
			medlemmer.clear();
			gruppemedlemmer_liste(medlemmer);
    		brukerliste(søkBrukere);
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
        			søkBrukere.add(bruker);
        		}
    			medlemmer.clear();
    		}
    		for (Bruker bruker : valgt_gruppe.getMedlemmer()) {
    			if (! bruker.getEmail().equals(Klienten.bruker.getEmail())) {
    				søkBrukere.remove(bruker);
    				medlemmer.add(bruker);
    			}
    		}
    		gruppemedlemmer_liste(medlemmer);
    		brukerliste(søkBrukere);
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
    	ScreenNavigator.loadScreen(ScreenNavigator.getForrigeScreen());  // Må endres etter grupper-klasse fix
    }
    
    @FXML
	public void avbryt(){
		ScreenNavigator.loadScreen(ScreenNavigator.getForrigeScreen());
	}
   
    
    
}

