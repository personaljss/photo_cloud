package gui.profile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import auth.Authentication;
import gui.home.FileChooser;
import models.User;
import services.ImageMatrix;
import services.Logger;
import utils.BlurFilter;

public class ProfilePhotoPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage profileImage;
	private BufferedImage originalImage;
	private int size;
	private boolean isHovered;
	private User user;
	private boolean isSelf;

	private static final int TEXT_SIZE = 14;
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final String CHANGE_TEXT = "Change";

	public ProfilePhotoPanel(User user, int size) {
		this.isSelf=user.equals(Authentication.getInstance().getCurrentUser());
		this.user = user;
		try {
			this.profileImage = loadProfileImage();
			originalImage = profileImage;
		} catch (IOException e1) {
        	Logger.getInstance().logError(e1.getMessage());
			e1.printStackTrace();
		}
		this.originalImage = profileImage;
		this.size = size;
		setPreferredSize(new Dimension(size, size));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
		setOpaque(false); // Make the panel transparent

		if (isSelf) {
			// Add mouse listener
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					isHovered = true;
					ProfilePhotoPanel.this.profileImage = applyBlurEffect(originalImage);
					repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					isHovered = false;
					ProfilePhotoPanel.this.profileImage = originalImage;
					repaint();
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					uploadPhoto();
				}
			});
		}
	}

	private BufferedImage loadProfileImage() throws IOException {
		return ImageIO.read(user.getProfilePhoto());
	}

	private BufferedImage applyBlurEffect(BufferedImage image) {
		ImageMatrix imageMatrix = new ImageMatrix(image);
		return new BlurFilter().apply(imageMatrix, 100);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Create a circular clip shape
		Ellipse2D clip = new Ellipse2D.Float(0, 0, size - 1, size - 1); // Subtract 1 from size for thicker border
		g2d.setClip(clip);

		// Draw the profile image within the clipped area
		g2d.drawImage(profileImage, 0, 0, size, size, null);

		// Draw border
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(6)); // Set border thickness
		g2d.drawOval(0, 0, size - 1, size - 1); // Subtract 1 from size for thicker border

		// Draw hover effect if hovered
		if (isHovered) {
			g2d.setColor(new Color(0, 0, 0, 0.5f));
			g2d.fillOval(0, 0, size - 1, size - 1); // Subtract 1 from size for thicker border

			// Draw the "Change" text on the blurred image
			g2d.setColor(TEXT_COLOR);
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, TEXT_SIZE));
			int textWidth = g2d.getFontMetrics().stringWidth(CHANGE_TEXT);
			int textX = (size - textWidth) / 2;
			int textY = size / 2;
			g2d.drawString(CHANGE_TEXT, textX, textY);
		}

		g2d.dispose();
	}

	private void uploadPhoto() {
		FileChooser fileChooser = new FileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			try {
				user.updateProfilePhoto(fileChooser.getSelectedFile());
				profileImage = loadProfileImage();
				originalImage = profileImage;
				revalidate();
				repaint();
			} catch (IOException e) {
            	Logger.getInstance().logError(e.getMessage());
				JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			} catch (Exception e) {
            	Logger.getInstance().logError(e.getMessage());
				JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			}
		}
	}
}
