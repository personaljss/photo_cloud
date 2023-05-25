package models;

public class HobbyistUser extends User {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -2811356941421089933L;

	public HobbyistUser(String nickname, String password, String realName, String surname, String string, String emailAddress) {
        super(nickname, password, realName, surname, string, emailAddress);
    }


	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Hobbyist";
	}
}