package photo.app;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This class will keep track of each of the individual photos.
 * 
 * @author Amit Patel, Hideyo Sakamoto
 *
 */
public class Photo implements Serializable{

	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = -1541823478468036214L;
	
	/**
	 * Caption for the photo
	 */
	private String photoCaption;
	
	/**
	 * List of tags for the photo
	 */
	private List<Tag> photoTags;
	
	/**
	 * The file path for the photo
	 */
	private String photoPath;
	
	/**
	 * The date when the Photo was taken
	 */
	private Date datePhotoTaken;
	
	/**
	 * Keeps track of how many location tags a user has. It should be either 0 or 1.
	 */
	private int locationTagsAmount;
	
	/**
	 * Constructor for Photo
	 * 
	 * @param photoPath	The path of the photo
	 * @param photoName	The name of the photo
	 */
	public Photo(String photoPath)
	{
		this.photoPath = photoPath;
		photoCaption = "";
		photoTags = new ArrayList<Tag>();
		locationTagsAmount = 0;
		File photo = new File(photoPath);
		datePhotoTaken = new Date(photo.lastModified());
	}
	
	/**
	 * Returns the file path for the photo
	 * 
	 * @return	Returns the file path for the photo as a string
	 */
	public String getPhotoPath()
	{
		return this.photoPath;
	}
	
	/**
	 * Allows the user to change the caption of the photo
	 * 
	 * @param newCaption	The new caption for the photo
	 */
	public void changePhotoCaption(String newCaption)
	{
		photoCaption = newCaption;
	}
	
	/**
	 * Returns the caption of the photo
	 * 
	 * @return	Returns the caption of the photo as a string
	 */
	public String getPhotoCaption()
	{
		return photoCaption;
	}
	
	/**
	 * Returns the date of the photo as a Date
	 * 
	 * @return	Returns the date of the photo as a Date
	 */
	public Date getDate()
	{
		return this.datePhotoTaken;
	}
	
