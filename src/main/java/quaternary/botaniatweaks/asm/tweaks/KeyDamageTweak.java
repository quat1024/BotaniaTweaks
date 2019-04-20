package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Collection;

public class KeyDamageTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.entity.EntityBabylonWeapon");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Patching Key of the King's Law damage output...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("func_70071_h_") || method.name.equals("onUpdate")) {
				InsnList insns = method.instructions;
				
				//the first number 20 is an unrelated call relating to playing sounds.
				//However, it's pushed using a BIPUSH since it's an int.
				//The 20s relating to damage are loaded on using an LDC instruction.
				//So there is no cross-contamination if I just stomp on all the 20s.
				for(int i = 0; i < insns.size(); i++) {
					AbstractInsnNode insn = insns.get(i);
					if(insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst.equals(20f)) {
						insns.insertBefore(insn, new MethodInsnNode(
							INVOKESTATIC,
							getHooksClass(),
							"getKeyDamage",
							"()F",
							false
						));
						
						insns.remove(insn);
					}
				}
			}
		}
	}
}
