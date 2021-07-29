package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.MethodTransformer;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

@MethodTransformer(targetClass = "ck", targetMethodName = "run", targetMethodDescription = "()V")
@SuppressWarnings("unused")
public class ResourceDownloadThreadTransformer extends MethodNode implements Opcodes {

	public ResourceDownloadThreadTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		mv.visitMaxs(0, 0);
	}

	@Override
	public void visitCode() {
		mv.visitCode();
		mv.visitInsn(Opcodes.RETURN);
	}

	@Override
	public void visitEnd() {
		mv.visitEnd();
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return mv.visitAnnotation(desc, visible);
	}
}
