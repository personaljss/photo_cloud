package gui.home;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import auth.Authentication;
import exceptions.UserAlreadyLikedPhotoException;
import exceptions.UserDidNotLikePhotoException;
import gui.Navigator;
import listeners.PhotoListener;
import models.Photo;
import models.User;

/**
 * Represents a photo panel in the discovery page.
 */
public class DiscoveryPhotoPanel extends JPanel implements PhotoListener{

    private static final long serialVersionUID = 3245079321754429824L;
    private PhotoPanel photoPanel;
    private JButton likeButton;
    private JButton commentButton;
    private JButton profileButton;
    private Photo photo;
    private User user;

    /**
     * Constructs a new DiscoveryPhotoPanel object.
     *
     * @param photo the photo to display in the panel
     * @throws IOException if an error occurs while reading the image
     */
    public DiscoveryPhotoPanel(Photo photo) throws IOException {
        this.photo = photo;
        photo.addListener(this);
        this.user = Authentication.getInstance().getCurrentUser();
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
            photoPanel = new PhotoPanel(photo,false);
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        likeButton = new JButton();
        updateLikeButtonIcon(); // Set the initial icon based on the user's like status
        likeButton.setText(Integer.toString(photo.getLikes().size()));
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!photo.getLikes().contains(user)) {
                    try {
                        photo.like(user);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (UserAlreadyLikedPhotoException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        photo.disLike(user);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (UserDidNotLikePhotoException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                likeButton.setText(Integer.toString(photo.getLikes().size()));
                updateLikeButtonIcon(); // Update the icon based on the user's like status
                likeButton.revalidate();
                likeButton.repaint();
            }
        });
        commentButton = new JButton();
        commentButton.setIcon(new ImageIcon("resources/comment.png"));
        commentButton.setText(Integer.toString(photo.getComments().size()));
        commentButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new CommentWindow(photo);
            }

        });

        profileButton = new JButton(photo.getOwner().getNickname());
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate to the profile page of the owner
                Navigator.getInstance().navigateTo(Navigator.OTHERS_PROFILE_PAGE,photo.getOwner());
            }
        });

        // Set button properties
        likeButton.setOpaque(false);
        likeButton.setContentAreaFilled(false);
        likeButton.setBorderPainted(false);

        commentButton.setOpaque(false);
        commentButton.setContentAreaFilled(false);
        commentButton.setBorderPainted(false);

        profileButton.setOpaque(false);
        profileButton.setContentAreaFilled(false);
        profileButton.setBorderPainted(false);
    }

    private void updateLikeButtonIcon() {
        if (photo.getLikes().contains(user)) {
            likeButton.setIcon(new ImageIcon("resources/likedIcon.png"));
        } else {
            likeButton.setIcon(new ImageIcon("resources/dislikedIcon.png"));
        }
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

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setOpaque(false);
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.insets = new Insets(5, 5, 5, 5);
        buttonConstraints.anchor = GridBagConstraints.LINE_START;
        bottomPanel.add(commentButton, buttonConstraints);

        buttonConstraints.anchor = GridBagConstraints.LINE_END;
        bottomPanel.add(likeButton, buttonConstraints);

        add(bottomPanel, gbc);
    }
    
    @Override
    public void removeNotify() {
        // Remove the listener when the panel is no longer visible
        photo.removeListener(this);

        super.removeNotify();
    }

	@Override
	public void onDeleted(Photo photo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFilterApplied(Photo photo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDescriptionChanged(Photo photo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommentAdded(Photo photo) {
		// TODO Auto-generated method stub
        commentButton.setText(Integer.toString(photo.getComments().size()));
        revalidate();
        repaint();
	}

	@Override
	public void onVisibilityChanged(Photo photo) {
		// TODO Auto-generated method stub
	}
}
