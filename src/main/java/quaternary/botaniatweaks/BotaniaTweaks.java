package quaternary.botaniatweaks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.botaniatweaks.modules.botania.BotaniaCompat;
import quaternary.botaniatweaks.modules.avaritia.AvaritiaCompat;
import quaternary.botaniatweaks.modules.crafttweaker.CTHandler;
import quaternary.botaniatweaks.modules.extendedcrafting.ExtendedCraftingCompat;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.etc.event.LexiconHandlerEvent;
import quaternary.botaniatweaks.modules.shared.helper.MiscHelpers;
import quaternary.botaniatweaks.proxy.ServerProxy;
import quaternary.botaniatweaks.modules.botania.recipe.AgglomerationRecipes;

@Mod(modid = BotaniaTweaks.MODID, name = BotaniaTweaks.NAME, version = BotaniaTweaks.VERSION, dependencies = BotaniaTweaks.DEPS, guiFactory = "quaternary.botaniatweaks.config.BotaniaTweaksGuiFactory")
@Mod.EventBusSubscriber
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
		ModContainer botania = MiscHelpers.getBotaniaModContainer();
		
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
		
		BotaniaCompat.preinit();
		
		if(Loader.isModLoaded("avaritia")) {
			AvaritiaCompat.preinit();
		}
		
		if(Loader.isModLoaded("extendedcrafting")) {
			ExtendedCraftingCompat.preinit();
		}
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
		BotaniaCompat.init();
	}
	
	@Mod.EventHandler
	public static void postinit(FMLPostInitializationEvent e) {
		//Botania adds knowledge types in init... but I run *before* botania
		//Let's do this in postinit then so knowledge types are available
		MinecraftForge.EVENT_BUS.post(new LexiconHandlerEvent());
	}
	
	//CT runs its scripts on this event with EventPriority.LOWEST; as long as this somehow runs first we're good
	@SubscribeEvent
	public static void recipes(RegistryEvent.Register<IRecipe> e) {
		AgglomerationRecipes.init();
	}
	
	@Mod.EventHandler
	public static void loadComplete(FMLLoadCompleteEvent e) {
		if(Loader.isModLoaded("crafttweaker")) {
			CTHandler.init();
		}
	}
}
