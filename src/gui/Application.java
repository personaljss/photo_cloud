package gui;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

public class Application {
	private final Dimension dimension = new Dimension(1200, 900);
	private static Application instance;

	private Application() {
	}

	public static synchronized Application getInstance() {
		if (instance == null) {
			instance = new Application();
		}
		return instance;
	}

	public void init() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Navigator navigator = Navigator.getInstance();

				navigator.navigateTo("AuthFrame");

			}
		});
	}

	public Dimension getDimension() {
		return dimension;
	}
}
