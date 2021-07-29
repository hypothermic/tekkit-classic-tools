package nl.hypothermic.tekkitclienttools.gui;

import nl.hypothermic.tekkitclienttools.gui.ingame.NotificationGuiOverlay;
import nl.hypothermic.tekkitclienttools.gui.swing.SwingGuiOverlay;

import java.util.Collection;
import java.util.HashSet;

public final class GuiManager {

	private static class InstanceHolder {

		private static final GuiManager INSTANCE = new GuiManager();

	}

	public static GuiManager getInstance() {
		return InstanceHolder.INSTANCE;
	}

	private GuiManager() {
		overlays = new HashSet<>();

		overlays.add(new NotificationGuiOverlay());
		overlays.add(new SwingGuiOverlay());
	}

	private final Collection<IIngameGuiOverlay> overlays;

	public void render() {
		overlays.forEach(overlay -> overlay.draw(null));
	}

	public void onKeyPress(int pressedKey) {
		overlays.forEach(overlay -> overlay.handleKey(pressedKey));
	}
}
