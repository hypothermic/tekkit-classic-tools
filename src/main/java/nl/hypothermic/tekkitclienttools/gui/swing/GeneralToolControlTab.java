package nl.hypothermic.tekkitclienttools.gui.swing;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class GeneralToolControlTab extends BaseToolControlTab {

	private final ToolControlFrame parentFrame;

	public GeneralToolControlTab(ToolControlFrame parentFrame) {
		this.parentFrame = parentFrame;

		GroupLayout groupLayout = getBaseLayout();
		JCheckBox checkBox = new JCheckBox("Always on top");
		JCheckBox checkBox2 = new JCheckBox("Window decoration");

		checkBox.setEnabled(parentFrame.isAlwaysOnTopSupported());
		checkBox.setSelected(parentFrame.isAlwaysOnTop());
		checkBox.addItemListener(e -> parentFrame.setAlwaysOnTop(e.getStateChange() == ItemEvent.SELECTED));

		checkBox2.setSelected(!parentFrame.isUndecorated());
		checkBox2.addItemListener(e -> {
			parentFrame.dispose();
			parentFrame.setUndecorated(e.getStateChange() == ItemEvent.DESELECTED);
			parentFrame.setVisible(true);
		});

		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(checkBox)
						.addComponent(checkBox2)
		);

		groupLayout.setVerticalGroup(
				groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(checkBox)
						)
						.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(checkBox2)
						)
		);
	}

	@Override
	public String getTitle() {
		return "General";
	}
}
