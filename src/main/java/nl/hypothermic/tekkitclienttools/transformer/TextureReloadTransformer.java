package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.Gateway;
import nl.hypothermic.htf.api.MethodTransformer;
import nl.hypothermic.htf.utils.GatewayCreator;
import nl.hypothermic.htf.utils.IfStatementBuilder;
import nl.hypothermic.tekkitclienttools.AddonGatewayIdIndex;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

import static org.objectweb.asm.Opcodes.*;

@MethodTransformer(targetClass = "net/minecraft/client/Minecraft", targetMethodName = "x", targetMethodDescription = "()V")
@SuppressWarnings("unused")
public class TextureReloadTransformer extends MethodNode {

	public static boolean reloadRequired = false;

	public TextureReloadTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Gateway(id = AddonGatewayIdIndex.RELOAD_REQUIRED_VALUE_ID, outputType = boolean.class)
	public static Object isReloadRequired(Object data) {
		if (reloadRequired) {
			reloadRequired = false;
			return true;
		}
		return false;
	}

	@Override
	public void visitCode() {
		IfStatementBuilder builder = IfStatementBuilder.newBuilder();

		builder.onLoad(methodVisitor ->
				GatewayCreator.create(
						this,
						GatewayCreator.ref(TextureReloadTransformer.class, "isReloadRequired")
				));
		builder.setOpcode(IFEQ);
		builder.onTrue(methodVisitor -> {
			// call <mc>.renderEngine.loadTextures() aka <mc>.g.a()V

			methodVisitor.visitFieldInsn(
					GETSTATIC,
					"net/minecraft/client/Minecraft",
					"a",
					"Lnet/minecraft/client/Minecraft;"
			);
			methodVisitor.visitFieldInsn(
					GETFIELD,
					"net/minecraft/client/Minecraft",
					"g",
					"Ll;"
			);
			methodVisitor.visitMethodInsn(
					INVOKEVIRTUAL,
					"l",
					"a",
					"()V"
			);
		});
		builder.onFalse(methodVisitor -> {});
		builder.insert(this);
	}

	@Override
	public void visitEnd() {
		accept(mv);
	}
}
