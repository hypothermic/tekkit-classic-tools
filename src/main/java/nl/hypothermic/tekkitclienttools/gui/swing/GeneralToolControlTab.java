package nl.hypothermic.tekkitclienttools.gui.swing;

import nl.hypothermic.flex.FlexComponent;
import nl.hypothermic.flex.component.BaseFlexComponent;

import javax.swing.*;
import java.awt.event.ItemEvent;

import static nl.hypothermic.flex.component.ListContainerFlexComponent.Direction.VERTICAL;

public class GeneralToolControlTab extends BaseToolControlTab {

	private final ToolControlFrame parentFrame;

	public GeneralToolControlTab(ToolControlFrame parentFrame) {
		this.parentFrame = parentFrame;

		BaseFlexComponent listComponent = FlexComponent
				.create(root -> root
						.list(VERTICAL, vertical -> vertical
								.label(label -> label.setText("Generic"))
								.checkbox(checkbox -> checkbox
										.setLabel("Always on top")
										.setInitialValue(parentFrame.isAlwaysOnTop())
										.setEnabled(parentFrame.isAlwaysOnTopSupported())
										.onChanged(parentFrame::setAlwaysOnTop)
								)
								.checkbox(checkbox -> checkbox
										.setLabel("Window decoration")
										.setInitialValue(!parentFrame.isUndecorated())
										.onChanged(newValue -> {
											parentFrame.dispose();
											parentFrame.setUndecorated(!newValue);
											parentFrame.setVisible(true);
										})
								)
						)
				).build();

		GroupLayout groupLayout = getBaseLayout();

		groupLayout.setHorizontalGroup(listComponent.createHorizontalGroup(groupLayout));
		groupLayout.setVerticalGroup(listComponent.createVerticalGroup(groupLayout));
	}

	@Override
	public String getTitle() {
		return "General";
	}
}
