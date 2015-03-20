package klient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Rediger_brukerController {
	
	
	@FXML
	private ListView<Bruker> eksisterendeBrukere, eksisterendeAdministratorer, slettListe;
	
	@FXML
	private TabPane BrukerTabs;
	
	@FXML
	private Button add, remove, slett, mkAdmin;
	
	private List<Bruker> brukere, admins;
	
	private Bruker valg;
	private Bruker fjernValg;
	
	private ObservableList<Bruker> slettItems;
	private List<Bruker> slettBrukere;
	private ObservableList<Bruker> items, adminItems;
	private boolean showBtns = true;
	
	
	
	@FXML
	private void initialize() throws IOException {
		mkAdmin.setVisible(showBtns);
		slettBrukere = new ArrayList<Bruker>();
		brukere = new ArrayList<Bruker>();
		brukere = Klienten.getAllNonAdmins();
		admins = Klienten.getAllAdminDetails();
		items = FXCollections.observableList(brukere);
		adminItems = FXCollections.observableList(admins);
		eksisterendeBrukere.setItems(items);
		eksisterendeAdministratorer.setItems(adminItems);
		ChangeListener<Bruker> currentValg = new ChangeListener<Bruker>() {

			@Override
			public void changed(ObservableValue<? extends Bruker> arg0,
					Bruker arg1, Bruker arg2) {
				valg = arg2;
			}
			
		};
		ChangeListener<Bruker> currentAdminValg = new ChangeListener<Bruker>(){
			@Override
			public void changed(ObservableValue<? extends Bruker> arg0,
					Bruker arg1, Bruker arg2){
				arg2.setRights(1);
				valg = arg2;
			}
		};
		eksisterendeBrukere.getSelectionModel().selectedItemProperty().addListener(currentValg);
		eksisterendeAdministratorer.getSelectionModel().selectedItemProperty().addListener(currentAdminValg);
		ChangeListener<Bruker> currentFjern = new ChangeListener<Bruker>() {

			@Override
			public void changed(ObservableValue<? extends Bruker> observable,
					Bruker oldValue, Bruker newValue) {
				fjernValg = newValue;
			}
			
		};
		slettListe.getSelectionModel().selectedItemProperty().addListener(currentFjern);
		BrukerTabs.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
						toggleBtns();
						if(!showBtns){
							for(Bruker bruker : slettBrukere){
								brukere.add(bruker);
							}
							items = FXCollections.observableList(brukere);
							eksisterendeBrukere.setItems(items);
						} else {
							for(Bruker bruker : slettBrukere){
								admins.add(bruker);
							}
							adminItems = FXCollections.observableList(admins);
							eksisterendeAdministratorer.setItems(adminItems);
						}
						slettBrukere.clear();
						slettItems = FXCollections.observableList(slettBrukere);
						slettListe.setItems(slettItems);
			        }
			    }
			);
	}
	
	@FXML// GÅ TIL BRUKEROPPRETTING KNAPPEN
	private void opprettingView(ActionEvent event)  {
		ScreenNavigator.loadScreen(ScreenNavigator.OPPRETTING);
	}
	@FXML
	private void monthView(ActionEvent event)  {
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
	}
	
	@FXML
	private void leggTilBruker(ActionEvent event) {
		if (valg != null) {
			slettBrukere.add(valg);
			slettItems = FXCollections.observableList(slettBrukere);
			slettListe.setItems(slettItems);
			brukere.remove(valg);
			admins.remove(valg);
			items = FXCollections.observableList(brukere);
			adminItems = FXCollections.observableList(admins);
			eksisterendeBrukere.setItems(items);
			eksisterendeAdministratorer.setItems(adminItems);
			valg = null;
		}
	}
	
	@FXML
	private void fjernBruker(ActionEvent event) {
		if (fjernValg != null) {
			System.out.println(fjernValg.getRights());
			slettBrukere.remove(fjernValg);
			if(!showBtns){
				admins.add(fjernValg);
				adminItems = FXCollections.observableList(admins);
				eksisterendeAdministratorer.setItems(adminItems);
			} else {
				brukere.add(fjernValg);
				items = FXCollections.observableList(brukere);
				eksisterendeBrukere.setItems(items);
			}
			slettItems = FXCollections.observableList(slettBrukere);
			slettListe.setItems(slettItems);
			fjernValg = null;
		}
	}
	
	@FXML
	private void slettBrukere(ActionEvent event) throws IOException {
		if (! slettBrukere.isEmpty()) {
			for (Bruker bruker : slettBrukere) {
				Klienten.deleteUser(bruker);
				Klienten.fjernBruker(bruker.getEmail(), bruker);
			}
			for (int i = 0; i < slettBrukere.size(); i++) {
				brukere.remove(slettBrukere.get(i));
				slettBrukere.remove(i);
			}
			slettItems = FXCollections.observableList(slettBrukere);
			items = FXCollections.observableList(brukere);
			slettListe.setItems(slettItems);
			eksisterendeBrukere.setItems(items);
		}
	}
	
	@FXML
	private void makeAdmin(ActionEvent event) throws IOException {
		if(! slettBrukere.isEmpty()){
			for(Bruker bruker : slettBrukere){
				Klienten.makeAdmin(bruker);
				bruker.setRights(1);
			}
			
			for(int i = 0; i < slettBrukere.size(); i++){
				admins.add(slettBrukere.get(i));
				items.remove(slettBrukere.get(i));
				slettBrukere.remove(i);
			}
			slettItems = FXCollections.observableList(slettBrukere);
			slettListe.setItems(slettItems);
			adminItems = FXCollections.observableList(admins);
			items = FXCollections.observableList(brukere);
			eksisterendeBrukere.setItems(items);
			eksisterendeAdministratorer.setItems(adminItems);
		}
	}
	
	
	@FXML
	private void removeAdmin(ActionEvent event) throws IOException {
		if(! slettBrukere.isEmpty()){
			for(Bruker bruker : slettBrukere){
				Klienten.removeAdmin(bruker);
				bruker.setRights(0);
			}
			
			for(int i = 0; i < slettBrukere.size(); i++){
				admins.remove(slettBrukere.get(i));
				items.add(slettBrukere.get(i));
				slettBrukere.remove(i);
			}
			slettItems = FXCollections.observableList(slettBrukere);
			slettListe.setItems(slettItems);
			adminItems = FXCollections.observableList(admins);
			items = FXCollections.observableList(brukere);
			eksisterendeBrukere.setItems(items);
			eksisterendeAdministratorer.setItems(adminItems);
		}
	}
	
	@FXML
	private void toggleBtns(){
		showBtns = !showBtns;
		if(showBtns){
			mkAdmin.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent t){
					try {
						makeAdmin(t);
					} catch (IOException e) {
					}
				}
			});
			mkAdmin.setText("Gjør til admin");
		} else {
			mkAdmin.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent t){
					try {
						removeAdmin(t);
					} catch (IOException e) {
					}
				}
			});
			mkAdmin.setText("Fjern adminstatus");
		}
	}
}
