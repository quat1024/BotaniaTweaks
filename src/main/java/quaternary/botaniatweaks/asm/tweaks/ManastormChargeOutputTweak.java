package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.List;

public class ManastormChargeOutputTweak extends Tweak {
	@Override
	public List<String> getAffectedClassesImpl() {
		return ImmutableList.of("vazkii.botania.common.entity.EntityManaStorm");
	}
	
	@Override
	String getName(String transformedName) {
		return "the manastorm charge's mana output";
	}
	
	@Override
	public void patchImpl(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("spawnBurst")) {
				InsnList ins = method.instructions;
				
				//the first BIPUSH is the setMana call.
				for(int i = 0; i < ins.size(); i++) {
					AbstractInsnNode instruction = ins.get(i);
					if(instruction.getOpcode() == BIPUSH) {						
						MethodInsnNode callInstruction = new MethodInsnNode(INVOKESTATIC, getHooksClass(), "getManastormBurstMana", "()I", false);
						ins.insertBefore(instruction, callInstruction);
						ins.remove(instruction);
						break;
					}
				}
				
				//the first SIPUSH is the setStartingMana call.
				//not super super sure what it is for. but hey!
				for(int i = 0; i < ins.size(); i++) {
					AbstractInsnNode instruction = ins.get(i);
					if(instruction.getOpcode() == SIPUSH) {						
						MethodInsnNode callInstruction = new MethodInsnNode(INVOKESTATIC, getHooksClass(), "getManastormBurstStartingMana", "()I", false);
						ins.insertBefore(instruction, callInstruction);
						ins.remove(instruction);
						break;
					}
				}
				
				//and the first FCONST_1 is the setManaLossPerTick call.
				for(int i = 0; i < ins.size(); i++) {
					AbstractInsnNode instruction = ins.get(i);
					if(instruction.getOpcode() == FCONST_1) {						
						MethodInsnNode callInstruction = new MethodInsnNode(INVOKESTATIC, getHooksClass(), "getManastormBurstLossjpgPerTick", "()F", false);
						ins.insertBefore(instruction, callInstruction);
						ins.remove(instruction);
						break;
					}
				}
				break;
				//TODO: maybe think about passing the entitymanaburst itself to a hook and just fixing it up w/ 1 method call
			}
		}
	}
}
