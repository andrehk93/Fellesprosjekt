package klient;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class Rediger_brukerController {
	
	
	@FXML
	private ListView<Bruker> eksisterendeBrukere, slettListe;
	
	@FXML
	private Button add, remove, slett;
	
	private List<Bruker> brukere;
	
	private Bruker valg;
	private Bruker fjernValg;
	
	private ObservableList<Bruker> slettItems;
	private List<Bruker> slettBrukere;
	
	
	
	@FXML
	private void initialize() throws IOException {
		slettBrukere = new ArrayList<Bruker>();
		brukere = new ArrayList<Bruker>();
		brukere = Klienten.getAllUserDetails();
		System.out.println("BRUKERNE: " + brukere);
		ObservableList<Bruker> items = FXCollections.observableList(brukere);
		eksisterendeBrukere.setItems(items);
		ChangeListener<Bruker> currentValg = new ChangeListener<Bruker>() {

			@Override
			public void changed(ObservableValue<? extends Bruker> arg0,
					Bruker arg1, Bruker arg2) {
				valg = arg2;
				System.out.println("CHOICE: " + valg);
			}
			
		};
		eksisterendeBrukere.getSelectionModel().selectedItemProperty().addListener(currentValg);
		ChangeListener<Bruker> currentFjern = new ChangeListener<Bruker>() {

			@Override
			public void changed(ObservableValue<? extends Bruker> observable,
					Bruker oldValue, Bruker newValue) {
				fjernValg = newValue;
				System.out.println("FJERNVALG: " + fjernValg);
			}
			
		};
		slettListe.getSelectionModel().selectedItemProperty().addListener(currentFjern);
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
		System.out.println("Legg til bruker");
		if (valg != null) {
			slettBrukere.add(valg);
			slettItems = FXCollections.observableList(slettBrukere);
			slettListe.setItems(slettItems);
		}
	}
	
	@FXML
	private void fjernBruker(ActionEvent event) {
		System.out.println("Fjern bruker");
		if (fjernValg != null) {
			slettBrukere.remove(fjernValg);
			slettItems = FXCollections.observableList(slettBrukere);
			slettListe.setItems(slettItems);
		}
	}
	
	@FXML
	private void slettBrukere(ActionEvent event) {
		
	}

}
