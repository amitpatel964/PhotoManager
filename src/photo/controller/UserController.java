package photo.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import photo.app.Album;
import photo.app.Photo;
import photo.app.User;
import photo.app.UserManagement;

/**
 * This window appears once user logs in with proper name.
 * It allows user to create, delete, and open album.
 * Open album or Search button opens a new separate window accordingly.
 * It also shows the list of albums the use own.
 * 
 * @author Amit Patel, Hideyo Sakamoto
 */
public class UserController {

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
	 * text field to show message
	 */
	@FXML
    private Text textToShow;
	
	/*
	 * create album
	 */
    @FXML
    private Button createButton;

    /*
     * rename album
     */
    @FXML
    private Button renameButton;
    
    /*
     * delete album
     */
    @FXML
    private Button deleteButton;

    /*
     * open album
     */
    @FXML
    private Button openButton;

    /*
     * album name which you want to change
     */
    @FXML
    private TextField oldAlbumText;

    /*
     * new album name which will replace old name
     */
    @FXML
    private TextField newAlbumText;

    /*
     * button to search photo
     */
    @FXML
    private Button searchButton;
    
    /**
     * Keeps track of who is currently logged in
     */
    private User currentUser;
    
    /**
     * Variable that keeps track of the users
     */
    private UserManagement userManager;
    
    /**
     * Observable List for the list of albums for the user
     */
    @FXML
    private ObservableList<Album> observAlbumList;
    
    /**
     * ListView for the albums for the user
     */
    @FXML
    private ListView<Album> albumListView;
    
    /**
     * Will search for photos between two dates when pressed
     */
    @FXML
    private Button searchDate;
    
    /**
     * Will search for photos with a certain tag
     */
    @FXML
    private Button searchOneTag;
    
    /**
     * Will search for photos with both of the tags specified
     */
    @FXML
    private Button searchTwoTagsAnd;
    
    /**
     * Will search for photos with at least one of the tags specified
     */
    @FXML
    private Button searchTwoTagsOr;
    
    /**
     * Input older date to find photos within a date range. Should be formatted as YYYY/MM/DD
     */
    @FXML
    private TextField searchFromDate;
    
    /**
     * Input earlier date to find photos within a date range. Should be formatted as YYYY/MM/DD
     */
    @FXML
    private TextField searchToDate;
    
    /**
     * Input tag type for one tag search
     */
    @FXML
    private TextField searchOneTagType;
    
    /**
     * Input tag value for one tag search
     */
    @FXML
    private TextField searchOneTagValue;
    
    /**
     * Input tag type for two tag search first entry
     */
    @FXML
    private TextField searchTwoTagTypeFirst;
    
    /**
     * Input tag value for two tag search first entry
     */
    @FXML
    private TextField searchTwoTagValueFirst;
    
    /**
     * Input tag type for two tag search second entry
     */
    @FXML
    private TextField searchTwoTagTypeSecond;
    
    /**
     * Input tag value for two tag search second entry
     */
    @FXML
    private TextField searchTwoTagValueSecond;
    
    /*
     * album 
     */
    private Album album;

