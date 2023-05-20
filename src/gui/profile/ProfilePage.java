package gui.profile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.ScrollPaneConstants;

import gui.home.DiscoveryPage;
import gui.home.FileChooser;
import listeners.GalleryActionListener;
import models.Photo;
import models.User;

/**
 * Represents the profile page of a user.
 */
public class ProfilePage extends JFrame implements GalleryActionListener{

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
        setMinimumSize(new Dimension(950, 700));
        setLocationRelativeTo(null); // center the frame
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
        rightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Use FlowLayout instead of GridLayout
        JScrollPane scrollPane = new JScrollPane(rightPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        displayImages();

        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Displays the images in the right panel.
     */
    private void displayImages() {
        rightPanel.removeAll(); // Clear existing images

        for (Photo photo : user.getAlbum()) {
            try {
            	GalleryPhotoPanel panel=new GalleryPhotoPanel(photo);
            	panel.setGalleryActionListener(this);
                rightPanel.add(panel);
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
            } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

	@Override
	public void delete(Photo photo) {
		// TODO Auto-generated method stub
		displayImages();
	}
}
