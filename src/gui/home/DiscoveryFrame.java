package gui.home;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class DiscoveryFrame extends JFrame {

    public DiscoveryFrame() {
        setTitle("Discovery");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950, 700));
        setLocationRelativeTo(null);  // center the frame

        // Main panel with BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        setContentPane(mainPanel);

        // Top panel with the title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Discovery");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, titleLabel.getPreferredSize().height));

        // Search panel with search bar and button
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchBar = new JTextField(20);  // adjust size as needed
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchBar);
        searchPanel.add(searchButton);

        // Photo panel with grid of PhotoPanels, using FlowLayout for equal-sized cells
        JPanel photoPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        for (int i = 0; i < 9; i++) {
            try {
				photoPanel.add(new PhotoPanel());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        // Scroll pane to allow scrolling, if necessary
        JScrollPane scrollPane = new JScrollPane(photoPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Add panels to main panel
        mainPanel.add(titlePanel);
        mainPanel.add(searchPanel);
        mainPanel.add(scrollPane);

        pack();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            DiscoveryFrame frame = new DiscoveryFrame();
            frame.setVisible(true);
        });
    }
}
