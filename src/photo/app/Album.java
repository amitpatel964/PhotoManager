package photo.app;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will handle the album itself, which consists of the album name and 
 * a list of pictures of the album. We will be able to add and remove pictures.
 * 
 * @author Amit Patel, Hideyo Sakamoto
 *
 */

public class Album implements Serializable{
	
	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = -7545450350976397396L;

	/**
	 * This list will contain all of the photos in the album
	 */
	private List<Photo> photosOfAlbum;
	
	/**
	 * Name of the album
	 */
	private String albumName;
	
	/**
	 * Keeps tracks of all of the image paths, or image directories, for all photos within the album.
	 * All paths should be unique within an album.
	 */
	private List<String> allPhotoPathsForAlbum;
	
	/**
	 * Contains the date of the oldest photo
	 */
	private LocalDate oldestDate;
	
	/**
	 * Contains the date of the earliest photo
	 */
	private LocalDate earliestDate;
	
	/**
	 * Constructor to create a new album
	 * 
	 * @param albumName	The name of the album
	 */
	public Album(String albumName)
	{
		this.albumName = albumName;
		photosOfAlbum = new ArrayList<Photo>();
		allPhotoPathsForAlbum = new ArrayList<String>();
		oldestDate = null;
		earliestDate = null;
	}
	
	/**
	 * Takes an index and returns the photo at the index
	 * @param index the index of photo in album
	 * @return photo at the given index
	 */
	public Photo getPhoto(int index){
		return this.photosOfAlbum.get(index);
	}
	
	/**
	 * Whenever a photo is added or removed from an album, this method will be called to keep track of the
	 * oldest date of the photo in the album.
	 */
	public void updateOldestDate()
	{
		// OldestDate is set to 0 if the album is currently empty
		if (photosOfAlbum.size() == 0)
		{
			oldestDate = null;
			return;
		}
		
		oldestDate = photosOfAlbum.get(0).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		for (int i = 0; i < photosOfAlbum.size(); i++)
		{
			LocalDate currentDate = photosOfAlbum.get(i).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			if (currentDate.isBefore(oldestDate))
			{
				oldestDate = currentDate;
			}
		}
	}
	
	/**
	 * Whenever a photo is added or removed from an album, this method will be called to keep track of the
	 * earliest date of the photo in the album.
	 */
	public void updateEarliestDate()
	{
		// EarliestDate is set to 0 if the album is currently empty
		if (photosOfAlbum.size() == 0)
		{
			earliestDate = null;
			return;
		}
		
		earliestDate = photosOfAlbum.get(0).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		for (int i = 0; i < photosOfAlbum.size(); i++)
		{
			LocalDate currentDate = photosOfAlbum.get(i).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			if (currentDate.isAfter(earliestDate))
			{
				earliestDate = currentDate;
			}
		}
	}
	
	/**
	 * Changes the name of the album
	 * 
	 * @param newAlbumName	The name of the album to change to
	 */
	public void changeAlbumName(String newAlbumName)
	{
		this.albumName = newAlbumName;
	}
	
	/**
	 * Returns the name of the album
	 * 
	 * @return	Returns the name of the album
	 */
	public String getAlbumName()
	{
		return this.albumName;
	}
	
	/**
	 * Returns the size of the current album
	 * 
	 * @return	The size of the album as an int
	 */
	public int getAlbumSize()
	{
		return photosOfAlbum.size();
	}
	
	/**
	 * Adds a photo to the album
	 * 
	 * @param photoToAdd	The picture that will be added to the album
	 */
	public void addPhoto(Photo photoToAdd)
	{
		photosOfAlbum.add(photoToAdd);
		addPhotoPath(photoToAdd.getPhotoPath());
		updateOldestDate();
		updateEarliestDate();
	}
	
	/**
	 * Removes a picture from the album
	 * 
	 * @param photoPathToRemove	Path of the picture to remove
	 */
	public void removePhoto(String photoPathToRemove)
	{
		for (int i = 0; i < photosOfAlbum.size(); i++)
		{
			if (photosOfAlbum.get(i).getPhotoPath().equals(photoPathToRemove))
			{
				photosOfAlbum.remove(i);
				removePhotoPath(photoPathToRemove);
				break;
			}
		}
		updateOldestDate();
		updateEarliestDate();
	}
	
	/**
	 * Returns the pictures of the album
	 * 
	 * @return	Pictures of the album
	 */
	public List<Photo> getPhotos()
	{
		return this.photosOfAlbum;
	}
	
	/**
	 * Returns certain pieces of info of the album as a string
	 * 
	 * @return A string
	 */
	public String toString()
	{
		if (this.getAlbumSize() == 0)
		{
			return "Name: " + this.albumName + " -Size: " + this.getAlbumSize(); 
		}
		
		return "Name: " + this.albumName + " -Size: " + this.getAlbumSize() + " -Date Range: " + 
		oldestDate.toString() + " to " + earliestDate.toString();
	}
	
	/**
	 * Adds the path for the photo to the list of paths.
	 * All paths need to be unique within an album.
	 * 
	 * @param photoPath	Name of the photo path that is going to be added
	 */
	public void addPhotoPath(String photoPath)
	{
		allPhotoPathsForAlbum.add(photoPath);
	}
	
	/**
	 * Checks to see if a photo path is present in the list of paths.
	 * All paths need to be unique within an album.
	 * 
	 * @param photoPath	New potential photo path
	 * @return	True if the path is used by another photo, false otherwise
	 */
	public boolean isPhotoPathPresent(String photoPath)
	{
		for (String path: allPhotoPathsForAlbum)
		{
			if (path.equals(photoPath))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Removes a photo's path from the list of paths if a photo is removed from an album.
	 * 
	 * @param photoPath	Name of the photo path that is going to be removed
	 */
	public void removePhotoPath(String photoPath)
	{
		for (int i = 0; i < allPhotoPathsForAlbum.size(); i++)
		{
			if (allPhotoPathsForAlbum.get(i).equals(photoPath))
			{
				allPhotoPathsForAlbum.remove(i);
				break;
			}
		}
	}
}


