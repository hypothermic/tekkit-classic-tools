package nl.hypothermic.tekkitclienttools.gui;

public class ScaledResolutionAdapter {

	private final int width, height;

	public ScaledResolutionAdapter(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
