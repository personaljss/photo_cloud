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
		users = new HashMap<>();
		readAllData();
		for (User user : users.values()) {
			System.out.println(user.getClass().getSimpleName());
		}
	}

	public static AppState getInstance() {
		if (instance == null) {
			instance = new AppState();
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
							// Read photos in data/username/images/
							File imagesDirectory = new File("data/"+user.getNickname()+"/images/");
					        if (imagesDirectory.exists()) {
					            File[] photoDirectories = imagesDirectory.listFiles();
                                List<Photo> photos = new ArrayList<>();
					            if (photoDirectories != null) {
					                for (File photoDirectory : photoDirectories) {
					                    if (photoDirectory.isDirectory()) {
					                        File dataFile = new File(photoDirectory, Photo.dataFile);
					                        if (dataFile.exists()) {
					                            try (ObjectInputStream dataIn = new ObjectInputStream(
					                                    new FileInputStream(dataFile))) {
					                                while (true) {
					                                    try {
					                                        Photo photo = (Photo) dataIn.readObject();
					                                        photos.add(photo);
					                                    } catch (EOFException e) {
					                                        break;
					                                    }
					                                }
					                            } catch (IOException | ClassNotFoundException e) {
					                                e.printStackTrace();
					                            }
					                        }
					                    }
					                }
					                
					            }
                                user.setAlbum(photos);
					        }else {
					        	
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
