package com.codenest.ui.dialog;

import com.codenest.model.Community;
import javax.swing.*;
import java.awt.*;

public class CommunityDetailsDialog extends JDialog {
	private Community community;

	public CommunityDetailsDialog(Community community) {
		this.community = community;
		setTitle("Community Details: " + community.getName());
		setSize(400, 300);
		setLocationRelativeTo(null);
		setModal(true);

		JPanel panel = new JPanel(new BorderLayout());
		JLabel nameLabel = new JLabel("<html><h2>" + community.getName() + "</h2></html>");
		JLabel descLabel = new JLabel("<html><p>" + community.getDescription() + "</p></html>");
		panel.add(nameLabel, BorderLayout.NORTH);
		panel.add(descLabel, BorderLayout.CENTER);
		add(panel);
	}
}
