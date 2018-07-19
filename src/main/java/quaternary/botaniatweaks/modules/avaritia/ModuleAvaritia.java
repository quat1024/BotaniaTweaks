package quaternary.botaniatweaks.modules.avaritia;

import morph.avaritia.Avaritia;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.IModule;
import quaternary.botaniatweaks.modules.avaritia.tile.TileDireCraftyCrate;
import quaternary.botaniatweaks.modules.shared.block.BlockCompatCrate;
import quaternary.botaniatweaks.modules.shared.helper.*;
import quaternary.botaniatweaks.modules.shared.lexi.DoubleCompatLexiconEntry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class ModuleAvaritia implements IModule {
	public static LexiconEntry direCrateEntry = null;
	
	public static Block direCrate;
	
	@Override
	public void preinit() {
		BotaniaTweaks.PROXY.registerSidedEventClasses(() -> CommonEvents.class, () -> ClientEvents.class);
	}
	
	@Override
	public void postinit() {
		if(AvaritiaConfig.DIRE_CRAFTY_CRATE) {
			ItemStack direCrateStack = ModCompatUtil.getStackFor(direCrate.getRegistryName());
			ItemStack extremeTableStack = ModCompatUtil.getStackFor(new ResourceLocation("avaritia", "extreme_crafting_table"));
			ItemStack craftyCrateStack = ModCompatUtil.getStackFor(new ResourceLocation("botania", "opencrate"), 1);
			
			RecipeElvenTrade direCrateRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack[] {direCrateStack}, extremeTableStack, craftyCrateStack);
			
			direCrateEntry = new DoubleCompatLexiconEntry("botania_tweaks.lexicon.category.direCrate", BotaniaAPI.categoryDevices, BotaniaTweaks.NAME, Avaritia.MOD_NAME);
			direCrateEntry.setKnowledgeType(BotaniaAPI.elvenKnowledge);
			direCrateEntry.setIcon(direCrateStack);
			direCrateEntry.addPage(new PageText("botania_tweaks.lexicon.direCrate.0"));
			direCrateEntry.addPage(new PageElvenRecipe("botania_tweaks.lexicon.direCrate.subtitle", direCrateRecipe));
		}
	}
	
	@Override
	public void readConfig(Configuration config) {
		AvaritiaConfig.readConfig(config);
	}
	
	public static class CommonEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			if(AvaritiaConfig.DIRE_CRAFTY_CRATE) {
				direCrate = new BlockCompatCrate(direCrateEntry, TileDireCraftyCrate::new);
				reg.register(RegHelpers.createBlock(direCrate, "dire_crafty_crate"));
				
				GameRegistry.registerTileEntity(TileDireCraftyCrate.class, BotaniaTweaks.MODID + ":dire_crafty_crate");
			}
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			if(AvaritiaConfig.DIRE_CRAFTY_CRATE) {
				reg.register(RegHelpers.createItemBlock(new ItemBlock(direCrate)));
			}
		}
	}
	
	public static class ClientEvents {
		@SubscribeEvent
		public static void models(ModelRegistryEvent e) {
			if(AvaritiaConfig.DIRE_CRAFTY_CRATE) {
				ClientHelpers.setModel(direCrate.getRegistryName().getResourcePath());
			}
		}
	}
}
