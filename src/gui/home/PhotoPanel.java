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
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.profile.PhotoEditingFrame;
import models.Photo;
import services.ImageMatrix;
import utils.BlurFilter;
import utils.PhotoFilter;

/**
 * The PhotoPanel class represents a panel that displays a photo along with its information.
 * It provides interaction functionalities such as mouse hover effects and click actions.
 */
public class PhotoPanel extends JPanel {

    private static final long serialVersionUID = 1305911466389510173L;
    private Photo photo;
    private ImageMatrix imageMatrix;
    private JLabel photoLabel;
    private JPanel infoPanel;
    private JLabel descriptionLabel;
    private JLabel filtersLabel;

    private static final int STANDARD_WIDTH = 300;
    private static final int STANDARD_HEIGHT = 300;
    private static final Dimension PANEL_SIZE = new Dimension(STANDARD_WIDTH, STANDARD_HEIGHT);

    /**
     * Constructs a PhotoPanel object with the specified photo and self identification.
     *
     * @param photo   the photo to display
     * @param isSelf  flag indicating if the photo belongs to the current user
     * @throws IOException if an I/O error occurs while loading the photo
     */
    public PhotoPanel(Photo photo, boolean isSelf) throws IOException {
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

        filtersLabel = new JLabel(getAppliedFiltersText(photo.getAppliedFilters()));
        filtersLabel.setForeground(Color.WHITE);
        infoPanel.add(filtersLabel, gbc);

        // Add info panel to photo label
        photoLabel.setLayout(new GridBagLayout());
        photoLabel.add(infoPanel, gbc);

        // Add mouse listener
        photoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Blur the image
                BufferedImage blurredImage = new BlurFilter().apply(imageMatrix, 100);
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
                if (isSelf) {
                    new PhotoEditingFrame(photo);
                } else {
                    new PhotoDetailsPage(photo);
                }
            }

        });
    }

    /**
     * Retrieves the photo associated with this panel.
     *
     * @return the photo object
     */
    public Photo getPhoto() {
        return photo;
    }

    /**
     * Generates the text representation of the applied filters for the photo.
     *
     * @param appliedFilters the list of applied filters
     * @return the text representation of the applied filters
     */
    private String getAppliedFiltersText(List<PhotoFilter> appliedFilters) {
        StringBuilder sb = new StringBuilder("Applied Filters: ");
        if (appliedFilters.size() == 0) {
            sb.append("none");
            return sb.toString();
        }
        for (PhotoFilter filter : appliedFilters) {
            sb.append(filter.toString()).append(", ");
        }
        // Remove the trailing comma and space
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

}
