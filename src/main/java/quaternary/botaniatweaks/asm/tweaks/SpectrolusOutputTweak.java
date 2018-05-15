package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.List;

public class SpectrolusOutputTweak extends Tweak {
	@Override
	public List<String> getAffectedClassesImpl() {
		return ImmutableList.of("vazkii.botania.common.block.subtile.generating.SubTileSpectrolus");
	}
	
	@Override
	String getName(String transformedName) {
		return "the spectrolus's mana output";
	}
	
	@Override
	public void patchImpl(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("onUpdate")) {
				InsnList ins = method.instructions;
				
				//The first SIPUSH is the mana generating value
				for(int i=0; i < ins.size(); i++) {
					AbstractInsnNode instruction = ins.get(i);
					if(instruction.getOpcode() == SIPUSH) {
						addRidiculousLineNumber(ins, instruction);
						
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
