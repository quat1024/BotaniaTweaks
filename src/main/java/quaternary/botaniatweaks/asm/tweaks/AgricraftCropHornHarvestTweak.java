package quaternary.botaniatweaks.asm.tweaks;

import com.google.common.collect.ImmutableList;
import org.objectweb.asm.tree.*;

import java.util.List;

public class AgricraftCropHornHarvestTweak extends Tweak {
	@Override
	List<String> getAffectedClassesImpl() {
		return ImmutableList.of("com.infinityraider.agricraft.blocks.BlockCrop");
	}
	
	@Override
	String getName(String transformedName) {
		return "Agricraft crops";
	}
	
	@Override
	void patchImpl(String transformedName, ClassNode node) {
		String hornHarvestable = "vazkii/botania/api/item/IHornHarvestable";
		if(node.interfaces.contains(hornHarvestable)) {
			log("Looks like AgriCraft already implements IHornHarvestable now!");
			log("Please, report this as a bug to Botania Tweaks. I can remove this patch now.");
			return;
		}
		
		//Add the interface
		node.interfaces.add(hornHarvestable);
		
		//Actually override the methods of said interface
		MethodNode canHornHarvestMethod = new MethodNode(ACC_PUBLIC, "canHornHarvest", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Lvazkii/botania/api/item/IHornHarvestable$EnumHornType;)Z", null, null);
		
		canHornHarvestMethod.instructions.add(new VarInsnNode(ALOAD, 4));
		canHornHarvestMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, getHooksClass() + "Agricraft", "canHornHarvestCrop", "(Lvazkii/botania/api/item/IHornHarvestable$EnumHornType;)Z", false));
		canHornHarvestMethod.instructions.add(new InsnNode(IRETURN));
		
		node.methods.add(canHornHarvestMethod);
	}
	
	
}
