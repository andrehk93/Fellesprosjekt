package klient;

import java.io.IOException;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Se_avtaleController {
	
	private String fratid, tiltid, dato, rom, admin, avtalenavn;
	@FXML private TextField fra, til, startdato, sluttdato, tittel;
	@FXML private TextArea begrunnelse;
	@FXML private ListView<String> deltaker_listeview;
	@FXML private RadioButton skal, skal_ikke, ikke_svart;
	private ObservableList<String> addDeltakere;
	private String avtaleid;
	@FXML private Button bekreft;
	
	public void setAvtale(String avtale, String avtaleid) throws IOException {
		String[] detaljer = avtale.trim().split(" ");
		String deltakerne = Klienten.getDeltakere(avtaleid);
		this.avtaleid = avtaleid;
		String[] deltakere = deltakerne.trim().split(" ");
		fratid = detaljer[0];
		tiltid = detaljer[1];
		dato = detaljer[2];
		rom = detaljer[3];
		admin = detaljer[4];
		avtalenavn = Klienten.getAppNavn(avtaleid);
		List<String> deltaker_liste = new ArrayList<String>();
		for (String deltaker : deltakere) {
			if (deltaker.trim().equals("NONE")) {
				deltaker_liste.add("Ingen deltakere");
			}
			else {
				deltaker_liste.add(deltaker);
			}
		}
		addDeltakere = FXCollections.observableList(deltaker_liste);
	}
	
	public void initialize() throws IOException {
		String[] enhet = KalenderController.enheter;
		String avtale = Klienten.hentAvtale(enhet[1]);
		setAvtale(avtale, enhet[1]);
		addListeners();
		deltaker_listeview.setItems(addDeltakere);
		fra.setText(fratid);
		til.setText(tiltid);
		startdato.setText(dato);
		sluttdato.setDisable(true);
		tittel.setText(avtalenavn);
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
	
	public void bekreftEndringer(ActionEvent event) throws IOException {
		if (skal.isSelected()) {
			Klienten.sendVarsel(avtaleid, admin, begrunnelse.getText());
			Klienten.changeStatus(avtaleid, "1");
		}
		else if (skal_ikke.isSelected()) {
			Klienten.sendVarsel(avtaleid, admin, begrunnelse.getText());
			Klienten.changeStatus(avtaleid, "0");
		}
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
	}
	

}
