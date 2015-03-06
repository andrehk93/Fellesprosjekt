package klient;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PopupController {
	
	@FXML private TextField tidspunkt;
	@FXML private TextField fraBruker;
	@FXML private TextField avtaleNavn;
	@FXML private TextField status;
	@FXML private TextArea melding;
	private String email, tid, meldingen, avtaleid;
	
	public PopupController() {
		tidspunkt = new TextField();
		fraBruker = new TextField();
		avtaleNavn = new TextField();
		status = new TextField();
		melding = new TextArea();
	}
	
	public void initialize(Varsel varsel) throws IOException {
		meldingen = varsel.getMelding();
		email = varsel.getBrukerSendtFra();
		tid = varsel.getTid();
		avtaleid = varsel.getAvtaleid();
		System.out.println("SPECS" + " " + meldingen + " " + email + " " + tid + " " + avtaleid);
		fraBruker.setText(email);
		avtaleNavn.setText(avtaleid);
		avtaleNavn.setVisible(true);
		tidspunkt.setText(tid);
		melding.setText(meldingen);
		if (Klienten.getStatus(varsel.getAvtaleid(), varsel.getBrukerSendtFra()).trim().equals("1")) {
			status.setText("Attending");
			System.out.println("ATTENDER");
		}
		else {
			System.out.println("IKKE ATTENDER :(");
			status.setText("Not attending");
		}
	}
	

}
