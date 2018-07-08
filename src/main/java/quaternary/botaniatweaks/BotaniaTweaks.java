package quaternary.botaniatweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.botaniatweaks.block.BotaniaTweaksBlocks;
import quaternary.botaniatweaks.compat.avaritia.AvaritiaCompat;
import quaternary.botaniatweaks.compat.crafttweaker.CTHandler;
import quaternary.botaniatweaks.compat.extendedcrafting.ExtendedCraftingCompat;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.etc.BehaviorEnderAirDispenser;
import quaternary.botaniatweaks.etc.Util;
import quaternary.botaniatweaks.item.BotaniaTweaksItems;
import quaternary.botaniatweaks.lexi.LexiconHandler;
import quaternary.botaniatweaks.net.BotaniaTweaksPacketHandler;
import quaternary.botaniatweaks.proxy.ServerProxy;
import quaternary.botaniatweaks.recipe.AgglomerationRecipes;
import quaternary.botaniatweaks.tile.*;

@Mod(modid = BotaniaTweaks.MODID, name = BotaniaTweaks.NAME, version = BotaniaTweaks.VERSION, dependencies = BotaniaTweaks.DEPS, guiFactory = "quaternary.botaniatweaks.config.BotaniaTweaksGuiFactory")
public class BotaniaTweaks {
	public static final String MODID = "botania_tweaks";
	public static final String NAME = "Botania Tweaks";
	public static final String VERSION = "GRADLE:VERSION";
	public static final String DEPS = "required-before:botania";
	
	public static final int TESTED_BOTANIA_VERSION = 354;
	
	public static final Logger LOG = LogManager.getLogger(NAME);
	
	@SidedProxy(clientSide = "quaternary.botaniatweaks.proxy.ClientProxy", serverSide = "quaternary.botaniatweaks.proxy.ServerProxy")
	public static ServerProxy PROXY;
	
	@GameRegistry.ItemStackHolder(MODID + ":compressed_tiny_potato_8")
	public static final ItemStack ICON = ItemStack.EMPTY;
	
	public static CreativeTabs TAB = new CreativeTabs(MODID) {
		@Override
		public ItemStack getTabIconItem() {
			return ICON;
		}
	};
	
	@Mod.EventHandler
	public static void preinit(FMLPreInitializationEvent e) {
		//Warn if the Botania version is wrong because btweaks is so fkin fragile lmao
		ModContainer botania = Util.getBotaniaModContainer();
		
		try {
			String minorVersionString = botania.getDisplayVersion().split("-")[1];
			double versionNumber = Double.parseDouble(minorVersionString);
			int flooredVersion = MathHelper.floor(versionNumber);
			
			if(flooredVersion != TESTED_BOTANIA_VERSION) {
				LOG.warn("********************************");
				LOG.warn("Detected a Botania version mismatch!");
				LOG.warn("Expected version {}, found version {}.", TESTED_BOTANIA_VERSION, flooredVersion);
				LOG.warn("This may cause issues and crashes! Please report any");
				LOG.warn("errors and crashes to Botania Tweaks first. Thanks!");
				LOG.warn("********************************");
			}
			
		} catch(Exception asdf) {
			LOG.warn("********************************");
			LOG.warn("Unable to detect or parse Botania's version!!!");
			LOG.warn("This is BAD!!! Serious incompatibilities and crashes may happen!!!");
			LOG.warn("********************************");
		}
		
		BotaniaTweaksConfig.initConfig();
		
		BotaniaTweaksBlocks.registerOverrides();
		
		if(Loader.isModLoaded("avaritia")) {
			AvaritiaCompat.preinit();
		}
		
		if(Loader.isModLoaded("extendedcrafting")) {
			ExtendedCraftingCompat.preinit();
		}
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
		BotaniaTweaksPacketHandler.init();
		PROXY.registerEvents();
		
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.GLASS_BOTTLE, new BehaviorEnderAirDispenser(BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(Items.GLASS_BOTTLE)));
	}
	
	@Mod.EventHandler
	public static void postinit(FMLPostInitializationEvent e) {
		//Botania adds knowledge types in init but I run *before* botania
		//Let's do this in postinit then so knowledge types are available
		LexiconHandler.registerLexicon();
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
			BotaniaTweaksBlocks.registerBlocks(e.getRegistry());
			
			GameRegistry.registerTileEntity(TileNerfedManaFluxfield.class, MODID + ":tweaked_fluxfield");
			GameRegistry.registerTileEntity(TileCustomAgglomerationPlate.class, MODID + ":custom_agglomeration_plate");
			GameRegistry.registerTileEntity(TileCompressedTinyPotato.class, MODID + ":compressed_tiny_potato");
			GameRegistry.registerTileEntity(TileCustomCraftyCrate.class, MODID + ":custom_crafty_crate");
			
			//While we're at it
			PROXY.registerTESR();
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {			
			BotaniaTweaksItems.registerItems(e.getRegistry());
		}
		
		//CT runs its scripts on this event with EventPriority.LOWEST; as long as this somehow runs first we're good
		@SubscribeEvent
		public static void recipes(RegistryEvent.Register<IRecipe> e) {
			AgglomerationRecipes.init();
		}
	}
	
	@Mod.EventBusSubscriber(value = Side.CLIENT)
	public static class ClientEvents {
		@SubscribeEvent
		public static void model(ModelRegistryEvent e) {
			BotaniaTweaksItems.Client.registerItemModels();
		}
	}
}
