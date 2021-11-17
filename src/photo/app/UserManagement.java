package photo.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will be responsible for keeping track of all of the users.
 * Each user will contain all of their photos and data.
 * 
 * @author Amit Patel, Hideyo Sakamoto
 *
 */

public class UserManagement implements Serializable{
	
	/**
	 * The serial ID
	 */
	private static final long serialVersionUID = -9080366602999037531L;
	
	/**
	 * Storage directory of user list
	 */
	public static final String storeDir = "dat";
	
	/**
	 * File that stores all of the Users and their data
	 */
	public static final String storeFile = "users.dat";
	
	/**
	 * Keeps track of all of the users
	 */
	public List<User> allUsersList;
	
	/**
	 * Keeps track of the current user
	 */
	public User currentUser;
	
	/**
	 * Keeps track of the current album the current user is editing
	 */
	public Album currentAlbum;
	
	/**
	 * Keeps track of the current photo the current user is looking at
	 */
	public Photo currentPhoto;
	
	/**
	 * After a search is done, the search results are saved so that user can create
	 * an album from it if they choose to.
	 */
	public List<Photo> photoSearchResults;
	
	/**
	 * Constructor for UserManagement
	 */
	public UserManagement()
	{
		allUsersList = new ArrayList<User>();
		currentUser = null;
		currentAlbum = null;
		photoSearchResults = new ArrayList<Photo>();
	}
	
	/**
	 * Allows new users to be added to the list of Users
	 * 
	 * @param userToAdd	New User to add to the list
	 */
	public void addUser(User userToAdd)
	{
		allUsersList.add(userToAdd);
	}
	
	/**
	 * Checks to see if the stock username is present in the list of users. 
	 * If it is not, it is added back into the list of users and the stock photos are also added.
	 */
	public void checkIfStockIsPresent()
	{
		boolean stockPresent = false;
		
		for (int i = 0; i < allUsersList.size(); i++)
		{
			String userName = allUsersList.get(i).getUserName();
			
			if (userName.equals("stock"))
			{
				stockPresent = true;
			}
		}
		
		if (!stockPresent)
		{
			User stockUser = new User("stock");
			
			Album stockAlbum = new Album("stock");
			
			Photo photo1 = new Photo("data/photo1.jpg");
			Photo photo2 = new Photo("data/photo2.jpg");
			Photo photo3 = new Photo("data/photo3.jpg");
			Photo photo4 = new Photo("data/photo4.jpg");
			Photo photo5 = new Photo("data/photo5.jpg");
			Photo photo6 = new Photo("data/photo6.jpg");
			Photo photo7 = new Photo("data/photo7.jpg");
			Photo photo8 = new Photo("data/photo8.jpg");
			Photo photo9 = new Photo("data/photo9.jpg");
			
			stockAlbum.addPhoto(photo1);
			stockAlbum.addPhoto(photo2);
			stockAlbum.addPhoto(photo3);
			stockAlbum.addPhoto(photo4);
			stockAlbum.addPhoto(photo5);
			stockAlbum.addPhoto(photo6);
			stockAlbum.addPhoto(photo7);
			stockAlbum.addPhoto(photo8);
			stockAlbum.addPhoto(photo9);
			
			stockUser.addAlbum(stockAlbum);
			
			allUsersList.add(stockUser);
		}
	}
	
	/**
	 * Returns the size of the List
	 * 
	 * @return size of the List
	 */
	public int getSize() {
		return allUsersList.size();
	}
	
	/**
	 * Deletes the given user as a parameter
	 * 
	 * @param userToDelete	Name of user to delete
	 */
	public void deleteUser(String userToDelete) {
		
		for (int i = 0; i < allUsersList.size(); i++)
		{
			if (allUsersList.get(i).getUserName().equals(userToDelete))
			{
				allUsersList.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Gets the User by username
	 * 
	 * @param userName	The name of the user
	 * @return	The User
	 */
	public User getUser(String userName)
	{
		for (int i = 0; i < allUsersList.size(); i++)
		{
			if (allUsersList.get(i).getUserName().equalsIgnoreCase(userName))
			{
				return allUsersList.get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the list of users that are currently present in the ArrayList
	 * 
	 * @return	The ArrayList of all of the users
	 */
	public List<User> getListOfUsers()
	{
		return allUsersList;
	}
	
	/**
	 * Checks to see if the new username is already taken
	 * 
	 * @param name	New username
	 * @return	True if the username is already in the list, false otherwise
	 */
	public boolean isNameTaken(String name)
	{
		if (name.equalsIgnoreCase("admin") || name.equalsIgnoreCase("stock"))
		{
			return true;
		}
		
		for (int i = 0; i < allUsersList.size(); i++)
		{
			if (allUsersList.get(i).getUserName().equalsIgnoreCase(name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * This method will be used when the user logouts or closes the program,
	 * This will allow us to save changes the user made between sessions.
	 * 
	 * @param userList	Instance of the UserManagement class, which handles all of the data for all Users
	 * @throws IOException	Any exceptions encountered
	 */
	public static void writeApp(UserManagement userList) throws IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(userList);
		oos.close();
	}
	
	/**
	 * This method will be used to read the save data for the list of users.
	 * This will allow us to keep any changes for all users inbetween sessions.
	 * 
	 * @return	The instance of UserManagement, which holds the list of users
	 * @throws IOException	Any exceptions encounters
	 * @throws ClassNotFoundException	If the class is not found
	 */
	public static UserManagement readApp() throws IOException, ClassNotFoundException
	{
		@SuppressWarnings("resource")
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		UserManagement userList = (UserManagement) ois.readObject();
		return userList;
	}
}
