package quaternary.botaniatweaks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.botaniatweaks.block.*;
import quaternary.botaniatweaks.etc.ItemSpork;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockMod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class BotaniaTweaksRegistry {
	static ArrayList<Block> OVERRIDE_BLOCKS = new ArrayList<>();
	static ArrayList<Item> OVERRIDE_ITEMS = new ArrayList<>();
	
	static ArrayList<Block> BLOCKS = new ArrayList<>();
	static ArrayList<Item> ITEMS = new ArrayList<>();
	
	static void populate() {
		//Overrides
		OVERRIDE_BLOCKS.add(new BlockNerfedManaFluxfield());
		OVERRIDE_BLOCKS.add(new BlockCustomAgglomerationPlate());
		
		for(Block b : OVERRIDE_BLOCKS) {
			Item i = new ItemBlockMod(b).setRegistryName(b.getRegistryName());
			i.setCreativeTab(BotaniaCreativeTab.INSTANCE);
			OVERRIDE_ITEMS.add(i);
		}
		
		//Other blocks and items
		for(int compressionLevel = 1; compressionLevel <= 8; compressionLevel++) {
			Block potat = new BlockCompressedTinyPotato(compressionLevel);
			BLOCKS.add(potat);
			
			Item i = compressionLevel == 8 ? BotaniaTweaks.PROXY.makeRainbowItem(potat) : new ItemBlock(potat);
			i.setRegistryName(potat.getRegistryName());
			ITEMS.add(i);
		}
		
		BLOCKS.add(new BlockPottedTinyPotato());
		ITEMS.add(new ItemSpork());
		
		for(Item i : ITEMS) {
			i.setCreativeTab(BotaniaTweaks.TAB);
		}
	}
	
	static void registerBlocks(IForgeRegistry<Block> reg) {
		for(Block b : OVERRIDE_BLOCKS) {
			reg.register(b);
		}
		
		for(Block b : BLOCKS) {
			reg.register(b);
		}
	}
	
	static void registerItems(IForgeRegistry<Item> reg) {
		for(Item i : OVERRIDE_ITEMS) {
			reg.register(i);
		}
		
		for(Item i : ITEMS) {
			reg.register(i);
		}
	}
	
	@SideOnly(Side.CLIENT)
	static void registerItemModels() {
		for(Item i : ITEMS) {
			ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
		}
	}
}
