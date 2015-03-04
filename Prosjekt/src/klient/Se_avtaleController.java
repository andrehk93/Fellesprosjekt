package klient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class Se_avtaleController {
	
	private String fratid, tiltid, dato, rom, admin;
	@FXML private TextField fra, til, startdato;
	@FXML private ListView<String> deltaker_listeview;
	@FXML private RadioButton skal, skal_ikke, ikke_svart;
	private ObservableList<String> addDeltakere;
	
	public void setAvtale(String avtale, String avtaleid) throws IOException {
		String[] detaljer = avtale.trim().split(" ");
		String deltakerne = Klienten.getDeltakere(avtaleid);
		System.out.println(deltakerne);
		String[] deltakere = deltakerne.trim().split(" ");
		fratid = detaljer[0];
		tiltid = detaljer[1];
		dato = detaljer[2];
		rom = detaljer[3];
		admin = detaljer[4];
		List<String> deltaker_liste = new ArrayList<String>();
		for (String deltaker : deltakere) {
			deltaker_liste.add(deltaker);
		}
		addDeltakere = FXCollections.observableList(deltaker_liste);
	}
	
	public void initialize() throws IOException {
		String[] enhet = KalenderController.enheter;
		setAvtale(Klienten.hentAvtale(enhet[1]), enhet[1]);
		addListeners();
		deltaker_listeview.setItems(addDeltakere);
		fra.setText(fratid);
		til.setText(tiltid);
		startdato.setText(dato);
	}
	
	public boolean skal() {
		return skal.isSelected();
	}
	
	public boolean ikke_svart() {
		return ikke_svart.isSelected();
	}
	
	public boolean skal_ikke() {
		return skal_ikke.isSelected();
	}
	
	public void addListeners() {
		ChangeListener<Boolean> select_skal = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if (skal.isSelected()) {
					skal_ikke.setSelected(false);
					ikke_svart.setSelected(false);
				}
			}
			
		};
		ChangeListener<Boolean> select_skal_ikke = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if (skal_ikke.isSelected()) {
					skal.setSelected(false);
					ikke_svart.setSelected(false);
				}
			}
			
		};
		ChangeListener<Boolean> select_ikke_svart = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if (ikke_svart.isSelected()) {
					skal_ikke.setSelected(false);
					skal.setSelected(false);
				}
			}
			
		};
		skal.selectedProperty().addListener(select_skal);
		skal_ikke.selectedProperty().addListener(select_skal_ikke);
		ikke_svart.selectedProperty().addListener(select_ikke_svart);
	}
	

}
