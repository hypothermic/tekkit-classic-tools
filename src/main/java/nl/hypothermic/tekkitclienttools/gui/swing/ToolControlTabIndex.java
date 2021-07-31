package nl.hypothermic.tekkitclienttools.gui.swing;

public enum ToolControlTabIndex {

	GENERAL(GeneralToolControlTab.class),
	TERRAIN(TerrainToolControlTab.class),

	;

	private final Class<? extends BaseToolControlTab> clazz;

	ToolControlTabIndex(Class<? extends BaseToolControlTab> clazz) {
		this.clazz = clazz;
	}

	public BaseToolControlTab newInstance(ToolControlFrame frame) throws Exception {
		return clazz.getDeclaredConstructor(ToolControlFrame.class).newInstance(frame);
	}
}
