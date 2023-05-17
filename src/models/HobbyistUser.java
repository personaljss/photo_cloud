package models;

public class HobbyistUser extends User {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -2811356941421089933L;

	public HobbyistUser(String nickname, String password, String realName, String surname, String string, String emailAddress) {
        super(nickname, password, realName, surname, string, emailAddress);
    }

    public void applyFilter(String filterName) {
        // allow "Blur", "Sharpen", "Brightness", and "Contrast"
    }

    public void sharePhoto(String photoPath, String description) {
        // Implementation for sharing photo
    }
}