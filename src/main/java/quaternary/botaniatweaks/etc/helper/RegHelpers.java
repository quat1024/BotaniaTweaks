package quaternary.botaniatweaks.etc.helper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import quaternary.botaniatweaks.BotaniaTweaks;

public class RegHelpers {
	public static <T extends Block> T createBlock(T block, String name) {
		block.setRegistryName(new ResourceLocation(BotaniaTweaks.MODID, name));
		block.setUnlocalizedName(BotaniaTweaks.MODID + "." + name);
		block.setCreativeTab(BotaniaTweaks.TAB);
		
		return block;
	}
	
	public static <T extends ItemBlock> T createItemBlock(T itemBlock) {
		itemBlock.setRegistryName(itemBlock.getBlock().getRegistryName());
		itemBlock.setCreativeTab(BotaniaTweaks.TAB);
		return itemBlock;
	}
	
	public static <T extends Item> T createItem(T item, String name) {
		item.setRegistryName(new ResourceLocation(BotaniaTweaks.MODID, name));
		item.setUnlocalizedName(BotaniaTweaks.MODID + "." + name);
		item.setCreativeTab(BotaniaTweaks.TAB);
		return item;
	}
}