    /**
     * This method takes care of all of the possible buttons the user can click.
     * It handles the user logging out, the user quitting the program, opening, deleting, or adding an album,
     * editing an album, and the various search functions.
     * 
     * @param event	Keeps track of which button is clicked, such as create album
     * @throws IOException	Throws any exceptions
     * @throws ClassNotFoundException	Determines if the class is not found
     */
    @FXML
    void ButtonAction(ActionEvent event) throws IOException, ClassNotFoundException {
    	
    	// Resets the text counter
    	textToShow.setText("");
    	
    	//logout
    	if(event.getSource()==logoutButton) {
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/PhotosGUI.fxml"));
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	
    	//quit
    	if(event.getSource()==quitButton) {
    		Platform.exit();
    	}
    	
    	// Get the input fields for album names
    	String oldName = oldAlbumText.getText();
    	String newName = newAlbumText.getText();
    	
    	// create album
    	if(event.getSource () == createButton) {
    		// checks if the name is not taken
    		if(currentUser.doesAlbumNameExist(oldName)) {
    			textToShow.setText("Name taken");
    			return;
    		}else {
    			album = new Album(oldName);
    			currentUser.addAlbum(album);
    			textToShow.setText("Album added");
    		}
    	}
    	
    	// rename album
    	if(event.getSource () == renameButton) {
    		// Can rename only existing name 
    		if(!(currentUser.doesAlbumNameExist(oldName))) {
    			textToShow.setText("Name doesn't exist");
    			return;
    		}else {
    			// get an instance of Album and set new name with given text
    			album = currentUser.getAlbum(oldName);
    			album.changeAlbumName(newName);
    			textToShow.setText("Album name changed");
    			newAlbumText.setText("Rename Album to This");
    		}
    	}
    	
    	
    	// delete album
    	if(event.getSource () == deleteButton) {
        		// checks if the name exists
        		if(!(currentUser.doesAlbumNameExist(oldName))) {
        			textToShow.setText("Name doesn't exist");
        			return;
        		}else {
        			//album = new Album(oldName);
        			currentUser.removeAlbum(oldName);
        			textToShow.setText("Album removed");
        		}
    	}
    	
    	// open album
    	if(event.getSource () == openButton) {
    		// checks if the name exists
    		if(!(currentUser.doesAlbumNameExist(oldName))) {
    			textToShow.setText("Name doesn't exist");
    		}
    		else
    		{
    			userManager.currentAlbum = currentUser.getAlbum(oldName);
    			UserManagement.writeApp(userManager);
    			
        		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/Album.fxml"));
        		
        		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        		stage.setTitle("User: "+ currentUser.getUserName());
        		Scene scene = new Scene(root);
        		stage.setScene(scene);
        		stage.show();
    		}
    		
    	}
    	
    	// Search button for date clicked
    	if(event.getSource()==searchDate) {
    		String fromDate = searchFromDate.getText();
    		String toDate = searchToDate.getText();
    		
    		// Return if the input fields are empty
    		if (fromDate.trim().isEmpty() || toDate.trim().isEmpty())
    		{
    			return;
    		}
    		
    		// Check if date entry is valid
    		if (fromDate.charAt(4) != '-' || fromDate.charAt(7) != '-' || toDate.charAt(4) != '-' || toDate.charAt(7) != '-')
    		{
    			textToShow.setText("Invalid date entry");
    			searchFromDate.setText("From Date: YYYY-MM-DD");
    			searchToDate.setText("To Date: YYYY-MM-DD");
    			return;
    		}
    		
    		LocalDate fromLocalDate = null;
    		LocalDate toLocalDate = null;
    		
    		try
    		{
    			fromLocalDate = LocalDate.parse(fromDate);
    			toLocalDate = LocalDate.parse(toDate);
    		}
    		catch (DateTimeParseException e)
    		{
    			textToShow.setText("Invalid date entry");
    			searchFromDate.setText("From Date: YYYY-MM-DD");
    			searchToDate.setText("To Date: YYYY-MM-DD");
    			return;
    		}
    		
    		List<Photo> searchResults = new ArrayList<Photo>();
    		List<Album> userAlbumList = currentUser.getListOfAlbums();
    		
    		// If there are any duplicate photos in the search, we only need one of them
    		List<String> photoPathsFromResults = new ArrayList<String>();
    		
    		for (int i = 0; i < userAlbumList.size(); i++)
    		{
    			Album currentAlbum = userAlbumList.get(i);
    			List<Photo> currentAlbumPhotos = currentAlbum.getPhotos();
    			
    			for (int j = 0; j < currentAlbumPhotos.size(); j++)
    			{
    				Photo currentPhoto = currentAlbumPhotos.get(j);
    				
    				if (currentPhoto.isPhotoWithinDateRange(fromLocalDate, toLocalDate) && 
    						!photoPathsFromResults.contains(currentPhoto.getPhotoPath()))
    				{
    					searchResults.add(currentPhoto);
    					photoPathsFromResults.add(currentPhoto.getPhotoPath());
    				}
    			}
    		}
    		
    		if (searchResults.size() == 0)
    		{
    			textToShow.setText("No photos found");
    			return;
    		}
    		
    		userManager.photoSearchResults.clear();
    		userManager.photoSearchResults.addAll(searchResults);
    		
			UserManagement.writeApp(userManager);
			
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/Search.fxml"));
    		
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		stage.setTitle("User: "+ currentUser.getUserName());
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	
    	// Search button for one tag clicked
    	if(event.getSource()==searchOneTag) {
    		String tagType = searchOneTagType.getText();
    		String tagValue = searchOneTagValue.getText();
    		
    		// Return if the input fields are empty
    		if (tagType.trim().isEmpty() || tagValue.trim().isEmpty())
    		{
    			return;
    		}
    		
    		List<Photo> searchResults = new ArrayList<Photo>();
    		List<Album> userAlbumList = currentUser.getListOfAlbums();
    		
    		// If there are any duplicate photos in the search, we only need one of them
    		List<String> photoPathsFromResults = new ArrayList<String>();
    		
    		for (int i = 0; i < userAlbumList.size(); i++)
    		{
    			Album currentAlbum = userAlbumList.get(i);
    			List<Photo> currentAlbumPhotos = currentAlbum.getPhotos();
    			
    			for (int j = 0; j < currentAlbumPhotos.size(); j++)
    			{
    				Photo currentPhoto = currentAlbumPhotos.get(j);
    				
    				if (currentPhoto.searchTagsOnePair(tagType, tagValue) && 
    						!photoPathsFromResults.contains(currentPhoto.getPhotoPath()))
    				{
    					searchResults.add(currentPhoto);
    					photoPathsFromResults.add(currentPhoto.getPhotoPath());
    				}
    			}
    		}
    		
    		if (searchResults.size() == 0)
    		{
    			textToShow.setText("No photos found");
    			return;
    		}
    		
    		userManager.photoSearchResults.clear();
    		userManager.photoSearchResults.addAll(searchResults);
    		
			UserManagement.writeApp(userManager);
			
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/Search.fxml"));
    		
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		stage.setTitle("User: "+ currentUser.getUserName());
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	
    	// Search button for two tag and clicked
    	if(event.getSource()==searchTwoTagsAnd) {
    		String firstTagType = searchTwoTagTypeFirst.getText();
    		String firstTagValue = searchTwoTagValueFirst.getText();
    		String secondTagType = searchTwoTagTypeSecond.getText();
    		String secondTagValue = searchTwoTagValueSecond.getText();
    		
    		// Return if the input fields are empty
    		if (firstTagType.trim().isEmpty() || firstTagValue.trim().isEmpty() || 
    				secondTagType.trim().isEmpty() || secondTagValue.trim().isEmpty())
    		{
    			return;
    		}
    		
    		List<Photo> searchResults = new ArrayList<Photo>();
    		List<Album> userAlbumList = currentUser.getListOfAlbums();
    		
    		// If there are any duplicate photos in the search, we only need one of them
    		List<String> photoPathsFromResults = new ArrayList<String>();
    		
    		for (int i = 0; i < userAlbumList.size(); i++)
    		{
    			Album currentAlbum = userAlbumList.get(i);
    			List<Photo> currentAlbumPhotos = currentAlbum.getPhotos();
    			
    			for (int j = 0; j < currentAlbumPhotos.size(); j++)
    			{
    				Photo currentPhoto = currentAlbumPhotos.get(j);
    				
    				if (currentPhoto.searchTagsTwoPairsAnd(firstTagType, firstTagValue, secondTagType, secondTagValue) && 
    						!photoPathsFromResults.contains(currentPhoto.getPhotoPath()))
    				{
    					searchResults.add(currentPhoto);
    					photoPathsFromResults.add(currentPhoto.getPhotoPath());
    				}
    			}
    		}
    		
    		if (searchResults.size() == 0)
    		{
    			textToShow.setText("No photos found");
    			return;
    		}
    		
    		userManager.photoSearchResults.clear();
    		userManager.photoSearchResults.addAll(searchResults);
    		
			UserManagement.writeApp(userManager);
			
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/Search.fxml"));
    		
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		stage.setTitle("User: "+ currentUser.getUserName());
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	
    	// Search button for two tag or clicked
    	if(event.getSource()==searchTwoTagsOr) {
    		String firstTagType = searchTwoTagTypeFirst.getText();
    		String firstTagValue = searchTwoTagValueFirst.getText();
    		String secondTagType = searchTwoTagTypeSecond.getText();
    		String secondTagValue = searchTwoTagValueSecond.getText();
    		
    		// Return if the input fields are empty
    		if (firstTagType.trim().isEmpty() || firstTagValue.trim().isEmpty() || 
    				secondTagType.trim().isEmpty() || secondTagValue.trim().isEmpty())
    		{
    			return;
    		}
    		
    		List<Photo> searchResults = new ArrayList<Photo>();
    		List<Album> userAlbumList = currentUser.getListOfAlbums();
    		
    		// If there are any duplicate photos in the search, we only need one of them
    		List<String> photoPathsFromResults = new ArrayList<String>();
    		
    		for (int i = 0; i < userAlbumList.size(); i++)
    		{
    			Album currentAlbum = userAlbumList.get(i);
    			List<Photo> currentAlbumPhotos = currentAlbum.getPhotos();
    			
    			for (int j = 0; j < currentAlbumPhotos.size(); j++)
    			{
    				Photo currentPhoto = currentAlbumPhotos.get(j);
    				
    				if (currentPhoto.searchTagsTwoPairsOr(firstTagType, firstTagValue, secondTagType, secondTagValue) && 
    						!photoPathsFromResults.contains(currentPhoto.getPhotoPath()))
    				{
    					searchResults.add(currentPhoto);
    					photoPathsFromResults.add(currentPhoto.getPhotoPath());
    				}
    			}
    		}
    		
    		if (searchResults.size() == 0)
    		{
    			textToShow.setText("No photos found");
    			return;
    		}
    		
    		userManager.photoSearchResults.clear();
    		userManager.photoSearchResults.addAll(searchResults);
    		
			UserManagement.writeApp(userManager);
			
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/Search.fxml"));
    		
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		stage.setTitle("User: "+ currentUser.getUserName());
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	
    	// Saves any changes done
    	UserManagement.writeApp(userManager);
    	
    	populateAlbumListview();
    	
    	textToShow.setText("");
    	
    }// end of event
    
    /**
     * This method will be used to populate the album ListView
     */
    public void populateAlbumListview()
    {
    	List<Album> albums = currentUser.getListOfAlbums();
    	
    	observAlbumList = FXCollections.observableArrayList();
    	
    	for (int i = 0; i < albums.size(); i++)
    	{
    		observAlbumList.add(albums.get(i));
    	}
    	
    	albumListView.setItems(observAlbumList);
    }
    
    /**
     * When an album is clicked, the oldAlbumText field is populated with the name of the album
     * that was clicked. This lets the user easily pick an album to remove, edit or open.
     * 
     * @param event An album in the album list is clicked on
     */
    public void highlightAlbum(MouseEvent event)
    {
    	Album album = (Album) albumListView.getSelectionModel().getSelectedItem();
    	
    	if (albumListView.getSelectionModel().getSelectedItem() == null)
    	{
    		return;
    	}
    	
    	oldAlbumText.setText(album.getAlbumName());
    }
    
    /**
     * This method is done when the window initializes
     * 
     * @throws IOException Throws any exceptions
     * @throws ClassNotFoundException If class is not found
     */
    @FXML
    void initialize() throws ClassNotFoundException, IOException {
    	
    	// Read the dat file
    	userManager = UserManagement.readApp();
    	currentUser = userManager.currentUser;
    	
    	populateAlbumListview();
    }
}