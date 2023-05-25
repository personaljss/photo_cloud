package gui.profile;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import auth.Authentication;
import listeners.PhotoListener;
import models.FreeUser;
import models.HobbyistUser;
import models.Photo;
import models.ProfessionalUser;
import models.User;
import services.Logger;
import utils.BlurFilter;
import utils.BrightnessFilter;
import utils.ContrastFilter;
import utils.EdgeDetectionFilter;
import utils.GrayscaleFilter;
import utils.PhotoFilter;
import utils.SharpenFilter;

/**
 * The PhotoEditingFrame class represents a JFrame for editing photos.
 */
public class PhotoEditingFrame extends JFrame implements PhotoListener {
	private static final long serialVersionUID = -4462951215231814237L;
	private Photo photo;
	private JLabel photoLabel;

	/**
	 * Constructs a PhotoEditingFrame object with the given photo.
	 *
	 * @param photo the photo to edit
	 */
	public PhotoEditingFrame(Photo photo) {
		this.photo = photo;
		photo.addListener(this);
		setTitle("Photo Editing");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Create the menu bar
		JMenuBar menuBar = createMenuBar();
		setJMenuBar(menuBar);

		// Create the photo label
		photoLabel = new JLabel();
		photoLabel.setIcon(new ImageIcon(photo.getImageFile().getPath()));

		// Center the photo within the frame
		JPanel centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.CENTER;
		centerPanel.add(photoLabel, gbc);

		// Add components to the frame
		add(centerPanel);
		pack();
		//setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		updatePhoto(photo);
		setVisible(true);
	}

	/*
	/**
	 * Updates the displayed photo with the given image.
	 *
	 * @param image the updated image
	 */

	public void updatePhoto(BufferedImage image) {
		photoLabel.setIcon(new ImageIcon(image));
		photoLabel.revalidate();
		photoLabel.repaint();
	}

	/**
	 * Updates the displayed photo with the given photo object.
	 *
	 * @param photo the updated photo
	 */
	public void updatePhoto(Photo photo) {
		try {
			photoLabel.setIcon(new ImageIcon(photo.getImageMatrix().getBufferedImage()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		photoLabel.revalidate();
		photoLabel.repaint();
	}

	/**
	 * Creates the menu bar with filter and options menus.
	 *
	 * @return the created menu bar
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// Create the filter menu
		JMenu filterMenu = new JMenu("Filter");

		// Create menu items for filters
		JMenuItem blurMenuItem = new JMenuItem("Blur");
		blurMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkType(new BlurFilter());
			}
		});
		filterMenu.add(blurMenuItem);

		JMenuItem detectEdgesMenuItem = new JMenuItem("Detect Edges");
		detectEdgesMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkType(new EdgeDetectionFilter());
			}
		});
		filterMenu.add(detectEdgesMenuItem);

		JMenuItem sharpenMenuItem = new JMenuItem("Sharpen");
		sharpenMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkType(new SharpenFilter());
			}
		});
		filterMenu.add(sharpenMenuItem);

		JMenuItem brightnessMenuItem = new JMenuItem("Brightness");
		brightnessMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkType(new BrightnessFilter());
			}
		});
		filterMenu.add(brightnessMenuItem);

		JMenuItem grayScaleMenuItem = new JMenuItem("Gray scale");
		grayScaleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkType(new GrayscaleFilter());
			}
		});
		filterMenu.add(grayScaleMenuItem);

		JMenuItem contrastMenuItem = new JMenuItem("Contrast");
		contrastMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkType(new ContrastFilter());
			}
		});
		filterMenu.add(contrastMenuItem);

		// Add the filter menu to the menu bar
		menuBar.add(filterMenu);

		// Create the options menu
		JMenu optionsMenu = new JMenu("Options");


		JMenuItem descriptionMenuItem = new JMenuItem("Add description");
		descriptionMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField textField = new JTextField(20);
				Object[] message = { "Enter the description:", textField };

				int option = JOptionPane.showOptionDialog(null, message, "Enter description",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

				if (option == JOptionPane.OK_OPTION) {
					String txt = textField.getText();
					try {
						photo.setDescription(txt);
					} catch (IOException e1) {
		            	Logger.getInstance().logError(e1.getMessage());
						e1.printStackTrace();
					}
					System.out.println("Entered: " + txt);
				}
			}
		});
		optionsMenu.add(descriptionMenuItem);

		// Add the options menu to the menu bar
		menuBar.add(optionsMenu);

		return menuBar;
	}

	/**
	 * Checks the user type and opens the filter dialog for the selected filter if
	 * applicable.
	 *
	 * @param filter the selected filter
	 */
	private void checkType(PhotoFilter filter) {
		User currentUser = Authentication.getInstance().getCurrentUser();

		if (currentUser instanceof FreeUser) {
			if (filter instanceof BlurFilter || filter instanceof SharpenFilter) {
				new FilterDialog(PhotoEditingFrame.this, photo, filter);
			} else {
				JOptionPane.showMessageDialog(this, "Free tier users can only apply blur and sharpen filters.",
						"Warning", JOptionPane.WARNING_MESSAGE);
			}
		} else if (currentUser instanceof HobbyistUser) {
			if (filter instanceof BlurFilter || filter instanceof SharpenFilter || filter instanceof BrightnessFilter) {
				new FilterDialog(PhotoEditingFrame.this, photo, filter);
			} else {
				JOptionPane.showMessageDialog(this,
						"Hobbyist tier users can apply blur, sharpen, brightness, and contrast filters.", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} else if (currentUser instanceof ProfessionalUser) {
			new FilterDialog(PhotoEditingFrame.this, photo, filter);
		}
	}

	@Override
	public void dispose() {
		photo.removeListener(this);
		super.dispose();
	}

	@Override
	public void onDeleted(Photo photo) {
		dispose();
	}

	@Override
	public void onFilterApplied(Photo photo) {
		photoLabel = new JLabel();
		photoLabel.setIcon(new ImageIcon(photo.getImageFile().getPath()));
		revalidate();
		repaint();
	}

	@Override
	public void onDescriptionChanged(Photo photo) {
		// TODO: Implement method
	}

	@Override
	public void onCommentAdded(Photo photo) {
		// TODO: Implement method
	}

	@Override
	public void onVisibilityChanged(Photo photo) {
		// TODO: Implement method
	}
}
