package quaternary.botaniatweaks.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import quaternary.botaniatweaks.config.ActiveGeneratingFlowers;

import java.util.function.Consumer;

public class BotaniaTweakerTransformer implements IClassTransformer, Opcodes {
	static final String HOOKS =  "quaternary/botaniatweaks/asm/BotaniaTweakerHooks";
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		ClassNode node = null;
		
		if(transformedName.equals("vazkii.botania.common.core.handler.InternalMethodHandler")) {
			node = patch("Botania's internal method handler", basicClass, BotaniaTweakerTransformer::patchInternalMethodHandler);
		}
		
		if(isActiveGeneratingFlowerClass(transformedName)) {
			String flowerName = getActiveGeneratingFlowerName(transformedName);
			
			node = patch("the " + flowerName + "'s decay settings", basicClass, createActiveGeneratingPatcher(flowerName));
		}
		
		if(transformedName.equals("vazkii.botania.common.entity.EntityManaStorm")) {
			node = patch("the manastorm charge", basicClass, BotaniaTweakerTransformer::patchManastormEntity);
		}
		
		if(transformedName.equals("vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum")) {
			node = patch("the entropinnyum's mana output", basicClass, BotaniaTweakerTransformer::patchEntropinnyum);
		}
		
		if(transformedName.equals("vazkii.botania.common.block.subtile.generating.SubTileSpectrolus")) {
			node = patch("the spectrolus's mana output", basicClass, BotaniaTweakerTransformer::patchSpectrolus);
		}
		
		if(node != null) {
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			node.accept(writer);
			
			return writer.toByteArray();
		}
		
		return basicClass;
	}
	
	static boolean isActiveGeneratingFlowerClass(String transformedName) {
		return ActiveGeneratingFlowers.allClasses.contains(transformedName);
	}
	
	static String getActiveGeneratingFlowerName(String className) {
		return ActiveGeneratingFlowers.classToNamesMap.getOrDefault(className, "Unknown flower?");
	}
	
	static ClassNode patch(String friendlyName, byte[] classs, Consumer<ClassNode> patcher) {
		log("Patching " + friendlyName + "...");
		
		ClassReader reader = new ClassReader(classs);
		ClassNode node = new ClassNode();
		reader.accept(node, 0);
		
		patcher.accept(node);
		
		return node;
	}
	
	/// Tweak: change passive decay time
	
	static void patchInternalMethodHandler(ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getPassiveFlowerDecay")) {
				InsnList ins = method.instructions;
				
				ins.clear();
				
				addRidiculousLineNumber(ins);
				
				ins.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "getPassiveDecayTime", "()I", false));
				ins.add(new InsnNode(IRETURN));
				break;
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
		ins.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "shouldFlowerDecay", "(Ljava/lang/String;)Z", false));
		ins.add(new InsnNode(IRETURN));
		
		node.methods.add(newPassiveMethod);
	}
	
	/// Tweak: buff mana output of the Manastorm Charge, making the "manastorm reactor" a profitable build
	
	static void patchManastormEntity(ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("spawnBurst")) {
				InsnList ins = method.instructions;
				
				//the first BIPUSH is the setMana call.
				for(int i=0; i < ins.size(); i++) {
					AbstractInsnNode instruction = ins.get(i);
					if(instruction.getOpcode() == BIPUSH) {
						addRidiculousLineNumber(ins, instruction);
						
						MethodInsnNode callInstruction = new MethodInsnNode(INVOKESTATIC, HOOKS, "getManastormBurstMana", "()I", false);
						ins.insertBefore(instruction, callInstruction);
						ins.remove(instruction);
						break;
					}
				}
				
				//the first SIPUSH is the setStartingMana call.
				//not super super sure what it is for. but hey!
				for(int i=0; i < ins.size(); i++) {
					AbstractInsnNode instruction = ins.get(i);
					if(instruction.getOpcode() == SIPUSH) {
						addRidiculousLineNumber(ins, instruction);
						
						MethodInsnNode callInstruction = new MethodInsnNode(INVOKESTATIC, HOOKS, "getManastormBurstStartingMana", "()I", false);
						ins.insertBefore(instruction, callInstruction);
						ins.remove(instruction);
						break;
					}
				}
				
				//and the first FCONST_1 is the setManaLossPerTick call.
				for(int i=0; i < ins.size(); i++) {
					AbstractInsnNode instruction = ins.get(i);
					if(instruction.getOpcode() == FCONST_1) {
						addRidiculousLineNumber(ins, instruction);
						
						MethodInsnNode callInstruction = new MethodInsnNode(INVOKESTATIC, HOOKS, "getManastormBurstLossjpgPerTick", "()F", false);
						ins.insertBefore(instruction, callInstruction);
						ins.remove(instruction);
						break;
					}
				}
				
				//TODO: maybe think about passing the entitymanaburst itself to a hook and just fixing it up w/ 1 method call
			}
		}
	}
	
	/// Tweak: buff entropinnyum
	
	static void patchEntropinnyum(ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getMaxMana")) {
				InsnList ins = method.instructions;
				
				ins.clear();
				
				addRidiculousLineNumber(ins);
				
				ins.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "getEntropinnyumMaxMana", "()I", false));
				ins.add(new InsnNode(IRETURN));
				break;
			}
		}
	}
	
	/// Tweak: buff spectrolus
	
	static void patchSpectrolus(ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("onUpdate")) {
				InsnList ins = method.instructions;
				
				//The first SIPUSH is the mana generating value
				for(int i=0; i < ins.size(); i++) {
					AbstractInsnNode instruction = ins.get(i);
					if(instruction.getOpcode() == SIPUSH) {
						addRidiculousLineNumber(ins, instruction);
						
						MethodInsnNode callInstruction = new MethodInsnNode(INVOKESTATIC, HOOKS, "getSpectrolusManaPerWool", "()I", false);
						ins.insertBefore(instruction, callInstruction);
						ins.remove(instruction);
						break;
					}
				}
			}
		}
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
	
	static void addRidiculousLineNumber(InsnList ins, AbstractInsnNode instructionAfter) {
		LabelNode lbl = new LabelNode(new Label());
		ins.insert(instructionAfter, lbl);
		ins.insert(instructionAfter, new LineNumberNode(6969, lbl));
	}
}
