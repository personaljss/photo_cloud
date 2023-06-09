package gui.authentication;

import javax.swing.JFrame;

public class AuthFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4251028888666973402L;
	private LoginForm loginForm;
    private SignupForm signupForm;

    public AuthFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signupForm = new SignupForm(this); // Temporarily pass this
        loginForm = new LoginForm(this);

        setContentPane(loginForm);
        //setSize(600, 300);
        setVisible(true);
        // Configure the frame

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void switchToLoginForm() {
        setContentPane(loginForm);
        validate();
    }

    public void switchToSignupForm() {
        setContentPane(signupForm);
        validate();
    }
}
