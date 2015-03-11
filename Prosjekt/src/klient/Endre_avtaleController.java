package klient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class Endre_avtaleController {
	
	private String fratid, tiltid, dato, rom, admin, avtalenavn;
	private ObservableList<HBox> addDeltakere;
	private String avtaleid;
	
	public void initialize() throws Exception{
		String enhet = Klienten.getValgtAvtale();
		String avtale = Klienten.hentAvtale(enhet);
		setAvtale(avtale, enhet);
		//addListeners();
		//deltaker_listeview.setItems(addDeltakere);
		//fra.setText(fratid);
		//til.setText(tiltid);
		//startdato.setText(dato);
		//sluttdato.setDisable(true);
		if (avtalenavn.trim().equals("NONE")) {
			//tittel.setText(avtaleid + ": Ingen beskrivelse");
		}
		else {
			//tittel.setText(avtaleid + ": " + avtalenavn);
		}
	}
	
	public void setAvtale(String avtale, String avtaleid) throws IOException {
		String[] detaljer = avtale.trim().split(" ");
		this.avtaleid = avtaleid;
		fratid = detaljer[0];
		tiltid = detaljer[1];
		dato = detaljer[2];
		rom = detaljer[3];
		admin = detaljer[4];
		avtalenavn = Klienten.getAppNavn(avtaleid);
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

}
