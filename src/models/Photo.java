package models;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import exceptions.UserAlreadyLikedPhotoException;
import exceptions.UserDidNotLikePhotoException;
import listeners.PhotoListener;
import services.ImageMatrix;
import services.ImageSecretary;
import utils.PhotoFilter;
//TODO:add logger

public class Photo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7238849752558552380L;
	public static final String DATA_FILE = "imageData.txt";
	private User owner;
	private String fileName;
	private List<PhotoFilter> appliedFilters;
	private List<Comment> comments;
	private List<User> likes;
	private String description;
	private boolean isPublic;
	private String extension;
	private transient List<PhotoListener> listeners;
	
	private static int initialId = 0;

	protected Photo(User owner, String fileName, String extension) {
		this.owner = owner;
		this.appliedFilters = new ArrayList<>();
		this.comments = new ArrayList<Comment>();
		this.likes = new ArrayList<User>();
		this.extension = extension;
		this.fileName = fileName;
		listeners=new ArrayList<>();
	}
	
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        listeners = new ArrayList<>(); // Initialize the listeners field during deserialization
    }
	
	public void addListener(PhotoListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PhotoListener listener) {
        listeners.remove(listener);
    }

	
	/**
	 * Creates a new photo.
	 * 
	 * This method creates a new Photo object and associates it with the given user.
	 * It first generates a unique name for the photo, then creates a directory for
	 * the photo in the user's directory. It then copies the source file to the new
	 * directory and creates a data file for the photo. If any errors occur during
	 * these operations, they are propagated as an IOException.
	 * 
	 * Note: This method expects that the source file is an image and that its
	 * extension is one that can be handled by the JavaFX Swing library.
	 * 
	 * @param user       The user who will own the photo
	 * @param sourceFile The file containing the source image
	 * @return The created Photo object
	 * @throws Exception 
	 * @throws IllegalArgumentException if the source file is not an image or has an
	 *                                  unsupported extension
	 */
	public static Photo create(User user, File sourceFile) throws Exception {
		// Extract the extension of the source file
		String fullImageName = sourceFile.getName();
		String extension = fullImageName.substring(fullImageName.lastIndexOf('.'), fullImageName.length());

		// Check that the extension is one that can be handled by JavaFX Swing
		if (!extension.matches(".+(jpg|png|gif|bmp)$")) {
			throw new IllegalArgumentException("Unsupported file extension: " + extension);
		}

		// Generate a unique name for the photo
		String imageName = generateName(user, "");

		// Create the directory for the photo
		Path targetDirectory = Paths.get("data/" + user.getNickname() + "/images/" + imageName);
		Files.createDirectories(targetDirectory); // May throw IOException

		// Copy the source file to the new directory
		Path targetPath = targetDirectory.resolve(imageName + extension);
		Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING); // May throw IOException

		// Create the data file for the photo
		File dataFile = new File(targetDirectory + "/" + DATA_FILE);
		dataFile.createNewFile(); // May throw IOException

		// Create the Photo object
		Photo photo = new Photo(user, imageName, extension);

		// Save the photo to the data file
		photo.save();

		// Add the photo to the user's album
		user.addPhoto(photo);

		return photo;
	}

	public static String generateName(User user, String extension) {
		File imagesDirectory = new File("data/" + user.getNickname() + "/images/");
		File[] photoDirectories = imagesDirectory.listFiles();
		if (photoDirectories == null) {
			return user.getNickname() + initialId;
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
		if (extension != null) {
			nextFilename = user.getNickname() + (maxId + 1) + extension;
		} else {
			nextFilename = user.getNickname() + (maxId + 1);
		}

		return nextFilename;
	}

	public static String getNextPhotoName(User owner) {
		return owner.getNickname() + initialId;
	}

	public static String getNextPhotoName(User owner, String extension) {
		return owner.getNickname() + initialId + extension;
	}

	private void save() throws IOException, FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(getDataFile());
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this);
		oos.close();
		fos.close();
	}
	
	public void update(BufferedImage image) throws IOException {
		ImageSecretary.writeImageToResources(image, getImageFile().getPath(),extension);
	}

	public File getDataFile() {
		File file = new File("data/" + owner.getNickname() + "/images/" + fileName + "/" + DATA_FILE);
		return file;
	}

	public File getImageFile() {
		return new File("data/" + owner.getNickname() + "/images/" + fileName + "/" + fileName + extension);
	}
	
	public void delete() throws Exception {
        String filePath = "data/" + owner.getNickname() + "/images/" + fileName;
        File folder = new File(filePath);

        if (!folder.exists()) {
            throw new IOException("Folder does not exist: " + filePath);
        }
        System.out.println(owner);
        //owner.removePhoto(this);
        deleteFolder(folder);
	    // Create a copy of the listeners list
	    List<PhotoListener> listenersCopy = new ArrayList<>(listeners);
        for (PhotoListener listener : listenersCopy) {
        	listener.onDeleted(this);
        }
    }

    private void deleteFolder(File folder) throws IOException {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    if (!file.delete()) {
                        throw new IOException("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
        }

        if (!folder.delete()) {
            throw new IOException("Failed to delete folder: " + folder.getAbsolutePath());
        }
    }
    
    public void addComment(Comment comment) {
    	try {
    		comments.add(comment);
    		save();
		    // Create a copy of the listeners list
		    List<PhotoListener> listenersCopy = new ArrayList<>(listeners);
            for (PhotoListener listener : listenersCopy) {
            	listener.onCommentAdded(this);
            }
    	}catch(Exception e) {
    		comments.remove(comment);
    	}
    }

    
    /**
     * Retrieves a resized version of the original image.
     * 
     * @param width  the desired width of the resized image
     * @param height the desired height of the resized image
     * @return the resized image
     * @throws IOException if an error occurs while reading the image file
     */
    public BufferedImage getResizedImage(int width, int height) throws IOException {
        BufferedImage originalImage = ImageIO.read(getImageFile());
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Resize the image using Graphics2D and interpolation hints
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(originalImage, 0, 0, width, height, null);
        graphics.dispose();

        return resizedImage;
    }
    
    

	public String getDescription() {
		if (description == null) {
			return "photo does not have a description";
		}
		return description;
	}

	public void setDescription(String description) throws IOException, FileNotFoundException{
		String old=this.description;
		try {
			this.description = description;
			save();
		    // Create a copy of the listeners list
		    List<PhotoListener> listenersCopy = new ArrayList<>(listeners);

		    // Iterate over the copy of the listeners list
		    for (PhotoListener listener : listenersCopy) {
		        listener.onDescriptionChanged(this);
		    }

		}catch(Exception e) {
			this.description = old;
			throw e;
		}
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) throws IOException, FileNotFoundException {
		User old=this.owner; 
		try {
			this.owner=owner;
			save();
		}catch(Exception e) {
			this.owner=old;
			throw e;
		}

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) throws IOException, FileNotFoundException{
		String old=this.fileName;
		try {
			this.fileName = fileName;
			save();	
		}catch(Exception e) {
			this.fileName=old;
			throw e;
		}
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) throws IOException, FileNotFoundException{
		List<Comment> old=this.comments;
		try {
			this.comments = comments;
			save();			
		}catch(Exception e) {
			this.comments=old;
			throw e;
		}

	}

	public List<User> getLikes() {
		return likes;
	}

	public void setLikes(List<User> likes) throws IOException, FileNotFoundException{
		List<User> old=this.likes;
		try {
			this.likes = likes;
			save();			
		}catch(Exception e) {
			this.likes=old;
			throw e;
		}
	}

	public List<PhotoFilter> getAppliedFilters() {
		return appliedFilters;
	}

	public void addFilter(PhotoFilter filter, BufferedImage filteredImage) throws Exception {
		try {
			appliedFilters.add(filter);
			update(filteredImage);
			save();
		    // Create a copy of the listeners list
		    List<PhotoListener> listenersCopy = new ArrayList<>(listeners);
	        for (PhotoListener listener : listenersCopy) {
	        	listener.onFilterApplied(this);
	        }
		}catch(Exception e) {
			appliedFilters.remove(filter);
			throw e;
		}
	}

	/*
	public void setImageMatrix(ImageMatrix matrix) {
		this.imageMatrix = matrix;
	}
	*/

	public ImageMatrix getImageMatrix() throws IOException {

		return new ImageMatrix(ImageIO.read(getImageFile()));

	}


	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) throws IOException, FileNotFoundException{
		boolean old=this.isPublic;
		try {
			this.isPublic = isPublic;
			save();			
		}catch(Exception e) {
			this.isPublic=old;
			throw e;
		}
	}

	public void like(User user) throws IOException, FileNotFoundException, UserAlreadyLikedPhotoException{
		if (likes.contains(user)) {
			throw new UserAlreadyLikedPhotoException();
		} else {
			try {
				this.likes.add(user);
				save();			
			}catch(Exception e) {
				this.likes.remove(user);
				throw e;
			}
		}
	}

	public void disLike(User user) throws UserDidNotLikePhotoException, FileNotFoundException, IOException {
		if (!likes.contains(user)) {
			throw new UserDidNotLikePhotoException();
		} else {
			likes.remove(user);
			try {
				save();	
			}catch(Exception e) {
				likes.add(user);
				throw e;
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    Photo otherPhoto = (Photo) obj;
	    return Objects.equals(fileName, otherPhoto.getFileName());
	}

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
