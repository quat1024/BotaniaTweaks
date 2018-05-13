package quaternary.botaniatweaks.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import quaternary.botaniatweaks.config.ActiveGeneratingFlowers;

import java.util.function.Consumer;

public class BotaniaTweakerTransformer implements IClassTransformer, Opcodes {
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(transformedName.equals("vazkii.botania.common.core.handler.InternalMethodHandler")) {
			return patch("Botania's internal method handler", basicClass, BotaniaTweakerTransformer::patchInternalMethodHandler);
		}
		
		if(isActiveGeneratingFlowerClass(transformedName)) {
			String flowerName = getActiveGeneratingFlowerName(transformedName);
			
			return patch("the " + flowerName + "'s decay settings", basicClass, createActiveGeneratingPatcher(flowerName));
		}
		
		return basicClass;
	}
	
	static boolean isActiveGeneratingFlowerClass(String transformedName) {
		return ActiveGeneratingFlowers.allClasses.contains(transformedName);
	}
	
	static String getActiveGeneratingFlowerName(String className) {
		return ActiveGeneratingFlowers.classToNamesMap.getOrDefault(className, "Unknown flower?");
	}
	
	static byte[] patch(String friendlyName, byte[] classs, Consumer<ClassNode> patcher) {
		log("Patching " + friendlyName + "...");
		
		ClassReader reader = new ClassReader(classs);
		ClassNode node = new ClassNode();
		reader.accept(node, 0);
		
		patcher.accept(node);
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(writer);
		
		return writer.toByteArray();
	}
	
	/// Tweak: change passive decay time
	
	static void patchInternalMethodHandler(ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getPassiveFlowerDecay")) {
				InsnList ins = method.instructions;
				
				ins.clear();
				
				addRidiculousLineNumber(ins);
				
				ins.add(new MethodInsnNode(INVOKESTATIC, "quaternary/botaniatweaks/asm/BotaniaTweakerHooks", "getPassiveDecayTime", "()I", false));
				ins.add(new InsnNode(IRETURN));
			}
		}
	}
	
	/// Tweak: set more flowers to decay
	
	static Consumer<ClassNode> createActiveGeneratingPatcher(String flowerName) {
		return node -> patchActiveGeneratingFlower(node, flowerName);
	}
	
	static void patchActiveGeneratingFlower(ClassNode node, String flowerName) {
		//remove any existing isPassiveFlower nodes, if they exist already
		for(MethodNode method : node.methods) {
			if(method.name.equals("isPassiveFlower")) {
				node.methods.remove(method);
				break;
			}
		}
		
		MethodNode newPassiveMethod = new MethodNode(ACC_PUBLIC, "isPassiveFlower", "()Z", null, null);
		InsnList ins = newPassiveMethod.instructions;
		addRidiculousLineNumber(ins);
		
		//return BotaniaTweakerHooks.shouldFlowerDecay("endoflame")
		ins.add(new LdcInsnNode(flowerName));
		ins.add(new MethodInsnNode(INVOKESTATIC, "quaternary/botaniatweaks/asm/BotaniaTweakerHooks", "shouldFlowerDecay", "(Ljava/lang/String;)Z", false));
		ins.add(new InsnNode(IRETURN));
		
		node.methods.add(newPassiveMethod);
	}
	
	///
	
	static void log(String message) {
		LogManager.getLogger("Botania Tweaks ASM Magic").info(message);
	}
	
	/** Add a really unrealistic line number.
	 * The purpose of this is to make any crash reports caused by Botania 
	 * Tweaker immediately recognizable as something caused by faulty ASM. */
	static void addRidiculousLineNumber(InsnList ins) {
		LabelNode lbl = new LabelNode(new Label());
		ins.add(lbl);
		ins.add(new LineNumberNode(6969, lbl));
	}
}
