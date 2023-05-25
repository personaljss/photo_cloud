package gui.profile;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import models.Photo;
import services.Logger;
import utils.PhotoFilter;

public class FilterDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4959273066865688139L;
	private JSlider filterSlider;
	private JButton applyButton;
	private JButton saveButton;
	private JButton cancelButton;

	private int selectedValue;
	private PhotoFilter filter;
	private Photo photo;
	private PhotoEditingFrame parent;
	private BufferedImage filteredImage;

	public FilterDialog(PhotoEditingFrame parent,Photo photo, PhotoFilter filter) {
		super(parent, "Filter Settings", true);
		this.filter = filter;
		this.photo =photo;
		this.parent = parent;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initComponents();
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	private void initComponents() {
		filterSlider = new JSlider(0, 100, 0);
		filterSlider.setMajorTickSpacing(20);
		filterSlider.setMinorTickSpacing(5);
		filterSlider.setPaintTicks(true);
		filterSlider.setPaintLabels(true);
		filterSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				selectedValue = filterSlider.getValue();
			}
		});

		applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyFilter();
			}
		});

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFilter();
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelFilter();
			}
		});

		JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPanel.add(new JLabel(filter.toString() + " Amount:"), BorderLayout.NORTH);
		contentPanel.add(filterSlider, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(applyButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		setContentPane(contentPanel);
	}

	private void applyFilter() {
		// Perform actions to apply the filter
		try {
			filteredImage=filter.apply(photo.getImageMatrix(), selectedValue);
			parent.updatePhoto(filteredImage);
		} catch (IOException e) {
        	Logger.getInstance().logError(e.getMessage());
			e.printStackTrace();
		}
	}

	private void saveFilter() {
		// Perform actions to save the filter
		if(filteredImage==null) {
			return;
		}
		try {
			photo.addFilter(filter, filteredImage);
			dispose();
		} catch (Exception e) {
        	Logger.getInstance().logError(e.getMessage());
			e.printStackTrace();
		}
		
	}

	private void cancelFilter() {
		// Perform actions to cancel the filter

		dispose();
	}
	
	@Override
	public void dispose() {
		parent.updatePhoto(photo);
		super.dispose();
	}

}
