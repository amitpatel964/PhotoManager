package photo.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import photo.app.User;
import photo.app.UserManagement;

/**
 * Login window. This is the very first window the user sees.
 * It basically has two functionalities, login for admin and login for user.
 * Two different logins make a new window accordingly.
 * It also has quit button as every window in this project will.
 * 
 * @author Amit Patel, Hideyo Sakamoto
 *
 */
public class PhotoLoginController {

	/*
	 * Password for login
	 */
    @FXML
    private TextField pw;

    /*
     * User name for login
     */
    @FXML
    private TextField name;

    /*
     * login button
     */
    @FXML
    private Button loginButton;

    /*
     * quit button
     */
    @FXML
    private Button quitButton;
    
    /*
     * text field to show message 
     */
    @FXML
    private Text showText;
    
    /**
     * Variable that keeps track of the users
     */
    private UserManagement userManager;
    
    ObservableList<User> obsList;
    
    /**
     * Starting stage
     */
    Stage primaryStage;
    
    /**
     * Initializes data needed to run the program
     * such as list of users
     * @param primaryStage
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public void start(Stage primaryStage) throws ClassNotFoundException, IOException {
    	this.primaryStage  = primaryStage;
    	
    	// Reads the dat file if it exists and loads it into userManager.
    	// This will help us keep track of the list of users and make sure
    	// that the user exists.
    	
		File datFile = new File("dat/users.dat");
		boolean datFileExists = datFile.exists();
		
		if (datFileExists)
		{
			userManager = UserManagement.readApp();
		}
		else
		{
			userManager = new UserManagement();
		}
		
		userManager.checkIfStockIsPresent();
		
		UserManagement.writeApp(userManager);
		
    }

    /**
     * Method to take care of login action.
     * Checks whether admin or user is trying to login, and sets up different window accordingly.
     * 
     * @param event	From mouse click
     * @throws IOException Throws any exceptions
     * @throws ClassNotFoundException If class is not found
     */
    @FXML
    void loginButton(ActionEvent event) throws IOException, ClassNotFoundException {
    	
    	userManager = UserManagement.readApp();
		
		String userName = name.getText();
		
		// name shouldn't be a length of 0
		if((userName.length() == 0)) {
			showText.setText("Name length cannot be 0");
			return;
		}
		
		// admin window
    	if((userName.equals("admin"))) {

    		UserManagement.writeApp(userManager);
    		
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/AdminAction.fxml"));
    		
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		stage.setTitle("User: "+ userName);
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	
    	// checks whether the name exists in the file

    	if((userManager.isNameTaken(userName)) && (!userName.equals("admin"))) {
    		// user subsystem
    		
    		userManager.currentUser = userManager.getUser(userName);
    		
    		UserManagement.writeApp(userManager);
    		
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/UserSubSystem.fxml"));
    		
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		stage.setTitle("User: "+ userName);
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	else
    	{
    		showText.setText("User not found");
    	}
    }
    
    /**
     * Used when the quit button is pressed
     * 
     * @param event	Quit button is pressed
     */
    @FXML
    void quitButton(ActionEvent event) {
    	Platform.exit();
    }

    @FXML
    void nameText(ActionEvent event) {

    }
    

    @FXML
    void pwText(ActionEvent event) {

    }

    

}
