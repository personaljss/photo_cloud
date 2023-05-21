package gui.profile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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

import auth.Authentication;
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
        setMinimumSize(new Dimension(950, 700));
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
		try {
			profilePhotoPanel = new ProfilePhotoPanel(resizeImage(loadProfileImage(), 150, 150), 150);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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


	private BufferedImage loadProfileImage() throws IOException {
		// TODO Auto-generated method stub
		return ImageIO.read(user.getProfilePhoto());
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

	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
	    BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2d = resizedImage.createGraphics();
	    g2d.drawImage(originalImage, 0, 0, width, height, null);
	    g2d.dispose();
	    return resizedImage;
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


}
