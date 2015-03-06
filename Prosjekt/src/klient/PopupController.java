package klient;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PopupController {
	
	@FXML private TextField tidspunkt, avtaleNavn, fraBruker, status;
	@FXML private TextArea melding;
	private ArrayList<String> detaljer;
	private String email, avtaleid, tiden;
	
	public void initialize() throws IOException {
		email = detaljer.get(0);
		avtaleid = detaljer.get(1);
		tiden = detaljer.get(2);
		detaljer = KalenderController.utenMelding;
		fraBruker.setText(email);
		avtaleNavn.setText(avtaleid);
		tidspunkt.setText(tiden);
		melding.setText(KalenderController.melding);
		if (Klienten.getStatus(avtaleid, email).equals("1")) {
			status.setText("Attending");
		}
		else {
			status.setText("Not attending");
		}
	}
	

}
