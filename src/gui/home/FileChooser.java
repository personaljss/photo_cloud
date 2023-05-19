package gui.home;

import javax.swing.JFileChooser;

public class FileChooser extends JFileChooser {
    /**
	 * 
	 */
	private static final long serialVersionUID = -844763092384722761L;

	public FileChooser() {
        // Here you can add any custom configuration for the JFileChooser
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setAcceptAllFileFilterUsed(false);
    }
}