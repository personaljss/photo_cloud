package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class TabBarPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5512980763226864744L;
	private JButton backButton;
    private JButton profileButton;
    private JButton discoveryButton;
    private boolean isBackEnabled;

    public TabBarPanel(boolean isBackEnabled) {
    	this.isBackEnabled=isBackEnabled;
        initialize();
        createComponents();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(getWidth(), 50));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY)); // Add bottom line

    }

    private void createComponents() {
        // Back button
        backButton = new IconButton(new ImageIcon("resources/back.png"), 32);
        backButton.setBorder(new EmptyBorder(0, 30, 0, 0)); // Add left margin
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle back button action
                Navigator.getInstance().navigateBack();
            }
        });
        if (this.isBackEnabled) {
            add(backButton, BorderLayout.WEST);
        }

        // Center panel for the other two buttons
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, (50 - 32) / 2)); // Adjust the spacing
        centerPanel.setOpaque(false);

        // Profile button
        profileButton = new IconButton(new ImageIcon("resources/profile.png"), 32);
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle profile button action
            	Navigator.getInstance().navigateTo(Navigator.PROFILE_PAGE);
            }
        });
        centerPanel.add(profileButton);
        // Add invisible box for spacing
        JPanel spacingPanel = new JPanel();
        spacingPanel.setPreferredSize(new Dimension(50, 1)); // Adjust the width as needed
        spacingPanel.setOpaque(false);
        centerPanel.add(spacingPanel);
        // Discovery button
        discoveryButton = new IconButton(new ImageIcon("resources/discovery.png"), 32);
        discoveryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle discovery button action
            	Navigator.getInstance().navigateTo(Navigator.DISCOVERY_PAGE);
            }
        });
        centerPanel.add(discoveryButton);

        add(centerPanel, BorderLayout.CENTER);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the bottom line
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GRAY);
        g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getProfileButton() {
        return profileButton;
    }

    public JButton getDiscoveryButton() {
        return discoveryButton;
    }
}
