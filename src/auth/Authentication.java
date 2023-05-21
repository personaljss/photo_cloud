package auth;


import java.io.IOException;
import models.AppState;
import models.User;
import services.Logger;

/**
 * Singleton class responsible for user authentication operations: logging in and signing up.
 */
public class Authentication {
	// The currently logged in user
	@SuppressWarnings("unused")
	private User user;

	// AppState instance to manage the user's state after signup and to validate login
	AppState appState;

	// Singleton instance
	private static Authentication instance;

	/**
	 * Private constructor for singleton pattern.
	 * Initializes the AppState instance.
	 */
	private Authentication() {
		appState = AppState.getInstance();
	}

	/**
	 * Returns the singleton instance.
	 */
	public static synchronized Authentication getInstance() {
		if (instance == null) {
			instance = new Authentication();
		}
		return instance;
	}

	/**
	 * Logs in a user with the given nickname and password.
	 * Returns an AuthResult indicating success or failure.
	 */
	public AuthResult logIn(String nickName, String password) {
	    AuthResult result = null;
	  
        User user = appState.getUser(nickName);
        if(user==null) {
            result = new AuthResult(false, "Wrong nickName.");
        }else if (password.equals(user.getPassword())) {
        	this.user = user;
            result = new AuthResult(true, "Login successful.");
        } else {
            result = new AuthResult(false, "Password is wrong.");
        }
	    Logger.logInfo(nickName + ": " + (result != null ? result.getMessage() : "null"));
	    return result;
	}

	/**
	 * Signs up a new user with the given details.
	 * If the user already exists, returns an AuthResult indicating failure.
	 * Otherwise, creates a new user, saves the user data, and returns an AuthResult indicating success.
	 */
	public AuthResult signUp(String nickName, String password, String realName, String surname, String age, String emailAddress, String type) {
	    AuthResult result = null;
	    if(!checkNickName(nickName)) {
	        return new AuthResult(false, "Inappropriate nickname");        
	    }
	    if(!checkPassword(password)) {
	        return new AuthResult(false, "Inappropriate password");
	    }
	    if(!checkEmail(emailAddress)) {
	        return new AuthResult(false,"Invalid email");
	    }
	    if(!checkAge(age)) {
	        return new AuthResult(false,"Invalid age");
	    }
	    if (User.exists(nickName)) {
	        return new AuthResult(false, "User already exists. Please log in.");
	    }
	    try {
	        User user = User.create(nickName, password, realName, surname, age, emailAddress, type);
	        appState.addUser(user);
	        result = new AuthResult(true, "Signed up successfully");
	    } catch (IOException e) {
	        result = new AuthResult(false, "A problem occurred.");
	        Logger.logError(e.getMessage());
	    }
	    Logger.logInfo("User " + nickName + " created.");
	    return result;
	}



	// The following methods are placeholders for checks for password, nickname, age, and email address
	// TODO: Implement these methods with actual checks

	public boolean checkPassword(String password) {
	    // Password should be at least 5 characters long and include at least one digit.
	    String passwordPattern = "^(?=.*[0-9]).{5,}$";
	    return password.matches(passwordPattern);
	}


	public boolean checkNickName(String nickName) {
	    // Nickname should start with a letter and can contain letters, numbers, and underscores.
	    String nicknamePattern = "^[a-zA-Z]\\w*$";
	    return nickName.matches(nicknamePattern);
	}

	public boolean checkAge(String age) {
	    // Age should be a positive number less than 130 (oldest human recorded was 122 years old).
	    try {
	        int ageNumber = Integer.parseInt(age);
	        return ageNumber > 0 && ageNumber < 130;
	    } catch (NumberFormatException e) {
	        // If age is not a valid number, return false.
	        return false;
	    }
	}
	
	

	public boolean checkEmail(String emailAddress) {
	    // Simple email validation. There are more complex patterns that could be used here.
	    String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
	    return emailAddress.matches(emailPattern);
	}
	
	public User getCurrentUser() {
		return user;
	}

}
