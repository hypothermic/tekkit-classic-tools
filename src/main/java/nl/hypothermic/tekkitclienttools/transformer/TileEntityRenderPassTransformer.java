package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.Gateway;
import nl.hypothermic.htf.api.MethodTransformer;
import nl.hypothermic.htf.utils.GatewayCreator;
import nl.hypothermic.htf.utils.IfStatementBuilder;
import nl.hypothermic.tekkitclienttools.AddonGatewayIdIndex;
import nl.hypothermic.tekkitclienttools.gui.ColorSetting;
import nl.hypothermic.tekkitclienttools.gui.GuiTools;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import java.awt.*;
import java.util.HashSet;

@MethodTransformer(targetClass = "ach", targetMethodName = "a", targetMethodDescription = "(Lkw;DDDF)V")
@SuppressWarnings("unused")
public class TileEntityRenderPassTransformer extends MethodNode implements Opcodes {

	public static final HashSet<Integer> INCLUDED_BLOCKS = new HashSet<Integer>() {{ add(54); }};

	public static boolean isEnabled = false;
	public static ColorSetting tileOutline = new ColorSetting(true, new Color(255, 0, 0, 255));
	public static ColorSetting tileCursorLine = new ColorSetting(true, new Color(255, 0, 0, 255));

	public TileEntityRenderPassTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Gateway(id = AddonGatewayIdIndex.TILE_ESP_ENABLED, inputType = int.class, outputType = boolean.class)
	public static Object isTileEspEnabledForBlock(Object data) {
		if (isEnabled && (tileOutline.isEnabled() || tileCursorLine.isEnabled())) {
			return INCLUDED_BLOCKS.contains((int) data);
		}
		return false;
	}

	@Gateway(id = AddonGatewayIdIndex.TILE_ESP_RENDER, inputType = double[].class)
	public static Object renderBlockOutline(Object data) {
		double[] xyz = (double[]) data;
		double x = xyz[0], y = xyz[1], z = xyz[2];

		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(1.0F);

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);

		if (tileOutline.isEnabled()) {
			Color color = tileOutline.getColor();
			GL11.glColor4d(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
			GuiTools.drawOutlinedBoundingBox(x, y, z, x + 1.0, y + 1.0, z + 1.0);
		}

		if (tileCursorLine.isEnabled()) {
			Color color = tileCursorLine.getColor();
			GL11.glColor4d(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(0D, 0D);
			GL11.glVertex3d(x + 0.5, y + 0.5, z + 0.5);
			GL11.glEnd();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);

		return null;
	}

	@Override
	public void visitCode() {
		IfStatementBuilder builder = IfStatementBuilder.newBuilder();

		builder.onLoad(methodVisitor ->
				// check if transparency is enabled for block with this ID
				GatewayCreator.create(
						methodVisitor,
						GatewayCreator.ref(TileEntityRenderPassTransformer.class, "isTileEspEnabledForBlock"),
						loadInsnVisitor -> {
							IfStatementBuilder ifStmt = IfStatementBuilder.newBuilder();

							ifStmt.onLoad(ifLoadInsnVisitor -> {
								ifLoadInsnVisitor.visitVarInsn(ALOAD, 1);
								ifLoadInsnVisitor.visitFieldInsn(GETFIELD, "kw", "o", "Lpb;");
							});
							ifStmt.setOpcode(IFNULL);
							ifStmt.onTrue(ifTrueInsnVisitor -> {
								ifTrueInsnVisitor.visitVarInsn(ALOAD, 1);
								ifTrueInsnVisitor.visitFieldInsn(GETFIELD, "kw", "o", "Lpb;");
								ifTrueInsnVisitor.visitFieldInsn(GETFIELD, "pb", "bO", "I");
							});
							ifStmt.onFalse(ifFalseInsnVisitor -> {
								ifFalseInsnVisitor.visitLdcInsn(-1);
							});
							ifStmt.insert(loadInsnVisitor);

							return null;
						}
				));
		builder.setOpcode(IFEQ);
		builder.onTrue(methodVisitor ->
				GatewayCreator.create(
						methodVisitor,
						GatewayCreator.ref(TileEntityRenderPassTransformer.class, "renderBlockOutline"),
						loadInsnVisitor -> {
							// create array with 3 elements
							loadInsnVisitor.visitLdcInsn(3);
							loadInsnVisitor.visitIntInsn(NEWARRAY, T_DOUBLE);

							// fill this array with the XYZ params (double = 2b wide, so vars 2,4,6 instead of 2,3,4)

							loadInsnVisitor.visitInsn(DUP);
							loadInsnVisitor.visitInsn(ICONST_0);
							loadInsnVisitor.visitVarInsn(DLOAD, 2);
							loadInsnVisitor.visitInsn(DASTORE);

							loadInsnVisitor.visitInsn(DUP);
							loadInsnVisitor.visitInsn(ICONST_1);
							loadInsnVisitor.visitVarInsn(DLOAD, 4);
							loadInsnVisitor.visitInsn(DASTORE);

							loadInsnVisitor.visitInsn(DUP);
							loadInsnVisitor.visitInsn(ICONST_2);
							loadInsnVisitor.visitVarInsn(DLOAD, 6);
							loadInsnVisitor.visitInsn(DASTORE);

							// leave array ref on stack
							return null;
						})
		);
		builder.onFalse(methodVisitor -> {});
		builder.insert(this);
	}

	@Override
	public void visitEnd() {
		accept(mv);
	}
}
