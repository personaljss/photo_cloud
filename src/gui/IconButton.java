package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconButton extends JButton {
    private Image icon;
    private int maxSize;

    public IconButton(ImageIcon icon, int maxSize) {
        this.icon = getScaledImage(icon.getImage(), maxSize);
        this.maxSize = maxSize;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
        setPreferredSize(new Dimension(this.icon.getWidth(null), this.icon.getHeight(null)));

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle button action
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });
    }

    private Image getScaledImage(Image image, int maxSize) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        double scaleFactor = 1.0;

        if (width > height) {
            scaleFactor = (double) maxSize / width;
            width = maxSize;
            height = (int) (height * scaleFactor);
        } else {
            scaleFactor = (double) maxSize / height;
            width = (int) (width * scaleFactor);
            height = maxSize;
        }

        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (icon != null) {
            int x = (getWidth() - icon.getWidth(null)) / 2;
            int y = (getHeight() - icon.getHeight(null)) / 2;
            g.drawImage(icon, x, y, null);
        }
    }
}
