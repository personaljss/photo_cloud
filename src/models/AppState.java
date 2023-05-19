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

/**
 * Singleton class that maintains the state of the application.
 */
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

	public static synchronized AppState getInstance() {
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

	/**
	 * Reads all users and their photos from the file system.
	 */
	public void readAllData() {
		File dir = new File("data/");

		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.isDirectory()) {
					readUser(child);
				}
			}
		}
	}

	/**
	 * Reads a user from the file system.
	 *
	 * @param userDir the directory of the user.
	 */
	private void readUser(File userDir) {
		File userFile = new File(userDir, "user.txt");
		if (userFile.exists()) {
			try (ObjectInputStream userIn = new ObjectInputStream(new FileInputStream(userFile))) {
				User user = (User) userIn.readObject();
				users.put(user.getNickname(), user);

				readImages(user);
			} catch (IOException | ClassNotFoundException e) {
				Logger.logError(e.getMessage());
			}
		}
	}

	/**
	 * Reads the images of a user from the file system.
	 *
	 * @param user the user.
	 */
	private void readImages(User user) {
		File imagesDirectory = new File("data/" + user.getNickname() + "/images/");
		if (imagesDirectory.exists()) {
			File[] photoDirectories = imagesDirectory.listFiles();
			List<Photo> photos = new ArrayList<>();
			if (photoDirectories != null) {
				for (File photoDirectory : photoDirectories) {
					if (photoDirectory.isDirectory()) {
						readPhoto(photoDirectory, photos);
					}
				}
			}
			user.setAlbum(photos);
		}
	}

	/**
	 * Reads a photo from the file system and adds it to a list of photos.
	 *
	 * @param photoDirectory the directory of the photo.
	 * @param photos         the list of photos.
	 */
	private void readPhoto(File photoDirectory, List<Photo> photos) {
		File dataFile = new File(photoDirectory, Photo.DATA_FILE);
		if (dataFile.exists()) {
			try (ObjectInputStream dataIn = new ObjectInputStream(new FileInputStream(dataFile))) {
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


	public User getUser(String nickName) {
		return users.get(nickName);
	}

	public void addUser(User user) {
		users.put(user.getNickname(), user);
	}

}
