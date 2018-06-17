package quaternary.botaniatweaks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.botaniatweaks.block.*;
import quaternary.botaniatweaks.etc.ItemSpork;
import quaternary.botaniatweaks.etc.Util;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockMod;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class BotaniaTweaksRegistry {
	static ArrayList<Block> OVERRIDE_BLOCKS = new ArrayList<>();
	static ArrayList<Item> OVERRIDE_ITEMS = new ArrayList<>();
	
	static ArrayList<Block> BLOCKS = new ArrayList<>();
	static ArrayList<Item> ITEMS = new ArrayList<>();
	
	//need to keep references to these for fixBlockReferences
	//TODO: don't do that in 1.13 if willie decides to go through with the objectholderification
	private static BlockNerfedManaFluxfield fluxfield;
	private static BlockCustomAgglomerationPlate agglo;
	private static BlockCustomOpenCrate crate;
	
	static void populate() {
		//Overrides
		BotaniaTweaks.LOG.info("Creating Botania registry replacements. Forge will print some warnings - it is safe to ignore these, since these overrides are very much intended. Here come the blocks:");
		
		fluxfield = new BlockNerfedManaFluxfield();
		agglo = new BlockCustomAgglomerationPlate();
		crate = new BlockCustomOpenCrate();
		
		OVERRIDE_BLOCKS.add(fluxfield);
		OVERRIDE_BLOCKS.add(agglo);
		OVERRIDE_BLOCKS.add(crate);
		
		BotaniaTweaks.LOG.info("And the items:");
		
		for(Block b : OVERRIDE_BLOCKS) {
			Item i = new ItemBlockMod(b).setRegistryName(b.getRegistryName());
			i.setCreativeTab(BotaniaCreativeTab.INSTANCE);
			OVERRIDE_ITEMS.add(i);
		}
		BotaniaTweaks.LOG.info("All done, have a nice day!");
		
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
		/*
		//The field references in ModBlocks are patched, so Botania will take care of these.
		for(Block b : OVERRIDE_BLOCKS) {
			reg.register(b);
		}
		*/
		
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
	
	/**
	 * Botania Tweaks uses registry replacements to change certain Botania blocks.
	 * However, Botania internally (in ModBlocks) keeps a giant list of references
	 * to blocks, as opposed to using things such as ObjectHolder to inject block references.
	 * This is a problem for me, as registry replacing won't magically update the field,
	 * and Botania references these fields a few times, notably when creating multiblock
	 * structure previews.
	 * 
	 * However, those multiblock structure previews are also created during preinit.
	 * I must run very early, before Botania's preinit, to swoop in and fix those
	 * field references before the multiblock preview can grab them. Naturally
	 * this means that Botania Tweaks is now the mod classloading ModBlocks, and
	 * Botania does setRegistryNaming in the static initializer, so that takes
	 * some hacks to work around too. It's harmless since Botania doesn't rely on the
	 * active mod container for registry names, but it prints a lot of scary console
	 * warnings (as it should). Easily fixable with scary setActiveModContainer stuff.
	 */
	static void fixBlockReferences() {
		classloadModBlocks();
		
		fixBlockReference(fluxfield, "rfGenerator");
		fixBlockReference(agglo, "terraPlate");
		fixBlockReference(crate, "openCrate");
	}
	
	private static void fixBlockReference(Block b, String fieldName) {
		try {
			//find the ModBlocks field
			Field f = ReflectionHelper.findField(ModBlocks.class, fieldName);
			//remove the final modifier
			Util.makeNonFinal(f);
			//set the field to the new block instance
			f.set(null, b);
		} catch (Exception e) {
			throw new RuntimeException("There was an error tweaking ModBlocks field " + fieldName, e);
		}
	}
	
	/**
	 * Classload ModBlocks.class while Botania is the active mod container.
	 * Classloading ModBlocks triggers a bunch of setRegistryNames, and if I'm the
	 * active mod container during that time, this dumps a lot of scary warnings
	 * into the console. But I need to load ModBlocks, to fix up the references.
	 */
	@SuppressWarnings("unused")
	static void classloadModBlocks() {
		Loader l = Loader.instance();
		ModContainer me = l.activeModContainer();
		ModContainer botania = Util.getBotaniaModContainer();
		
		l.setActiveModContainer(botania);
		Block classloadTrigger = ModBlocks.cacophonium;
		l.setActiveModContainer(me);
	}
	
	@SideOnly(Side.CLIENT)
	static void registerItemModels() {
		for(Item i : ITEMS) {
			ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
		}
	}
}
