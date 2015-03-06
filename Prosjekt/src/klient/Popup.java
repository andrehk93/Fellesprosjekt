package klient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Popup extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setMaxHeight(300);
		primaryStage.setMaxWidth(400);
		Parent root = FXMLLoader.load(getClass().getResource("/klient/SePopup.FXML"));
		System.out.println("JA");
		Scene scene = new Scene(root, 400, 300);
		System.out.println("NIEEAAAAAA");
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	

}
