package models;

import java.awt.image.ImageFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import exceptions.UserAlreadyLikedPhotoException;
import exceptions.UserDidNotLikePhotoException;
import services.ImageMatrix;
import services.ImageSecretary;
import services.Logger;


public class Photo implements Serializable{
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
	private ImageMatrix imageMatrix;
	
	
	public Photo(User owner, String fileName, List<ImageFilter> appliedFilters, List<Comment> comments,
			List<User> likes, ImageMatrix imageMatrix) {
		this.owner = owner;
		this.fileName = fileName;
		this.appliedFilters = appliedFilters;
		this.comments = comments;
		this.likes = likes;
		this.imageMatrix = imageMatrix;
		this.isPublic=false;
	}
	
	public Photo(User owner, String fileName, List<ImageFilter> appliedFilters, List<Comment> comments,
			List<User> likes) {
		this.owner = owner;
		this.fileName = fileName;
		this.appliedFilters = appliedFilters;
		this.comments = comments;
		this.likes = likes;
		try {
			this.imageMatrix=ImageSecretary.readResourceImage(getSource());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			Logger.logError(e.getMessage());;
		}
	}
	
	public String getSource() {
		return "data/"+owner.getNickname()+"/images/"+fileName;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<User> getLikes() {
		return likes;
	}

	public void setLikes(List<User> likes) {
		this.likes = likes;
	}

	public List<ImageFilter> getAppliedFilters() {
		return appliedFilters;
	}

	public void addFilter(ImageFilter filter) {
		//TODO: manipulate imageMatrix
		appliedFilters.add(filter);
	}
	
	public void setImageMatrix(ImageMatrix matrix) {
		this.imageMatrix=matrix;
	}
	
	public ImageMatrix getImageMatrix() {
		return imageMatrix;
	}
	
	public void setAppliedFilters(List<ImageFilter> appliedFilters) {
		this.appliedFilters = appliedFilters;
	}
	
	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public void like(User user) throws UserAlreadyLikedPhotoException {
		if(likes.contains(user)) {
			throw new UserAlreadyLikedPhotoException();
		}else {
			likes.add(user);
		}
	}
	
	public void disLike(User user) throws UserDidNotLikePhotoException {
		if(likes.contains(user)) {
			throw new UserDidNotLikePhotoException();
		}else {
			likes.remove(user);
		}
	}
	
	
	public void addComment(Comment comment) {
		comments.add(comment);
	}
	
	public void removeComment(Comment comment) {
		comments.remove(comment);
	}// In Photo class
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("{")
	      .append("\"fileName\": \"").append(fileName).append("\",")
	      .append("\"comments\": [");

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
