package models;

public class Administrator extends ProfessionalUser {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4068110302331780962L;

	public Administrator(String nickname, String password, String realName, String surname, String age, String emailAddress) {
        super(nickname, password, realName, surname, age, emailAddress);
    }

    public void removePhoto(String photoPath) {
        // Implementation for removing photo from Discover page
        // and the owner's Profile page
    }
}
