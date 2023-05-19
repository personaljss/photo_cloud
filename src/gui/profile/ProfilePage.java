package gui.profile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.home.DiscoveryPage;
import gui.home.FileChooser;
import gui.home.PhotoPanel;
import models.Photo;
import models.User;

/**
 * Represents the profile page of a user.
 */
public class ProfilePage extends JFrame {

    private static final long serialVersionUID = 8018934881827210300L;
    private User user;
    private JPanel rightPanel;

    /**
     * Creates a new ProfilePage instance.
     * 
     * @param user the user associated with the profile page
     */
    public ProfilePage(User user) {
        this.user = user;
        initialize();
        createLeftPanel();
        createRightPanel();
    }

    /**
     * Initializes the profile page.
     */
    private void initialize() {
        setTitle("Profile Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        getContentPane().setLayout(new BorderLayout());
    }

    /**
     * Creates the left panel of the profile page.
     */
    private void createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, getHeight()));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add profile picture (assuming you have a default profile picture path)
        JLabel profilePicLabel = new JLabel(new ImageIcon("resources/likeIcon.png"));
        leftPanel.add(profilePicLabel);

        // Add user information
        JLabel nicknameLabel = new JLabel("Nickname: " + user.getNickname());
        leftPanel.add(nicknameLabel);

        JLabel emailLabel = new JLabel("Email: " + user.getEmailAddress());
        leftPanel.add(emailLabel);

        JLabel ageLabel = new JLabel("Age: " + user.getAge());
        leftPanel.add(ageLabel);

        // Add image upload button
        JButton uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadPhoto();
            }
        });
        leftPanel.add(uploadButton);

        getContentPane().add(leftPanel, BorderLayout.WEST);
        
        JButton discoveryButton = new JButton("Discovery");
        discoveryButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new DiscoveryPage();
        		ProfilePage.this.dispose();
        	}
        });
        leftPanel.add(discoveryButton);
        setVisible(true);
    }

    /**
     * Creates the right panel of the profile page.
     */
    private void createRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 3, 10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(rightPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        displayImages();
    }

    /**
     * Displays the images in the right panel.
     */
    private void displayImages() {
        rightPanel.removeAll(); // Clear existing images

        for (Photo photo : user.getAlbum()) {
            try {
                rightPanel.add(new PhotoPanel(photo));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        rightPanel.revalidate();
        rightPanel.repaint();
    }


    /**
     * Handles the photo upload process.
     */
    private void uploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                Photo.create(user, fileChooser.getSelectedFile());
                displayImages();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
