package photo.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will handle the information for an individual user.
 * This class will contain a user's username and their albums.
 * 
 * @author Amit Patel, Hideyo Sakamoto
 *
 */

public class User implements Serializable{
	
	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = -3748439693527033733L;

	/**
	 *  name used to login
	 */
	private String name;
	
	/**
	 * Keeps track of all of the albums a user has.
	 */
	private List<Album> albumList;
	
	/**
	 * The list of tag types for the user. Initially, there is only location and person,
	 * but the user is able to define their own types.
	 */
	private List<String> tagTypes;
	
	/**
	 * Keeps track of the album the user is currently looking at
	 */
	public Album currentAlbum;
	
	/**
	 * constructor makes an instance of user with given name only as password is optional
	 * @param name
	 */
	public User(String name) {
		this.name = name;
		albumList = new ArrayList<Album>();
		tagTypes = new ArrayList<String>();
		tagTypes.add("Location");
		tagTypes.add("Person");
		currentAlbum = null;
	}
	
	/**
	 * The user is able to select and add to a preset list of tag types.
	 * The user will be able to pick it in the future.
	 * If the tag type is already present, it will not be added again.
	 * 
	 * @param tagType	The tag type that may be added to the list of tag types
	 */
	public void addNewTagTypeIfNotInList(String tagType)
	{
		tagTypes.add(tagType);
	}
	
