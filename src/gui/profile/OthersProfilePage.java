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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import auth.Authentication;
import gui.IconButton;
import gui.Navigator;
import gui.TabBarPanel;
import gui.home.DiscoveryPhotoPanel;
import models.Administrator;
import models.Photo;
import models.User;
import services.Logger;

public class OthersProfilePage extends JFrame {

    private static final long serialVersionUID = 8018934881827210304L;
    private User user;
    private JPanel rightPanel;

    public OthersProfilePage(User user) {
        this.user = user;
        initialize();
        createLeftPanel();
        createRightPanel();
        createTabBarPanel(); // Create the tab bar panel
    }

    private void initialize() {
        setTitle("Profile Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        getContentPane().setLayout(new BorderLayout());
    }

    private void createTabBarPanel() {
        TabBarPanel tabBarPanel = new TabBarPanel(true);
        getContentPane().add(tabBarPanel, BorderLayout.NORTH);
    }


   
    private void createLeftPanel() {
        JPanel leftPanel = new JPanel(new GridBagLayout());
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

        JLabel nicknameLabel = new JLabel(user.getNickname() + " (" + user.getType() + ")");
        userInfoPanel.add(nicknameLabel, gbc);

        gbc.gridy++;
        JLabel emailLabel = new JLabel(user.getEmailAddress());
        userInfoPanel.add(emailLabel, gbc);

        gbc.gridy++;
        JLabel ageLabel = new JLabel(user.getAge());
        userInfoPanel.add(ageLabel, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER; // Center horizontally
        gbc.insets = new Insets(0, 0, 0, 0); // Reset insets

        // If the current user is an Admin, create a ban user button
        if (Authentication.getInstance().getCurrentUser() instanceof Administrator && !(user instanceof Administrator)) {
            gbc.gridy++;
            IconButton banButton = new IconButton(new ImageIcon("resources/ban.png"), 32);
            banButton.setToolTipText("delete account");
            banButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + user.getNickname() + "'s account?", "Confirm Account Deletion", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        try {
                            User.delete(user);
                            Logger.getInstance().logInfo(user.getNickname() + "'s account is deleted.");
                            Navigator.getInstance().navigateBack();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            Logger.getInstance().logError(e1.getMessage());
                        }
                    }
                }
            });
            userInfoPanel.add(banButton, gbc);
        }

        // Add the components to the left panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftPanel.add(photoPanelWrapper, gbc);

        gbc.gridy++;
        leftPanel.add(userInfoPanel, gbc);

        // Adjust the position of the banButton in the grid
        gbc.gridy++;
        gbc.weighty = 1.0; // Increase weight to push the button downwards
        leftPanel.add(new JPanel(), gbc); // Add an empty panel for vertical spacing

        getContentPane().add(leftPanel, BorderLayout.WEST);
    }

   
    
    
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

        for (Photo photo : user.getSharedAlbum()) {
            try {
                DiscoveryPhotoPanel panel = new DiscoveryPhotoPanel(photo);
                //panel.setGalleryActionListener(this);
                rightPanel.add(panel);
            } catch (IOException e) {
            	Logger.getInstance().logError(e.getMessage());
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


 
}
