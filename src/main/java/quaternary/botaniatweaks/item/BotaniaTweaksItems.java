package quaternary.botaniatweaks.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.compat.shared.OptionalExtensions;

public class BotaniaTweaksItems {
	public static void registerItems(IForgeRegistry<Item> reg) {
		registerItemBlocks(reg);
		registerOtherItems(reg);
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
	
	private static void registerItemBlocks(IForgeRegistry<Item> reg) {
		//Oh no it's horrible
		for(int compressionLevel = 1; compressionLevel <= 8; compressionLevel++) {
			Block tater = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(BotaniaTweaks.MODID, "compressed_tiny_potato_" + compressionLevel));
			
			reg.register(createItemBlock(compressionLevel == 8 ? BotaniaTweaks.PROXY.makeRainbowItem(tater) : new ItemBlock(tater)));
		}
		
		reg.register(createItemBlock(new ItemBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(BotaniaTweaks.MODID, "potted_tiny_potato")))));
	}
	
	private static void registerOtherItems(IForgeRegistry<Item> reg) {
		reg.register(createItem(new ItemSpork(), "spork"));
		
		OptionalExtensions.callItems(reg);
	}
	
	@SideOnly(Side.CLIENT)
	public static class Client {
		public static void registerItemModels() {
			//Don't look it's horrible!
			for(int compressionLevel = 1; compressionLevel <= 8; compressionLevel++) {
				setModel("compressed_tiny_potato_" + compressionLevel);
			}
			
			setModel("spork");
			setModel("potted_tiny_potato");
			
			OptionalExtensions.callModels();
		}
		
		public static void setModel(String name) {
			Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(BotaniaTweaks.MODID, name));
			ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
		}
	}
}
