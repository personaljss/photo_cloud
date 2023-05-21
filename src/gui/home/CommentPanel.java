package gui.home;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import models.Comment;

public class CommentPanel extends JPanel {
    private static final long serialVersionUID = 6671729988924193099L;
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 200;
    private Comment comment;

    public CommentPanel(Comment comment) {
        this.comment = comment;
        initialize();
        createComponents();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
    }

    private void createComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel ownerPanel = createClickablePanel(true);
        JLabel ownerLabel = new JLabel("Owner: " + comment.getOwner().getNickname());
        ownerPanel.add(ownerLabel);
        contentPanel.add(ownerPanel);

        JPanel textPanel = createClickablePanel(false);
        JTextArea textArea = new JTextArea(comment.getText());
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setBackground(textPanel.getBackground());
        textPanel.add(textArea);
        contentPanel.add(textPanel);

        JPanel datePanel = createClickablePanel(false);
        JLabel dateLabel = new JLabel("Date: " + comment.getDate().toString());
        datePanel.add(dateLabel);
        contentPanel.add(datePanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createClickablePanel(boolean setCursor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        if (setCursor) {
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        return panel;
    }
}
