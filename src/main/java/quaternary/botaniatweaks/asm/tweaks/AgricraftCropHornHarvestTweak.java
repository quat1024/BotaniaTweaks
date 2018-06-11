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
		return "Agricraft crop blocks";
	}
	
	@Override
	void patchImpl(String transformedName, ClassNode node) {
		String hornHarvestable = "vazkii/botania/api/item/IHornHarvestable";
		if(node.interfaces.contains(hornHarvestable)) {
			log("Looks like AgriCraft already implements IHornHarvestable now!");
			log("Please, report this as a bug to Botania Tweaks. I can remove this patch.");
			return;
		}
		
		//Add the interface
		node.interfaces.add(hornHarvestable);
		
		//Override the methods of said interface
		MethodNode canHornHarvestMethod = new MethodNode(ACC_PUBLIC, "canHornHarvest", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Lvazkii/botania/api/item/IHornHarvestable$EnumHornType;)Z", null, null);
		
		canHornHarvestMethod.instructions.add(new VarInsnNode(ALOAD, 4)); //EnumHornType
		canHornHarvestMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, getHooksClass("Agricraft"), "canHornHarvestAgriCrop", "(Lvazkii/botania/api/item/IHornHarvestable$EnumHornType;)Z", false));
		canHornHarvestMethod.instructions.add(new InsnNode(IRETURN));
		
		node.methods.add(canHornHarvestMethod);
		
		MethodNode hasSpecialHornHarvestMethod = new MethodNode(ACC_PUBLIC, "hasSpecialHornHarvest", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Lvazkii/botania/api/item/IHornHarvestable$EnumHornType;)Z", null, null);
		
		hasSpecialHornHarvestMethod.instructions.add(new VarInsnNode(ALOAD, 4)); //EnumHornType
		hasSpecialHornHarvestMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, getHooksClass("Agricraft"), "hasSpecialHornHarvestAgriCrop", "(Lvazkii/botania/api/item/IHornHarvestable$EnumHornType;)Z", false));
		hasSpecialHornHarvestMethod.instructions.add(new InsnNode(IRETURN));
		
		node.methods.add(hasSpecialHornHarvestMethod);
		
		MethodNode harvestByHornMethod = new MethodNode(ACC_PUBLIC, "harvestByHorn", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Lvazkii/botania/api/item/IHornHarvestable$EnumHornType;)V", null, null);
		
		harvestByHornMethod.instructions.add(new VarInsnNode(ALOAD, 0)); //BlockCrop
		harvestByHornMethod.instructions.add(new VarInsnNode(ALOAD, 1)); //World
		harvestByHornMethod.instructions.add(new VarInsnNode(ALOAD, 2)); //BlockPos
		harvestByHornMethod.instructions.add(new VarInsnNode(ALOAD, 4)); //EnumHornType
		harvestByHornMethod.instructions.add(new MethodInsnNode(INVOKESTATIC, getHooksClass("Agricraft"), "harvestByHornAgriCrop", "(Lcom/infinityraider/agricraft/blocks/BlockCrop;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lvazkii/botania/api/item/IHornHarvestable$EnumHornType;)V", false));
		harvestByHornMethod.instructions.add(new InsnNode(RETURN));
		
		node.methods.add(harvestByHornMethod);
	}
	
	
}
