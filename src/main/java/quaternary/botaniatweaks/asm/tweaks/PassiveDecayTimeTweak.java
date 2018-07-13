package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.Collection;

public class PassiveDecayTimeTweak extends Tweak {
	@Override
	public Collection<String> computeAffectedClasses() {
		return ImmutableList.of("vazkii.botania.common.core.handler.InternalMethodHandler");
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Patching the passive decay timer...";
	}
	
	@Override
	public void doPatch(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getPassiveFlowerDecay")) {
				InsnList ins = method.instructions;
				
				ins.clear();
				
				ins.add(new MethodInsnNode(INVOKESTATIC, getHooksClass(), "getPassiveDecayTime", "()I", false));
				ins.add(new InsnNode(IRETURN));
				break;
			}
		}
	}
}
