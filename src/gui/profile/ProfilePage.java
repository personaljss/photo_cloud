package gui.profile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import gui.home.PhotoPanel;

public class ProfilePage extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8018934881827210300L;

	public ProfilePage() {
        setTitle("Profile Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(200, getHeight()));

        // Add profile picture (assuming you have a default profile picture path)
        JLabel profilePicLabel = new JLabel(new ImageIcon("resources/likeIcon.png"));
        profilePicLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(profilePicLabel);

        // Add user information
        JLabel nicknameLabel = new JLabel("Nickname: User Nickname");
        nicknameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(nicknameLabel);

        JLabel emailLabel = new JLabel("Email: user@email.com");
        emailLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(emailLabel);

        JLabel ageLabel = new JLabel("Age: User Age");
        ageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(ageLabel);

        // Add image upload button
        JButton uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle image upload
            }
        });
        uploadButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(uploadButton);

        // Add photo grid on the right (reusing your PhotoGrid class)
        JPanel rightPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rightPanel.setLayout(new GridLayout(0, 3, 10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Assuming you have a method getImages() that returns a list of user's images
        for (int i=0; i<10; i++) {
            try {
				rightPanel.add(new PhotoPanel());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }

        // Add panels to the content pane
        getContentPane().add(leftPanel, BorderLayout.WEST);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
    
    // Dummy getImages() method to be replaced with your actual implementation

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ProfilePage window = new ProfilePage();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
