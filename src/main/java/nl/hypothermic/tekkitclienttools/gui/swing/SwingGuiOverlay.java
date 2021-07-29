package nl.hypothermic.tekkitclienttools.gui.swing;

import nl.hypothermic.tekkitclienttools.gui.EventHandlerResult;
import nl.hypothermic.tekkitclienttools.gui.IIngameGuiOverlay;
import nl.hypothermic.tekkitclienttools.gui.ScaledResolutionAdapter;

import javax.swing.*;

public class SwingGuiOverlay implements IIngameGuiOverlay {

	private final JFrame frame;

	{
		frame = new ToolControlFrame();
		frame.setVisible(false);
	}

	@Override
	public void draw(ScaledResolutionAdapter scaledResolutionAdapter) {

	}

	@Override
	public EventHandlerResult handleKey(int key) {
		if (key == 62) {
			frame.setVisible(!frame.isVisible());

			return EventHandlerResult.STOP_PROPAGATION;
		}

		return EventHandlerResult.NOT_HANDLED;
	}
}
