package quaternary.botaniatweaks.asm.tweaks;

import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import quaternary.botaniatweaks.asm.BotaniaTweakerTransformer;

import java.util.Collection;

public abstract class Tweak implements Opcodes {
	protected abstract Collection<String> computeAffectedClasses();
	abstract void doPatch(String transformedName, ClassNode node);
	abstract String getLogMessage(String transformedName);
	
	Collection<String> affectedClasses;
	
	public Collection<String> getAffectedClasses() {
		if(affectedClasses == null) affectedClasses = computeAffectedClasses();
		return affectedClasses;
	}
	
	public void patch(String transformedName, ClassNode node) {
		if(affectedClasses.contains(transformedName)) {
			log(getLogMessage(transformedName));
			doPatch(transformedName, node);
		}
	}
	
	static String getHooksClass() {
		return BotaniaTweakerTransformer.HOOKS;
	}
	
	static String getHooksClass(String plugin) {
		return BotaniaTweakerTransformer.HOOKS + '$' + plugin;
	}
	
	static void log(String message) {
		LogManager.getLogger("Botania Tweaks ASM").info(message);
	}
}
