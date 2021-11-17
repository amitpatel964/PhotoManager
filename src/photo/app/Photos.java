package photo.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import photo.controller.PhotoLoginController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
* Main method is responsible for initializing the GUI window for login
* 
* @author  Amit Patel, Hideyo Sakamoto
*
*/
public class Photos extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("/photo/view/PhotosGUI.fxml"));
    	
    	AnchorPane root = (AnchorPane)loader.load();
    	
    	PhotoLoginController loginview = loader.getController();
        loginview.start(primaryStage);

    	Scene scene = new Scene(root);
    	primaryStage.setScene(scene);
    	primaryStage.setTitle("Photos");
    	primaryStage.setResizable(false);
    	primaryStage.show();

	}

	public static void main(String[] args){
		launch(args);

	}

}
