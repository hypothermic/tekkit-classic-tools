package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.MethodTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

@MethodTransformer(targetClass = "vl", targetMethodName = "b", targetMethodDescription = "(Lpb;III)Z")
@SuppressWarnings("unused")
public class BlockTypeRenderBehaviourTransformer extends MethodNode implements Opcodes {

	public BlockTypeRenderBehaviourTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Override
	public void visitEnd() {
		ListIterator<AbstractInsnNode> iterator = this.instructions.iterator();
		boolean isInserted = false;

		/*
		 * Check for the combination of the following bytecode entries:
		 *
		 * ILOAD 4
		 * INVOKEVIRTUAL pb.a (Lali;III)V
		 */

		while (iterator.hasNext() && !isInserted) {
			AbstractInsnNode node = iterator.next();

			// Check if next node could be an ILOAD node
			if (node.getNext() == null
					|| !(node.getNext() instanceof VarInsnNode))
				continue;

			// Check ILOAD node for correctness
			VarInsnNode iloadNode = (VarInsnNode) node.getNext();
			if (iloadNode.getOpcode() != Opcodes.ILOAD
					|| iloadNode.var != 4)
				continue;

			// Check if next node could be an INVOKEVIRTUAL node
			if (iloadNode.getNext() == null
					|| !(iloadNode.getNext() instanceof MethodInsnNode))
				continue;

			// Check BIPUSH node for correctness
			MethodInsnNode invokeVirtualNode = (MethodInsnNode) iloadNode.getNext();
			if (invokeVirtualNode.getOpcode() != Opcodes.INVOKEVIRTUAL
					|| !invokeVirtualNode.owner.equals("pb")
					|| !invokeVirtualNode.name.equals("a")
					|| !invokeVirtualNode.desc.equals("(Lali;III)V"))
				continue;

			LabelNode trueLabelNode = new LabelNode(new Label()),
					afterFalseLabelNode = new LabelNode(new Label());

			InsnList insnList = new InsnList();

			// if
			insnList.add(new VarInsnNode(ALOAD, 1));
			insnList.add(new FieldInsnNode(GETFIELD, "pb", "bO", "I"));
			insnList.add(new LdcInsnNode(17));
			insnList.add(new JumpInsnNode(IF_ICMPEQ, trueLabelNode));

			// true
			insnList.add(new VarInsnNode(ALOAD, 0));
			insnList.add(new InsnNode(ICONST_1));
			insnList.add(new FieldInsnNode(PUTFIELD, "vl", "f", "Z"));
			insnList.add(new JumpInsnNode(GOTO, afterFalseLabelNode));

			// false
			insnList.add(trueLabelNode);
			insnList.add(new JumpInsnNode(GOTO, afterFalseLabelNode));

			// merge
			insnList.add(afterFalseLabelNode);

			instructions.insert(invokeVirtualNode, insnList);

			isInserted = true;
		}
		accept(mv);
	}
}