package models;

public class FreeUser extends User {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7043280030984140319L;

	public FreeUser(String nickname, String password, String realName, String surname, String age, String emailAddress) {
        super(nickname, password, realName, surname, age, emailAddress);
    }


	public void applyFilter(String filterName) {
        // only allow "Blur" and "Sharpen"
    }

    public void sharePhoto(String photoPath, String description) {
        // Implementation for sharing photo
    }
}