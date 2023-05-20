package gui.home;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import auth.Authentication;
import gui.profile.ProfilePage;
import models.AppState;
import models.Photo;

/**
 * The Discovery Page class represents the user interface for the discovery feature.
 * It displays a grid of photos and allows the user to navigate to other pages.
 */
public class DiscoveryPage extends JFrame {

    private static final long serialVersionUID = 4040835824146582617L;

    private List<Photo> photos;
    private JPanel photoPanel;
    private JMenuBar menuBar;

    /**
     * Constructs a new DiscoveryPage object.
     */
    public DiscoveryPage() {
        initializeFrame();
        createMenuBar();
        createContentPanel();
    }

    /**
     * Initializes the frame properties.
     */
    private void initializeFrame() {
        photos = AppState.getInstance().getDiscoveryContent();
        setTitle("Discovery");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950, 700));
        setLocationRelativeTo(null); // center the frame
    }

    /**
     * Creates the menu bar and adds menu items.
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();

        JMenu navigationMenu = new JMenu("Navigation");
        JMenuItem profileMenuItem = new JMenuItem("Profile");
        profileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateToProfilePage();
            }
        });
        navigationMenu.add(profileMenuItem);
        menuBar.add(navigationMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Creates the main content panel with the search panel and photo grid.
     */
    private void createContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        createLeftPanel(mainPanel);
        createRightPanel(mainPanel);

        pack();
        setVisible(true);
    }

    /**
     * Creates the left search panel.
     *
     * @param mainPanel the main panel to which the search panel will be added.
     */
    private void createLeftPanel(JPanel mainPanel) {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, getHeight()));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search bar and button
        JTextField searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(150, 25));
        JButton searchButton = new JButton("Search");

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(searchBar);
        searchPanel.add(searchButton);

        // Add search panel to the left panel
        leftPanel.add(searchPanel);

        // Add left panel to the main panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
    }

    /**
     * Creates the right photo grid panel.
     *
     * @param mainPanel the main panel to which the photo grid panel will be added.
     */
    private void createRightPanel(JPanel mainPanel) {
        photoPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(photoPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        addPhotosToGrid();

        mainPanel.add(scrollPane, BorderLayout.EAST);
    }

    /**
     * Adds the photos to the photo grid panel.
     */
    private void addPhotosToGrid() {
        for (Photo photo : photos) {
            try {
                photoPanel.add(new PhotoPanel(photo));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Navigates to the profile page.
     */
    private void navigateToProfilePage() {
        new ProfilePage(Authentication.getInstance().getCurrentUser());
        dispose();
    }
}
