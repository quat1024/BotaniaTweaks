package quaternary.botaniatweaks.asm;

import com.google.common.collect.ImmutableList;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import scala.actors.threadpool.Arrays;

import java.util.List;
import java.util.function.Consumer;

public class BotaniaTweakerTransformer implements IClassTransformer, Opcodes {
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(transformedName.equals("vazkii.botania.common.core.handler.InternalMethodHandler")) {
			return patch("Botania's internal method handler", basicClass, BotaniaTweakerTransformer::patchInternalMethodHandler);
		}
		
		if(isNonPassiveGeneratingFlowerClass(transformedName)) {
			String flowerName = getFriendlyNameFromNonPassiveClass(transformedName);
			
			return patch("the " + flowerName + " flower", basicClass, createNonPassivePatcher(flowerName));
		}
		
		return basicClass;
	}
	
	//I feel like using a whitelist is the safest way
	static List<String> nonPassiveList = ImmutableList.of(
		"vazkii.botania.common.block.subtile.generating.SubTileArcaneRose",
		"vazkii.botania.common.block.subtile.generating.SubTileDandelifeon",
		"vazkii.botania.common.block.subtile.generating.SubTileEndoflame",
		"vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum",
		"vazkii.botania.common.block.subtile.generating.SubTileGourmaryllis",
		"vazkii.botania.common.block.subtile.generating.SubTileKekimurus",
		"vazkii.botania.common.block.subtile.generating.SubTileMunchdew",
		"vazkii.botania.common.block.subtile.generating.SubTileNarslimmus",
		"vazkii.botania.common.block.subtile.generating.SubTileRafflowsia",
		"vazkii.botania.common.block.subtile.generating.SubTileShulkMeNot",
		"vazkii.botania.common.block.subtile.generating.SubTileSpectrolus",
		"vazkii.botania.common.block.subtile.generating.SubTileThermalily"
	);
	
	static boolean isNonPassiveGeneratingFlowerClass(String transformedName) {
		return nonPassiveList.contains(transformedName);
	}
	
	//this is silly.
	static String getFriendlyNameFromNonPassiveClass(String className) {
		if(className.startsWith("vazkii.botania")) {
			String[] blah = className.split("\\.");
			return blah[blah.length - 1].replace("SubTile", "");
		}
		
		return "Unknown flower";
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
	
	static Consumer<ClassNode> createNonPassivePatcher(String flowerName) {
		return node -> patchNonPassiveFlower(node, flowerName);
	}
	
	static void patchNonPassiveFlower(ClassNode node, String flowerName) {
		//remove any existing isPassiveFlower nodes, if they exist already
		for(MethodNode method : node.methods) {
			if(method.name.equals("isPassiveFlower")) {
				node.methods.remove(method);
				break;
			}
		}
		
		//todo temp for testing: just return true
		MethodNode newPassiveMethod = new MethodNode(ACC_PUBLIC, "isPassiveFlower", "()Z", null, null);
		InsnList ins = newPassiveMethod.instructions;
		addRidiculousLineNumber(ins);
		
		//Hardcode this flower name into the method
		ins.add(new LdcInsnNode(flowerName));
		ins.add(new MethodInsnNode(INVOKESTATIC, "quaternary/botaniatweaks/asm/BotaniaTweakerHooks", "shouldFlowerDecay", "(Ljava/lang/String;)Z", false));
		ins.add(new InsnNode(IRETURN));
		
		node.methods.add(newPassiveMethod);
	}
	
	///
	
	static void log(String message) {
		System.out.println("[Botania Tweaks ASM Magic] " + message);
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
