package gui.profile;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import auth.Authentication;
import models.User;

public class ChangeCredentialsWindow extends JFrame {

    private static final long serialVersionUID = 7574049147951311089L;
    private ProfilePage profilePage;
    private JTextField emailTextField;
    private JTextField passwordTextField;
    private JTextField ageTextField;
    private User user;

    // Constructor
    public ChangeCredentialsWindow(ProfilePage profilePage) {
        this.profilePage = profilePage;
        this.user=Authentication.getInstance().getCurrentUser();

        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField(20);
        emailTextField.setText(user.getEmailAddress());

        JLabel passwordLabel = new JLabel("Password:");
        passwordTextField = new JTextField(20);
        passwordTextField.setText(user.getPassword());

        JLabel ageLabel = new JLabel("Age:");
        ageTextField = new JTextField(20);
        ageTextField.setText(user.getAge());

        JButton approveButton = new JButton("Approve");
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeCredentials();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindow();
            }
        });

        // Create panel for the fields
        JPanel fieldPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        fieldPanel.add(emailLabel, gbc);
        gbc.gridy++;
        fieldPanel.add(passwordLabel, gbc);
        gbc.gridy++;
        fieldPanel.add(ageLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        fieldPanel.add(emailTextField, gbc);
        gbc.gridy++;
        fieldPanel.add(passwordTextField, gbc);
        gbc.gridy++;
        fieldPanel.add(ageTextField, gbc);

        // Create panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(approveButton);
        buttonPanel.add(cancelButton);

        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(fieldPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to the frame
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        // Set the window properties
        setTitle("Change Credentials");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    private void changeCredentials() {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String age = ageTextField.getText();

        Authentication auth = Authentication.getInstance();
        User user = auth.getCurrentUser();

        // Check if the email is valid
        if (!auth.checkEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email address!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the password is valid
        if (!auth.checkPassword(password)) {
            JOptionPane.showMessageDialog(this, "Invalid password! Password should be at least 5 characters long and include at least one digit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the age is valid
        if (!auth.checkAge(age)) {
            JOptionPane.showMessageDialog(this, "Invalid age! Age should be a positive number less than 130.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Update the user's age, email, and password
            user.setAge(age);
            user.setEmailAddress(email);
            user.setPassword(password);
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Display a success message
        JOptionPane.showMessageDialog(this, "Credentials updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        profilePage.updateUser();
        disposeWindow(); // Close the window after changing the credentials
    }

    // Method to dispose the window
    private void disposeWindow() {
        dispose();
    }


}
