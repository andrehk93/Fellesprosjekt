package klient;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Popup extends Application{
	
	public Popup() {
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setMinHeight(350);
		primaryStage.setMinWidth(500);
		primaryStage.setMaxHeight(350);
		primaryStage.setMaxWidth(500);
		primaryStage.setTitle("Notifikasjon");
		Parent root = FXMLLoader.load(getClass().getResource("/klient/SePopup.fxml"));
		Scene scene = new Scene(root, 400, 400);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	

}
