package gui.profile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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

import auth.Authentication;
import gui.Application;
import gui.IconButton;
import gui.Navigator;
import gui.TabBarPanel;
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
    public ProfilePage() {
        this.user = Authentication.getInstance().getCurrentUser();
        initialize();
        createTabBarPanel();
        createLeftPanel();
        createRightPanel();
        createMenuBar();
        //setVisible(true);
    }

    /**
     * Initializes the profile page.
     */
    private void initialize() {
        setTitle("Profile Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(Application.getInstance().getDimension());
        setLocationRelativeTo(null); // center the frame
        setBounds(100, 100, 800, 600);
        getContentPane().setLayout(new BorderLayout());
    }
    
    private void createTabBarPanel() {
        TabBarPanel tabBarPanel = new TabBarPanel(false);
        getContentPane().add(tabBarPanel, BorderLayout.NORTH);
    }


    /**
     * Creates the left panel of the profile page.
     */
    /**
     * Creates the left panel of the profile page.
     */
   
     
    private void createLeftPanel() {
        leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(200, getHeight()));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create the profile photo panel
        ProfilePhotoPanel profilePhotoPanel = new ProfilePhotoPanel(user, 150);
        JPanel photoPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        photoPanelWrapper.add(profilePhotoPanel);

        // Create the user information panel
        JPanel userInfoPanel = new JPanel(new GridBagLayout());
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel nicknameLabel = new JLabel(user.getNickname());
        userInfoPanel.add(nicknameLabel, gbc);

        gbc.gridy++;
        JLabel realName = new JLabel(user.getRealName()+user.getSurname());
        userInfoPanel.add(realName, gbc);

        gbc.gridy++;
        JLabel ageLabel = new JLabel(user.getAge());
        userInfoPanel.add(ageLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        IconButton uploadButton = new IconButton(new ImageIcon("resources/upload.png"), 32);
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadPhoto();
            }
        });
        userInfoPanel.add(uploadButton, gbc);

        // Add the components to the left panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftPanel.add(photoPanelWrapper, gbc);

        gbc.gridy++;
        leftPanel.add(userInfoPanel, gbc);

        getContentPane().add(leftPanel, BorderLayout.WEST);
    }


	/**
     * Creates the menu bar and adds menu items.
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();
       
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem credentialsMenuItem = new JMenuItem("account");
        credentialsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new ChangeCredentialsWindow(ProfilePage.this);
            }

        });
        optionsMenu.add(credentialsMenuItem);
        
        JMenuItem signOutMenuItem = new JMenuItem("account");
        signOutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to sign out?", "Sign Out Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    Navigator.getInstance().navigateTo(Navigator.AUTH_FRAME);
                }
            }
        });

        optionsMenu.add(signOutMenuItem);
        
        menuBar.add(optionsMenu);

        setJMenuBar(menuBar);
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

	@Override
	public void onVisibilityChanged(Photo photo) {
		// TODO Auto-generated method stub
		
	}

}
