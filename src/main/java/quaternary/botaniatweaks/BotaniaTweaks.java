package quaternary.botaniatweaks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.botaniatweaks.modules.IModule;
import quaternary.botaniatweaks.modules.avaritia.ModuleAvaritia;
import quaternary.botaniatweaks.modules.botania.ModuleBotania;
import quaternary.botaniatweaks.modules.crafttweaker.ModuleCrafttweaker;
import quaternary.botaniatweaks.modules.dynamictrees.ModuleDynamicTrees;
import quaternary.botaniatweaks.modules.extendedcrafting.ModuleExtendedCrafting;
import quaternary.botaniatweaks.modules.shared.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.modules.shared.lib.GeneratingFlowers;
import quaternary.botaniatweaks.modules.shared.proxy.ServerProxy;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = BotaniaTweaks.MODID, name = BotaniaTweaks.NAME, version = BotaniaTweaks.VERSION, dependencies = BotaniaTweaks.DEPS, guiFactory = "quaternary.botaniatweaks.modules.shared.config.BotaniaTweaksGuiFactory")
public class BotaniaTweaks {
	public static final String MODID = "botania_tweaks";
	public static final String NAME = "Botania Tweaks";
	public static final String VERSION = "GRADLE:VERSION";
	public static final String DEPS = "required-before:botania";
	
	public static final int TESTED_BOTANIA_VERSION = 358;
	
	public static final Logger LOG = LogManager.getLogger(NAME);
	
	@SidedProxy(clientSide = "quaternary.botaniatweaks.modules.shared.proxy.ClientProxy", serverSide = "quaternary.botaniatweaks.modules.shared.proxy.ServerProxy")
	public static ServerProxy PROXY;
	
	@GameRegistry.ItemStackHolder(MODID + ":compressed_tiny_potato_8")
	public static final ItemStack ICON = ItemStack.EMPTY;
	
	public static CreativeTabs TAB = new CreativeTabs(MODID) {
		@Override
		public ItemStack createIcon() {
			return ICON;
		}
	};
	
	public static final List<IModule> modules = new ArrayList<>();
	
	@Mod.EventHandler
	public static void preinit(FMLPreInitializationEvent e) {
		modules.add(new ModuleBotania());
		if(Loader.isModLoaded("avaritia")) modules.add(new ModuleAvaritia());
		if(Loader.isModLoaded("crafttweaker")) modules.add(new ModuleCrafttweaker());
		if(Loader.isModLoaded("extendedcrafting")) modules.add(new ModuleExtendedCrafting());
		if(Loader.isModLoaded("dynamictrees")) modules.add(new ModuleDynamicTrees());
		
		BotaniaTweaksConfig.initConfig();
		modules.forEach(IModule::preinit);
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
		modules.forEach(IModule::init);
	}
	
	@Mod.EventHandler
	public static void postinit(FMLPostInitializationEvent e) {
		modules.forEach(IModule::postinit);
		
		GeneratingFlowers.PostInitHandler.postinit(); //Memes
	}
	
	@Mod.EventHandler
	public static void serverStarting(FMLServerStartingEvent e) {
		modules.forEach(m -> m.serverStarting(e));
	}
}
