package klient;

import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class ScreenNavigator {

	public static final String MAIN    = "main.fxml";
	public static final String INNLOGGING = "Startside_innlogging.fxml";
	public static final String OPPRETTING = "Oppretting.fxml";
	public static final String AVTALE = "ny_avtale.fxml";
	public static final String MANEDSVISNING = "Kalender_månedsvisning.fxml";
	public static final String DAGSVISNING = "Dagsvisning.fxml";
	public static final String UKESVISNING = "Ukesvisning.fxml";
	public static final String GRUPPER ="grupper.fxml";
	public static final String SE_AVTALE = "Se_avtale.fxml";
	public static final String ENDRE_AVTALE = "endre_avtale.fxml";
	public static final String TILKOBLING_ERROR = "ConnectionError.fxml";
	public static final String BRUKERREDIGERING = "Rediger_brukere.fxml";
	
	private static String current = "Startside_innlogging.fxml";
	private static String last;
	/** The main application layout controller. */
	private static MainController mainController;

	/**
	 * Stores the main controller for later use in navigation tasks.
	 *
	 * @param mainController the main application layout controller.
	 */
	public static void setMainController(MainController mainController) {
		ScreenNavigator.mainController = mainController;
	}
	

	/**
	 * Loads the vista specified by the fxml file into the
	 * vistaHolder pane of the main application layout.
	 *
	 * Previously loaded vista for the same fxml file are not cached.
	 * The fxml is loaded anew and a new vista node hierarchy generated
	 * every time this method is invoked.
	 *
	 * A more sophisticated load function could potentially add some
	 * enhancements or optimizations, for example:
	 *   cache FXMLLoaders
	 *   cache loaded vista nodes, so they can be recalled or reused
	 *   allow a user to specify vista node reuse or new creation
	 *   allow back and forward history like a browser
	 *
	 * @param fxml the fxml file to be loaded.
	 */
	public static void loadScreen(String fxml) {
		try {
			last = current;
			current = fxml;
			mainController.setScreen(
					FXMLLoader.load(
							ScreenNavigator.class.getResource(fxml))
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getLoadScreen() {
		return current;
	}
	
	public static String getForrigeScreen() {
		return last;
	}

}
