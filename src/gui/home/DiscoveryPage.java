package gui.home;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

public class DiscoveryPage extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4040835824146582617L;
	
	private List<Photo> photos;
	private JPanel photoPanel;
	private JMenuBar menuBar;
	private JMenu navigationMenu;

	public DiscoveryPage() {
		photos=AppState.getInstance().getDiscoveryContent();
        setTitle("Discovery");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950, 700));
        setLocationRelativeTo(null);  // center the frame
        
        //creating menu
        menuBar=new JMenuBar();
        navigationMenu=new JMenu("navigation");
        JMenuItem navigationItem=new JMenuItem("profile");
        navigationMenu.add(navigationItem);
        navigationItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("menu clicked");
				new ProfilePage(Authentication.getInstance().getCurrentUser());
				DiscoveryPage.this.dispose();
			}
        	
        });
        menuBar.add(navigationMenu);
        setJMenuBar(menuBar);

        // Main panel with BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        setContentPane(mainPanel);

        // Top panel with the title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Discovery");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, titleLabel.getPreferredSize().height));

        // Search panel with search bar and button
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchBar = new JTextField(20);  // adjust size as needed
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchBar);
        searchPanel.add(searchButton);

        // Photo panel with grid of PhotoPanels, using FlowLayout for equal-sized cells
        photoPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        addPhotos();

        // Scroll pane to allow scrolling, if necessary
        JScrollPane scrollPane = new JScrollPane(photoPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Add panels to main panel
        mainPanel.add(titlePanel);
        mainPanel.add(searchPanel);
        mainPanel.add(scrollPane);

        pack();
        setVisible(true);
    }

    private void addPhotos() {
        for (Photo photo: photos) {
            try {
				photoPanel.add(new PhotoPanel(photo));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}