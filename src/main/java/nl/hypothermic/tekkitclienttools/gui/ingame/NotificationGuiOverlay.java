package nl.hypothermic.tekkitclienttools.gui.ingame;

import nl.hypothermic.tekkitclienttools.gui.EventHandlerResult;
import nl.hypothermic.tekkitclienttools.gui.GuiTools;
import nl.hypothermic.tekkitclienttools.gui.IIngameGuiOverlay;
import nl.hypothermic.tekkitclienttools.gui.ScaledResolutionAdapter;

public class NotificationGuiOverlay implements IIngameGuiOverlay {

	@Override
	public void draw(ScaledResolutionAdapter scaledResolutionAdapter) {
		GuiTools.drawRect(10, 20, 36, 35, 0x000000, 99F);
		GuiTools.drawText("§a§lF4", 16, 8, 0xff0000, false);
	}

	@Override
	public EventHandlerResult handleKey(int key) {
		return EventHandlerResult.NOT_HANDLED;
	}
}
