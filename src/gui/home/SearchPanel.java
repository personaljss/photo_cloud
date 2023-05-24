package gui.home;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The SearchPanel class represents a panel with a search bar and a list of elements
 * that can be filtered based on the entered search word.
 */

// List of elements to search in
// Panel to display filtered elements

/**
	 * Creates a new SearchPanel instance.
	 */
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import gui.Navigator;
import gui.profile.OthersProfilePage;
import gui.profile.ProfilePage;
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
	        filteredElementsPanel = new JPanel(new FlowLayout());
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
			//JLabel elementLabel = new JLabel(element);
			JButton elementLabel=new JButton(element);
			elementLabel.setPreferredSize(new Dimension(scrollPane.getWidth()-50,30));
			//elementLabel.setMaximumSize(new Dimension(getWidth(),50));
			elementLabel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Navigator.getInstance().navigateTo(Navigator.OTHERS_PROFILE_PAGE,state.getUser(element));
				}
				
			});
			filteredElementsPanel.add(elementLabel);
		}
		//addInvisibleBoxes();
		
		// Refresh the UI
		revalidate();
		repaint();
	}
	

	/*
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    */

    public static void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Search Panel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the SearchPanel
        SearchPanel searchPanel = createSearchPanel();

        // Add the SearchPanel to the frame
        frame.getContentPane().add(searchPanel);

        // Display the frame
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static SearchPanel createSearchPanel() {
        SearchPanel searchPanel = new SearchPanel();

        // Create some arbitrary elements for testing
        searchPanel.addElements("Apple");
        searchPanel.addElements("Banana");
        searchPanel.addElements("Cherry");
        searchPanel.addElements("Durian");
        searchPanel.addElements("Elderberry");
        searchPanel.addElements("Fig");
        searchPanel.addElements("Grape");
        searchPanel.addElements("Honeydew");
        searchPanel.addElements("Jackfruit");
        searchPanel.addElements("Kiwi");

        return searchPanel;
    }

	private void addElements(String string) {
		// TODO Auto-generated method stub
		if(elements==null) {
			this.elements=new HashSet<>();	
		}
		elements.add(string);
	}
}
