package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.Gateway;
import nl.hypothermic.htf.api.MethodTransformer;
import nl.hypothermic.htf.utils.GatewayCreator;
import nl.hypothermic.htf.utils.IfStatementBuilder;
import nl.hypothermic.tekkitclienttools.AddonGatewayIdIndex;
import nl.hypothermic.tekkitclienttools.gui.ColorSetting;
import nl.hypothermic.tekkitclienttools.gui.GuiTools;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashSet;

//@MethodTransformer(targetClass = "it", targetMethodName = "a", targetMethodDescription = "(Lnn;DDDFF)V")
@MethodTransformer(targetClass = "fe", targetMethodName = "a", targetMethodDescription = "(Lacq;DDDFF)V")
@SuppressWarnings("unused")
public class EntityRenderPassTransformer extends MethodNode implements Opcodes {

	public static boolean isEnabled = false;
	public static ColorSetting entityOutline = new ColorSetting(true, new Color(255, 0, 0, 255));
	public static ColorSetting entityTracer = new ColorSetting(false, new Color(255, 0, 0, 255));
	public static ColorSetting entityColor = new ColorSetting(false, new Color(255, 0, 0, 255));

	public EntityRenderPassTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Gateway(id = AddonGatewayIdIndex.ENTITY_ESP_ENABLED, outputType = boolean.class)
	public static Object isEntityRenderPassEnabled(Object data) {
		return isEnabled && (entityOutline.isEnabled() || entityTracer.isEnabled());
	}

	@Gateway(id = AddonGatewayIdIndex.ENTITY_ESP_RENDER, inputType = double[].class)
	public static Object renderEntityOutline(Object data) {
		double[] xyz = (double[]) data;
		double x = xyz[0], y = xyz[1], z = xyz[2], height = xyz[3], width = xyz[4];

		GL11.glBlendFunc(770, 771);
		GL11.glLineWidth(1.0F);

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);

		x = x - (width / 2);
		z = z - (width / 2);

		if (entityOutline.isEnabled()) {
			Color color = entityOutline.getColor();
			GL11.glColor4d(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
			GuiTools.drawOutlinedBoundingBox(x, y, z, x + width, y + height, z + width);
		}

		if (entityTracer.isEnabled()) {
			Color color = entityTracer.getColor();
			GL11.glColor4d(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2d(0D, 0D);
			GL11.glVertex3d(x + (width/2), y + (height/2), z + (width/2));
			GL11.glEnd();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);

		if (entityColor.isEnabled()) {
			Color color = entityColor.getColor();
			GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		} else {
			GL11.glColor4f(255, 255, 255, 255);
		}

		return null;
	}

	@Override
	public void visitCode() {
		IfStatementBuilder builder = IfStatementBuilder.newBuilder();

		builder.onLoad(methodVisitor ->
				// check if transparency is enabled for block with this ID
				GatewayCreator.create(
						methodVisitor,
						GatewayCreator.ref(EntityRenderPassTransformer.class, "isEntityRenderPassEnabled")
				)
		);
		builder.setOpcode(IFEQ);
		builder.onTrue(methodVisitor ->
				GatewayCreator.create(
						methodVisitor,
						GatewayCreator.ref(EntityRenderPassTransformer.class, "renderEntityOutline"),
						loadInsnVisitor -> {
							// create array with 5 elements
							loadInsnVisitor.visitLdcInsn(5);
							loadInsnVisitor.visitIntInsn(NEWARRAY, T_DOUBLE);

							// fill this array with the XYZ params (double = 2b wide, flt = 1b wide, so vars 2,4,6,8,9 instead of 2,3,4,5,6)

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

							loadInsnVisitor.visitInsn(DUP);
							loadInsnVisitor.visitInsn(ICONST_3);
							loadInsnVisitor.visitVarInsn(ALOAD, 1);
							loadInsnVisitor.visitFieldInsn(GETFIELD, "nn", "J", "F");
							loadInsnVisitor.visitInsn(F2D);
							loadInsnVisitor.visitInsn(DASTORE);

							loadInsnVisitor.visitInsn(DUP);
							loadInsnVisitor.visitInsn(ICONST_4);
							loadInsnVisitor.visitVarInsn(ALOAD, 1);
							loadInsnVisitor.visitFieldInsn(GETFIELD, "nn", "I", "F");
							loadInsnVisitor.visitInsn(F2D);
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
