package gui.home;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import gui.Application;
import gui.Navigator;
import gui.TabBarPanel;
import listeners.PhotoListener;
import models.AppState;
import models.Photo;

/**
 * The DiscoveryPage class represents the user interface for the discovery
 * feature. It displays a grid of photos and allows the user to navigate to
 * other pages.
 */
public class DiscoveryPage extends JFrame implements PhotoListener{

    private static final long serialVersionUID = 4040835824146582617L;

    private List<Photo> photos;
    private JPanel rightPanel;
    private TabBarPanel tabBarPanel;

    /**
     * Constructs a new DiscoveryPage object.
     */
    public DiscoveryPage() {
        initializeFrame();
        createComponents();
    }

    /**
     * Initializes the frame properties.
     */
    private void initializeFrame() {
        photos = AppState.getInstance().getDiscoveryContent();
        setTitle("Discovery");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(Application.getInstance().getDimension());
        setLocationRelativeTo(null); // center the frame
        setBounds(100, 100, 800, 600);
        setLayout(new BorderLayout());
    }

    /**
     * Creates the components of the DiscoveryPage.
     */
    private void createComponents() {
        createTabBarPanel();
        createContentPanel();
    }

    /**
     * Creates the content panel with the search panel and photo grid.
     */
    private void createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new SearchPanel();
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(leftPanel, BorderLayout.WEST);

        createPhotoPanel();
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }


    /**
     * Creates the TabBarPanel and adds it to the frame.
     */
    private void createTabBarPanel() {
        tabBarPanel = new TabBarPanel(false);
        JButton backButton = tabBarPanel.getBackButton();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateBack();
            }
        });
        add(tabBarPanel, BorderLayout.NORTH);
    }

    /**
     * Creates the photo grid panel.
     */
    private void createPhotoPanel() {
        rightPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // Use GridLayout with 3 columns
        JScrollPane scrollPane = new JScrollPane(rightPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        displayImages();
        addInvisibleBoxes();

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Adds the photos to the photo grid panel.
     */
    public void displayImages() {
        rightPanel.removeAll(); // Clear existing images

        for (Photo photo : AppState.getInstance().getDiscoveryContent()) {
        	photo.addListener(this);
            try {
                DiscoveryPhotoPanel panel = new DiscoveryPhotoPanel(photo);
                //panel.setGalleryActionListener(this);
                rightPanel.add(panel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Add invisible boxes if there are less than 4 images
        int numImages = AppState.getInstance().getDiscoveryContent().size();
        if (numImages < 4) {
            for (int i = 0; i < (4 - numImages); i++) {
                rightPanel.add(createInvisibleBox());
            }
        }

        rightPanel.revalidate();
        rightPanel.repaint();
    }
    /**
     * Adds invisible boxes to fill the remaining slots in the photo grid panel.
     */
    private void addInvisibleBoxes() {
        int numPhotos = photos.size();
        int remainingSlots = 4 - (numPhotos % 4); // Calculate the number of remaining slots for invisible boxes

        for (int i = 0; i < remainingSlots; i++) {
            JPanel invisibleBox = createInvisibleBox();
            this.rightPanel.add(invisibleBox);
        }
    }

    /**
     * Creates an invisible box with the same size as the photo panel.
     * The invisible box is used to maintain a consistent layout with empty slots.
     *
     * @return the invisible box panel
     */
    private JPanel createInvisibleBox() {
        JPanel invisibleBox = new JPanel();
        invisibleBox.setPreferredSize(new Dimension(300, 300)); // Set the desired size
        invisibleBox.setOpaque(false); // Make the panel transparent
        return invisibleBox;
    }

    
    @Override
    public void dispose() {
    	for(Photo photo : photos) {
    		photo.removeListener(this);
    	}
    	super.dispose();
    }


    /**
     * Navigates back to the previous page.
     */
    private void navigateBack() {
        Navigator.getInstance().navigateBack();
    }


	@Override
	public void onDeleted(Photo photo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFilterApplied(Photo photo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDescriptionChanged(Photo photo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommentAdded(Photo photo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVisibilityChanged(Photo photo) {
		// TODO Auto-generated method stub
		displayImages();
		revalidate();
		repaint();
	}
}
