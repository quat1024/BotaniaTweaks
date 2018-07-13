package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;

public class SpectrolusOutputTweak extends Tweak {
	@Override
	public Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.block.subtile.generating.SubTileSpectrolus");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Patching the spectrolus's mana output...";
	}
	
	@Override
	public void doPatch(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("onUpdate")) {
				InsnList ins = method.instructions;
				
				//The first SIPUSH is the mana generating value
				for(int i = 0; i < ins.size(); i++) {
					AbstractInsnNode instruction = ins.get(i);
					if(instruction.getOpcode() == SIPUSH) {						
						MethodInsnNode callInstruction = new MethodInsnNode(INVOKESTATIC, getHooksClass(), "getSpectrolusManaPerWool", "()I", false);
						ins.insertBefore(instruction, callInstruction);
						ins.remove(instruction);
						return;
					}
				}
			}
		}
	}
}
