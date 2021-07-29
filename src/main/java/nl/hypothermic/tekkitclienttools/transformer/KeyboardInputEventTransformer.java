package nl.hypothermic.tekkitclienttools.transformer;

import nl.hypothermic.htf.api.Gateway;
import nl.hypothermic.htf.api.MethodTransformer;
import nl.hypothermic.htf.utils.GatewayCreator;
import nl.hypothermic.tekkitclienttools.AddonGatewayIdIndex;
import nl.hypothermic.tekkitclienttools.gui.GuiManager;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

@MethodTransformer(targetClass = "net/minecraft/client/Minecraft", targetMethodName = "k", targetMethodDescription = "()V")
@SuppressWarnings("unused")
public class KeyboardInputEventTransformer extends MethodNode implements Opcodes {

	public KeyboardInputEventTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Gateway(id = AddonGatewayIdIndex.KEYBOARD_INPUT, inputType = int.class)
	public static Object onKeyPress(Object data) {
		GuiManager.getInstance().onKeyPress((Integer) data);
		return null;
	}

	@Override
	public void visitEnd() {
		ListIterator<AbstractInsnNode> iterator = this.instructions.iterator();
		boolean isInserted = false;

		/*
		 * Check for the combination of the following bytecode entries:
		 *
		 * INVOKESTATIC org/lwjgl/input/Keyboard.getEventKey ()I
		 * BIPUSH 87
		 *
		 * These should be unique because KEY_F11 (87) is only used once
		 */

		while (iterator.hasNext() && !isInserted) {
			AbstractInsnNode node = iterator.next();

			// Check if next node could be an INVOKESTATIC node
			if (node.getNext() == null
				|| !(node.getNext() instanceof MethodInsnNode))
				continue;

			// Check INVOKESTATIC node for correctness
			MethodInsnNode invokeStaticNode = (MethodInsnNode) node.getNext();
			if (invokeStaticNode.getOpcode() != Opcodes.INVOKESTATIC
				|| !invokeStaticNode.owner.equals("org/lwjgl/input/Keyboard")
				|| !invokeStaticNode.name.equals("getEventKey")
				|| !invokeStaticNode.desc.equals("()I"))
				continue;

			// Check if next node could be a BIPUSH node
			if (invokeStaticNode.getNext() == null
				|| !(invokeStaticNode.getNext() instanceof IntInsnNode))
				continue;

			// Check BIPUSH node for correctness
			IntInsnNode intInsnNode = (IntInsnNode) invokeStaticNode.getNext();
			if (intInsnNode.getOpcode() != BIPUSH
					|| intInsnNode.operand != 87)
				continue;

			// Insert the keypress capture code

			InsnList eventKeyLoadInstructions = new InsnList();
			eventKeyLoadInstructions.add(new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"org/lwjgl/input/Keyboard",
					"getEventKey",
					"()I")
			);
			instructions.insert(
					node,
					GatewayCreator.create(
							GatewayCreator.ref(KeyboardInputEventTransformer.class, "onKeyPress"),
							eventKeyLoadInstructions
					)
			);

			isInserted = true;
		}
		accept(mv);
	}
}
