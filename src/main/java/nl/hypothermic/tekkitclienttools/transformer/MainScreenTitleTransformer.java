package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.MethodTransformer;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

import static org.objectweb.asm.Opcodes.*;

@MethodTransformer(targetClass = "xt", targetMethodName = "a", targetMethodDescription = "(IIF)V")
@SuppressWarnings("unused")
public class MainScreenTitleTransformer extends MethodNode {

	public MainScreenTitleTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Override
	public void visitLdcInsn(Object cst) {
		if (cst.equals("Copyright Mojang AB. Do not distribute!")) {
			cst = "";
		}

		if (cst.equals("Minecraft 1.2.5")) {
			cst = "§a§lasm-htf = awesome!";
		}

		super.visitLdcInsn(cst);
	}

	@Override
	public void visitEnd() {
		accept(mv);
	}
}
