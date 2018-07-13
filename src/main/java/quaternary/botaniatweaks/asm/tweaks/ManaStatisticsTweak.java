package quaternary.botaniatweaks.asm.tweaks;

import org.objectweb.asm.tree.*;
import quaternary.botaniatweaks.etc.lib.GeneratingFlowers;

import java.util.Collection;
import java.util.ListIterator;

public class ManaStatisticsTweak extends Tweak {
	@Override
	protected Collection<String> computeAffectedClasses() {
		return GeneratingFlowers.flowerClasses;
	}
	
	@Override
	String getLogMessage(String transformedName) {
		return "Adding managen statistics for the " + GeneratingFlowers.getFlowerName(transformedName) + " flower...";
	}
	
	@Override
	void doPatch(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			//Quick note: these are *sub*tileentities, not TileEntities
			//The name is always "onUpdate" and not some SRG nonsense
			//since it's really not part of Minecraft, despite sharing the same name.
			if(method.name.equals("onUpdate")) {
				
				boolean addedAfterSuperHook = false;
				
				for(int insIndex = 0; insIndex < method.instructions.size(); insIndex++) {
					AbstractInsnNode instruction = method.instructions.get(insIndex);
					
					//Add a call to beginManaStatSection after the super.onUpdate call
					if(!addedAfterSuperHook && instruction instanceof MethodInsnNode) {
						MethodInsnNode methodInstruction = (MethodInsnNode) instruction;
						if(methodInstruction.getOpcode() == INVOKESPECIAL &&
										methodInstruction.name.equals("onUpdate") && 
										methodInstruction.owner.equals("vazkii/botania/api/subtile/SubTileGenerating")) {
							
							InsnList hook = genInsnListHook(transformedName, "beginManaStatSection");
							
							method.instructions.insert(methodInstruction, hook);
							addedAfterSuperHook = true;
						}
					}
					
					//Add a call to endManaStatSection before every return point
					if(instruction.getOpcode() == RETURN) {
						InsnList hook = genInsnListHook(transformedName, "endManaStatSection");
						
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
		
		hook.add(new LdcInsnNode(GeneratingFlowers.getFlowerName(transformedName)));
		hook.add(new VarInsnNode(ALOAD, 0));
		hook.add(new InsnNode(DUP));
		hook.add(new FieldInsnNode(GETFIELD, bytecodeClassName, "mana", "I"));
		hook.add(new MethodInsnNode(INVOKESTATIC, getHooksClass(), hookMethodName, "(Ljava/lang/String;Lvazkii/botania/api/subtile/SubTileGenerating;I)V", false));
		
		return hook;
	}
}
