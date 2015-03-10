package klient;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Rediger_brukerController {

	@FXML// GÅ TIL BRUKEROPPRETTING KNAPPEN
	private void opprettingView(ActionEvent event)  {
		ScreenNavigator.loadScreen(ScreenNavigator.OPPRETTING);
	}
	@FXML
	private void monthView(ActionEvent event)  {
		ScreenNavigator.loadScreen(ScreenNavigator.MANEDSVISNING);
	}

}
