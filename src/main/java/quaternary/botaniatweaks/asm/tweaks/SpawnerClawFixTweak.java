package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;
import java.util.ListIterator;

public class SpawnerClawFixTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.block.tile.TileSpawnerClaw");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Backporting Hubry's life imbuer fix...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		//Spawner claws (life imbuers) have a similar mana-overconsumption problem to livingwood avatars.
		//See AvatarFixTweak for more details.
		
		for(MethodNode method : node.methods) {
			if(method.name.equals("recieveMana")) {
				InsnList insns = method.instructions;
				for(int i = 0; i < insns.size(); i++) {
					AbstractInsnNode insn = insns.get(i);
					if(insn.getOpcode() == SIPUSH && ((IntInsnNode) insn).operand == 160) {
						insns.insertBefore(insn, new MethodInsnNode(
							INVOKESTATIC,
							getHooksClass(),
							"getSpawnerClawMax",
							"()I",
							false
						));
						insns.remove(insn);
						
						return;
					}
				}
			}
		}
	}
}
