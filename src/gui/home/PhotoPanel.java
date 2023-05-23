package gui.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import auth.Authentication;
import gui.profile.PhotoEditingFrame;
import models.Photo;
import services.ImageMatrix;
import utils.BlurFilter;

public class PhotoPanel extends JPanel {

    private static final long serialVersionUID = 1305911466389510173L;
    private Photo photo;
    private ImageMatrix imageMatrix;
    private JLabel photoLabel;
    private JPanel infoPanel;
    private JLabel descriptionLabel;

    private static final int STANDARD_WIDTH = 300;
    private static final int STANDARD_HEIGHT = 300;
    private static final Dimension PANEL_SIZE = new Dimension(STANDARD_WIDTH, STANDARD_HEIGHT);

    public PhotoPanel(Photo photo) throws IOException {
        this.photo = photo;
        setLayout(new BorderLayout());
        setPreferredSize(PANEL_SIZE);

        BufferedImage resizedImage = photo.getResizedImage(STANDARD_WIDTH, STANDARD_HEIGHT);
        imageMatrix = new ImageMatrix(resizedImage);

        // Prepare photo label
        photoLabel = new JLabel(new ImageIcon(resizedImage), SwingConstants.CENTER);
        add(photoLabel, BorderLayout.CENTER);

        // Create info panel
        infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        infoPanel.setVisible(false);

        // Add info labels to info panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;

        descriptionLabel = new JLabel(photo.getDescription());
        descriptionLabel.setForeground(Color.WHITE);
        infoPanel.add(descriptionLabel, gbc);

        // Add info panel to photo label
        photoLabel.setLayout(new GridBagLayout());
        photoLabel.add(infoPanel, gbc);

        // Add mouse listener
        photoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Blur the image
                BufferedImage blurredImage = new BlurFilter().apply(imageMatrix,100);
                photoLabel.setIcon(new ImageIcon(blurredImage));

                // Show info panel
                infoPanel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Revert to original image
                photoLabel.setIcon(new ImageIcon(resizedImage));

                // Hide info panel
                infoPanel.setVisible(false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.print("clicked");
                if (Authentication.getInstance().getCurrentUser().equals(photo.getOwner())) {
                    new PhotoEditingFrame(photo);
                }
            }

        });

    }
    
    

    public Photo getPhoto() {
		return photo;
	}



	public void setDescription(String txt) {
        descriptionLabel.setText(txt);
        descriptionLabel.revalidate();
        descriptionLabel.repaint();
    }
/*
    public void update() {
        BufferedImage resizedImage;
		try {
			resizedImage = photo.getResizedImage(STANDARD_WIDTH, STANDARD_HEIGHT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
        imageMatrix = new ImageMatrix(resizedImage);

        // Prepare photo label
        photoLabel = new JLabel(new ImageIcon(resizedImage), SwingConstants.CENTER);
        add(photoLabel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    */
 
}
