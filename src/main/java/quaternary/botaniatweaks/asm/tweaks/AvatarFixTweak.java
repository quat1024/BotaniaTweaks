package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;
import java.util.ListIterator;

public class AvatarFixTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.block.tile.TileAvatar");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Backporting Hubry's livingwood avatar fixes...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		nextMethod:
		for(MethodNode method : node.methods) {
			//part 1 of this fix: The field "TAG_MANA" is mistakenly set to "ticksElapsed",
			//instead of "mana", due to a copy-paste error. I'd love to just reflect the field but it gets inlined.
			if(method.name.equals("writePacketNBT") || method.name.equals("readPacketNBT")) {
				InsnList insns = method.instructions;
				int foundLdcCount = 0;
				
				for(int i = 0; i < insns.size(); i++) {
					AbstractInsnNode insn = insns.get(i);
					if(insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst.equals("ticksElapsed")) {
						foundLdcCount++;
						
						//It's the second interaction with "ticksElapsed", the first one is the intended ticksElapsed field.
						if(foundLdcCount == 2) {
							insns.insertBefore(insn, new MethodInsnNode(
								INVOKESTATIC,
								getHooksClass(),
								"getAvatarManaField",
								"()Ljava/lang/String;",
								false
							));
							insns.remove(insn);
							continue nextMethod;
						}
					}
				}
			}
			
			//Part 2 of this fix: The avatar bites off more than it can chew.
			//If the mana is less than 6400, a spreader will fire a mana burst containing a lot of mana.
			//But the avatar will only store up to 6400 mana.
			//This means the avatar gets an entire mana burst, consumes 1 mana, then is available for more mana bursts
			//making naive spreader-to-avatar setups much more expensive than they need to be.
			if(method.name.equals("recieveMana")) {
				InsnList insns = method.instructions;
				for(int i = 0; i < insns.size(); i++) {
					AbstractInsnNode insn = insns.get(i);
					if(insn.getOpcode() == SIPUSH && ((IntInsnNode) insn).operand == 6400) {
						insns.insertBefore(insn, new MethodInsnNode(
							INVOKESTATIC,
							getHooksClass(),
							"getAvatarMax",
							"()I",
							false
						));
						insns.remove(insn);
						continue nextMethod;
					}
				}
			}
		}
	}
}
