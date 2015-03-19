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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class Se_avtaleController {
	
	private String fratid, tiltid, dato, rommet, admin, avtalenavn;
	@FXML private TextField fra, til, startdato, tittel, rom;
	@FXML private TextArea begrunnelse;
	@FXML private ListView<HBox> deltaker_listeview;
	@FXML private RadioButton skal, skal_ikke, ikke_svart;
	private ObservableList<HBox> addDeltakere;
	private String avtaleid;
	private Avtale avtalen;
	@FXML private Button bekreft;
	@FXML private CheckBox remove;
	
	public void setAvtale() throws IOException {
		fratid = avtalen.getTid().getStart().toString();
		tiltid = avtalen.getTid().getSlutt().toString();
		dato = avtalen.getTid().getDato().toString();
		rommet = avtalen.getRom().getNavn();
		admin = avtalen.getEier().getEmail();
		avtalenavn = avtalen.getAvtaleNavn();
		skal.disableProperty().set(true);
		skal_ikke.disableProperty().set(true);
		ikke_svart.disableProperty().set(true);
		for(Bruker b : avtalen.getDeltakere()){
			if(Klienten.bruker.getEmail().equals(b.getEmail())){
				skal.disableProperty().set(false);
				skal_ikke.disableProperty().set(false);
				ikke_svart.disableProperty().set(false);
			}
		}
		String deltakerne = Klienten.getDeltakere(avtaleid, "1");
		String[] deltakere_attender = deltakerne.trim().split(" ");
		List<HBox> deltaker_liste = new ArrayList<HBox>();
		for (String deltaker : deltakere_attender) {
			HBox boks = new HBox();
			boks.setStyle("-fx-background-color:#44cc44");
			if (deltaker.trim().equals("NONE")) {
			}
			else {
				boks.getChildren().add(new Text(deltaker));
				deltaker_liste.add(boks);
			}
		}
		String deltakerne_ikkeAttender = Klienten.getDeltakere(avtaleid, "0");
		String[] deltakere_ikkeAttender = deltakerne_ikkeAttender.trim().split(" ");
		for (String deltaker : deltakere_ikkeAttender) {
			HBox boks = new HBox();
			boks.setStyle("-fx-background-color:#cc4444");
			if (deltaker.trim().equals("NONE")) {
			}
			else {
				boks.getChildren().add(new Text(deltaker));
				deltaker_liste.add(boks);
			}
		}
		String deltakerne_ikkeSvart = Klienten.getDeltakere(avtaleid, null);
		String[] deltakere_ikkeSvart = deltakerne_ikkeSvart.trim().split(" ");
		for (String deltaker : deltakere_ikkeSvart) {
			HBox boks = new HBox();
			boks.setStyle("-fx-background-color:yellow");
			if (deltaker.trim().equals("NONE")) {
			}
			else {
				boks.getChildren().add(new Text(deltaker));
				deltaker_liste.add(boks);
			}
		}
		addDeltakere = FXCollections.observableList(deltaker_liste);
	}
	
	public void initialize() throws IOException {
		for(Avtale app : Klienten.avtaler){
			if(Klienten.getValgtAvtale().equals(app.getAvtaleid())){
				avtalen = app;
				avtaleid = app.getAvtaleid();
				break;
			}
		}
		setAvtale();
		addListeners();
		deltaker_listeview.setItems(addDeltakere);
		fra.setText(fratid);
		til.setText(tiltid);
		rom.setText(rommet);
		startdato.setText(dato);
		if (avtalenavn == null || avtalenavn.trim().equals("NONE")) {
			tittel.setText(avtaleid + ": Ingen beskrivelse");
		}
		else {
			tittel.setText(avtaleid + ": " + avtalenavn);
		}
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
	
	@FXML
	public void bekreftEndringer(ActionEvent event) throws IOException {
		if (skal.isSelected()) {
			Klienten.sendVarsel(avtaleid, admin, begrunnelse.getText());
			Klienten.changeStatus(avtaleid, "1");
		}
		else if (skal_ikke.isSelected()) {
			Klienten.sendVarsel(avtaleid, admin, begrunnelse.getText());
			Klienten.changeStatus(avtaleid, "0");
		}
		KalenderController.enheter = null;
		if(remove.isSelected()){
			Klienten.deleteAttendant(avtaleid, Klienten.bruker.getEmail());
			Klienten.avtaler.remove(avtalen);
		}
		ScreenNavigator.loadScreen(ScreenNavigator.getForrigeScreen());
	}
	

}
