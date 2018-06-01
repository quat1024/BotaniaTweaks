package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.ListIterator;

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
				
				ListIterator<AbstractInsnNode> inserator = ins.iterator();
				while(inserator.hasNext()) {
					AbstractInsnNode in = inserator.next();
					if(in.getOpcode() == ARETURN) {
						inserator.remove();
						
						inserator.add(new VarInsnNode(ALOAD, 1));
						inserator.add(new MethodInsnNode(INVOKESTATIC, getHooksClass(), "getFlowerComponent", "(Lvazkii/botania/api/recipe/IFlowerComponent;Lnet/minecraft/item/ItemStack;)Lvazkii/botania/api/recipe/IFlowerComponent;", false));
						inserator.add(new InsnNode(ARETURN));
						
						return;
					}
				}
			}
		}
	}
}
