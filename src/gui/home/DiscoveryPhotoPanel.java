package gui.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.Photo;

/**
 * Represents a photo panel in the discovery page.
 */
public class DiscoveryPhotoPanel extends JPanel {

    private static final long serialVersionUID = 3245079321754429824L;
    private PhotoPanel photoPanel;
    private JButton likeButton;
    private JButton commentButton;
    private JButton profileButton;
    private Photo photo;

    /**
     * Constructs a new DiscoveryPhotoPanel object.
     * 
     * @param photo the photo to display in the panel
     * @throws IOException if an error occurs while reading the image
     */
    public DiscoveryPhotoPanel(Photo photo) throws IOException {
        this.photo = photo;
        setLayout(new GridBagLayout());
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
            e1.printStackTrace();
        }

        likeButton = new JButton("Like");
        commentButton = new JButton("Comment");

        profileButton = new JButton(photo.getOwner().getNickname());
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to the profile page of the owner
            }
        });
    }

    /**
     * Adds the components to the discovery photo panel.
     */
    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(profileButton);
        add(topPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        add(photoPanel, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_END;

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(commentButton);
        bottomPanel.add(likeButton);
        add(bottomPanel, gbc);
    }
}
