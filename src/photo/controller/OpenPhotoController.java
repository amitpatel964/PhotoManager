package photo.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import photo.app.Album;
import photo.app.Photo;
import photo.app.UserManagement;

/**
 * This GUI window shows up when a picture is chosen and displayed.
 * When initializing, this class takes an instance of picture, and 
 * sets up the window with information such as, name, date, and tags
 * according to the selected picture
 * 
 * @author Amit Patel, Hideyo Sakamoto
 */
public class OpenPhotoController {

	/*
	 * image to show 
	 */
    @FXML
    private ImageView imageDisplay;

    /*
     * photo label 
     */
    @FXML
    private Label photoLabel;

    /*
     * actual text field to assign photo name
     */
    @FXML
    private Text photoText;

    /*
     * caption label
     */
    @FXML
    private Label captionLabel;

    /*
     * actual text field to show caption
     */
    @FXML
    private Text captionText;

    /*
     * tag label
     */
    @FXML
    private Label tagLabel;

    /*
     * actual text field to show tags
     */
    @FXML
    private Text tagText;
    
    /**
     * This variable holds all of the data for all users
     */
    private UserManagement userManager;
    
    /**
     * This will be used to refer to the current photo and display it
     */
    private Photo currentPhoto;
    
    /**
     * Button that will let the user go back to the list of photos
     */
    @FXML
    private Button returnButton;
    
    /**
     * Displays the title for the display window
     */
    @FXML
    private Label displayTitle;
    
    /**
     * Format for how the date will be printed
     */
    String dateFormatPattern = "yyyy-MM-dd";
    
    /**
     * This method is called whenever the return button is pressed.
     * It will let the user exit the image display window and go back to the list of photos.
     * 
     * @param event	The button was pressed
     * @throws IOException	Throws any exceptions
     */
    @FXML
    public void ReturnToPhotoList(ActionEvent event) throws IOException
    {
		Parent root = FXMLLoader.load(getClass().getResource("/photo/view/Album.fxml"));
		
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setTitle("User: "+ userManager.currentUser.getUserName());
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }
    
    /**
     * This method is responsible for setting up photo information such as
     * date, caption, and tags.
     * @throws FileNotFoundException Throws any exceptions
     */
	public void PhotoSetUp() throws FileNotFoundException {
		Date date = currentPhoto.getDate();
		DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);  
		String strDate = dateFormat.format(date);  
		photoText.setText(strDate);	// it's for date, not name

		// set caption
		captionText.setText(currentPhoto.getPhotoCaption());
		
		// getting tags
		String tags = "";
		if(currentPhoto.getTagList().size()==0) {
			tagText.setText("None");
			
		}else {
			tags = tags + currentPhoto.getTagList().get(0);
			for(int i=1; i< currentPhoto.getTagList().size(); i++) {
				tags = tags + ", " + currentPhoto.getTagList().get(i);
			}
			
		}
		
		// set tags
		tagText.setText(tags);
		
		InputStream stream = new FileInputStream(currentPhoto.getPhotoPath());;
		
		Image image = new Image(stream);
		imageDisplay.setImage(image);
		
		displayTitle.setText(userManager.currentAlbum.getAlbumName() + " Photo Display");
	}
	
	/**
	 * When the window is initialized, the save data is loaded and the selected photo
	 * will be loaded into the window along with its info.
	 * 
	 * @throws ClassNotFoundException	If class is not found
	 * @throws IOException	Throws any exceptions
	 */
	@FXML
	void initialize() throws ClassNotFoundException, IOException
	{
    	// Read the dat file
    	userManager = UserManagement.readApp();
    	currentPhoto = userManager.currentPhoto;
    	
    	PhotoSetUp();
	}

}

