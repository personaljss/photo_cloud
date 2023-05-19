package gui.home;

import java.awt.*;
import java.io.*;
import javax.swing.*;

import auth.Authentication;
import models.Photo;
import models.User;

/**
 * HomeFrame is the main window that displays the grid of images for a user.
 */
public class HomeFrame extends JFrame{
    private static final long serialVersionUID = 2076299585750150639L;

    private JButton addPhotoButton;
    private User user;

    private int currentPhotoIndex;
    private JLabel photoLabel;
    private JButton prevButton;
    private JButton nextButton;
    private JPanel menuPanel;
    private JButton profileButton;
    private JButton galleryButton;
    private JButton discoverButton;
    private JButton editButton;
    private JButton shareButton;

    /**
     * Constructs a new HomeFrame window.
     */
    public HomeFrame() {
        user = Authentication.getInstance().getCurrentUser();

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeMenuPanel();
        initializePhotoDisplay();
        initializeNavigationArrows();

        setSize(800, 600);
        setVisible(true);
    }
    
	

    private void uploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                Photo.create(user, fileChooser.getSelectedFile());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, 
                    e.getMessage(), 
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
                e.printStackTrace();
            }
        }
    }


    /**
     * Initializes the menu panel with navigation and edit options.
     */
    private void initializeMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        profileButton = new JButton("Profile");
        profileButton.addActionListener(e -> navigateToProfile());
        menuPanel.add(profileButton);

        galleryButton = new JButton("Gallery");
        galleryButton.addActionListener(e -> navigateToGallery());
        menuPanel.add(galleryButton);

        discoverButton = new JButton("Discover");
        discoverButton.addActionListener(e -> navigateToDiscover());
        menuPanel.add(discoverButton);

        editButton = new JButton("Edit");
        editButton.addActionListener(e -> editPhoto());
        menuPanel.add(editButton);

        shareButton = new JButton("Share");
        shareButton.addActionListener(e -> sharePhoto());
        menuPanel.add(shareButton);
        
        addPhotoButton=new JButton("Add photo");
        addPhotoButton.addActionListener(e -> uploadPhoto());
        menuPanel.add(addPhotoButton);

        add(menuPanel, BorderLayout.NORTH);
    }

    /**
     * Initializes the photo display panel with navigation arrows.
     */
    private void initializePhotoDisplay() {
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new BorderLayout());

        photoLabel = new JLabel();
        photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        photoLabel.setVerticalAlignment(SwingConstants.CENTER);
        photoPanel.add(photoLabel, BorderLayout.CENTER);

        add(photoPanel, BorderLayout.CENTER);
    }

    /**
     * Initializes the navigation arrows.
     */
    private void initializeNavigationArrows() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        prevButton = new JButton("< Prev");
        prevButton.addActionListener(e -> showPreviousPhoto());
        buttonPanel.add(prevButton);

        nextButton = new JButton("Next >");
        nextButton.addActionListener(e -> showNextPhoto());
        buttonPanel.add(nextButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Shows the previous photo in the user's gallery.
     */
    private void showPreviousPhoto() {
        if (currentPhotoIndex > 0) {
            currentPhotoIndex--;
            displayPhoto(user.getAlbum().get(currentPhotoIndex));
        }
    }

    /**
     * Shows the next photo in the user's gallery.
     */
    private void showNextPhoto() {
        if (currentPhotoIndex < user.getAlbum().size() - 1) {
            currentPhotoIndex++;
            displayPhoto(user.getAlbum().get(currentPhotoIndex));
        }
    }

    /**
     * Displays the specified photo in the photo label.
     * @param photo the photo to display
     */
    private void displayPhoto(Photo photo) {
        ImageIcon imageIcon = new ImageIcon(photo.getImageFile().getPath());
        Image image = imageIcon.getImage().getScaledInstance(photoLabel.getWidth(), photoLabel.getHeight(), Image.SCALE_SMOOTH);
        photoLabel.setIcon(new ImageIcon(image));

        updateShareButtonVisibility(photo);
    }

    /**
     * Updates the visibility of the share button based on the privacy of the photo.
     * @param photo the photo to check privacy
     */
    private void updateShareButtonVisibility(Photo photo) {
        shareButton.setVisible(!photo.isPublic());
    }
    
    

    /**
     * Navigates to the user's profile.
     */
    private void navigateToProfile() {
        // TODO: Implement the navigation logic to the profile screen
    }

    /**
     * Navigates to the user's gallery.
     */
    private void navigateToGallery() {
        // TODO: Implement the navigation logic to the gallery screen
    }

    /**
     * Navigates to the discover screen.
     */
    private void navigateToDiscover() {
        // TODO: Implement the navigation logic to the discover screen
    }

    /**
     * Opens the photo editing screen.
     */
    private void editPhoto() {
        // TODO: Implement the logic to open the photo editing screen
    }

    /**
     * Shares the photo.
     */
    private void sharePhoto() {
        // TODO: Implement the logic to share the photo
    }

}
