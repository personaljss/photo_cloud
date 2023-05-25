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
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import listeners.PhotoListener;
import services.ImageMatrix;
import services.ImageSecretary;
import services.Logger;
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

	/**
	 * Generates a unique filename for a photo associated with the given user.
	 * If there are existing photos for the user, the generated name will be based on the maximum ID found in the existing photos.
	 * The generated filename follows the pattern: [userNickname][id][extension].
	 * 
	 * @param user The user associated with the photo
	 * @param extension The file extension of the photo
	 * @return The generated filename
	 */
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

	/**
	 * Gets the next available photo name for the given owner.
	 * The photo name is generated based on the initialId.
	 * 
	 * @param owner The owner of the photo
	 * @return The next available photo name
	 */
	public static String getNextPhotoName(User owner) {
	    return owner.getNickname() + initialId;
	}

	/**
	 * Gets the next available photo name for the given owner with the specified extension.
	 * The photo name is generated based on the initialId.
	 * 
	 * @param owner The owner of the photo
	 * @param extension The file extension of the photo
	 * @return The next available photo name with the extension
	 */
	public static String getNextPhotoName(User owner, String extension) {
	    return owner.getNickname() + initialId + extension;
	}

	/**
	 * Saves the photo by serializing it to the associated data file.
	 * 
	 * @throws IOException if an I/O error occurs while saving the photo
	 * @throws FileNotFoundException if the associated data file is not found
	 */
	private void save() throws IOException, FileNotFoundException {
	    FileOutputStream fos = new FileOutputStream(getDataFile());
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(this);
	    oos.close();
	    fos.close();
	}

	/**
	 * Updates the photo by writing the provided image to the associated image file.
	 * 
	 * @param image The updated image to write
	 * @throws IOException if an I/O error occurs while updating the photo
	 */
	private void update(BufferedImage image) throws IOException {
		long startTime = System.currentTimeMillis();
	    ImageSecretary.writeImageToResources(image, getImageFile().getPath(),extension);
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		Logger.getInstance().logInfo(this.fileName+" image has been modified.Elapsed time: "+elapsedTime);
	}

	/**
	 * Gets the data file associated with the photo.
	 * 
	 * @return The data file of the photo
	 */
	public File getDataFile() {
	    File file = new File("data/" + owner.getNickname() + "/images/" + fileName + "/" + DATA_FILE);
	    return file;
	}

	/**
	 * Gets the image file associated with the photo.
	 * 
	 * @return The image file of the photo
	 */
	public File getImageFile() {
	    return new File("data/" + owner.getNickname() + "/images/" + fileName + "/" + fileName + extension);
	}

	/**
	 * Deletes the photo.
	 * This includes deleting the associated image directory and notifying the listeners of the photo deletion.
	 * 
	 * @throws Exception if an error occurs during the photo deletion process
	 */
	public void delete() throws Exception {
	    String filePath = "data/" + owner.getNickname() + "/images/" + fileName;
	    File folder = new File(filePath);

	    if (!folder.exists()) {
	        throw new IOException("Folder does not exist: " + filePath);
	    }

	    deleteFolder(folder);

	    // Create a copy of the listeners list
	    List<PhotoListener> listenersCopy = new ArrayList<>(listeners);
	    for (PhotoListener listener : listenersCopy) {
	        listener.onDeleted(this);
	    }
	}

	/**
	 * Recursively deletes a folder and its contents.
	 * 
	 * @param folder The folder to delete
	 * @throws IOException if an I/O error occurs while deleting the folder
	 */
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
    
    /**
     * Adds a comment to the photo.
     * The comment is added to the list of comments and the photo is saved.
     * If an exception occurs during the saving process, the comment is removed from the list.
     * The listeners are notified of the comment addition.
     * 
     * @param comment The comment to add
     */
    public void addComment(Comment comment) {
        try {
            comments.add(comment);
            save();

            // Create a copy of the listeners list
            List<PhotoListener> listenersCopy = new ArrayList<>(listeners);
            for (PhotoListener listener : listenersCopy) {
                listener.onCommentAdded(this);
            }
        } catch (Exception e) {
            Logger.getInstance().logInfo(e.getMessage());
            comments.remove(comment);
        }
    }

    /**
     * Gets the description of the photo.
     * If the description is null, returns a default message.
     * 
     * @return The description of the photo or a default message if the description is null
     */
    public String getDescription() {
        if (description == null) {
            return "photo does not have a description";
        }
        return description;
    }

    /**
     * Sets the description of the photo.
     * The description is updated and the photo is saved.
     * If an exception occurs during the saving process, the description is rolled back to the previous value.
     * The listeners are notified of the description change.
     * 
     * @param description The new description to set
     * @throws IOException if an I/O error occurs while saving the photo
     * @throws FileNotFoundException if the associated data file is not found
     */
    public void setDescription(String description) throws IOException, FileNotFoundException {
        String old = this.description;
        try {
            this.description = description;
            save();

            // Create a copy of the listeners list
            List<PhotoListener> listenersCopy = new ArrayList<>(listeners);

            // Iterate over the copy of the listeners list
            for (PhotoListener listener : listenersCopy) {
                listener.onDescriptionChanged(this);
            }
        } catch (Exception e) {
            Logger.getInstance().logError(e.getMessage());
            this.description = old;
            throw e;
        }
    }

    /**
     * Gets the owner of the photo.
     * 
     * @return The owner of the photo
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the photo.
     * The owner is updated and the photo is saved.
     * If an exception occurs during the saving process, the owner is rolled back to the previous value.
     * 
     * @param owner The new owner to set
     * @throws IOException if an I/O error occurs while saving the photo
     * @throws FileNotFoundException if the associated data file is not found
     */
    public void setOwner(User owner) throws IOException, FileNotFoundException {
        User old = this.owner;
        try {
            this.owner = owner;
            save();
        } catch (Exception e) {
            Logger.getInstance().logError(e.getMessage());
            this.owner = old;
            throw e;
        }
    }

    /**
     * Gets the filename of the photo.
     * 
     * @return The filename of the photo
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the filename of the photo.
     * The filename is updated and the photo is saved.
     * If an exception occurs during the saving process, the filename is rolled back to the previous value.
     * 
     * @param fileName The new filename to set
     * @throws IOException if an I/O error occurs while saving the photo
     * @throws FileNotFoundException if the associated data file is not found
     */
    public void setFileName(String fileName) throws IOException, FileNotFoundException {
        String old = this.fileName;
        try {
            this.fileName = fileName;
            save();
        } catch (Exception e) {
            Logger.getInstance().logError(e.getMessage());
            this.fileName = old;
            throw e;
        }
    }

    /**
     * Gets the list of comments for the photo.
     * 
     * @return The list of comments for the photo
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Sets the list of comments for the photo.
     * The comments are updated and the photo is saved.
     * If an exception occurs during the saving process, the comments are rolled back to the previous value.
     * 
     * @param comments The new list of comments to set
     * @throws IOException if an I/O error occurs while saving the photo
     * @throws FileNotFoundException if the associated data file is not found
     */
    public void setComments(List<Comment> comments) throws IOException, FileNotFoundException {
        List<Comment> old = this.comments;
        try {
            this.comments = comments;
            save();
        } catch (Exception e) {
            Logger.getInstance().logError(e.getMessage());
            this.comments = old;
            throw e;
        }
    }

    /**
     * Gets the list of users who liked the photo.
     * 
     * @return The list of users who liked the photo
     */
    public List<User> getLikes() {
        return likes;
    }

    /**
     * Sets the list of users who liked the photo.
     * The likes are updated and the photo is saved.
     * If an exception occurs during the saving process, the likes are rolled back to the previous value.
     * 
     * @param likes The new list of users who liked the photo
     * @throws IOException if an I/O error occurs while saving the photo
     * @throws FileNotFoundException if the associated data file is not found
     */
    public void setLikes(List<User> likes) throws IOException, FileNotFoundException {
        List<User> old = this.likes;
        try {
            this.likes = likes;
            save();
        } catch (Exception e) {
            Logger.getInstance().logError(e.getMessage());
            this.likes = old;
            throw e;
        }
    }

    /**
     * Gets the list of applied filters for the photo.
     * 
     * @return The list of applied filters for the photo
     */
    public List<PhotoFilter> getAppliedFilters() {
        return appliedFilters;
    }

    /**
     * Adds a filter to the photo.
     * The filter is added to the list of applied filters, and the filtered image is updated.
     * The photo is then saved, and the listeners are notified of the filter application.
     * If an exception occurs during the saving process, the filter is rolled back from the list of applied filters.
     * 
     * @param filter The filter to add
     * @param filteredImage The filtered image resulting from applying the filter
     * @throws Exception if an error occurs while applying the filter or saving the photo
     */
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

            Logger.getInstance().logInfo(owner.getNickname() + " applied " + filter + " to the image: " + fileName);
        } catch (Exception e) {
            Logger.getInstance().logError(e.getMessage());
            appliedFilters.remove(filter);
            throw e;
        }
    }

    /**
     * Gets the image matrix representation of the photo.
     * 
     * @return The image matrix representation of the photo
     * @throws IOException if an I/O error occurs while reading the image file
     */
    public ImageMatrix getImageMatrix() throws IOException {
        return new ImageMatrix(ImageIO.read(getImageFile()));
    }

    /**
     * Checks if the photo is public.
     * 
     * @return true if the photo is public, false otherwise
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Sets the visibility of the photo.
     * The visibility is updated and the photo is saved.
     * If an exception occurs during the saving process, the visibility is rolled back to the previous value.
     * The listeners are notified of the visibility change.
     * 
     * @param isPublic true to set the photo as public, false to set it as private
     * @throws IOException if an I/O error occurs while saving the photo
     * @throws FileNotFoundException if the associated data file is not found
     */
    public void setPublic(boolean isPublic) throws IOException, FileNotFoundException {
        boolean old = this.isPublic;
        try {
            this.isPublic = isPublic;
            save();

            // Create a copy of the listeners list
            List<PhotoListener> listenersCopy = new ArrayList<>(listeners);

            // Iterate over the copy of the listeners list
            for (PhotoListener listener : listenersCopy) {
                listener.onVisibilityChanged(this);
            }
        } catch (Exception e) {
            Logger.getInstance().logError(e.getMessage());
            this.isPublic = old;
            throw e;
        }
    }

    /**
     * Adds a like to the photo from a user.
     * If the user has already liked the photo, an IllegalArgumentException is thrown.
     * The like is added to the list of likes, and the photo is saved.
     * If an exception occurs during the saving process, the like is rolled back from the list of likes.
     * 
     * @param user The user who liked the photo
     * @throws IOException if an I/O error occurs while saving the photo
     * @throws FileNotFoundException if the associated data file is not found
     * @throws IllegalArgumentException if the user has already liked the photo
     */
    public void like(User user) throws IOException, FileNotFoundException, IllegalArgumentException {
        if (likes.contains(user)) {
            throw new IllegalArgumentException("The user " + user.getNickname() + " already liked the photo.");
        } else {
            try {
                this.likes.add(user);
                save();
            } catch (Exception e) {
                Logger.getInstance().logError(e.getMessage());
                this.likes.remove(user);
                throw e;
            }
        }
    }

    /**
     * Removes a like from the photo for a user.
     * If the user has not liked the photo, an IllegalArgumentException is thrown.
     * The like is removed from the list of likes, and the photo is saved.
     * If an exception occurs during the saving process, the like is rolled back to the list of likes.
     * 
     * @param user The user who unliked the photo
     * @throws IllegalArgumentException if the user has not liked the photo
     * @throws FileNotFoundException if the associated data file is not found
     * @throws IOException if an I/O error occurs while saving the photo
     */
    public void disLike(User user) throws IllegalArgumentException, FileNotFoundException, IOException {
        if (!likes.contains(user)) {
            throw new IllegalArgumentException("The user " + user.getNickname() + " did not like the photo.");
        } else {
            likes.remove(user);
            try {
                save();
            } catch (Exception e) {
                Logger.getInstance().logError(e.getMessage());
                likes.add(user);
                throw e;
            }
        }
    }

    /**
     * Checks if the photo is equal to another object.
     * Two photos are considered equal if their filenames are equal.
     * 
     * @param obj The object to compare
     * @return true if the photos are equal, false otherwise
     */
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

    /**
     * Generates a string representation of the photo.
     * The string includes the filename and the list of comments.
     * 
     * @return The string representation of the photo
     */
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
