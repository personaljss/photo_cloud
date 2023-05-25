package gui.profile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import auth.Authentication;
import gui.home.PhotoPanel;
import models.Photo;
import models.User;
import services.Logger;

public class GalleryPhotoPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6352284272654626082L;
	private PhotoPanel photoPanel;
    private JButton deleteButton;
    private JButton shareButton;
    private Photo photo;

    public GalleryPhotoPanel(Photo photo) throws IOException {
        this.photo = photo;
        setPreferredSize(new Dimension(300, 300));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        createComponents();
        addComponents();
    }

    /**
     * Creates the components for the gallery photo panel.
     */
    private void createComponents() {
        try {
            photoPanel = new PhotoPanel(photo,true);
        } catch (IOException e1) {
        	Logger.getInstance().logError(e1.getMessage());
            e1.printStackTrace();
        }

        deleteButton = new JButton("Delete");
        deleteButton.setIcon(new ImageIcon("resources/delete.png"));
        if (photo.isPublic()) {
            shareButton = new JButton("Make private");
            shareButton.setIcon(new ImageIcon("resources/invisible.png"));
        } else {
            shareButton = new JButton("Share");
            shareButton.setIcon(new ImageIcon("resources/share.png"));
        }

        // Set button properties
        deleteButton.setOpaque(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setBorderPainted(false);

        shareButton.setOpaque(false);
        shareButton.setContentAreaFilled(false);
        shareButton.setBorderPainted(false);
    }

    /**
     * Adds the components to the gallery photo panel.
     */
    private void addComponents() {
        // Add delete button to the top
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);
        topPanel.add(deleteButton);

        // Add share button to the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(shareButton);

        // Add the components to the gallery photo panel
        add(topPanel, BorderLayout.NORTH);
        add(photoPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        addOnClickListeners();
    }

    /**
     * Adds click listeners to the buttons.
     */
    private void addOnClickListeners() {
        shareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (photo.isPublic()) {
                    try {
                        photo.setPublic(false);
                        shareButton.setText("Share");
                        shareButton.setIcon(new ImageIcon("resources/share.png"));
                    } catch (Exception e1) {
                    	Logger.getInstance().logError(e1.getMessage());
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        photo.setPublic(true);
                        shareButton.setText("Make private");
                        shareButton.setIcon(new ImageIcon("resources/invisible.png"));
                    } catch (Exception e1) {
                    	Logger.getInstance().logError(e1.getMessage());
                        e1.printStackTrace();
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = false;
                User currentUser = Authentication.getInstance().getCurrentUser();
                try {
                    currentUser.removePhoto(photo);
                    success = true;
                } catch (Exception e1) {
                	Logger.getInstance().logError(e1.getMessage());
                    e1.printStackTrace();
                }

                if (success) {
                    // Handle successful delete
                } else {
                    // Handle delete failure
                }
            }
        });
    }
}
