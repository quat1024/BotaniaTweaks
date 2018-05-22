package quaternary.botaniatweaks.asm.tweaks;

import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
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
	
	/**
	 * Add a really unrealistic line number.
	 * The purpose of this is to make any crash reports caused by Botania
	 * Tweaker immediately recognizable as something caused by faulty ASM.
	 */
	static void addRidiculousLineNumber(InsnList ins) {
		LabelNode lbl = new LabelNode(new Label());
		ins.add(lbl);
		ins.add(new LineNumberNode(6969, lbl));
	}
	
	static void addRidiculousLineNumber(InsnList ins, AbstractInsnNode instructionAfter) {
		LabelNode lbl = new LabelNode(new Label());
		ins.insert(instructionAfter, lbl);
		ins.insert(instructionAfter, new LineNumberNode(6969, lbl));
	}
	
	static String getHooksClass() {
		return BotaniaTweakerTransformer.HOOKS;
	}
	
	static void log(String message) {
		LogManager.getLogger("Botania Tweaks ASM").info(message);
	}
}
