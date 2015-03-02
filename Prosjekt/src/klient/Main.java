package klient;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception{
			stage.setTitle("Kalender Viewer");
			stage.setScene(createScene ( loadMainPane()));
			stage.show();
//			Parent root = FXMLLoader.load(getClass().getResource("../klient/Startside_innlogging.FXML"));
//			Scene scene = new Scene(root,1000, 700);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.show();
	}
	
	private Pane loadMainPane() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		
		Pane mainPane = (Pane) loader.load(
				getClass().getResourceAsStream(
						ScreenNavigator.MAIN
						));
		MainController mainController = loader.getController();
		
		ScreenNavigator.setMainController(mainController);
		ScreenNavigator.loadScreen(ScreenNavigator.GRUPPER);
		
		return mainPane;
	}
	
	private Scene createScene(Pane mainPane){
		Scene scene = new Scene(mainPane);
		scene.getStylesheets().setAll(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
