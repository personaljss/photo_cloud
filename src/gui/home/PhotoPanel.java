package gui.home;

import services.ImageMatrix;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class PhotoPanel extends JPanel {

    private BufferedImage originalImage;
    private ImageMatrix imageMatrix;
    private JLabel photoLabel;
    private JPanel infoPanel;

    public PhotoPanel() throws IOException {
        setLayout(new BorderLayout());

        // Assuming you have a default image path
        String imagePath = "resources/likeIcon.png";
        originalImage = ImageIO.read(new File(imagePath));
        imageMatrix = new ImageMatrix(originalImage);

        // Prepare photo label
        photoLabel = new JLabel(new ImageIcon(originalImage), SwingConstants.CENTER);
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

        JLabel nicknameLabel = new JLabel("Nickname: " + "User Nickname", SwingConstants.CENTER);
        nicknameLabel.setForeground(Color.WHITE);
        infoPanel.add(nicknameLabel, gbc);

        JLabel likesLabel = new JLabel("Likes: " + "Number of likes", SwingConstants.CENTER);
        likesLabel.setForeground(Color.WHITE);
        infoPanel.add(likesLabel, gbc);

        JLabel commentsLabel = new JLabel("Comments: " + "Number of comments", SwingConstants.CENTER);
        commentsLabel.setForeground(Color.WHITE);
        infoPanel.add(commentsLabel, gbc);

        // Add info panel to photo label
        photoLabel.setLayout(new GridBagLayout());
        photoLabel.add(infoPanel, gbc);

        // Add mouse listener
        photoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Blur the image
                BufferedImage blurredImage = blurImage();
                photoLabel.setIcon(new ImageIcon(blurredImage));

                // Show info panel
                infoPanel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Revert to original image
                photoLabel.setIcon(new ImageIcon(originalImage));

                // Hide info panel
                infoPanel.setVisible(false);
            }
        });
    }
    private BufferedImage blurImage() {
        // Use a 5x5 Gaussian blur kernel for more blur
        int[][] kernel = {
                {1, 4, 6, 4, 1},
                {4, 16, 24, 16, 4},
                {6, 24, 36, 24, 6},
                {4, 16, 24, 16, 4},
                {1, 4, 6, 4, 1}
        };
        int kernelSum = 256;  // Sum of all elements in the kernel

        ImageMatrix blurredMatrix = new ImageMatrix(imageMatrix.getWidth(), imageMatrix.getHeight());

        // Apply the kernel to each pixel in the image
        for (int i = 2; i < imageMatrix.getWidth() - 2; i++) {
            for (int j = 2; j < imageMatrix.getHeight() - 2; j++) {
                int red = 0, green = 0, blue = 0;

                for (int ki = -2; ki <= 2; ki++) {
                    for (int kj = -2; kj <= 2; kj++) {
                        red += kernel[ki + 2][kj + 2] * imageMatrix.getRed(i + ki, j + kj);
                        green += kernel[ki + 2][kj + 2] * imageMatrix.getGreen(i + ki, j + kj);
                        blue += kernel[ki + 2][kj + 2] * imageMatrix.getBlue(i + ki, j + kj);
                    }
                }

                red /= kernelSum;
                green /= kernelSum;
                blue /= kernelSum;

                blurredMatrix.setRGB(i, j, ImageMatrix.convertRGB(red, green, blue));
            }
        }

        return blurredMatrix.getBufferedImage();
    }
}
