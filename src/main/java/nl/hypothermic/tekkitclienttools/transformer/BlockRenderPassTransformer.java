package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.Gateway;
import nl.hypothermic.htf.api.MethodTransformer;
import nl.hypothermic.htf.utils.GatewayCreator;
import nl.hypothermic.tekkitclienttools.AddonGatewayIdIndex;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashSet;

@MethodTransformer(targetClass = "pb", targetMethodName = "c", targetMethodDescription = "()I")
@SuppressWarnings("unused")
public class BlockRenderPassTransformer extends MethodNode implements Opcodes {

	public static final HashSet<Integer> EXCLUDED_BLOCKS = new HashSet<>();

	public static boolean transparencyEnabled = false;

	public BlockRenderPassTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Gateway(id = AddonGatewayIdIndex.BLOCK_TRANSPARENCY_ENABLED, inputType = int.class, outputType = boolean.class)
	public static Object isTransparencyEnabled(Object data) {
		if (data == null) {
			return transparencyEnabled;
		}
		if (transparencyEnabled) {
			return !EXCLUDED_BLOCKS.contains((int) data);
		}
		return false;
	}

	@Override
	public void visitCode() {
		// check if transparency is enabled for block with this ID
		GatewayCreator.create(
				this,
				GatewayCreator.ref(BlockRenderPassTransformer.class, "isTransparencyEnabled"),
				methodVisitor -> {
					methodVisitor.visitVarInsn(ALOAD, 0);
					methodVisitor.visitFieldInsn(GETFIELD, "pb", "bO", "I");
					return null;
				}
		);

		// convert the return value from boolean to int
		visitMethodInsn(
				Opcodes.INVOKESTATIC,
				GatewayCreator.CAST_UTIL_CLASS,
				"toInt",
				"(Z)I"
		);

		// return the int (this int describes which render pass should be used)
		visitInsn(IRETURN);

		visitMaxs(2, 0);
		visitEnd();
	}

	@Override
	public void visitEnd() {
		accept(mv);
	}
}
