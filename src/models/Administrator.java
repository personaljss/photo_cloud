package models;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Administrator extends ProfessionalUser {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4068110302331780962L;

	public Administrator(String nickname, String password, String realName, String surname, String age, String emailAddress) {
        super(nickname, password, realName, surname, age, emailAddress);
    }

    public void banPhoto(Photo photo) throws FileNotFoundException, IOException {
        // Implementation for removing photo from Discover page
        photo.setPublic(false);
    }
    
    @Override 
    public String getType() {
    	return "Administrator";
    }
}
