package nl.hypothermic.tekkitclienttools.gui;

import java.awt.*;

public class ColorSetting {

	private boolean enabled;
	private Color color;

	public ColorSetting(boolean enabled, Color color) {
		this.enabled = enabled;
		this.color = color;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
