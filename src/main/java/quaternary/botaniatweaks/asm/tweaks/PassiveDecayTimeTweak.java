package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.List;

public class PassiveDecayTimeTweak extends Tweak {
	@Override
	public List<String> getAffectedClassesImpl() {
		return ImmutableList.of("vazkii.botania.common.core.handler.InternalMethodHandler");
	}
	
	@Override
	String getName(String transformedName) {
		return "the passive decay timer";
	}
	
	@Override
	public void patchImpl(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getPassiveFlowerDecay")) {
				InsnList ins = method.instructions;
				
				ins.clear();
				
				addRidiculousLineNumber(ins);
				
				ins.add(new MethodInsnNode(INVOKESTATIC, getHooksClass(), "getPassiveDecayTime", "()I", false));
				ins.add(new InsnNode(IRETURN));
				break;
			}
		}
	}
}
