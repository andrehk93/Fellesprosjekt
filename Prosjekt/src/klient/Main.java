package klient;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class Main extends Application {
	
	Klienten klienten;
	String startside;
	
	@Override
	public void start(Stage stage) throws Exception{
			stage.setTitle("Kalender Viewer");
			klienten = new Klienten();
			stage.setScene(createScene ( loadMainPane()));
			stage.show();
//			Parent root = FXMLLoader.load(getClass().getResource("../klient/Startside_innlogging.FXML"));
//			Scene scene = new Scene(root,1000, 700);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
				public void handle(WindowEvent we){
					try {
						Klienten.logout();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
	}
	
	private Pane loadMainPane() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		
		Pane mainPane = (Pane) loader.load(
				getClass().getResourceAsStream(
						ScreenNavigator.MAIN
						));
		MainController mainController = loader.getController();
		
		ScreenNavigator.setMainController(mainController);
		if(klienten.getTilkobling()){
			ScreenNavigator.loadScreen(ScreenNavigator.INNLOGGING);
		}
		else {
			ScreenNavigator.loadScreen(ScreenNavigator.TILKOBLING_ERROR);
		}
		
		return mainPane;
	}
	
	public void setStart(String start) {
		this.startside = start;
	}
	
	public String getStart() {
		return this.startside;
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
