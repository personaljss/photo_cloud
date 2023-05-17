package models;

import java.io.Serializable;

public class Comment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1100575652486847167L;
	private User owner;
	private String text;
	
	public Comment(User owner, String text) {
		this.owner = owner;
		this.text = text;
	}
	
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}// In Comment class
	
	@Override
	public String toString() {
	    return "{" +
	            "\"owner\": \"" + owner.getNickname() + "\"," +
	            "\"text\": \"" + text + "\"" +
	            "}";
	}

	
}
