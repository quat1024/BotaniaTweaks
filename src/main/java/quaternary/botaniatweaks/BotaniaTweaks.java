package quaternary.botaniatweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.botaniatweaks.compat.crafttweaker.CTHandler;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.etc.*;
import quaternary.botaniatweaks.net.BotaniaTweaksPacketHandler;
import quaternary.botaniatweaks.proxy.ServerProxy;
import quaternary.botaniatweaks.recipe.AgglomerationRecipes;
import quaternary.botaniatweaks.tile.*;
import vazkii.botania.common.lib.LibMisc;

@Mod(modid = BotaniaTweaks.MODID, name = BotaniaTweaks.NAME, version = BotaniaTweaks.VERSION, dependencies = BotaniaTweaks.DEPS, guiFactory = "quaternary.botaniatweaks.config.BotaniaTweaksGuiFactory")
public class BotaniaTweaks {
	public static final String MODID = "botania_tweaks";
	public static final String NAME = "Botania Tweaks";
	public static final String VERSION = "1.1.0";
	public static final String DEPS = "required-before:botania";
	
	public static final int MAX_TESTED_BOTANIA_VERSION = 355;
	
	public static final Logger LOG = LogManager.getLogger(NAME);
	
	@SidedProxy(clientSide = "quaternary.botaniatweaks.proxy.ClientProxy", serverSide = "quaternary.botaniatweaks.proxy.ServerProxy")
	public static ServerProxy PROXY;
	
	@GameRegistry.ItemStackHolder(MODID + ":compressed_potato_8")
	public static final ItemStack ICON = ItemStack.EMPTY;
	
	public static CreativeTabs TAB = new CreativeTabs(MODID) {
		@Override
		public ItemStack getTabIconItem() {
			return ICON;
		}
	};
	
	@Mod.EventHandler
	public static void preinit(FMLPreInitializationEvent e) {
		BotaniaTweaksConfig.initConfig();
		
		BotaniaTweaksRegistry.populate();
		BotaniaTweaksRegistry.fixBlockReferences();
		
		//Warn if the Botania version is wrong because btweaks is so fkin fragile lmao
		ModContainer botania = Util.getBotaniaModContainer();
		
		try {
			String minorVersionString = botania.getDisplayVersion().split("-")[1];
			double versionNumber = Double.parseDouble(minorVersionString);
			int flooredVersion = MathHelper.floor(versionNumber);
			
			if(flooredVersion != MAX_TESTED_BOTANIA_VERSION) {
				LOG.warn("********************************");
				LOG.warn("Detected a Botania version mismatch!");
				LOG.warn("Expected version {}, found version {}.", MAX_TESTED_BOTANIA_VERSION, flooredVersion);
				LOG.warn("This may cause issues and crashes! Please report any");
				LOG.warn("errors and crashes to Botania Tweaks first. Thanks!");
				LOG.warn("********************************");
			}
			
		} catch (Exception asdf) {
			LOG.warn("********************************");
			LOG.warn("Unable to detect or parse Botania's version!!!");
			LOG.warn("This is BAD!!! Serious incompatibilities and crashes may happen!!!");
			LOG.warn("********************************");
		}
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
		AgglomerationRecipes.init();
		BotaniaTweaksPacketHandler.init();
		
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.GLASS_BOTTLE, new BehaviorEnderAirDispenser(BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(Items.GLASS_BOTTLE)));
	}
	
	@Mod.EventHandler
	public static void postinit(FMLPostInitializationEvent e) {
		//Run this in postinit.
		//BTweaks has to load before Botania to fix up references to TileTerraPlate etc
		//in ModBlocks, so things like the multiblock preview can work properly (the multiblock
		//preview is created in preinit, and I have to catch and fix the reference first)
		//So normally I would put this in init, since botania lexicon data is created in init
		LexiconHandler.fixKnowledgeTypes();
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
			
			BotaniaTweaksRegistry.registerBlocks(reg);
			
			GameRegistry.registerTileEntity(TileNerfedManaFluxfield.class, MODID + ":tweaked_fluxfield");
			GameRegistry.registerTileEntity(TileCustomAgglomerationPlate.class, MODID + ":custom_agglomeration_plate");
			GameRegistry.registerTileEntity(TileCompressedTinyPotato.class, MODID + ":compressed_tiny_potato");
			
			//While we're at it
			PROXY.registerTESR();
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			BotaniaTweaksRegistry.registerItems(reg);
		}
	}
	
	@Mod.EventBusSubscriber
	public static class ClientEvents {
		@SubscribeEvent
		public static void model(ModelRegistryEvent e) {
			BotaniaTweaksRegistry.registerItemModels();
		}
	}
}
