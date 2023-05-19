package gui;

import javax.swing.SwingUtilities;
import gui.authentication.AuthFrame;


public class Application {
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
        		new AuthFrame();
            }
        });	
	}
}
