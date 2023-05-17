package models;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import services.Logger;

public class AppState {
	private HashMap<String, User> users;
	
	private static AppState instance;
	private AppState() {
		users=new HashMap<>();
		readAllData();
	}
	public static AppState getInstance() {
		if(instance==null) {
			instance=new AppState();
		}
		return instance;
	}
	
	
	public HashMap<String, User> getUsers() {
		return users;
	}
	public void setUsers(HashMap<String, User> users) {
		this.users = users;
	}
	
	public void readAllData() {
	    // The path to the directory
	    File dir = new File("data/");

	    // Get all the files from a directory
	    File[] directoryListing = dir.listFiles();
	    if (directoryListing != null) {
	        for (File child : directoryListing) {
	            // If the file is a directory, read the "user.txt" file in it
	            if (child.isDirectory()) {
	                File userFile = new File(child, "user.txt");
	                if (userFile.exists()) {
	                    try (ObjectInputStream userIn = new ObjectInputStream(new FileInputStream(userFile))) {
	                        User user = (User) userIn.readObject();
	                        users.put(user.getNickname(), user);

	                        // Read photos in data/username/photos.txt
	                        File photosFile = new File(child, "photos.txt");
	                        if (photosFile.exists()) {
	                            try (ObjectInputStream photosIn = new ObjectInputStream(new FileInputStream(photosFile))) {
	                                List<Photo> photos = new ArrayList<>();
	                                while (true) {
	                                    try {
	                                        Photo photo = (Photo) photosIn.readObject();
	                                        photos.add(photo);
	                                    } catch (EOFException e) {
	                                        break;
	                                    }
	                                }
	                                user.setAlbum(photos);
	                            }
	                        }
	                    } catch (IOException | ClassNotFoundException e) {
	                        Logger.logError(e.getMessage());
	                    }
	                }
	            }
	        }
	    }
	}


	public User getUser(String nickName) {
		return users.get(nickName);
	}
	
	public void addUser(User user) {
		users.put(user.getNickname(), user);
	}


}
