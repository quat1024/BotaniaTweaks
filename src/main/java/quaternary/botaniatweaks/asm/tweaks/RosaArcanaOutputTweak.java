package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Collection;

public class RosaArcanaOutputTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.block.subtile.generating.SubTileArcaneRose");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Patching the arcanerose's XP orb flower output...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("onUpdate")) {
				InsnList insns = method.instructions;
				
				for(int i = 0; i < insns.size(); i++) {
					AbstractInsnNode instruction = insns.get(i);
					if(instruction.getOpcode() == BIPUSH && ((IntInsnNode) instruction).operand == 35) {
						MethodInsnNode hook = new MethodInsnNode(INVOKESTATIC, getHooksClass(), "getRosaArcanaXPOrbMana", "()I", false);
						insns.insertBefore(instruction, hook);
						insns.remove(instruction);
						return;
					}
				}
			}
		}
	}
}
