package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;
import quaternary.botaniatweaks.modules.shared.lib.GeneratingFlowers;

import java.util.Collection;

public class EntryExitPointsTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return ImmutableList.copyOf(GeneratingFlowers.getAllFlowerClassesMayOrMayNotExist());
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Instrumenting entry/exit points for the " + GeneratingFlowers.flowerNameFromClass(transformedName) + " flower...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			//Quick note: these are *sub*tileentities, not TileEntities
			//The name is always "onUpdate" and not some SRG nonsense
			//since it's really not part of Minecraft, despite sharing the same name.
			if(method.name.equals("onUpdate")) {
				
				method.instructions.insertBefore(method.instructions.getFirst(), genInsnListHook(transformedName, "beforeFlowerSuper"));
				
				boolean addedAfterSuperHook = false;
				
				for(int insIndex = 0; insIndex < method.instructions.size(); insIndex++) {
					AbstractInsnNode instruction = method.instructions.get(insIndex);
					
					//Add a call to beginManaStatSection after the super.onUpdate call
					if(!addedAfterSuperHook && instruction instanceof MethodInsnNode) {
						MethodInsnNode methodInstruction = (MethodInsnNode) instruction;
						if(methodInstruction.getOpcode() == INVOKESPECIAL &&
										methodInstruction.name.equals("onUpdate") && 
										methodInstruction.owner.equals("vazkii/botania/api/subtile/SubTileGenerating")) {
							
							InsnList hook = genInsnListHook(transformedName, "afterFlowerSuper");
							
							method.instructions.insert(methodInstruction, hook);
							addedAfterSuperHook = true;
						}
					}
					
					//Add a call to endManaStatSection before every return point
					if(instruction.getOpcode() == RETURN) {
						InsnList hook = genInsnListHook(transformedName, "beforeFlowerReturn");
						
						insIndex += hook.size(); //Skip over this RETURN ins
						
						method.instructions.insertBefore(instruction, hook);
					}
				}
				
				return;
			}
		}
	}
	
	private static InsnList genInsnListHook(String transformedName, String hookMethodName) {
		String bytecodeClassName = transformedName.replace('.', '/');
		
		InsnList hook = new InsnList();
		
		hook.add(new LdcInsnNode(GeneratingFlowers.flowerNameFromClass(transformedName)));
		hook.add(new VarInsnNode(ALOAD, 0));
		hook.add(new InsnNode(DUP));
		hook.add(new FieldInsnNode(GETFIELD, bytecodeClassName, "mana", "I"));
		hook.add(new MethodInsnNode(INVOKESTATIC, getHooksClass(), hookMethodName, "(Ljava/lang/String;Lvazkii/botania/api/subtile/SubTileGenerating;I)V", false));
		
		return hook;
	}
}
