package gui.profile;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ProfilePhotoPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private BufferedImage profileImage;
    private int size;

    public ProfilePhotoPanel(BufferedImage profileImage, int size) {
        this.profileImage = profileImage;
        this.size = size;
        setPreferredSize(new Dimension(size, size));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create a circular clip using a RoundRectangle2D shape
        RoundRectangle2D clip = new RoundRectangle2D.Float(0, 0, size, size, size, size);
        g2d.clip(clip);

        // Draw the profile image within the circular clip
        g2d.drawImage(profileImage, 0, 0, size, size, null);

        g2d.dispose();
    }
}
