package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;
import quaternary.botaniatweaks.config.ActiveGeneratingFlowers;

import java.util.List;

public class EverythingCanDecayTweak extends Tweak {
	@Override
	public List<String> getAffectedClassesImpl() {
		return ImmutableList.copyOf(ActiveGeneratingFlowers.classToNamesMap.keySet());
	}
	
	@Override
	String getName(String transformedName) {
		return "the " + getFlowerFromClassName(transformedName) + "'s decayability";
	}
	
	@Override
	public void patchImpl(String transformedName, ClassNode node) {
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
		ins.add(new LdcInsnNode(getFlowerFromClassName(transformedName)));
		ins.add(new MethodInsnNode(INVOKESTATIC, getHooksClass(), "shouldFlowerDecay", "(Ljava/lang/String;)Z", false));
		ins.add(new InsnNode(IRETURN));
		
		node.methods.add(newPassiveMethod);
	}
	
	static String getFlowerFromClassName(String className) {
		return ActiveGeneratingFlowers.classToNamesMap.getOrDefault(className, "Unknown flower?");
	}
}
