package gui.authentication;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import auth.AuthResult;
import auth.Authentication;
import gui.Navigator;
import gui.profile.ProfilePage;

public class LoginForm extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton loginButton;
	private JButton signupButton;
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JLabel messageLabel;
	private AuthFrame authFrame;

	public LoginForm(AuthFrame authFrame) {
		this.authFrame=authFrame;
		setLayout(new GridLayout(4, 2));
		userNameField = new JTextField();
		passwordField = new JPasswordField();
		loginButton = new JButton("Login");
		signupButton = new JButton("Sign Up");
		messageLabel = new JLabel();

		add(new JLabel("Username:"));
		add(userNameField);
		add(new JLabel("Password:"));
		add(passwordField);
		add(loginButton);
		add(signupButton);
		add(messageLabel);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = userNameField.getText();
				String password = new String(passwordField.getPassword());
				// Use the Authentication singleton to attempt login
				Authentication auth = Authentication.getInstance();
				AuthResult result = auth.logIn(username, password);
				if (result.isSuccess()) {
					messageLabel.setForeground(Color.GREEN);
					//open home screen
					goToHomeFrame();
					
				} else {
					messageLabel.setForeground(Color.RED);
				}
				messageLabel.setText(result.getMessage());
			}
		});

		signupButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				authFrame.switchToSignupForm();
			}
		});
	}
	
	
	private void goToHomeFrame() {

		Navigator.getInstance().navigateTo(Navigator.PROFILE_PAGE);

	}

}
