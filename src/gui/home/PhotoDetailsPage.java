package gui.home;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import auth.Authentication;
import models.Administrator;
import models.Photo;

public class PhotoDetailsPage extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7227763757149923457L;
	private Photo photo;
    private JLabel photoLabel;
    private JMenuBar menuBar;

    public PhotoDetailsPage(Photo photo) {
        this.photo = photo;
        setTitle("Photo Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create the photo label
        photoLabel = new JLabel();
        photoLabel.setIcon(new ImageIcon(photo.getImageFile().getPath()));
        if(Authentication.getInstance().getCurrentUser() instanceof Administrator) {
            createMenuBar();	
        }

        // Add the photo label to the frame
        getContentPane().add(photoLabel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Creates the menu bar and adds menu items.
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();

        JMenu navigationMenu = new JMenu("Options");
        JMenuItem profileMenuItem = new JMenuItem("remove");
        profileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	((Administrator) Authentication.getInstance().getCurrentUser()).banPhoto(photo);
					dispose();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        navigationMenu.add(profileMenuItem);
        menuBar.add(navigationMenu);

        setJMenuBar(menuBar);
    }
}
