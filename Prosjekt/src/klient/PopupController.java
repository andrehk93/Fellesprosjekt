package klient;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PopupController {
	
	@FXML private TextField tidspunkt;
	@FXML private TextField fraBruker;
	@FXML private TextField avtaleNavn;
	@FXML private TextField status;
	@FXML private TextArea melding;
	@FXML private CheckBox lest;
	private String email, tid, meldingen, avtaleid, avtaleNavnet;
	
	public PopupController() {
	}
	
	Varsel varsel;
	
	public void initialize() throws IOException {
		for (Varsel vars : KalenderController.oppdelte_notifikasjoner) {
			if (vars.getAvtaleid().equals(KalenderController.valg)) {
				this.varsel = vars;
			}
		}
		meldingen = varsel.getMelding();
		email = varsel.getBrukerSendtFra();
		tid = varsel.getTid();
		avtaleid = varsel.getAvtaleid();
		avtaleNavnet = Klienten.getAppNavn(avtaleid);
		fraBruker.setText(email);
		if (avtaleNavnet.trim().equals("NONE")) {
			avtaleNavn.setText(avtaleid + ": Ingen beskrivelse");
		}
		else {
			avtaleNavn.setText(avtaleid + ": " + avtaleNavnet);
		}
		tidspunkt.setText(tid);
		melding.setText(meldingen);
		if (Klienten.getStatus(varsel.getAvtaleid(), varsel.getBrukerSendtFra()).trim().equals("1")) {
			status.setText("Attending");
		}
		else {
			status.setText("Not attending");
		}
	}
	
	public void ferdig(ActionEvent event) throws IOException {
		if (lest.isSelected()) {
			Klienten.setLest(email, avtaleid);
			if (ScreenNavigator.getLoadScreen().equals("Ukesvisning.fxml")) {
				ScreenNavigator.loadScreen(ScreenNavigator.UKESVISNING);
			}
			else if (ScreenNavigator.getLoadScreen().equals("Kalender_månedsvisning.fxml")) {
				ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
			}
			else {
				ScreenNavigator.loadScreen(ScreenNavigator.DAGSVISNING);
			}
		}
		else {
			
		}
		Popup.exit();
	}
	

}
