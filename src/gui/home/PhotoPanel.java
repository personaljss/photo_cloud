package gui.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import auth.Authentication;
import gui.profile.PhotoEditingFrame;
import models.Photo;
import services.ImageMatrix;

public class PhotoPanel extends JPanel implements ActionListener{

    /**
     * 
     */
    private static final long serialVersionUID = 1305911466389510173L;
    private Photo photo;
    private BufferedImage originalImage;
    private ImageMatrix imageMatrix;
    private JLabel photoLabel;
    private JPanel infoPanel;

    private static final int STANDARD_WIDTH = 300;
    private static final int STANDARD_HEIGHT = 200;

    public PhotoPanel(Photo photo) throws IOException {
        this.photo = photo;
        setLayout(new BorderLayout());

        // Assuming you have a default image path
        originalImage = ImageIO.read(photo.getImageFile());
        BufferedImage resizedImage = resizeImage(originalImage, STANDARD_WIDTH, STANDARD_HEIGHT);
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

        JLabel nicknameLabel = new JLabel("Nickname: " + photo.getOwner().getNickname(), SwingConstants.CENTER);
        nicknameLabel.setForeground(Color.WHITE);
        infoPanel.add(nicknameLabel, gbc);

        JLabel likesLabel = new JLabel("Likes: " + photo.getLikes().size(), SwingConstants.CENTER);
        likesLabel.setForeground(Color.WHITE);
        infoPanel.add(likesLabel, gbc);

        JLabel commentsLabel = new JLabel("Comments: " + photo.getComments().size(), SwingConstants.CENTER);
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
                photoLabel.setIcon(new ImageIcon(resizedImage));

                // Hide info panel
                infoPanel.setVisible(false);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
        		System.out.print("clicked");
            	if(Authentication.getInstance().getCurrentUser().equals(photo.getOwner())) {
            		new PhotoEditingFrame(photo);
            	}
            }
            
        });
        
    }

    private BufferedImage blurImage() {
        // Use a 5x5 Gaussian blur kernel for more blur
        int[][] kernel = { { 1, 4, 6, 4, 1 }, { 4, 16, 24, 16, 4 }, { 6, 24, 36, 24, 6 }, { 4, 16, 24, 16, 4 },
                { 1, 4, 6, 4, 1 } };
        int kernelSum = 256; // Sum of all elements in the kernel

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

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(resizedImage, 0, 0, null);
        graphics.dispose();
        return bufferedImage;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
