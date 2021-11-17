package photo.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import photo.app.Album;
import photo.app.Photo;
import photo.app.User;
import photo.app.UserManagement;

/**
 * This window opens once user chose to search pictures.
 * 
 * @author Amit Patel, Hideyo Sakamoto
 */
public class SearchPhotoController {

	/*
	 * button to logout
	 */
    @FXML
    private Button logoutBottun;

    /*
     * button to quit
     */
    @FXML
    private Button quitButton;
    
    /**
     * This button will lead the user back to the user's list of programs
     */
    @FXML
    private Button returnButton;
    
    /**
     * Create album from the photo results
     */
    @FXML
    private Button createAlbumFromResultsButton;
    
    /**
     * The Textfield where the user will input the new album name
     */
    @FXML
    private TextField newAlbumNameField;
    
    /**
     * List of the search results shown as images using ImageView
     */
    @FXML
    private ListView<ImageView> photoResults;
    
    /**
     * Keeps track of who is currently logged in
     */
    private User currentUser;
    
    /**
     * Variable that keeps track of the users
     */
    private UserManagement userManager;
    
    /**
     * ListView that will show thumbanisl and its caption when clicked on
     */
    @FXML
    private ListView<ImageView> photosAndCaptionsTable;
    
    /**
     * ObservableList that will help us build the ListView table
     */
    @FXML
    private ObservableList<ImageView> tableBuilder;
    
    /**
     * Will be used to show any problems encountered, such as the album name already existing
     */
    @FXML
    private Label showProblems;
    
	/**
	 * After a search is done, the search results are saved so that user can create
	 * an album from it if they choose to. These are the current results.
	 */
	public List<Photo> photoSearchResults;
	
    /**
     * Shows the photo caption of the selected photo in ListView
     */
    @FXML
    private Label textPhotoCaptionSelected;
    
    /**
     * TextField for photo caption
     */
    @FXML
    private TextField captionTextField;
    
    /**
     * Handles all of the actions from all of the button presses.
     * Logout button logs out, quit button quits, and return button returns the user back to the album list.
     * Create album list creates a new album from the search results assuming the input name is not taken.
     * 
     * @param event	Button pressed
     * @throws IOException	Throws any exceptions
     * @throws ClassNotFoundException If class is not found
     */
    @FXML
    void ButtonAction(ActionEvent event) throws IOException, ClassNotFoundException {
    	
    	showProblems.setText("");
    	
    	// logout
    	if(event.getSource()==logoutBottun) {
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/PhotosGUI.fxml"));
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	
    	// quit
    	if(event.getSource()==quitButton) {
    		Platform.exit();
    	}
    	
    	// Go back to album list
    	if(event.getSource()==returnButton)
    	{
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/UserSubSystem.fxml"));
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	
    	// Create album from the search results
    	if(event.getSource()==createAlbumFromResultsButton)
    	{
    		String newAlbumName = newAlbumNameField.getText();
    		
    		if (newAlbumName.trim().isEmpty())
    		{
    			return;
    		}
    		
    		if (currentUser.doesAlbumNameExist(newAlbumName))
    		{
    			showProblems.setText("Album name already exists");
    			return;
    		}
    		
    		Album albumToAdd = new Album(newAlbumName);
    		
    		for(int i = 0; i < photoSearchResults.size(); i++)
    		{
    			albumToAdd.addPhoto(photoSearchResults.get(i));
    		}
    		
    		showProblems.setText("Album created and added");
    		currentUser.addAlbum(albumToAdd);
    	}
    	
    	// Saves any changes done
    	UserManagement.writeApp(userManager);
    	
    	populateListView();
    }
    
    /**
     * Populates the listview with thumbnails of the photos in the album.
     * 
     * @throws IOException	Throws any exceptions
     */
    public void populateListView() throws IOException
    {    	
    	tableBuilder = FXCollections.observableArrayList();
    	
    	for (int i = 0; i < photoSearchResults.size(); i++)
    	{
    		InputStream stream = new FileInputStream(photoSearchResults.get(i).getPhotoPath());
    		Image image = new Image(stream);
    		ImageView imageView = new ImageView();
    		imageView.setImage(image);
    		
    		imageView.setX(10);
    		imageView.setY(10);
    		imageView.setFitWidth(125);
    		imageView.setFitHeight(125);
    		imageView.setPreserveRatio(true);
    		
    		tableBuilder.add(imageView);
    	}
    	
    	photosAndCaptionsTable.setItems(tableBuilder);
    }
    
    /**
     * Get the index of the selected item in listview.
     * This will help us get the caption of the photo.
     * 
     * @param event	ListView was clicked
     */
    public void highlightPhoto(MouseEvent event)
    {
    	showProblems.setText("");
    	
    	int selectedIndex = photosAndCaptionsTable.getSelectionModel().getSelectedIndex();
    	
    	if (selectedIndex == -1)
    	{
    		return;
    	}
    	
    	textPhotoCaptionSelected.setText("Photo Caption: " + photoSearchResults.get(selectedIndex).getPhotoCaption());
    }
    
    /**
     * Loads in data from the dat file and populates the listview with images.
     * 
     * @throws ClassNotFoundException	If class is not found
     * @throws IOException	Throws any exceptions
     */
    @FXML
    void initialize() throws ClassNotFoundException, IOException {
        
    	// Read the dat file
    	userManager = UserManagement.readApp();
    	currentUser = userManager.currentUser;
    	
    	photoSearchResults = new ArrayList<Photo>();
    	photoSearchResults.addAll(userManager.photoSearchResults);
    	
    	populateListView();
    }
}

