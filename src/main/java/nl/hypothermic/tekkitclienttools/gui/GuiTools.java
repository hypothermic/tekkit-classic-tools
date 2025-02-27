package nl.hypothermic.tekkitclienttools.gui;

import nl.hypothermic.tekkitclienttools.transformer.TextureReloadTransformer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class GuiTools {

	private static final Class<?> tessellatorClass, minecraftClass, renderGlobalClass, aabbClass;
	private static final Object tessellatorInstance, minecraftInstance, fontRenderer, renderGlobalInstance;
	private static final Method drawStringMethod, drawObbMethod, newAabbMethod;

	public static void drawRect(int x, int y, int width, int height, int color, float opacity) {
		float f = (float)(color >> 24 & 0xff) / 255F;
		float f1 = (float)(color >> 16 & 0xff) / 255F;
		float f2 = (float)(color >> 8 & 0xff) / 255F;
		try {
			Method startDrawingMethod = tessellatorClass.getMethod("b");
			Method addVertexMethod = tessellatorClass.getMethod("a", double.class, double.class, double.class);
			Method setColorRgbaMethod = tessellatorClass.getMethod("a", int.class, int.class, int.class, int.class);
			Method stopDrawingMethod = tessellatorClass.getMethod("a");

			startDrawingMethod.invoke(tessellatorInstance);
			setColorRgbaMethod.invoke(tessellatorInstance, (int) f, (int) f1, (int) f2, (int) opacity);
			addVertexMethod.invoke(tessellatorInstance, x, y, 0.0D);
			addVertexMethod.invoke(tessellatorInstance, width, y, 0.0D);
			addVertexMethod.invoke(tessellatorInstance, width, y - height, 0.0D);
			addVertexMethod.invoke(tessellatorInstance, x, y - height, 0.0D);
			stopDrawingMethod.invoke(tessellatorInstance);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void drawText(String text, int x, int y, int color, boolean drawShadow) {
		try {
			drawStringMethod.invoke(fontRenderer, text, x, y, color, false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void drawOutlinedBoundingBox(double xStart, double yStart, double zStart, double xEnd, double yEnd, double zEnd) {
		try {
			drawObbMethod.invoke(renderGlobalInstance, newAabbMethod.invoke(null, xStart, yStart, zStart, xEnd, yEnd, zEnd));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void reloadTextures() {
		TextureReloadTransformer.reloadRequired = true;
	}

	private GuiTools() {
		throw new UnsupportedOperationException();
	}

	static {
		try {
			minecraftClass = Class.forName("net.minecraft.client.Minecraft");
			Field minecraftInstanceField = minecraftClass.getDeclaredField("a");
			minecraftInstanceField.setAccessible(true);
			minecraftInstance = minecraftInstanceField.get(null);

			fontRenderer = minecraftClass.getField("q").get(minecraftInstance);
			Class<?> fontRendererClass = fontRenderer.getClass();
			drawStringMethod = fontRendererClass.getMethod("a", String.class, int.class, int.class, int.class, boolean.class);

			tessellatorClass = Class.forName("adz");
			tessellatorInstance = tessellatorClass.getField("a").get(null);

			aabbClass = Class.forName("wu");
			newAabbMethod = aabbClass.getMethod("a", double.class, double.class, double.class, double.class, double.class, double.class);

			renderGlobalClass = Class.forName("l");
			drawObbMethod = renderGlobalClass.getDeclaredMethod("a", aabbClass);
			drawObbMethod.setAccessible(true);

			renderGlobalInstance = minecraftClass.getField("g").get(minecraftInstance);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
