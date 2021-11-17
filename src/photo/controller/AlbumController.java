package photo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import photo.app.Album;
import photo.app.Photo;
import photo.app.User;
import photo.app.UserManagement;

/**
 * Controller class to takes care of actions associated with album.
 * Actions are follows: add, remove, and edit pictures, and go to 
 * pages to see all pictures.
 * Also copies picture from different album
 * 
 * @author Amit Patel, Hideyo Sakamoto
 */
public class AlbumController {
	
    /*
     * text shows user name
     */
    @FXML
    private Text userText;
    
    /**
     * Shows the photo caption of the selected photo in ListView
     */
    @FXML
    private Label textPhotoCaptionSelected;
    
    /**
     * Will be used to show any problems encountered, such as the same photo being uploaded.
     */
    @FXML
    private Label showProblems;

    /*
     * button to add picture
     */
    @FXML
    private Button addButton;
    
    /*
     *button to remove picture 
     */
    @FXML
    private Button removeButton;

    /*
     * button to display picture
     */
    @FXML
    private Button displayButton;

    /*
     * button to log out
     */
    @FXML
    private Button logoutBottun;

    /*
     * button to quit
     */
    @FXML
    private Button quitButton;
    
    /**
     * TextField for photo caption
     */
    @FXML
    private TextField captionTextField;
    
    /**
     * Button that will change the caption of the photo with whatever is entered in captionTextField
     */
    private Button captionButton;
    
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

    /*
     * button to go to previous page
     */
    @FXML
    private Button prePageButton;

    /*
     * button to go to next page
     */
    @FXML
    private Button nextPageButton;

    /*
     * button to search pictures
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
     * Keeps track of the selected album
     */
    private Album currentAlbum;
    
    /**
     * Button pressed when a tag is being added
     */
    @FXML
    private Button addTagButton;
    
    /**
     * Button pressed when a tag is being removed
     */
    @FXML
    private Button removeTagButton;
    
    /**
     * TextField that holds the tag type from user input
     */
    @FXML
    private TextField tagTypeTextField;
    
    /**
     * TextField that holds the tag value from user input
     */
    @FXML
    private TextField tagValueTextField;
    
    /**
     * Button that will return to the album list window
     */
    @FXML
    private Button returnButton;
    
    /**
     * TextField that holds the album name input
     */
    @FXML
    private TextField albumTextField;
    
    /**
     * Button that will copy the selected photo to the inputted album if the photo is not present there
     */
    @FXML
    private Button albumCopyButton;
    
    /**
     * Button that will move the selected photo to the inputted album if the photo is not present there
     */
    @FXML
    private Button albumMoveButton;
    
    /**
     * Button that will display a slideshow of the photos in the current album
     */
    @FXML
    private Button slideshowButton;
    
    /**
     * Will show the preset tags the user can choose from and add or remove from
     */
    @FXML
    private ComboBox<String> tagTypeChooser;
    
    /**
     * When pressed, will add the tag type input to the list of preset tag types if it's not present
     */
    @FXML
    private Button addTagTypeButton;
    
    /**
     * When pressed, will remove the tag type input to the list of preset tag types if it's present
     */
    @FXML
    private Button removeTagTypeButton;
    
