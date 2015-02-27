package klient;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Main controller class for the entire layout.
 */
public class MainController {

	/** Holder of a switchable vista. */
	
	@FXML
	private StackPane screenHolder;

	/**
	 * Replaces the vista displayed in the vista holder with a new vista.
	 *
	 * @param node the vista node to be swapped in.
	 */
	public void setScreen(Node node) {
		screenHolder.getChildren().setAll(node);
	}

}