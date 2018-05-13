package quaternary.botaniatweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.botaniatweaks.block.*;
import quaternary.botaniatweaks.compat.crafttweaker.CTHandler;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.dispense.BehaviorEnderAirDispenser;
import quaternary.botaniatweaks.recipe.AgglomerationRecipes;
import quaternary.botaniatweaks.tile.TileCustomAgglomerationPlate;
import quaternary.botaniatweaks.tile.TileNerfedManaFluxfield;
import vazkii.botania.common.item.block.ItemBlockMod;

import java.util.ArrayList;

@Mod(modid = BotaniaTweaks.MODID, name = BotaniaTweaks.NAME, version = BotaniaTweaks.VERSION, dependencies = BotaniaTweaks.DEPS, guiFactory = "quaternary.botaniatweaks.config.BotaniaTweaksGuiFactory")
public class BotaniaTweaks {
	public static final String MODID = "botania_tweaks";
	public static final String NAME = "Botania Tweaks";
	public static final String VERSION = "1.0.0";
	public static final String DEPS = "required-after:botania";
	
	public static final Logger LOG = LogManager.getLogger(NAME);
	
	public static final ArrayList<Block> BLOCKS = new ArrayList<>();
	public static final ArrayList<Item> ITEMS = new ArrayList<>();
	
	@GameRegistry.ItemStackHolder("botania:rfgenerator")
	public static final ItemStack icon = ItemStack.EMPTY;
	public static final CreativeTabs TAB = new CreativeTabs(MODID) {
		@Override
		public ItemStack getTabIconItem() {
			return icon;
		}
	};
	
	static {
		LOG.info("Creating some registry replacements for Botania. Here come the blocks:");
		BLOCKS.add(new BlockNerfedManaFluxfield());
		BLOCKS.add(new BlockCustomAgglomerationPlate());
		
		LOG.info("And the items:");
		for(Block b : BLOCKS) {
			Item i = new ItemBlockMod(b).setRegistryName(b.getRegistryName());
			i.setCreativeTab(BotaniaTweaks.TAB); //plS
			ITEMS.add(i);
		}
		
		LOG.info("It is safe to ignore the warnings Forge just printed!");
		
		BLOCKS.add(new BlockPottedTinyPotato());
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
		AgglomerationRecipes.init();
		BotaniaTweaksConfig.readConfig();
		
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.GLASS_BOTTLE, new BehaviorEnderAirDispenser(BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(Items.GLASS_BOTTLE)));
	}
	
	@Mod.EventHandler
	public static void loadComplete(FMLLoadCompleteEvent e) {
		if(Loader.isModLoaded("crafttweaker")) {
			CTHandler.init();
		}
	}
	
	@Mod.EventBusSubscriber
	public static class CommonEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			for(Block b : BLOCKS) {
				reg.register(b);
			}
			
			GameRegistry.registerTileEntity(TileNerfedManaFluxfield.class, MODID + ":tweaked_fluxfield");
			GameRegistry.registerTileEntity(TileCustomAgglomerationPlate.class, MODID + ":custom_agglomeration_plate");
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			for(Item i : ITEMS) {
				reg.register(i);
			}
		}
	}
}
