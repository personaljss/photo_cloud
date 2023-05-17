package gui.home;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
                    String fullImageName = selectedFile.getName();
                    String extension=fullImageName.substring(fullImageName.lastIndexOf('.'),fullImageName.length());
                    
                    // Create target path
                    String imageName=Photo.generateName(user,"");
                    //String imageFullName=Photo.generateName(user,extension);
                    Path targetDirectory = Paths.get("data/" + user.getNickname() + "/images/" + imageName);
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
                        Path newFilePath = Paths.get(targetPath.getParent().toString(), imageName+extension);
                        Files.move(targetPath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
                	    File file = new File("data/" + user.getNickname() + "/images/" + imageName + "/" + Photo.dataFile);
                	    if(!file.exists()) {
                    	    file.createNewFile();	
                	    }
                	  
                        try {
                            //create the Photo object if photo file is copied and updated photolist is saved successfully
                            Photo photo = new Photo(user,imageName,extension);
                            
                            FileOutputStream fileOut = new FileOutputStream(photo.getDataFile());
                            ObjectOutputStream oos = new ObjectOutputStream(fileOut);
                            oos.writeObject(photo);
                            user.addPhoto(photo);
                            addPhotoToGrid(photo);
                            fileOut.close();
                            oos.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    } catch (IOException e1) {
                        System.out.println(e1.toString());
                    }

                }

            }
        });


        gridPanel = new JPanel(new GridLayout(0, 3)); // for example, 3 photos per row

        add(addPhotoButton, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        setSize(800, 600);
        setPhotosToGrid();
        setVisible(true);
    }

    private void addPhotoToGrid(Photo photo) {
        // Here you can add the photo to the grid. For example, you could create a JLabel with an ImageIcon:
        ImageIcon imageIcon = new ImageIcon(photo.getImageFile().getPath());
        //
        System.out.println("add photo:"+photo.getImageFile());
        JLabel photoLabel = new JLabel(imageIcon);
        gridPanel.add(photoLabel);
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void setPhotosToGrid() {
    	for(Photo photo : user.getAlbum()) {
            ImageIcon imageIcon = new ImageIcon(photo.getImageFile().getPath());
            System.out.println(photo.getImageFile().getPath());
            JLabel photoLabel = new JLabel(imageIcon);
            gridPanel.add(photoLabel);
    	}
        gridPanel.revalidate();
        gridPanel.repaint();
    }
}

