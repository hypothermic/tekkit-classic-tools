package nl.hypothermic.tekkitclienttools.gui.swing;

import javax.swing.*;

public abstract class BaseToolControlTab extends JPanel {

	private final GroupLayout groupLayout;

	{
		groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);
		setLayout(groupLayout);
	}

	public abstract String getTitle();

	public GroupLayout getBaseLayout() {
		return groupLayout;
	}
}
