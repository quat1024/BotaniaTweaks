package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;

public class CreativeManaPoolSizeTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.block.tile.mana.TilePool");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Bumping the size of the creative mana pool...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getCurrentMana")) {
				for(int insIndex = 0; insIndex < method.instructions.size(); insIndex++) {
					
					AbstractInsnNode instruction = method.instructions.get(insIndex);
					if(instruction instanceof LdcInsnNode) {
						
						LdcInsnNode ldc = (LdcInsnNode) instruction;
						if(ldc.cst.equals(1_000_000)) {
							
							AbstractInsnNode hook = new MethodInsnNode(INVOKESTATIC, getHooksClass(), "getCreativePoolSize", "()I", false);
							method.instructions.insert(instruction, hook);
							method.instructions.remove(instruction);
							return;
						}
					}
				}
			}
		}
	}
}
