package gui.profile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import gui.home.DiscoveryPage;
import gui.home.FileChooser;
import listeners.PhotoListener;
import models.Photo;
import models.User;

/**
 * Represents the profile page of a user.
 */
public class ProfilePage extends JFrame implements PhotoListener{

    private static final long serialVersionUID = 8018934881827210300L;
    private User user;
    private JPanel rightPanel;
    private JMenuBar menuBar;
    private JPanel leftPanel;

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
        createMenuBar();
    }

    /**
     * Initializes the profile page.
     */
    private void initialize() {
        setTitle("Profile Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        setLocationRelativeTo(null); // center the frame
        setBounds(100, 100, 800, 600);
        getContentPane().setLayout(new BorderLayout());
    }

    /**
     * Creates the left panel of the profile page.
     */
    private void createLeftPanel() {
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(200, getHeight()));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the profile photo panel
        ProfilePhotoPanel profilePhotoPanel = null;

		profilePhotoPanel = new ProfilePhotoPanel(user, 150);

        leftPanel.add(profilePhotoPanel, BorderLayout.NORTH);

        // Create the user information panel
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel nicknameLabel = new JLabel("Nickname: " + user.getNickname());
        userInfoPanel.add(nicknameLabel);

        JLabel emailLabel = new JLabel("Email: " + user.getEmailAddress());
        userInfoPanel.add(emailLabel);

        JLabel ageLabel = new JLabel("Age: " + user.getAge());
        userInfoPanel.add(ageLabel);

        leftPanel.add(userInfoPanel, BorderLayout.CENTER);

        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadPhoto();
            }
        });
        buttonPanel.add(uploadButton);

        JButton discoveryButton = new JButton("Discovery");
        discoveryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DiscoveryPage();
                ProfilePage.this.dispose();
            }
        });
        buttonPanel.add(discoveryButton);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(leftPanel, BorderLayout.WEST);
        setVisible(true);
    }


	/**
     * Creates the menu bar and adds menu items.
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();

        JMenu navigationMenu = new JMenu("Navigation");
        JMenuItem discoveryMenuItem = new JMenuItem("Discovery");
        discoveryMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	navigateToDicoveryPage();
            }

        });
        navigationMenu.add(discoveryMenuItem);
        menuBar.add(navigationMenu);
        
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem credentialsMenuItem = new JMenuItem("Credentials");
        credentialsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new ChangeCredentialsWindow(ProfilePage.this);
            }

        });
        
        optionsMenu.add(credentialsMenuItem);
        menuBar.add(optionsMenu);

        setJMenuBar(menuBar);
    }
    
    
    private void navigateToDicoveryPage() {
        new DiscoveryPage();
        dispose();
    }

    /**
     * Creates the right panel of the profile page.
     */

    private void createRightPanel() {
        rightPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // Use GridLayout with 3 columns
        JScrollPane scrollPane = new JScrollPane(rightPanel);
        rightPanel.setPreferredSize(new Dimension(930, getHeight()));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        displayImages();

        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
    

    /**
     * Displays the images in the right panel.
     */

    public void displayImages() {
        rightPanel.removeAll(); // Clear existing images

        for (Photo photo : user.getAlbum()) {
        	photo.addListener(this);
            try {
                GalleryPhotoPanel panel = new GalleryPhotoPanel(photo);
                //panel.setGalleryActionListener(this);
                rightPanel.add(panel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Add invisible boxes if there are less than 4 images
        int numImages = user.getAlbum().size();
        if (numImages < 4) {
            for (int i = 0; i < (4 - numImages); i++) {
                rightPanel.add(createInvisibleBox());
            }
        }

        rightPanel.revalidate();
        rightPanel.repaint();
    }

    /**
     * Creates an invisible box with the same size as the gallery photo panel.
     * The invisible box is used to maintain a consistent layout with empty slots.
     *
     * @return the invisible box panel
     */
    private JPanel createInvisibleBox() {
        JPanel invisibleBox = new JPanel();
        invisibleBox.setPreferredSize(new Dimension(300, 200)); // Set the desired size
        invisibleBox.setOpaque(false); // Make the panel transparent
        return invisibleBox;
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
                displayImages(); // Update the gallery display
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


	public void updateUser() {
	    // Remove the old leftPanel from the frame
	    getContentPane().remove(leftPanel);
	    
	    // Create a new leftPanel with the updated user information
	    createLeftPanel();
	    
	    // Add the new leftPanel to the frame
	    getContentPane().add(leftPanel, BorderLayout.WEST);
	    
	    revalidate(); // Revalidate the frame to reflect the changes
	}

	//TODO: write separate methods
	@Override
	public void onDeleted(Photo photo) {
		// TODO Auto-generated method stub
		displayImages();
	}

	@Override
	public void onFilterApplied(Photo photo) {
		// TODO Auto-generated method stub
		displayImages();
	}

	@Override
	public void onDescriptionChanged(Photo photo) {
		// TODO Auto-generated method stub
		displayImages();
	}

	@Override
	public void onCommentAdded(Photo photo) {
		// TODO Auto-generated method stub
		displayImages();
	}

	@Override
	public void dispose() {
		for(Photo photo : user.getAlbum()) {
			photo.removeListener(this);
		}
		super.dispose();
	}

}
