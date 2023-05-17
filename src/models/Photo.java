package models;

import java.awt.image.ImageFilter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import exceptions.UserAlreadyLikedPhotoException;
import exceptions.UserDidNotLikePhotoException;
import services.ImageMatrix;
import services.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Photo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7238849752558552380L;
	private User owner;
	private String fileName;
	private List<ImageFilter> appliedFilters;
	private List<Comment> comments;
	private List<User> likes;
	private boolean isPublic;
	private transient ImageMatrix imageMatrix;
	private String extension;
	public static String dataFile = "imageData.txt";

	private static int id = 0;

	public Photo(User owner,String fileName, String extension) {
		this.owner = owner;
		this.appliedFilters = new ArrayList<>();
		this.comments = new ArrayList<Comment>();
		this.likes = new ArrayList<User>();
		this.extension = extension;
		this.fileName = fileName;
	}



	public static String generateName(User user, String extension) {
	    File imagesDirectory = new File("data/" + user.getNickname() + "/images/");
	    File[] photoDirectories = imagesDirectory.listFiles();
	    if(photoDirectories==null) {
	    	return user.getNickname()+id;
	    }

	    // Regular expression pattern to extract the id part from the filename
	    Pattern pattern = Pattern.compile(user.getNickname() + "(\\d+)");

	    int maxId = 0; // Initialize maxId to 0
	    for (File file : photoDirectories) {
	        Matcher matcher = pattern.matcher(file.getName());
	        if (matcher.find()) {
	            int id = Integer.parseInt(matcher.group(1));
	            if (id > maxId) {
	                maxId = id; // Update maxId if a larger id is found
	            }
	        }
	    }
	   

	    // Generate the next filename by incrementing the maximum id by 1
	    String nextFilename;
	    if(extension!=null) {
	    	nextFilename = user.getNickname() + (maxId + 1) + extension;	
	    }else {
		    nextFilename = user.getNickname() + (maxId + 1);
	    }

	    return nextFilename;
	}


	public static String getNextPhotoName(User owner) {
		return owner.getNickname() + id;
	}

	public static String getNextPhotoName(User owner, String extension) {
		return owner.getNickname() + id + extension;
	}

	private void save() {
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

	}

	public File getDataFile() {
	    File file = new File("data/" + owner.getNickname() + "/images/" + fileName + "/" + dataFile);
	    /*
	    // Create the directories if they do not exist
	    File parentDirectory = file.getParentFile();
	    if (!parentDirectory.exists()) {
	        parentDirectory.mkdirs();
	    }

	    // Create the file if it does not exist
	    if (!file.exists()) {
	        try {
	            file.createNewFile();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    */
	    return file;
	}



	public File getImageFile() {
		return new File("data/" + owner.getNickname() + "/images/" + fileName + "/" + fileName + extension);
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
		save();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		save();
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
		save();
	}

	public List<User> getLikes() {
		return likes;
	}

	public void setLikes(List<User> likes) {
		this.likes = likes;
		save();
	}

	public List<ImageFilter> getAppliedFilters() {
		return appliedFilters;
	}

	public void addFilter(ImageFilter filter) {
		// TODO: manipulate imageMatrix
		appliedFilters.add(filter);
	}

	public void setImageMatrix(ImageMatrix matrix) {
		this.imageMatrix = matrix;
	}

	public ImageMatrix getImageMatrix() {
		return imageMatrix;
	}

	public void setAppliedFilters(List<ImageFilter> appliedFilters) {
		this.appliedFilters = appliedFilters;
		save();
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
		save();
	}

	public void like(User user) throws UserAlreadyLikedPhotoException {
		if (likes.contains(user)) {
			throw new UserAlreadyLikedPhotoException();
		} else {
			likes.add(user);
		}
	}

	public void disLike(User user) throws UserDidNotLikePhotoException {
		if (likes.contains(user)) {
			throw new UserDidNotLikePhotoException();
		} else {
			likes.remove(user);
			save();
		}
	}

	public void addComment(Comment comment) {
		comments.add(comment);
		save();
	}

	public void removeComment(Comment comment) {
		comments.remove(comment);
		save();
	}// In Photo class

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\"fileName\": \"").append(fileName).append("\",").append("\"comments\": [");

		// Add comments information
		for (int i = 0; i < comments.size(); i++) {
			sb.append(comments.get(i).toString());
			if (i < comments.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]}");

		return sb.toString();
	}

}
