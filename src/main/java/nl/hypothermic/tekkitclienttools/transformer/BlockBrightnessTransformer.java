package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.Gateway;
import nl.hypothermic.htf.api.MethodTransformer;
import nl.hypothermic.htf.utils.GatewayCreator;
import nl.hypothermic.htf.utils.IfStatementBuilder;
import nl.hypothermic.tekkitclienttools.AddonGatewayIdIndex;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

import static org.objectweb.asm.Opcodes.*;

@MethodTransformer(targetClass = "pb", targetMethodName = "e", targetMethodDescription = "(Lali;III)F")
@SuppressWarnings("unused")
public class BlockBrightnessTransformer extends MethodNode {

	public static final float MIN_BRIGHTNESS = 0f,
							  MAX_BRIGHTNESS = 1000f,
							  DEFAULT_BRIGHTNESS = MAX_BRIGHTNESS;

	public static boolean brightnessEnabled = false;
	public static float brightnessValue = DEFAULT_BRIGHTNESS;

	public BlockBrightnessTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Gateway(id = AddonGatewayIdIndex.BLOCK_BRIGHTNESS_ENABLED, outputType = boolean.class)
	public static Object isBrightnessEnabled(Object data) {
		return brightnessEnabled;
	}

	@Gateway(id = AddonGatewayIdIndex.BLOCK_BRIGHTNESS_VALUE, outputType = float.class)
	public static Object getBrightnessValue(Object data) {
		return brightnessValue;
	}

	@Override
	public void visitCode() {
		IfStatementBuilder builder = IfStatementBuilder.newBuilder();

		builder.onLoad(methodVisitor ->
				GatewayCreator.create(
						methodVisitor,
						GatewayCreator.ref(BlockBrightnessTransformer.class, "isBrightnessEnabled")
				));
		builder.setOpcode(IFEQ);
		builder.onTrue(methodVisitor -> GatewayCreator.create(
				this,
				GatewayCreator.ref(BlockBrightnessTransformer.class, "getBrightnessValue")
		));
		builder.onFalse(methodVisitor -> {
			methodVisitor.visitVarInsn(ALOAD, 1);
			methodVisitor.visitVarInsn(ILOAD, 2);
			methodVisitor.visitVarInsn(ILOAD, 3);
			methodVisitor.visitVarInsn(ILOAD, 4);
			methodVisitor.visitFieldInsn(
					GETSTATIC,
					"pb",
					"q",
					"[I"
			);
			methodVisitor.visitVarInsn(ALOAD, 0);
			methodVisitor.visitFieldInsn(
					GETFIELD,
					"pb",
					"bO",
					"I"
			);
			methodVisitor.visitInsn(IALOAD);
			methodVisitor.visitMethodInsn(
					INVOKEINTERFACE,
					"ali",
					"a",
					"(IIII)F"
			);
		});

		builder.insert(this);

		// return the brightness
		visitInsn(FRETURN);
	}

	@Override
	public void visitEnd() {
		accept(mv);
	}
}
