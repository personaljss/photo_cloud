package gui.authentication;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import auth.AuthResult;
import auth.Authentication;
import gui.Navigator;

/**
 * Represents the login form panel.
 */
public class LoginForm extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8864639821195149474L;
	private JButton loginButton;
	private JLabel signupLabel;
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JLabel messageLabel;
	private AuthFrame authFrame;
	private JLabel emailLabel;
	private JLabel passwordLabel;
	private JLabel titleLabel;

	/**
	 * Constructs a new LoginForm object.
	 *
	 * @param authFrame the parent AuthFrame
	 */
	public LoginForm(AuthFrame authFrame) {
		this.authFrame = authFrame;
		initializePanel();
		createComponents();
		addComponents();
		addEventListeners();
	}

	/**
	 * Initializes the panel settings.
	 */
	private void initializePanel() {
		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(400, 600));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	/**
	 * Creates the components of the login form.
	 */
	private void createComponents() {
		titleLabel = new JLabel("PhotoCloud");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(Color.BLACK);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setVerticalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		emailLabel = new JLabel("NickName: ");
		emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		emailLabel.setForeground(Color.BLACK);

		passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		passwordLabel.setForeground(Color.BLACK);

		userNameField = new JTextField();
		userNameField.setPreferredSize(new Dimension(250, 40));
		userNameField
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY),
						BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		userNameField.setToolTipText("Username");

		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(250, 40));
		passwordField
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY),
						BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		passwordField.setToolTipText("Password");

		loginButton = new JButton("Login");
		loginButton.setPreferredSize(new Dimension(250, 40));

		signupLabel = new JLabel("<html><u>Don't you have an account? Signup</u></html>");
		signupLabel.setForeground(Color.BLUE);
		signupLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		signupLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		signupLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		messageLabel = new JLabel();
	}

	/**
	 * Adds the components to the panel.
	 */
	private void addComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 0, 10, 0);

		add(titleLabel, gbc);
		add(emailLabel, gbc);
		add(userNameField, gbc);
		add(createInvisibleBox(), gbc);
		add(passwordLabel, gbc);
		add(passwordField, gbc);
		add(createInvisibleBox(), gbc);
		add(createInvisibleBox(), gbc);
		add(loginButton, gbc);
		add(createInvisibleBox(), gbc);
		add(signupLabel, gbc);
		add(messageLabel, gbc);
	}

	/**
	 * Creates an invisible box with the desired height.
	 *
	 * @return the invisible box panel
	 */
	private JPanel createInvisibleBox() {
		JPanel invisibleBox = new JPanel();
		invisibleBox.setPreferredSize(new Dimension(250, 10));
		invisibleBox.setOpaque(false);
		return invisibleBox;
	}

	/**
	 * Adds event listeners to the buttons and labels.
	 */
	private void addEventListeners() {
		signupLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				authFrame.switchToSignupForm();
			}
		});

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
					goToHomeFrame();
				} else {
					messageLabel.setForeground(Color.RED);
				}
				messageLabel.setText(result.getMessage());
			}
		});
	}

	/**
	 * Navigates to the home frame.
	 */
	private void goToHomeFrame() {
		Navigator.getInstance().navigateTo(Navigator.PROFILE_PAGE);
	}
}
