package authentication;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import auth.AuthResult;
import auth.Authentication;

public class SignupForm extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7081734238708443993L;
	private JButton signupButton;
    private JButton backButton;
    private JTextField nickNameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField realNameField;
    private JTextField surnameField;
    private JTextField ageField;
    private JTextField emailField;
    private JComboBox<String> userTypeBox;
    private JLabel messageLabel;

    public SignupForm(AuthFrame authFrame) {
        setLayout(new GridLayout(10, 2));
        

        nickNameField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        realNameField = new JTextField();
        surnameField = new JTextField();
        ageField = new JTextField();
        emailField=new JTextField();
        userTypeBox = new JComboBox<>(new String[] {"Administrator","ProfessionalUser","HobbyistUser",});
        signupButton = new JButton("Sign Up");
        backButton = new JButton("Back");
        messageLabel = new JLabel();

        add(new JLabel("Nickname:"));
        add(nickNameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Confirm Password:"));
        add(confirmPasswordField);
        add(new JLabel("Real Name:"));
        add(realNameField);
        add(new JLabel("Surname:"));
        add(surnameField);
        add(new JLabel("Age:"));
        add(ageField);
        add(new JLabel("email"));
        add(emailField);
        add(new JLabel("User Type:"));
        add(userTypeBox);
        add(signupButton);
        add(backButton);
        add(messageLabel);

        signupButton.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
                Authentication auth = Authentication.getInstance();
                
                String nickName = nickNameField.getText();
                String password = new String(passwordField.getPassword());  // getPassword() returns a char array
                String realName = realNameField.getText();
                String surname = surnameField.getText();
                String emailAddress = emailField.getText();
                String age = ageField.getText();  // This may throw a NumberFormatException if the age is not a valid integer
                String type = (String) userTypeBox.getSelectedItem();

                AuthResult result = auth.signUp(nickName, password, realName, surname, age, emailAddress, type);
                
                // Print the AuthResult message
                JOptionPane.showMessageDialog(null, result.getMessage());
                
                // If the sign-up was successful, close this form and open the login form
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authFrame.switchToLoginForm();
            }
        });
    }
}
