package gui.home;

import java.awt.CardLayout;

import javax.swing.JFrame;

import auth.Authentication;
import gui.profile.ProfilePage;
import models.Photo;
import models.User;

public class MainFrame extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3948604611347566603L;
	private CardLayout cardLayout;
    private DiscoveryPage discoveryPage;
    private ProfilePage currentUserProfilePage;
    private ProfilePage visitedUserProfilePage;
    private PhotoDetailsPage photoDetailsPage;

    public MainFrame() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Instantiate pages
        discoveryPage = new DiscoveryPage();
        currentUserProfilePage = new ProfilePage(Authentication.getInstance().getCurrentUser());
        photoDetailsPage = new PhotoDetailsPage();

        // Add pages to card layout
        add(discoveryPage, "DISCOVERY");
        add(currentUserProfilePage, "PROFILE");

        // Switch to the profile page initially
        cardLayout.show(this.getContentPane(), "PROFILE");
        setVisible(true);
    }


    // Navigation methods
    public void switchToProfilePage() {
        cardLayout.show(this.getContentPane(), "PROFILE");
    }

    public void switchToDiscoveryPage() {
        cardLayout.show(this.getContentPane(), "DISCOVERY");
    }

    public void openProfile(User user) {
        if(visitedUserProfilePage != null) {
            remove(visitedUserProfilePage);
        }
        visitedUserProfilePage = new ProfilePage(user);
        add(visitedUserProfilePage, "VISITED_USER_PROFILE");
        cardLayout.show(this.getContentPane(), "VISITED_USER_PROFILE");
    }

    public void openPhoto(Photo photo) {
        //photoDetailsPage.updatePhoto(photo);
        cardLayout.show(this.getContentPane(), "PHOTO_DETAILS");
    }
}
