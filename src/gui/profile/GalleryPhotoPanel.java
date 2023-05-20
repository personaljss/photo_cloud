package gui.profile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import gui.home.PhotoPanel;
import listeners.GalleryActionListener;
import models.Photo;

public class GalleryPhotoPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -51937104700847247L;
	private PhotoPanel photoPanel;
	private JButton deleteButton;
	private JButton shareButton;
	private Photo photo;
	private GalleryActionListener galleryActionListener;

	/**
	 * Constructs a new DiscoveryPhotoPanel object.
	 * 
	 * @param photo the photo to display in the panel
	 * @throws IOException if an error occurs while reading the image
	 */
	public GalleryPhotoPanel(Photo photo) throws IOException {
		this.photo = photo;
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		createComponents();
		addComponents();
	}

	/**
	 * Creates the components for the discovery photo panel.
	 */
	private void createComponents() {
		try {
			photoPanel = new PhotoPanel(photo);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		deleteButton = new JButton("Delete");
		if (photo.isPublic()) {
			shareButton = new JButton("Make private");
		} else {
			shareButton = new JButton("Share");
		}

	}

	/**
	 * Adds the components to the discovery photo panel.
	 * 
	 * @param photo the photo to display
	 */
	private void addComponents() {
		// Add profile button to the top
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.setOpaque(false);

		// Add buttons to the bottom
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.setOpaque(false);
		bottomPanel.add(shareButton);
		bottomPanel.add(deleteButton);

		// Add the components to the photo panel
		add(topPanel, BorderLayout.NORTH);
		add(photoPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		addOnClickListeners();
	}

	private void addOnClickListeners() {
		shareButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (photo.isPublic()) {
					try {
						photo.setPublic(false);
						shareButton.setText("Share");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					try {
						photo.setPublic(true);
						shareButton.setText("Make private");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}

		});

		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				boolean success = false;
				try {
					photo.delete();
					success = true;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (success) {
					galleryActionListener.delete(photo);
				}
			}

		});

	}

	public void setGalleryActionListener(GalleryActionListener listener) {
		this.galleryActionListener = listener;
	}

	/**
	 * Sets the owner nickname for the photo.
	 * 
	 * @param nickname the owner nickname
	 */

}
