package models;

public class ProfessionalUser extends User {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4095539975158893192L;

	public ProfessionalUser(String nickname, String password, String realName, String surname, String age, String emailAddress) {
        super(nickname, password, realName, surname, age, emailAddress);
    }

    public void applyFilter(String filterName) {
        // allow all filters
    }

    public void sharePhoto(String photoPath, String description) {
        // Implementation for sharing photo
    }
}