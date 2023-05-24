package gui.authentication;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import auth.AuthResult;
import auth.Authentication;

/**
 * Represents the signup form panel.
 */
public class SignupForm extends JPanel {
    private JLabel nickNameLabel;
    private JTextField nickNameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel confirmPasswordLabel;
    private JPasswordField confirmPasswordField;
    private JLabel realNameLabel;
    private JTextField realNameField;
    private JLabel surnameLabel;
    private JTextField surnameField;
    private JLabel ageLabel;
    private JTextField ageField;
    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel userTypeLabel;
    private JComboBox<String> userTypeBox;
    private JButton signupButton;
    private JButton backButton;
    private JLabel messageLabel;
    private AuthFrame authFrame;

    /**
     * Constructs a new SignupForm object.
     *
     * @param authFrame the parent AuthFrame
     */
    public SignupForm(AuthFrame authFrame) {
    	this.authFrame=authFrame;
        initializePanel();
        createComponents();
        addComponents();
        addEventListeners(authFrame);
    }

    /**
     * Initializes the panel settings.
     */
    private void initializePanel() {
        setLayout(new GridBagLayout());
    }

    /**
     * Creates the components of the signup form.
     */
    private void createComponents() {
        nickNameLabel = createLabel("Nickname:");
        nickNameField = createTextField();

        passwordLabel = createLabel("Password:");
        passwordField = createPasswordField();

        confirmPasswordLabel = createLabel("Confirm Password:");
        confirmPasswordField = createPasswordField();

        realNameLabel = createLabel("Real Name:");
        realNameField = createTextField();

        surnameLabel = createLabel("Surname:");
        surnameField = createTextField();

        ageLabel = createLabel("Age:");
        ageField = createTextField();

        emailLabel = createLabel("Email:");
        emailField = createTextField();

        userTypeLabel = createLabel("User Type:");
        userTypeBox = createUserTypeComboBox();

        signupButton = new JButton("Sign Up");
        backButton = new JButton("Do you have an account? Log in");
        messageLabel = new JLabel();
    }

    /**
     * Creates a label component with the given text.
     *
     * @param text the label text
     * @return the created label component
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(120, 30));
        return label;
    }

    /**
     * Creates a text field component.
     *
     * @return the created text field component
     */
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(250, 30));
        return textField;
    }

    /**
     * Creates a password field component.
     *
     * @return the created password field component
     */
    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 30));
        return passwordField;
    }

    /**
     * Creates a user type combo box component.
     *
     * @return the created combo box component
     */
    private JComboBox<String> createUserTypeComboBox() {
        String[] userTypes = {"Administrator", "ProfessionalUser", "HobbyistUser","FreeUser",};
        JComboBox<String> comboBox = new JComboBox<>(userTypes);
        comboBox.setPreferredSize(new Dimension(250, 30));
        return comboBox;
    }
    /**
     * Adds the components to the panel.
     */
    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        addDuo(nickNameLabel, nickNameField, gbc);
        addDuo(passwordLabel, passwordField, gbc);
        addDuo(confirmPasswordLabel, confirmPasswordField, gbc);
        addDuo(realNameLabel, realNameField, gbc);
        addDuo(surnameLabel, surnameField, gbc);
        addDuo(ageLabel, ageField, gbc);
        addDuo(emailLabel, emailField, gbc);
        addDuo(userTypeLabel, userTypeBox, gbc);
        addSignupButton(gbc);
        addLoginButton(gbc);
        //add(messageLabel, createInvisibleBox(gbc));
    }

    /**
     * Adds the signup button to the panel.
     *
     * @param gbc the GridBagConstraints for layout positioning
     */
    private void addSignupButton(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);
        signupButton.setPreferredSize(new Dimension(200, 30)); // Adjust the width as desired
        add(signupButton, gbc);
    }

    /**
     * Adds the login button (label) to the panel.
     *
     * @param gbc the GridBagConstraints for layout positioning
     */
    private void addLoginButton(GridBagConstraints gbc) {
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);
        backButton.setText("<html><u>Do you have an account? Log in</u></html>");
        backButton.setForeground(Color.BLUE);
        backButton.setPreferredSize(new Dimension(200, 30)); // Adjust the width as desired
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authFrame.switchToLoginForm();
            }
        });
        add(backButton, gbc);
    }

    /**
     * Adds a label-field pair (duo) to the panel.
     *
     * @param component the label component
     * @param component2 the field component
     * @param gbc   the GridBagConstraints for layout positioning
     */
    private void addDuo(Component component, Component component2, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        add(component, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(component2, gbc);
    }

    /**
     * Creates an invisible box to add vertical spacing in the layout.
     *
     * @return the invisible box component
     */
    private Component createInvisibleBox() {
        return Box.createRigidArea(new Dimension(1, 20));
    }

    /**
     * Adds event listeners to the buttons.
     *
     * @param authFrame the parent AuthFrame
     */
    private void addEventListeners(AuthFrame authFrame) {
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Authentication auth = Authentication.getInstance();

                String nickName = nickNameField.getText();
                String password = new String(passwordField.getPassword());
                String realName = realNameField.getText();
                String surname = surnameField.getText();
                String emailAddress = emailField.getText();
                String age = ageField.getText();
                String type = (String) userTypeBox.getSelectedItem();

                AuthResult result = auth.signUp(nickName, password, realName, surname, age, emailAddress, type);

                // Print the AuthResult message
                JOptionPane.showMessageDialog(null, result.getMessage());

                // If the sign-up was successful, close this form and open the login form
                if (result.isSuccess()) {
                    authFrame.switchToLoginForm();
                }
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
