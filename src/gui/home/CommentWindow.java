package gui.home;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import auth.Authentication;
import models.Comment;
import models.Photo;

public class CommentWindow extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -342350227170131884L;
	private JPanel commentPanelContainer;
    private JPanel commentContentContainer;
    private JTextArea commentTextArea;
    private JButton commentButton;
    private JScrollPane scrollPane;
    private List<CommentPanel> commentPanels;
    private Photo photo;

    public CommentWindow(Photo photo) {
        commentPanels = new ArrayList<>();
        this.photo=photo;
        setComments(photo);

        commentPanelContainer = new JPanel(new BorderLayout());
        commentContentContainer = new JPanel();
        commentContentContainer.setLayout(new BoxLayout(commentContentContainer, BoxLayout.Y_AXIS));

        commentTextArea = new JTextArea();
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);

        commentButton = new JButton("Add Comment");
        commentButton.addActionListener(e -> addComment());

        commentPanelContainer.add(commentContentContainer, BorderLayout.CENTER);

        scrollPane = new JScrollPane(commentPanelContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(commentTextArea, BorderLayout.CENTER);
        inputPanel.add(commentButton, BorderLayout.EAST);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(500, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Comment Window");
        setVisible(true);
    }
    
    private void setComments(Photo photo) {
    	for(Comment comment : photo.getComments()) {
        	CommentPanel commentPanel = new CommentPanel(comment);
            commentPanels.add(commentPanel);
            commentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, commentPanel.getPreferredSize().height));
            commentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    	}
    }

    private void addComment() {
        String text = commentTextArea.getText();
        if (!text.isEmpty()) {
        	Comment comment=new Comment(Authentication.getInstance().getCurrentUser(),text);
        	photo.addComment(comment);
        	CommentPanel commentPanel = new CommentPanel(comment);
            commentPanels.add(commentPanel);

            commentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, commentPanel.getPreferredSize().height));
            commentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            commentContentContainer.add(commentPanel);
            commentContentContainer.revalidate();
            commentContentContainer.repaint();

            commentTextArea.setText("");

            // Scroll to the newly added comment
            SwingUtilities.invokeLater(() -> {
                JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                verticalScrollBar.setValue(verticalScrollBar.getMaximum());
            });
        }
    }
/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CommentWindow commentWindow = new CommentWindow();
            commentWindow.pack();
            commentWindow.setLocationRelativeTo(null);
            commentWindow.setVisible(true);
        });
    }
    */
}
