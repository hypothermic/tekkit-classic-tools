package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.Gateway;
import nl.hypothermic.htf.api.MethodTransformer;
import nl.hypothermic.htf.utils.GatewayCreator;
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
		Label trueLabel = new Label(),
			  afterFalseLabel = new Label();

		// if

		GatewayCreator.create(
				this,
				GatewayCreator.ref(BlockBrightnessTransformer.class, "isBrightnessEnabled")
		);
		visitJumpInsn(IFEQ, trueLabel);

		// true

		GatewayCreator.create(
				this,
				GatewayCreator.ref(BlockBrightnessTransformer.class, "getBrightnessValue")
		);

		visitJumpInsn(GOTO, afterFalseLabel);
		visitLabel(trueLabel);

		// false

		visitVarInsn(ALOAD, 1);
		visitVarInsn(ILOAD, 2);
		visitVarInsn(ILOAD, 3);
		visitVarInsn(ILOAD, 4);
		visitFieldInsn(
				GETSTATIC,
				"pb",
				"q",
				"[I"
		);
		visitVarInsn(ALOAD, 0);
		visitFieldInsn(
				GETFIELD,
				"pb",
				"bO",
				"I"
		);
		visitInsn(IALOAD);
		visitMethodInsn(
				INVOKEINTERFACE,
				"ali",
				"a",
				"(IIII)F"
		);

		visitLabel(afterFalseLabel);

		// merge

		visitInsn(FRETURN);
	}

	@Override
	public void visitEnd() {
		accept(mv);
	}
}
