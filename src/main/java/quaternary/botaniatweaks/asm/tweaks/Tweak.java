package quaternary.botaniatweaks.asm.tweaks;

import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import quaternary.botaniatweaks.asm.BotaniaTweakerTransformer;

import java.util.List;

public abstract class Tweak implements Opcodes {
	List<String> affectedClasses;
	
	abstract List<String> getAffectedClassesImpl();
	
	abstract void patchImpl(String transformedName, ClassNode node);
	
	abstract String getName(String transformedName);
	
	public List<String> getAffectedClasses() {
		if(affectedClasses == null) affectedClasses = getAffectedClassesImpl();
		return affectedClasses;
	}
	
	public void patch(String transformedName, ClassNode node) {
		log("Patching " + getName(transformedName) + "...");
		patchImpl(transformedName, node);
	}
	
	static String getHooksClass() {
		return BotaniaTweakerTransformer.HOOKS;
	}
	
	static void log(String message) {
		LogManager.getLogger("Botania Tweaks ASM").info(message);
	}
}
