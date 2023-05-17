package gui;

import javax.swing.SwingUtilities;
import gui.authentication.AuthFrame;


public class Application {
	public void init() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Start the application with the login form
        		new AuthFrame();
            }
        });	
	}
}
