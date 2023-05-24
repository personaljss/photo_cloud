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
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import gui.IconButton;
import gui.TabBarPanel;
import gui.home.DiscoveryPhotoPanel;
import models.Photo;
import models.User;

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

        JLabel nicknameLabel = new JLabel(user.getNickname());
        userInfoPanel.add(nicknameLabel, gbc);

        gbc.gridy++;
        JLabel emailLabel = new JLabel(user.getEmailAddress());
        userInfoPanel.add(emailLabel, gbc);

        gbc.gridy++;
        JLabel ageLabel = new JLabel(user.getAge());
        userInfoPanel.add(ageLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);

        // Add the components to the left panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftPanel.add(photoPanelWrapper, gbc);

        gbc.gridy++;
        leftPanel.add(userInfoPanel, gbc);

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

        for (Photo photo : user.getAlbum()) {
            try {
                DiscoveryPhotoPanel panel = new DiscoveryPhotoPanel(photo);
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


 
}
