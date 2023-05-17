package gui.home;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import auth.Authentication;
import models.Photo;
import models.User;

public class HomeFrame extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2076299585750150639L;
	private JButton addPhotoButton;
    private JPanel gridPanel;
    private User user;

    public HomeFrame() {
    	user=Authentication.getInstance().getCurrentUser();
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        addPhotoButton = new JButton("Add Photo");
        addPhotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Path sourcePath = selectedFile.toPath();

                    // Create target path
                    Path targetDirectory = Paths.get("data/" + user.getNickname() + "/images/");
                    if (!Files.exists(targetDirectory)) {
                        try {
                            Files.createDirectories(targetDirectory); // Create directories if they don't exist
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    Path targetPath = targetDirectory.resolve(selectedFile.getName());

                    try {
                        // Copy file
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }

                    // Add it to the grid
                    addPhotoToGrid(selectedFile);
                }

            }
        });

        gridPanel = new JPanel(new GridLayout(0, 3)); // for example, 3 photos per row

        add(addPhotoButton, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        setSize(800, 600);
        setVisible(true);
        setPhotosToGrid();
    }

    private void addPhotoToGrid(File photoFile) {
        // Here you can add the photo to the grid. For example, you could create a JLabel with an ImageIcon:
        ImageIcon imageIcon = new ImageIcon(photoFile.getPath());
        JLabel photoLabel = new JLabel(imageIcon);
        gridPanel.add(photoLabel);
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void setPhotosToGrid() {
    	for(Photo photo : user.getAlbum()) {
            ImageIcon imageIcon = new ImageIcon(photo.getSource());
            JLabel photoLabel = new JLabel(imageIcon);
            gridPanel.add(photoLabel);
    	}
        gridPanel.revalidate();
        gridPanel.repaint();
    }
}

