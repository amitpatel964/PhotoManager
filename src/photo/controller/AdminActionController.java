package photo.controller;

import javafx.collections.FXCollections;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import photo.app.Album;
import photo.app.User;
import photo.app.UserManagement;

/**
 * This window opens when user logs in as admin.
 * It takes care of admin related actions such as creating and deleting users.
 * Also capable of listing existing users in the listview.
 * 
 * @author Amit Patel, Hideyo Sakamoto
 */
public class AdminActionController {

    /*
     * button to list existing users
     */
    @FXML
    private Button listUserButton;

    /*
     * button to logout
     */
    @FXML
    private Button logoutButton;

    /*
     * button to quit
     */
    @FXML
    private Button quitButton;

    /*
     * button to create user
     */
    @FXML
    private Button createUserButton;

    /*
     * button to delete user
     */
    @FXML
    private Button deleteUserButton;

    /*
     * text field in which user name is entered
     */
    @FXML
    private TextField userNameTextField;

    /*
     * displays text to inform user about his/her action
     */
    @FXML
    private Text showText;
    
    /*
     * list of users 
     */
    @FXML
    private ListView<User> myListView;
    
    /**
     * Holds the list of users
     */
    private UserManagement userManager;
    
    /*
     * user object which to be added to list
     */
    private User newUser;
    
    /*
     * checks whether the name entered is suitable for action
     */
    private boolean is_name_taken = false;
    
    /**
     * Used to populate the list of users
     */
    private ObservableList<User> observList = FXCollections.observableArrayList();
    
    /**
     * The primary stage
     */
    private Stage primarystage;
    
    /**
     * The program starts with this mthod
     * 
     * @param primarystage	Stage
     * @throws ClassNotFoundException	If class is not found
     * @throws IOException	Thrwos any exceptions
     */
    public void start(Stage primarystage) throws ClassNotFoundException, IOException {
    	this.primarystage = primarystage;
    	
    	// Read the dat file
    	userManager = UserManagement.readApp();
    }
    
    /**
     * Creates user with name entered by admin
     * @param event	From user interaction with create user being pressed
     * @throws IOException Thrwos any exceptions
     */
    @FXML
    void createUserAction(ActionEvent event) throws IOException {
    	String newUserName = userNameTextField.getText();

    	// checks if the name is already taken
    	if(userManager.allUsersList.size() == 0) {
    		newUser = new User(newUserName);
    		userManager.addUser(newUser);
    		showText.setText("User added");
    	}
    	
    	for(User user: userManager.allUsersList) {
    		if(newUserName.equals(user.getUserName())) {
    			showText.setText("Name already taken");
    			is_name_taken = true;
    		}
    	}
    	if(!is_name_taken) {
    		// create user
    		newUser = new User(newUserName);
    		userManager.addUser(newUser);
    		showText.setText("User added");
    	}
    	else
    	{
    		showText.setText("User name already taken");
    	}
    	
    	// Save data
    	UserManagement.writeApp(userManager);
    	
    	// Reset list
    	myListView.getItems().clear();
    }

    /**
     * Deletes a user if the user is found after the delete user button is clicked
     * 
     * @param event	From user interaction with the delete button pressed
     * @throws IOException Throws any exceptions
     */
    @FXML
    void deleteUserAction(ActionEvent event) throws IOException {
    	String deletingUserName = userNameTextField.getText();
    	
    	for(int i = 0; i < userManager.allUsersList.size(); i++) {
    		if(userManager.allUsersList.get(i).getUserName().equalsIgnoreCase(deletingUserName)) {
    			userManager.deleteUser(deletingUserName);
    			UserManagement.writeApp(userManager);
    			showText.setText("Deleted");
    	    	// Reset list
    	    	myListView.getItems().clear();
    			return;
    		}
    	}
    	
    	showText.setText("Name Not Found");
    }

    /**
     * Shows the list of users
     * 
     * @param event	From User interaction with list users button
     */
    @FXML
    public void listuserAction(ActionEvent event) {
    	List<User> users = userManager.getListOfUsers();
    	
    	if (users.size() == 0)
    	{
    		showText.setText("No users at the moment");
    		return;
    	}
    	
    	observList = FXCollections.observableArrayList();
    	
    	for (int i = 0; i < users.size(); i++)
    	{
    		observList.add(users.get(i));
    	}
    	
    	myListView.setItems(observList);
    }

    /**
     * Logs the admin out
     * 
     * @param event	From user interaction with the logout button
     * @throws IOException	Throws any exceptions
     */
    @FXML
    void logoutAction(ActionEvent event) throws IOException {
    	
    	Parent root = FXMLLoader.load(getClass().getResource("/photo/view/PhotosGUI.fxml"));
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }
    
    /**
     * When the admin clicked on a name in the user list, the name is put into the textfield
     * 
     * @param event	ListView clicked
     */
    public void highlightUserList(MouseEvent event)
    {
    	User userName = (User) myListView.getSelectionModel().getSelectedItem();
    	
    	if (userName == null)
    	{
    		return;
    	}
    	
    	userNameTextField.setText(userName.getUserName());
    }

    /**
     * Exits the program
     * 
     * @param event	Quit button pressed
     */
    @FXML
    void quitAction(ActionEvent event) {
    	Platform.exit();
    }

    /**
     * This method is used when the window initializes
     * 
     * @throws IOException Throws any exceptions
     * @throws ClassNotFoundException If class is not found
     */
    @FXML
    void initialize() throws ClassNotFoundException, IOException {
    	
    	// Read the dat file
    	userManager = UserManagement.readApp();
    	
        assert listUserButton != null : "fx:id=\"listUserButton\" was not injected: check your FXML file 'AdminAction.fxml'.";
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'AdminAction.fxml'.";
        assert quitButton != null : "fx:id=\"quitButton\" was not injected: check your FXML file 'AdminAction.fxml'.";
        assert createUserButton != null : "fx:id=\"createUserButton\" was not injected: check your FXML file 'AdminAction.fxml'.";
        assert deleteUserButton != null : "fx:id=\"deleteUserButton\" was not injected: check your FXML file 'AdminAction.fxml'.";
        assert userNameTextField != null : "fx:id=\"userNameTextField\" was not injected: check your FXML file 'AdminAction.fxml'.";
        assert showText != null : "fx:id=\"showText\" was not injected: check your FXML file 'AdminAction.fxml'.";
    }
}


    


