package nl.hypothermic.tekkitclienttools.gui;

public interface IIngameGuiOverlay {

	void draw(ScaledResolutionAdapter scaledResolutionAdapter);

	EventHandlerResult handleKey(int key);

}
