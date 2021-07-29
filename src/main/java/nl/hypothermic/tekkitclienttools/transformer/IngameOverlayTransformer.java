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

@MethodTransformer(targetClass = "aiy", targetMethodName = "a", targetMethodDescription = "(FZII)V")
@SuppressWarnings("unused")
public class IngameOverlayTransformer extends MethodNode implements Opcodes {

	public IngameOverlayTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Gateway(id = AddonGatewayIdIndex.INGAME_OVERLAY_RENDER)
	public static Object onRender(Object data) {
		GuiManager.getInstance().render();
		return null;
	}

	@Override
	public void visitEnd() {
		ListIterator<AbstractInsnNode> iterator = this.instructions.iterator();
		boolean inserted = false;

		while (iterator.hasNext() && !inserted) {
			AbstractInsnNode node = iterator.next();

			if (node.getNext() == null)
				continue;

			if (!(node.getNext() instanceof VarInsnNode))
				continue;

			VarInsnNode aloadNode = (VarInsnNode) node.getNext();

			if (aloadNode.getOpcode() != ALOAD || aloadNode.var != 0)
				continue;

			if (!(aloadNode.getNext() instanceof FieldInsnNode))
				continue;

			FieldInsnNode getMinecraftNode = ((FieldInsnNode) aloadNode.getNext());

			if (getMinecraftNode.getOpcode() != Opcodes.GETFIELD
				|| !getMinecraftNode.owner.equals("aiy")
				|| !getMinecraftNode.name.equals("i")
				|| !getMinecraftNode.desc.equals("Lnet/minecraft/client/Minecraft;"))
				continue;

			if (!(getMinecraftNode.getNext() instanceof FieldInsnNode))
				continue;

			FieldInsnNode getKiNode = ((FieldInsnNode) getMinecraftNode.getNext());

			if (getKiNode.getOpcode() != Opcodes.GETFIELD
					|| !getKiNode.owner.equals("net/minecraft/client/Minecraft")
					|| !getKiNode.name.equals("c")
					|| !getKiNode.desc.equals("Lki;"))
				continue;

			if (!(getKiNode.getNext().getNext() instanceof JumpInsnNode))
				continue;

			JumpInsnNode jumpNode = ((JumpInsnNode) getKiNode.getNext().getNext());

			if (jumpNode.getOpcode() != Opcodes.IFNE)
				continue;

			instructions.insert(
					node,
					GatewayCreator.create(
						GatewayCreator.ref(IngameOverlayTransformer.class, "onRender")
					)
			);

			inserted = true;
		}
		accept(mv);
	}
}