	/**
	 * Removes a tag type from the list of preset tag types.
	 * 
	 * @param tagType The tag type that is being removed
	 */
	public void removeTagTypeFromList(String tagType)
	{
		for (int i = 0; i < tagTypes.size(); i++)
		{
			if (tagTypes.get(i).equalsIgnoreCase(tagType))
			{
				tagTypes.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Checks to see if a tag type is in the list of tag types
	 * 
	 * @param tagType The tag type that is being checked
	 * @return True if the tag is in the list, false otherwise
	 */
	public boolean isTagTypeInList(String tagType)
	{
		for (int i = 0; i < tagTypes.size(); i++)
		{
			if (tagTypes.get(i).equalsIgnoreCase(tagType))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Return the list og tag types
	 * 
	 * @return	Return the list of tag types
	 */
	public List<String> getTagTypes()
	{
		return tagTypes;
	}
	
	/**
	 * Changes user name
	 * @param name The new name for user
	 */
	public void setUserName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns user name
	 * @return user name
	 */
	public String getUserName() {
		return this.name;
	}
	
	/**
	 * Add album to the current list of albums 
	 * 
	 * @param albumToAdd	New album that is being added
	 */
	public void addAlbum(Album albumToAdd)
	{
		albumList.add(albumToAdd);
	}
	
	/**
	 * This method is used to check if an album name already taken by another album for a user.
	 * All album names should be unique for a user.
	 * 
	 * @param albumNameToCheck	Name that is being checked
	 * @return	True if there is another album with the same name for the user, false otherwise
	 */
	public boolean doesAlbumNameExist(String albumNameToCheck)
	{
		for (int i = 0; i < albumList.size(); i++)
		{
			if (albumList.get(i).getAlbumName().equalsIgnoreCase(albumNameToCheck))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the album that the user is trying to access
	 * 
	 * @param name	Name of the album
	 * @return	The album the user is trying to access
	 */
	public Album getAlbum(String name)
	{
		Album albumToReturn = null;
		
		for (int i = 0; i < albumList.size(); i++)
		{
			if (albumList.get(i).getAlbumName().equalsIgnoreCase(name))
			{
				albumToReturn = albumList.get(i);
			}
		}
		
		return albumToReturn;
	}
	
	/**
	 * This method allows a user to remove an album from their list of albums
	 * 
	 * @param albumNameToRemove	Name of album to remove from list
	 */
	public void removeAlbum(String albumNameToRemove)
	{
		for (int i = 0; i < albumList.size(); i++)
		{
			if (albumList.get(i).getAlbumName().equalsIgnoreCase(albumNameToRemove))
			{
				albumList.remove(i);
				break;
			}
		}
	}
	
	public List<Album> getListOfAlbums()
	{
		return this.albumList;
	}
	
	/**
	 * Create a new album from the search results.
	 * The results can be a result of data range or tag values.
	 * 
	 * @param filteredPhotos
	 * @param newAlbumName
	 */
	public void createAlbumFromSearchResults(List<Photo> filteredPhotos, String newAlbumName)
	{
		Album albumToAdd = new Album(newAlbumName);
		
		albumList.add(albumToAdd);
	}
	
	/**
	 * Return the name as a string
	 * 
	 * @return	The name as a string
	 */
	public String toString()
	{
		return this.name;
	}
	
	/**
	 * Whenever a tag is added to a photo, this method will be called to see if there are copies
	 * of this photo across other albums. This is checked using the path of the photo since each photo will
	 * have a unique path. If there is a match, the tag will also be added to that photo in the other album.
	 * 
	 * @param photoPath	Path of the photo, used to identify if the same photo is in another album
	 * @param albumThatWasAlteredFirst	First album that has the photo that has the tag added
	 * @param tagType	The type of the tag, such as location
	 * @param tagValue	The value of the tag, such as the name of a person
	 */
	public void makeChangesToPhotoAcrossAllAlbumsAddTag(String photoPath, String albumThatWasAlteredFirst,
			String tagType, String tagValue)
	{
		
		for (int i = 0; i < albumList.size(); i++)
		{
			Album currentAlbum = albumList.get(i);
			
			// This is the album that was already changed. No need to change it again.
			if (currentAlbum.getAlbumName().equalsIgnoreCase(albumThatWasAlteredFirst))
			{
				continue;
			}
			
			for (int j = 0; j < currentAlbum.getPhotos().size(); j++)
			{
				Photo currentPhoto = currentAlbum.getPhotos().get(j);
				
				if (currentPhoto.getPhotoPath().equals(photoPath))
				{
					if (currentPhoto.checkIfTagExists(tagType, tagValue))
					{
						break;
					}
					currentPhoto.addTag(tagType, tagValue);
					break;
				}
			}
		}
	}
	
	/**
	 * Whenever a tag is removed from a photo, this method will be called to see if there are copies
	 * of this photo across other albums. This is checked using the path of the photo since each photo will
	 * have a unique path. If there is a match, the tag will also be removed from that photo in the other album.
	 * 
	 * @param photoPath	Path of the photo, used to identify if the same photo is in another album
	 * @param albumThatWasAlteredFirst	First album that has the photo that has the tag added
	 * @param tagType	The type of the tag, such as location
	 * @param tagValue	The value of the tag, such as the name of a person
	 */
	public void makeChangesToPhotoAcrossAllAlbumsRemoveTag(String photoPath, String albumThatWasAlteredFirst,
			String tagType, String tagValue)
	{
		for (int i = 0; i < albumList.size(); i++)
		{
			Album currentAlbum = albumList.get(i);
			
			// This is the album that was already changed. No need to change it again.
			if (currentAlbum.getAlbumName().equalsIgnoreCase(albumThatWasAlteredFirst))
			{
				continue;
			}
			
			for (int j = 0; j < currentAlbum.getPhotos().size(); j++)
			{
				Photo currentPhoto = currentAlbum.getPhotos().get(j);
				
				if (currentPhoto.getPhotoPath().equals(photoPath))
				{
					if (!currentPhoto.checkIfTagExists(tagType, tagValue))
					{
						break;
					}
					currentPhoto.removeTag(tagType, tagValue);
					break;
				}
			}
		}
	}
	
	/**
	 * Whenever a photo's caption is changed, this method will be called to see if there are copies
	 * of this photo across other albums. This is checked using the path of the photo since each photo will
	 * have a unique path. If there is a match, the caption for the photo in the other album will also be changed.
	 * 
	 * @param photoPath	Path of the photo, used to identify if the same photo is in another album
	 * @param albumThatWasAlteredFirst	First album that has the photo that has the tag added
	 * @param caption	The new caption of the photo
	 */
	public void makeChangesToPhotoAcrossAllAlbumsChangeCaption(String photoPath, String albumThatWasAlteredFirst,
			String caption)
	{
		for (int i = 0; i < albumList.size(); i++)
		{
			Album currentAlbum = albumList.get(i);
			
			// This is the album that was already changed. No need to change it again.
			if (currentAlbum.getAlbumName().equalsIgnoreCase(albumThatWasAlteredFirst))
			{
				continue;
			}
			
			for (int j = 0; j < currentAlbum.getPhotos().size(); j++)
			{
				Photo currentPhoto = currentAlbum.getPhotos().get(j);
				if (currentPhoto.getPhotoPath().equals(photoPath))
				{
					currentPhoto.changePhotoCaption(caption);
					break;
				}
			}
		}
	}
	
	/**
	 * This method is used when a new photo is added to an album.
	 * It checks to see if the same photo is another album.
	 * If there is a match, checked by seeing if the photoPath is the same, 
	 * the tags and caption from the photo that is found is added to the photo
	 * that was uploaded.
	 * 
	 * @param photoToCheck	The new photo that was added
	 * @param albumPhotoWasAddedTo	The album that the photo was added to
	 */
	public void checkIfNewPhotoIsInAnotherAlbum(Photo photoToCheck, String albumPhotoWasAddedTo)
	{
		for (int i = 0; i < albumList.size(); i++)
		{
			Album currentAlbum = albumList.get(i);
			
			// This is the album that the photo was added to. No need to check it.
			if (currentAlbum.getAlbumName().equalsIgnoreCase(albumPhotoWasAddedTo))
			{
				continue;
			}
			
			for (int j = 0; j < currentAlbum.getPhotos().size(); j++)
			{
				Photo currentPhoto = currentAlbum.getPhotos().get(j);
				
				if (currentPhoto.getPhotoPath().equals(photoToCheck.getPhotoPath()))
				{
					List<Tag> tagsToAdd = currentPhoto.getTagList();
					String captionToSet = currentPhoto.getPhotoCaption();
					
					for (int k = 0; k < tagsToAdd.size(); k++)
					{
						photoToCheck.addTag(tagsToAdd.get(k).getTagType(), tagsToAdd.get(k).getTagValue());
					}
					
					photoToCheck.changePhotoCaption(captionToSet);
					
					return;
				}
			}
		}
	}
}
