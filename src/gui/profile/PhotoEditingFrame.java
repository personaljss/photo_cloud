package gui.profile;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;

import models.Photo;

public class PhotoEditingFrame extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4462951215231814237L;
	private Photo photo;
    private JLabel photoLabel;

    public PhotoEditingFrame(Photo photo) {
        this.photo = photo;

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

    /**
     * Sets the photo to be displayed in the frame.
     *
     * @param photo the photo to set
     */
    public void setPhoto(Photo photo) {
        this.photo = photo;
        photoLabel.setIcon(new ImageIcon(photo.getImageFile().getPath()));
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
            }
        });
        filterMenu.add(blurMenuItem);

        JMenuItem detectEdgesMenuItem = new JMenuItem("Detect Edges");
        detectEdgesMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Apply detect edges filter to the photo
            }
        });
        filterMenu.add(detectEdgesMenuItem);

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
                    "Enter your name:", textField
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
        // Enable the parent frame when this frame is disposed
        //parentFrame.setEnabled(true);
        super.dispose();
    }
}
