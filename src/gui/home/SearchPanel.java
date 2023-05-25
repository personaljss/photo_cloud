package gui.home;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import gui.Navigator;
import models.AppState;

public class SearchPanel extends JPanel {

    private static final long serialVersionUID = -1938590336420829808L;

    private Set<String> elements; // List of elements to search in
    private JPanel filteredElementsPanel; // Panel to display filtered elements
    private AppState state;
    private JScrollPane scrollPane;

    /**
     * Creates a new SearchPanel instance.
     */
    public SearchPanel() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(300, getHeight()));
        this.setMaximumSize(new Dimension(300, getHeight()));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.state = AppState.getInstance();
        this.elements = state.getUsers().keySet();

        // Search bar and button
        JTextField searchBar = new JTextField();
        searchBar.setMaximumSize(new Dimension(150, 25));
        JButton searchButton = new JButton("Search");
        searchButton.setIcon(new ImageIcon("resources/search.png"));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchWord = searchBar.getText();
                performSearch(searchWord);
            }
        });

        // Search panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.add(searchBar);
        inputPanel.add(Box.createRigidArea(new Dimension(5, 0))); // Add some spacing
        inputPanel.add(searchButton);

        // Add search panel to the top
        this.add(inputPanel, BorderLayout.NORTH);

        // Filtered elements panel
        filteredElementsPanel = new JPanel();
        filteredElementsPanel.setLayout(new BoxLayout(filteredElementsPanel, BoxLayout.Y_AXIS)); // Update layout manager
        scrollPane = new JScrollPane(filteredElementsPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Add scroll pane to the center
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Performs the search operation based on the entered search word.
     *
     * @param searchWord The search word to filter the elements.
     */
    private void performSearch(String searchWord) {
        List<String> filteredElements = new ArrayList<>();

        for (String element : elements) {
            if (element.contains(searchWord)) {
                filteredElements.add(element);
            }
        }

        // Update the UI to display the filtered elements
        updateFilteredElements(filteredElements);
    }

    /**
     * Updates the UI to display the filtered elements.
     *
     * @param filteredElements The filtered elements to display.
     */
    private void updateFilteredElements(List<String> filteredElements) {
        // Clear existing components
        filteredElementsPanel.removeAll();

        for (String element : filteredElements) {
            JButton elementButton = new JButton(element);
            elementButton.setAlignmentX(Component.LEFT_ALIGNMENT); // Align the button to the left
            elementButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, elementButton.getPreferredSize().height)); // Make the button fill the horizontal space
            elementButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Navigator.getInstance().navigateTo(Navigator.OTHERS_PROFILE_PAGE, state.getUser(element));
                }
            });
            filteredElementsPanel.add(elementButton);
            filteredElementsPanel.add(Box.createVerticalStrut(5)); // Add vertical spacing between buttons
        }

        // Refresh the UI
        revalidate();
        repaint();
    }
}
