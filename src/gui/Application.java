package gui;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import auth.Authentication;
import gui.authentication.AuthFrame;
import gui.home.DiscoveryPage;
import gui.profile.OthersProfilePage;
import gui.profile.ProfilePage;


public class Application {
	private final Dimension dimension=new Dimension(1200, 900);
	private static Application instance;
	private Application() {}
	public static synchronized Application getInstance() {
		if(instance==null) {
			instance=new Application();
		}
		return instance;
	}
	public void init() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Start the application with the login form
        		//new AuthFrame();
            	//Navigator navigator = Navigator.getInstance();
            	//new AuthFrame();
                // Register frames with the Navigator
            	Navigator navigator=Navigator.getInstance();

                // Navigate from AuthFrame to ProfilePage
                navigator.navigateTo("AuthFrame");

                // Simulate navigation between frames
           

       
            }
        });	
	}
	
	public Dimension getDimension() {
		return dimension;
	}
}
