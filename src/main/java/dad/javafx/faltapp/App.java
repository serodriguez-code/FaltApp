package dad.javafx.faltapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application{

	private MainController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		controller=new MainController();
		
		Scene scene=new Scene(controller.getView());
				
		primaryStage.setScene(scene); 
		primaryStage.setTitle("FaltApp");
		primaryStage.getIcons().add(new Image("images/classroom-24x24.png"));
		
		primaryStage.setOnCloseRequest(e->{
			controller.exitAction(e);
		});
		
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}


