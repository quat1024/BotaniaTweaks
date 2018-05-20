package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.List;

public class EverythingApothecaryTweak extends Tweak {
	@Override
	List<String> getAffectedClassesImpl() {
		return ImmutableList.of("vazkii.botania.common.block.tile.TileAltar");
	}
	
	@Override
	String getName(String transformedName) {
		return "the petal apothecary";
	}
	
	@Override
	void patchImpl(String transformedName, ClassNode node) {
		for(MethodNode method : node.methods) {
			if(method.name.equals("getFlowerComponent")) {
				InsnList ins = method.instructions;
				
				for(int i = 0; i < ins.size(); i++) {
					AbstractInsnNode in = ins.get(i);
					if(in.getOpcode() == ARETURN) {
						AbstractInsnNode prev = in.getPrevious();
						
						//the local variables are:
						//* 0: this
						//* 1: ItemStack
						//* 2: IFlowerComponent
						
						//copy the passed-in ItemStack argument onto the stack
						VarInsnNode copyInsn = new VarInsnNode(ALOAD, 1);
						ins.insert(prev, copyInsn);
						
						//call the hook
						MethodInsnNode hook = new MethodInsnNode(INVOKESTATIC, getHooksClass(), "getFlowerComponent", "(Lvazkii/botania/api/recipe/IFlowerComponent;Lnet/minecraft/item/ItemStack;)Lvazkii/botania/api/recipe/IFlowerComponent;", false);
						ins.insert(copyInsn, hook);
						return;
					}
				}
			}
		}
	}
}