	/**
	 * This method will be used to see if a photo is within two dates.
	 * This method will be called when a user is earching for photos between two dates.
	 * 
	 * @param olderDate		The older date in the date range
	 * @param earlierDate	The earlier date in the date range
	 * @return	False if the photo date is older than the older date or earlier than the earlierDate, otherwise true
	 */
	public boolean isPhotoWithinDateRange(LocalDate olderDate, LocalDate earlierDate)
	{
		LocalDate currentDate = datePhotoTaken.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		if (currentDate.isBefore(olderDate) || currentDate.isAfter(earlierDate))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Creates a new tag from the given parameters and then adds the tag to the Photo's list of tags.
	 * If the tag is a location tag, locationTagsAmount is set to 1, preventing the user from making
	 * anymore location tags for this photo.
	 * 
	 * @param tagType	Type of the tag
	 * @param tagValue	Value of the tag
	 */
	public void addTag(String tagType, String tagValue)
	{
		Tag tagToAdd = new Tag(tagType, tagValue);
		
		this.photoTags.add(tagToAdd);
		
		// There should only be one location tag per photo
		if (tagType.equalsIgnoreCase("location"))
		{
			locationTagsAmount = 1;
		}
	}
	
	/**
	 * This method is called when a user is trying to remove a tag from a photo.
	 * If the tag is a location tag, locationTagsAmount is set to 0, allowing the user to make another
	 * location tag.
	 * 
	 * @param tagType
	 * @param tagValue
	 */
	public void removeTag(String tagType, String tagValue)
	{
		for (int i = 0; i < photoTags.size(); i++)
		{
			if (photoTags.get(i).getTagType().equalsIgnoreCase(tagType) && 
				photoTags.get(i).getTagValue().equalsIgnoreCase(tagValue))
			{
				photoTags.remove(i);
				break;
			}
		}
		
		// If a location tag is removed from a photo, another location tag can take its place
		if (tagType.equalsIgnoreCase("location"))
		{
			locationTagsAmount = 0;
		}
	}
	
	/**
	 * Checks to see if a location tag already exists for a photo.
	 * There should be only 1 location tag per photo
	 * 
	 * @return	True if locationTagsAmount is equal to 1, false otherwise
	 */
	public boolean checkIfLocationTagExists()
	{
		if (locationTagsAmount == 1)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Return the list of tags of the photo
	 * 
	 * @return	Return the list of tags of the photo
	 */
	public List<Tag> getTagList()
	{
		return this.photoTags;
	}
	
	/**
	 * Checks to see if the new tag already exists for this photo.
	 * There should not be any duplicate tags.
	 * 
	 * @param newTagType	Name of the tag's type that is trying to be added
	 * @param newTagValue	Name of the tag's value that is trying to be added
	 * @return	True if the pair already exists, false otherwise
	 */
	public boolean checkIfTagExists(String newTagType, String newTagValue)
	{
		for (int i = 0; i < photoTags.size(); i++)
		{
			if (photoTags.get(i).getTagType().equalsIgnoreCase(newTagType) && 
					photoTags.get(i).getTagValue().equalsIgnoreCase(newTagValue))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * This method is used when the user is trying to find photos with a certain tag pair.
	 * If the tag matches, the photo should then be added to the search results.
	 * 
	 * @param tagType	Name of the tag's type that is being searched for
	 * @param tagValue	Name of the tag's value that is being searched for
	 * @return	True if the tag matches, false otherwise
	 */
	public boolean searchTagsOnePair(String tagType, String tagValue)
	{
		for (int i = 0; i < photoTags.size(); i++)
		{
			if (photoTags.get(i).getTagType().equalsIgnoreCase(tagType) && 
					photoTags.get(i).getTagValue().equalsIgnoreCase(tagValue))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks to see if the photo has both of the tag pairs.
	 * This method will be used when searching for photos that have two user defined tag pairs.
	 * 
	 * @param tagType1	Name of the first tag's type that is being searched for
	 * @param tagValue1	Name of the first tag's value that is being searched for
	 * @param tagType2	Name of the second tag's type that is being searched for
	 * @param tagValue2	Name of the second tag's value that is being searched for
	 * @return	True if both of the tag pairs are found, false otherwise
	 */
	public boolean searchTagsTwoPairsAnd(String tagType1, String tagValue1, String tagType2, String tagValue2)
	{
		int matches = 0;
		
		for (int i = 0; i < photoTags.size(); i++)
		{
			if (photoTags.get(i).getTagType().equalsIgnoreCase(tagType1) && 
					photoTags.get(i).getTagValue().equalsIgnoreCase(tagValue1))
			{
				matches++;
			}
			else if (photoTags.get(i).getTagType().equalsIgnoreCase(tagType2) && 
					photoTags.get(i).getTagValue().equalsIgnoreCase(tagValue2))
			{
				matches++;
			}
		}
		
		if (matches == 2)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks to see if the photo has at least one of the tag pairs.
	 * This methid is used when the user is searching for photos with at least one tag pair 
	 * between the two tag pairs given by the user.
	 * 
	 * @param tagType1	Name of the first tag's type that is being searched for
	 * @param tagValue1	Name of the first tag's value that is being searched for
	 * @param tagType2	Name of the second tag's type that is being searched for
	 * @param tagValue2	Name of the second tag's value that is being searched for
	 * @return	True if at least one of the tag pairs are found, false otherwise
	 */
	public boolean searchTagsTwoPairsOr(String tagType1, String tagValue1, String tagType2, String tagValue2)
	{
		for (int i = 0; i < photoTags.size(); i++)
		{
			if (photoTags.get(i).getTagType().equalsIgnoreCase(tagType1) && 
					photoTags.get(i).getTagValue().equalsIgnoreCase(tagValue1) ||
					photoTags.get(i).getTagType().equalsIgnoreCase(tagType2) &&
					photoTags.get(i).getTagValue().equalsIgnoreCase(tagValue2))
			{
				return true;
			}
		}
		
		return false;
	}
}
