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
import listeners.PhotoListener;
import models.Photo;
import utils.BlurFilter;
import utils.BrightnessFilter;
import utils.EdgeDetectionFilter;
import utils.GrayscaleFilter;
import utils.SharpenFilter;


public class PhotoEditingFrame extends JFrame implements PhotoListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = -4462951215231814237L;
	private Photo photo;
    private JLabel photoLabel;


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
        //setLocationRelativeTo(parentFrame);
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setVisible(true);

        // Disable the parent frame
        //parentFrame.setEnabled(false);
    }


    
    public void updatePhoto(BufferedImage image) {
    	photoLabel.setIcon(new ImageIcon(image));
        photoLabel.revalidate();
        photoLabel.repaint();
    }
    
    public void updatePhoto(Photo photo) {
    	photoLabel.setIcon(new ImageIcon(photo.getImageFile().getPath()));
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
                // Apply blur filter to the photo
            	new FilterDialog(PhotoEditingFrame.this,photo, new BlurFilter());
            }
        });
        filterMenu.add(blurMenuItem);

        JMenuItem detectEdgesMenuItem = new JMenuItem("Detect Edges");
        detectEdgesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Apply detect edges filter to the photo
            	new FilterDialog(PhotoEditingFrame.this,photo, new EdgeDetectionFilter());
            }
        });
        filterMenu.add(detectEdgesMenuItem);

        JMenuItem sharpenMenuItem = new JMenuItem("Sharpen");
        sharpenMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Apply detect edges filter to the photo
            	new FilterDialog(PhotoEditingFrame.this,photo, new SharpenFilter());
            }
        });
        filterMenu.add(sharpenMenuItem);
        
        JMenuItem brightnessMenuItem = new JMenuItem("Brightness");
        brightnessMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Apply detect edges filter to the photo
            	new FilterDialog(PhotoEditingFrame.this,photo, new BrightnessFilter());
            }
        });
        filterMenu.add(brightnessMenuItem);
        
        JMenuItem grayScaleMenuItem = new JMenuItem("GrayScale");
        grayScaleMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Apply detect edges filter to the photo
            	new FilterDialog(PhotoEditingFrame.this,photo, new GrayscaleFilter());
            }
        });
        filterMenu.add(grayScaleMenuItem);
        
        // Add the filter menu to the menu bar
        menuBar.add(filterMenu);

        // Create the options menu
        JMenu optionsMenu = new JMenu("Options");
        
        
        JMenuItem shareMenuItem = new JMenuItem("Share");
        shareMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform rotation on the photo
            	try {
					photo.setPublic(true);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        optionsMenu.add(shareMenuItem);
        
        //add a description
        JMenuItem descriptionMenuItem = new JMenuItem("Add description");
        descriptionMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField textField = new JTextField(20); // Create a text field
                
                // Create a panel with the text field
                Object[] message = {
                    "Enter the description:", textField
                };
                
                int option = JOptionPane.showOptionDialog(null, message, "Enter description", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                
                if (option == JOptionPane.OK_OPTION) {
                    String txt = textField.getText(); // Retrieve the text from the text field
                    try {
						photo.setDescription(txt);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    System.out.println("Entered : " + txt);
                } 
            }
        });
        optionsMenu.add(descriptionMenuItem);

        // Add the options menu to the menu bar
        menuBar.add(optionsMenu);

        return menuBar;
    }

    @Override 
    public void dispose() {
    	photo.removeListener(this);
    	super.dispose();
    }

	@Override
	public void onDeleted(Photo photo) {
		// TODO Auto-generated method stub
		dispose();
	}



	@Override
	public void onFilterApplied(Photo photo) {
		// TODO Auto-generated method stub
        photoLabel = new JLabel();
        photoLabel.setIcon(new ImageIcon(photo.getImageFile().getPath()));
        photoLabel.revalidate();
        photoLabel.repaint();
	}



	@Override
	public void onDescriptionChanged(Photo photo) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onCommentAdded(Photo photo) {
		// TODO Auto-generated method stub
		
	}
    
}
