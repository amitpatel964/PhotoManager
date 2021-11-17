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
 * This Controller will handle the slideshow function for the selected album.
 * The images are shown one by one and the user can navigate between photos using two buttons.
 * The buttons may be disabled if the slideshow is currently at the beginning or end of the album.
 * 
 * @author Amit Patel, Hideyo Sakamoto
 *
 */

public class SlideshowController {
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
     * Keeps track of the which album is currently having a slideshow
     */
    private Album currentAlbum;
    
    /**
     * Button that will let the user go back to the list of photos
     */
    @FXML
    private Button returnButton;
    
    /**
     * Keeps track of where in the album we are.
     * This value should never be below 0 or greater than or equal to the size of the album.
     */
    private int photoCounter;
    
    /**
     * The button that will decrement the photo count
     */
    @FXML
    private Button previousButton;
    
    /**
     * The button that will increment the photo count
     */
    @FXML
    private Button nextButton;
    
    /**
     * Title for the window, keeps track of where in the slideshow the user is
     */
    @FXML
    private Label slideshowTitleCount;
    
    /**
     * Date format as string
     */
    String dateFormatPattern = "yyyy-MM-dd";
    
    /**
     * Handles the action when the previous button is pressed.
     * The slideshow will go back to the previous photo.
     * The nextButton is enabled and the previousButton is disabled if it is at the beginning of the album.
     * 
     * @param event Previous button is pressed
     * @throws FileNotFoundException	File not found
     */
    @FXML
    public void slideshowPreviousPhoto(ActionEvent event) throws FileNotFoundException
    {
    	nextButton.setDisable(false);
    	photoCounter--;
    	if (photoCounter <= 0)
    	{
    		previousButton.setDisable(true);
    	}
    	
    	currentPhoto = currentAlbum.getPhoto(photoCounter);
    	
    	PhotoSetUp();
    }
    
    /**
     * Handles the action when the next button is pressed
     * The slideshow will go to the next photo.
     * The previousButton is enabled and the nextButton is disabled if it is at the beginning of the album.
     * 
     * @param event	Next button is pressed
     * @throws FileNotFoundException	File not found
     */
    @FXML
    public void slideshowNextPhoto(ActionEvent event) throws FileNotFoundException
    {
    	previousButton.setDisable(false);
    	
    	photoCounter++;
    	if (photoCounter >= currentAlbum.getAlbumSize() - 1)
    	{
    		nextButton.setDisable(true);
    	}
    	
    	currentPhoto = currentAlbum.getPhoto(photoCounter);
    	
    	PhotoSetUp();
    }
    
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
		
		int numberToShow = photoCounter + 1;
		slideshowTitleCount.setText(currentAlbum.getAlbumName() + " Slideshow: Photo " + numberToShow);
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
    	currentAlbum = userManager.currentAlbum;
    	currentPhoto = currentAlbum.getPhoto(0);
    	photoCounter = 0;
    	
    	previousButton.setDisable(true);
    	if (currentAlbum.getAlbumSize() == 1)
    	{
    		nextButton.setDisable(true);
    	}
    	
    	PhotoSetUp();
	}
}
