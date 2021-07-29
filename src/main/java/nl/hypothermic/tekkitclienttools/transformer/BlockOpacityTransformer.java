package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.Gateway;
import nl.hypothermic.htf.api.MethodTransformer;
import nl.hypothermic.htf.utils.GatewayCreator;
import nl.hypothermic.tekkitclienttools.AddonGatewayIdIndex;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

@MethodTransformer(targetClass = "adz", targetMethodName = "a", targetMethodDescription = "(III)V")
@SuppressWarnings("unused")
public class BlockOpacityTransformer extends MethodNode implements Opcodes {

	public static final int MIN_OPACITY = 0,
							MAX_OPACITY = 255,
							DEFAULT_OPACITY = 70;

	public static int opacityValue = DEFAULT_OPACITY;

	public BlockOpacityTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Gateway(id = AddonGatewayIdIndex.BLOCK_OPACITY_VALUE, outputType = int.class)
	public static Object getOpacityValue(Object data) {
		return opacityValue;
	}

	@Override
	public void visitCode() {
		Label trueLabel = new Label(),
			  afterFalseLabel = new Label();

		visitVarInsn(Opcodes.ALOAD, 0);
		visitVarInsn(Opcodes.ILOAD, 1);
		visitVarInsn(Opcodes.ILOAD, 2);
		visitVarInsn(Opcodes.ILOAD, 3);

		// if

		// Because isTransparencyEnabled accepts an int as input value, we need to pass a dummy int
		GatewayCreator.create(
				this,
				GatewayCreator.ref(BlockRenderPassTransformer.class, "isTransparencyEnabled"),
				methodVisitor -> {
					methodVisitor.visitLdcInsn(-1);
					return null;
				}
		);
		visitJumpInsn(IFEQ, trueLabel);

		// true

		GatewayCreator.create(
				this,
				GatewayCreator.ref(BlockOpacityTransformer.class, "getOpacityValue")
		);

		visitJumpInsn(GOTO, afterFalseLabel);
		visitLabel(trueLabel);

		// false

		visitLdcInsn(255);

		visitLabel(afterFalseLabel);

		// merge

		visitMethodInsn(
				Opcodes.INVOKEVIRTUAL,
				"adz",
				"a",
				"(IIII)V"
		);

		visitInsn(RETURN);
		visitEnd();
	}

	@Override
	public void visitEnd() {
		accept(mv);
	}
}
