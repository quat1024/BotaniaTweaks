package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;
import quaternary.botaniatweaks.modules.shared.lib.GeneratingFlowers;

import java.util.Collection;

public class EverythingCanDecayTweak extends Tweak {
	@Override
	public Collection<String> computeAffectedClasses() {
		return ImmutableList.copyOf(GeneratingFlowers.getAllFlowerClassesMayOrMayNotExist());
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Patching the " + GeneratingFlowers.flowerNameFromClass(transformedName) + "'s decayability...";
	}
	
	@Override
	public void doPatch(String transformedName, ClassNode node) {
		//remove any existing isPassiveFlower nodes, if they exist already
		for(MethodNode method : node.methods) {
			if(method.name.equals("isPassiveFlower")) {
				node.methods.remove(method);
				break;
			}
		}
		
		MethodNode newPassiveMethod = new MethodNode(ACC_PUBLIC, "isPassiveFlower", "()Z", null, null);
		InsnList ins = newPassiveMethod.instructions;
		
		//return BotaniaTweakerHooks.shouldFlowerDecay("endoflame")
		ins.add(new LdcInsnNode(GeneratingFlowers.flowerNameFromClass(transformedName)));
		ins.add(new MethodInsnNode(INVOKESTATIC, getHooksClass(), "shouldFlowerDecay", "(Ljava/lang/String;)Z", false));
		ins.add(new InsnNode(IRETURN));
		
		node.methods.add(newPassiveMethod);
	}
}