    /**
     * Handles many of the actions that occur when a certain button is pressed.
     * When logoutButton is pressed, the user is logged out and the login window is opened.
     * When quitButton is pressed, the program exits.
     * When addButton is pressed, the photo is added if it is not present in the current album.
     * When removeButton is pressed, the selected photo is removed from the album.
     * When displaybutton is pressed, the selected photo is displayed in a new window along with its date, caption and tags
     * When addTagButton is pressed, the input tag pair is added to the selected photo if the combo is not already added
     * When removeTagButton is pressed, the input tag pair is removed from the selected photo if the combo is present
     * When albumCopyButton is pressed, the selected photo is copied to the target album if it exists and does not already
     * have the photo.
     * When albumCopyButton is pressed, the selected photo is moved to the target album if it exists and does not already
     * have the photo.
     * When slideshowButton is pressed, the photos in the album are displayed as a slideshow the user can go through
     * in a new window.
     * When addTagTypeButton is pressed, the inputted tag type is added to the tag type ComboBox if it is not there already
     * When removeTagTypeButton is pressed, the inputted tag type is removed from the tag type ComboBox if it is there
     * 
     * @param event	One of the buttons is clicked
     * @throws IOException	Throws any exceptions
     */
    @FXML
    void ButtonAction(ActionEvent event) throws IOException {

    	//logout
    	if(event.getSource()==logoutBottun) {
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
    	
    	// Go back to album list
    	if(event.getSource()==returnButton)
    	{
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/UserSubSystem.fxml"));
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	
    	// add
    	if(event.getSource()==addButton) {
    		// search picture and add to the album
    		
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Get image");
            fileChooser.getExtensionFilters().addAll(
            		new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"),
            		new FileChooser.ExtensionFilter("PNG Files", "*.png"),
            		new FileChooser.ExtensionFilter("BMP Files", "*.bmp"), 
                    new FileChooser.ExtensionFilter("GIF Files", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile == null)
            {
            	return;
            }
            String filePath = selectedFile.getPath();
            
            if (currentAlbum.isPhotoPathPresent(filePath))
            {
            	showProblems.setText("Photo is already present in album");
            	return;
            }
            
            Photo photoToAdd = new Photo(filePath);
            currentAlbum.addPhoto(photoToAdd);
            currentUser.checkIfNewPhotoIsInAnotherAlbum(photoToAdd, currentAlbum.getAlbumName());
    	}
    	
    	
    	// remove 
    	if(event.getSource()==removeButton) {
    		// remove pic
    		int selectedIndex = photosAndCaptionsTable.getSelectionModel().getSelectedIndex();
    		if (selectedIndex == -1)
    		{
    			return;
    		}
    		
    		String photoPathToRemove = currentAlbum.getPhoto(selectedIndex).getPhotoPath();
    		currentAlbum.removePhoto(photoPathToRemove);
    	}
    	
    	
    	// display 
    	if(event.getSource()==displayButton) {
    		// display selected pic
    		int selectedIndex = photosAndCaptionsTable.getSelectionModel().getSelectedIndex();
    		if (selectedIndex == -1)
    		{
    			return;
    		}
    		
    		Photo currentPhoto = currentAlbum.getPhoto(selectedIndex);
    		
    		userManager.currentPhoto = currentPhoto;
    		UserManagement.writeApp(userManager);
    		
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/OpenPhoto.fxml"));
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	
    	// Add a tag
    	if(event.getSource()==addTagButton)
    	{
    		String tagType = tagTypeTextField.getText();
    		String tagValue = tagValueTextField.getText();
    		
    		// Return if one of the fields are empty
    		if (tagType.trim().isEmpty() || tagValue.trim().isEmpty())
    		{
    			return;
    		}
    		
    		int selectedIndex = photosAndCaptionsTable.getSelectionModel().getSelectedIndex();
    		if (selectedIndex == -1)
    		{
    			return;
    		}
    		
    		Photo currentPhoto = currentAlbum.getPhoto(selectedIndex);
    		
    		// Return if the tag already exists. There should be no duplicate tags for the same photo.
    		if (currentPhoto.checkIfTagExists(tagType, tagValue))
    		{
    			showProblems.setText("Tag already exists for selected photo");
    			return;
    		}
    		
    		if (currentPhoto.checkIfLocationTagExists())
    		{
    			showProblems.setText("There can only be one location type per photo");
    			return;
    		}
    		
    		currentPhoto.addTag(tagType, tagValue);
    		showProblems.setText("Tag added");
    		
    		currentUser.makeChangesToPhotoAcrossAllAlbumsAddTag(currentPhoto.getPhotoPath(), 
    				currentAlbum.getAlbumName(), tagType, tagValue);
    	}
    	
    	// Remove a tag
    	if(event.getSource()==removeTagButton)
    	{
    		String tagType = tagTypeTextField.getText();
    		String tagValue = tagValueTextField.getText();
    		
    		// Return if one of the fields are empty
    		if (tagType.trim().isEmpty() || tagValue.trim().isEmpty())
    		{
    			return;
    		}
    		
    		int selectedIndex = photosAndCaptionsTable.getSelectionModel().getSelectedIndex();
    		if (selectedIndex == -1)
    		{
    			return;
    		}
    		
    		Photo currentPhoto = currentAlbum.getPhoto(selectedIndex);
    		
    		// Return if the selected photo does not have the tag
    		if (!currentPhoto.checkIfTagExists(tagType, tagValue))
    		{
    			showProblems.setText("Tag does not exist for selected photo");
    			return;
    		}
    		
    		currentPhoto.removeTag(tagType, tagValue);
    		showProblems.setText("Tag removed");
    		
    		currentUser.makeChangesToPhotoAcrossAllAlbumsRemoveTag(currentPhoto.getPhotoPath(), 
    				currentAlbum.getAlbumName(), tagType, tagValue);
    	}
    	
    	// Copy photo to another album
    	if (event.getSource()==albumCopyButton)
    	{
    		int selectedIndex = photosAndCaptionsTable.getSelectionModel().getSelectedIndex();
    		if (selectedIndex == -1)
    		{
    			return;
    		}
    		
    		Photo currentPhoto = currentAlbum.getPhoto(selectedIndex);
    		
    		String inputAlbumName = albumTextField.getText();
    		
    		// If album TextField is empty, return
    		if (inputAlbumName.trim().isEmpty())
    		{
    			return;
    		}
    		
    		// Check to see if the target album exists
    		if (!currentUser.doesAlbumNameExist(inputAlbumName))
    		{
    			showProblems.setText("Album name does not exist for this user");
    			return;
    		}
    		
    		// Check to see if the target album already contains the selected photo
    		if (currentUser.getAlbum(inputAlbumName).isPhotoPathPresent(currentPhoto.getPhotoPath()))
    		{
    			showProblems.setText("Album already contains selected photo");
    			return;
    		}
    		
    		currentUser.getAlbum(inputAlbumName).addPhoto(currentPhoto);
    	}
    	
    	// Move photo to another album
    	if (event.getSource()==albumMoveButton)
    	{
    		int selectedIndex = photosAndCaptionsTable.getSelectionModel().getSelectedIndex();
    		if (selectedIndex == -1)
    		{
    			return;
    		}
    		
    		Photo currentPhoto = currentAlbum.getPhoto(selectedIndex);
    		
    		String inputAlbumName = albumTextField.getText();
    		
    		// If album TextField is empty, return
    		if (inputAlbumName.trim().isEmpty())
    		{
    			return;
    		}
    		
    		// Check to see if the target album exists
    		if (!currentUser.doesAlbumNameExist(inputAlbumName))
    		{
    			showProblems.setText("Album name does not exist for this user");
    			return;
    		}
    		
    		// Check to see if the target album already contains the selected photo
    		if (currentUser.getAlbum(inputAlbumName).isPhotoPathPresent(currentPhoto.getPhotoPath()))
    		{
    			showProblems.setText("Album already contains selected photo");
    			return;
    		}
    		
    		currentUser.getAlbum(inputAlbumName).addPhoto(currentPhoto);
    		currentAlbum.removePhoto(currentPhoto.getPhotoPath());
    	}
    	
    	// Show slideshow of photos in the current album
    	if (event.getSource()==slideshowButton)
    	{
    		// If there are no photos, return
    		if (currentAlbum.getAlbumSize() == 0)
    		{
    			showProblems.setText("Cannot show a slideshow since there are no photos in this album");
    			return;
    		}
    		
    		UserManagement.writeApp(userManager);
    		
    		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/SlideshowPhotos.fxml"));
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.show();
    	}
    	
    	// Adding tag type to ComboBox
    	if (event.getSource()==addTagTypeButton)
    	{
    		String tagTypeToAdd = tagTypeTextField.getText();
    		
    		// If TextField is empty, return
    		if (tagTypeToAdd.trim().isEmpty())
    		{
    			return;
    		}
    		
    		// If tag type is already in the user's list of tag types, return;
    		if (currentUser.isTagTypeInList(tagTypeToAdd))
    		{
    			showProblems.setText("Tag Type already present in list");
    			return;
    		}
    		
    		currentUser.addNewTagTypeIfNotInList(tagTypeToAdd);
    		showProblems.setText("Tag Type added to list");
    		
    		populateComboBox();
    	}
    	
    	// Removing tag type from ComboBox
    	if (event.getSource()==removeTagTypeButton)
    	{
    		String tagTypeToRemove = tagTypeTextField.getText();
    		
    		// If TextField is empty, return
    		if (tagTypeToRemove.trim().isEmpty())
    		{
    			return;
    		}
    		
    		// If tag type is already in the user's list of tag types, return;
    		if (!currentUser.isTagTypeInList(tagTypeToRemove))
    		{
    			showProblems.setText("Tag Type is not in list");
    			return;
    		}
    		
    		currentUser.removeTagTypeFromList(tagTypeToRemove);
    		showProblems.setText("Tag Type removed from list");
    		
    		populateComboBox();
    	}
    	
    	// Saves any changes done
    	UserManagement.writeApp(userManager);
    	
    	populateListView();
    }
    
    /**
     * This method handles the event that the caption button is pressed.
     * The selected index is used to find the photo and the text from the textfield is used
     * to change the caption of the photo.
     * 
     * @param event	Event from pressing caption button
     * @throws IOException Throws any exceptions
     */
    @FXML
    public void changePhotoCaption(ActionEvent event) throws IOException
    {
		String newCaption = captionTextField.getText();
		
		int selectedIndex = photosAndCaptionsTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex == -1)
		{
			return;
		}
		
		Photo currentPhoto = currentAlbum.getPhoto(selectedIndex);
		
		currentPhoto.changePhotoCaption(newCaption);
		
		captionTextField.clear();
		

		showProblems.setText("Caption changed");
		
		// If the same photo is in another album, the caption is also changed
		currentUser.makeChangesToPhotoAcrossAllAlbumsChangeCaption(currentPhoto.getPhotoPath(), 
				currentAlbum.getAlbumName(), newCaption);
		
    	// Saves any changes done
		UserManagement.writeApp(userManager);
    	
		populateListView();
    }
    
    /**
     * Populates the listview with thumbnails of the photos in the album.
     * It is called whenever the user makes any changes to the album.
     * 
     * @throws IOException	Throws any exceptions
     */
    public void populateListView() throws IOException
    {
    	List<Photo> photosOfCurrentAlbum = currentAlbum.getPhotos();
    	
    	tableBuilder = FXCollections.observableArrayList();
    	
    	for (int i = 0; i < photosOfCurrentAlbum.size(); i++)
    	{
    		InputStream stream = new FileInputStream(photosOfCurrentAlbum.get(i).getPhotoPath());
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
     * Populates the tagTypeChoose ComboBox with all of the user's tag types
     */
    public void populateComboBox()
    {
    	List<String> tagTypes = currentUser.getTagTypes();
    	
    	tagTypeChooser.getItems().clear();
    	
    	tagTypeChooser.getItems().addAll(tagTypes);
    }
    
    /**
     * Sets the tagTypeTextField to what was selected in the ComboBox
     */
    @FXML
    public void tagTypeChosen()
    {
    	int tagTypeIndex = tagTypeChooser.getSelectionModel().getSelectedIndex();
    	
    	if (tagTypeIndex == -1)
    	{
    		return;
    	}
    	
    	tagTypeTextField.setText(currentUser.getTagTypes().get(tagTypeIndex));
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
    	
    	captionTextField.setText(currentAlbum.getPhotos().get(selectedIndex).getPhotoCaption());
    	textPhotoCaptionSelected.setText("Photo Caption: " + currentAlbum.getPhotos().get(selectedIndex).getPhotoCaption());
    }

    /**
     * Loads in data from the dat file and populates the listview with images.
     * 
     * @throws ClassNotFoundException	If class is not found
     * @throws IOException	Throws any exceptions
     */
    @FXML
    void initialize() throws ClassNotFoundException, IOException {
        assert userText != null : "fx:id=\"userText\" was not injected: check your FXML file 'Album.fxml'.";
        assert addButton != null : "fx:id=\"addButton\" was not injected: check your FXML file 'Album.fxml'.";
        assert removeButton != null : "fx:id=\"removeButton\" was not injected: check your FXML file 'Album.fxml'.";
        assert logoutBottun != null : "fx:id=\"logoutBottun\" was not injected: check your FXML file 'Album.fxml'.";
        assert quitButton != null : "fx:id=\"quitButton\" was not injected: check your FXML file 'Album.fxml'.";
        assert prePageButton != null : "fx:id=\"prePageButton\" was not injected: check your FXML file 'Album.fxml'.";
        assert nextPageButton != null : "fx:id=\"nextPageButton\" was not injected: check your FXML file 'Album.fxml'.";
        assert searchButton != null : "fx:id=\"searchButton\" was not injected: check your FXML file 'Album.fxml'.";
        assert captionButton != null : "fx:id=\"captionButton\" was not injected: check your FXML file 'Album.fxml'.";
        
    	// Read the dat file
    	userManager = UserManagement.readApp();
    	currentUser = userManager.currentUser;
    	currentAlbum = userManager.currentAlbum;
    	
    	userText.setText(currentAlbum.getAlbumName() + " Photos");
    	
    	populateListView();
    	populateComboBox();
    }
}
