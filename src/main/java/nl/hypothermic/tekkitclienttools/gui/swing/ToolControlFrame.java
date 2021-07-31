package nl.hypothermic.tekkitclienttools.gui.swing;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class ToolControlFrame extends JFrame {

	private final JTabbedPane tabbedPane;

	{
		setAutoRequestFocus(false);
		setTitle("Tekkit Classic Tools");
		setSize(500, 350);
		setAlwaysOnTop(true);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		Arrays.stream(ToolControlTabIndex.values())
				.map(tabClazz -> {
					try {
						return tabClazz.newInstance(this);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				})
				.filter(Objects::nonNull)
				.forEachOrdered(tab -> tabbedPane.addTab(tab.getTitle(), tab));

		add(tabbedPane, BorderLayout.CENTER);
	}
}
