package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.ClassTransformer;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

@ClassTransformer(targetClass = "net/minecraft/client/Minecraft")
@SuppressWarnings("unused")
public class MinecraftPublicizerTransformer extends ClassVisitor {

	public MinecraftPublicizerTransformer(ClassVisitor classVisitor) {
		super(ASM4, classVisitor);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		if (name.equals("a")) {
			return cv.visitField(Opcodes.ACC_STATIC + Opcodes.ACC_PUBLIC, name, desc, signature, value);
		}

		return cv.visitField(access, name, desc, signature, value);
	}
}
