package models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1054930822071093069L;
	private static final String DATA_DIR = "data/";
	// private static final String IMAGES_DIR = "/images";
	private static final String USER_DATA_FILE = "/user.txt";

	private String nickname;
	private String password;
	private String realName;
	private String surname;
	private String age;
	private String emailAddress;
	private String profilePhotoPath = "resources/likeIcon.png"; // default profile photo
	private List<Photo> album;

	public User(String nickname, String password, String realName, String surname, String age, String emailAddress) {
		this.nickname = nickname;
		this.password = password;
		this.realName = realName;
		this.surname = surname;
		this.age = age;
		this.emailAddress = emailAddress;
		album = new ArrayList<>();
	}

	/**
	 * Checks if a User with the given nickname already exists.
	 * 
	 * @param nickname - the nickname of the user to check
	 * @return true if a user with the nickname exists, false otherwise
	 */
	public static boolean exists(String nickname) {
		File userFolder = new File(DATA_DIR + nickname);
		return userFolder.exists();
	}

	/**
	 * Creates a new User with the given details and writes the new User to disk.
	 * 
	 * @param nickname     - the user's nickname
	 * @param password     - the user's password
	 * @param realName     - the user's real name
	 * @param surname      - the user's surname
	 * @param age          - the user's age
	 * @param emailAddress - the user's email address
	 * @param type         - the type of user to create
	 * @return the newly created User
	 * @throws IOException if there was an error writing the new User to disk
	 */
	public static User create(String nickname, String password, String realName, String surname, String age,
			String emailAddress, String type) throws IOException {
		User user = null;
		// Check if user already exists
		if (exists(nickname)) {
			throw new IOException("User already exists. Please log in.");
		}
		File userFolder = new File("data/" + nickname);
		// Create user folder
		boolean folderCreated = userFolder.mkdir();
		if (!folderCreated) {
			throw new IOException("Could not create user folder.");
		}
		// Create images folder inside user folder
		File imagesFolder = new File(userFolder, "images");
		folderCreated = imagesFolder.mkdir();
		if (!folderCreated) {
			throw new IOException("Could not create images folder.");
		}
		// Continue with user creation based on type
		switch (type) {
		case "FreeUser":
			user = new FreeUser(nickname, password, realName, surname, age, emailAddress);
			break;
		case "HobbyistUser":
			user = new HobbyistUser(nickname, password, realName, surname, age, emailAddress);
			break;
		case "ProfessionalUser":
			user = new ProfessionalUser(nickname, password, realName, surname, age, emailAddress);
			break;
		case "Administrator":
			user = new Administrator(nickname, password, realName, surname, age, emailAddress);
			break;
		default:
			throw new IllegalArgumentException("Invalid user type: " + type);
		}
		// Set other fields for the user
		user.setAlbum(new ArrayList<>());
		// Serialize the User object
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(user.getDataFile()))) {
			oos.writeObject(user);
		} catch (IOException e) {
			throw new IOException("Could not write user data.");
		}
		return user;
	}

	protected void save() throws IOException {
		try (FileOutputStream fos = new FileOutputStream(getDataFile());
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(this);
		}
	}

	/**
	 * Returns the File that represents this User's data on disk.
	 * 
	 * @return the User's data File
	 */
	public File getDataFile() {
		return new File(DATA_DIR + getNickname() + USER_DATA_FILE);
	}

	public void addPhoto(Photo photo) throws Exception {
		album.add(photo);
		try {
			save();
		} catch (Exception e) {
			album.remove(photo);
			throw e;
		}
	}
	
	public void removePhoto(Photo photo) throws Exception {
	    boolean removed = album.remove(photo);
	    if (!removed) {
	        throw new Exception("Photo not found");
	    }
	    try {
	        save();
	    } catch (Exception e) {
	        album.add(photo);
	        throw e;
	    }
	}


	public List<Photo> getAlbum() {
		return album;
	}

	public void setAlbum(List<Photo> album) {
		List<Photo> oldAlbum = this.album;
		this.album = album;
		try {
			save();
		} catch (Exception e) {
			this.album = oldAlbum;
		}
	}

	public void setNickname(String nickname) throws IOException {
		String oldNickname = this.nickname;
		this.nickname = nickname;
		try {
			save();
		} catch (IOException e) {
			this.nickname = oldNickname;
			throw e;
		}
	}

	public String getNickname() {
		return nickname;
	}

	public void setPassword(String password) throws IOException {
		String oldPassword = this.password;
		this.password = password;
		try {
			save();
		} catch (IOException e) {
			this.password = oldPassword;
			throw e;
		}
	}

	public void setRealName(String realName) throws IOException {
		String oldRealName = this.realName;
		this.realName = realName;
		try {
			save();
		} catch (IOException e) {
			this.realName = oldRealName;
			throw e;
		}
	}

	public void setSurname(String surname) throws IOException {
		String oldSurname = this.surname;
		this.surname = surname;
		try {
			save();
		} catch (IOException e) {
			this.surname = oldSurname;
			throw e;
		}
	}

	public void setAge(String age) throws IOException {
		String oldAge = this.age;
		this.age = age;
		try {
			save();
		} catch (IOException e) {
			this.age = oldAge;
			throw e;
		}
	}

	public void setEmailAddress(String emailAddress) throws IOException {
		String oldEmailAddress = this.emailAddress;
		this.emailAddress = emailAddress;
		try {
			save();
		} catch (IOException e) {
			this.emailAddress = oldEmailAddress;
			throw e;
		}
	}

	public void setProfilePhotoPath(String profilePhotoPath) throws IOException {
		String oldProfilePhotoPath = this.profilePhotoPath;
		this.profilePhotoPath = profilePhotoPath;
		try {
			save();
		} catch (IOException e) {
			this.profilePhotoPath = oldProfilePhotoPath;
			throw e;
		}
	}

	public String getPassword() {
		return password;
	}

	public String getRealName() {
		return realName;
	}

	public String getSurname() {
		return surname;
	}

	public String getAge() {
		return age;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getProfilePhotoPath() {
		return profilePhotoPath;
	}
	
	public File getProfilePhoto() {
		return new File(profilePhotoPath);
	}



	public abstract void applyFilter(String filterName);

	public abstract void sharePhoto(String photoPath, String description);

	@Override
	public boolean equals(Object object) {

		if (object instanceof User) {
			User other = (User) object;
			if (this.nickname.equals(other.getNickname())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\"nickname\": \"").append(nickname).append("\",").append("\"password\": \"")
				.append(password).append("\",").append("\"realName\": \"").append(realName).append("\",")
				.append("\"surname\": \"").append(surname).append("\",").append("\"age\": ").append(age).append(",")
				.append("\"emailAddress\": \"").append(emailAddress).append("\",").append("\"profilePhotoPath\": \"")
				.append(profilePhotoPath).append("\",").append("\"type\": \"").append(this.getClass().getSimpleName())
				.append("\",").append("\"album\": [");

		// Add album information
		for (int i = 0; i < album.size(); i++) {
			sb.append(album.get(i).toString());
			if (i < album.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]}");

		return sb.toString();
	}

	// Other methods for uploading photos, modifying personal info, etc.
}