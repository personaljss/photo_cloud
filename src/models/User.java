package models;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import services.Logger;

public abstract class User implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1054930822071093069L;
	
	private String nickname;
    private String password;
    private String realName;
    private String surname;
    private String age;
    private String emailAddress;
    private String profilePhotoPath = "default.jpg"; // default profile photo
    private List<Photo> album;




	// methods for getting and setting these attributes
    public User(String nickname, String password, String realName, String surname, String age, String emailAddress) {
        this.nickname = nickname;
        this.password = password;
        this.realName = realName;
        this.surname = surname;
        this.age = age;
        this.emailAddress = emailAddress;
        album=new ArrayList<>();
    }
    
    public static User create(Map<String, String> map) {
        User user = null;
        // The type should be stored in the map
        String type = map.get("type");

        switch (type) {
            case "FreeUser":
                user = new FreeUser(
                    map.get("nickname"),
                    map.get("password"),
                    map.get("realName"),
                    map.get("surname"),
                    map.get("age"),
                    map.get("emailAddress"));
                break;
            case "HobbyistUser":
                user = new HobbyistUser(
                        map.get("nickname"),
                        map.get("password"),
                        map.get("realName"),
                        map.get("surname"),
                        map.get("age"),
                        map.get("emailAddress"));
                break;
            case "ProfessionalUser":
                user = new ProfessionalUser(
                    map.get("nickname"),
                    map.get("password"),
                    map.get("realName"),
                    map.get("surname"),
                    map.get("age"),
                    map.get("emailAddress"));
                break;
            case "Administrator":
                user = new Administrator(
                    map.get("nickname"),
                    map.get("password"),
                    map.get("realName"),
                    map.get("surname"),
                    map.get("age"),
                    map.get("emailAddress"));
                break;
            default:
                throw new IllegalArgumentException("Invalid user type: " + type);
        }
        // Set other fields for the user
        user.setProfilePhotoPath(map.get("profilePhotoPath"));
        
        // TODO: Set the album, you might need to implement a method to convert from String to List<Photo>
        // user.setAlbum(map.get("album"));

        return user;
    }
    
    public static User create(String nickname, String password, String realName, String surname, String age, String emailAddress, String type) {
        User user = null;
        
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
        // Here you might want to set a default profile photo path and an empty album
        user.setProfilePhotoPath("default.jpg");
        user.setAlbum(new ArrayList<>());

        return user;
    }
    
	protected void save() {
		try {
			FileOutputStream fos = new FileOutputStream(getDataFile());
			try {
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(this);
				oos.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Logger.logError(e.getMessage());
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.logError(e.getMessage());
		}

		// ObjectOutputStream oos=new ObjectOutputStream()
	}

	
	public File getDataFile() {
		return new File("data/" + getNickname() + "/user.txt");
	}

    
	public void addPhoto(Photo photo) {
    	album.add(photo);
    }


    public List<Photo> getAlbum() {
		return album;
	}

	public void setAlbum(List<Photo> album) {
		this.album = album;
		save();
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
		save();
	}

	public String getNickname() {
        return nickname;
    }

    public void setPassword(String password) {
        this.password = password;
		save();
    }

    public void setRealName(String realName) {
        this.realName = realName;
		save();
    }

    public void setSurname(String surname) {
        this.surname = surname;
		save();
    }

    public void setAge(String age) {
        this.age = age;
		save();
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
		save();
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
		save();
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

    public abstract void applyFilter(String filterName);

    public abstract void sharePhoto(String photoPath, String description);
    
 // In User class
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
          .append("\"nickname\": \"").append(nickname).append("\",")
          .append("\"password\": \"").append(password).append("\",")
          .append("\"realName\": \"").append(realName).append("\",")
          .append("\"surname\": \"").append(surname).append("\",")
          .append("\"age\": ").append(age).append(",")
          .append("\"emailAddress\": \"").append(emailAddress).append("\",")
          .append("\"profilePhotoPath\": \"").append(profilePhotoPath).append("\",")
          .append("\"type\": \"").append(this.getClass().getSimpleName()).append("\",")
          .append("\"album\": [");

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