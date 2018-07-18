package quaternary.botaniatweaks.modules.avaritia;

import morph.avaritia.Avaritia;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.shared.helper.ModCompatUtil;
import quaternary.botaniatweaks.modules.shared.helper.ClientHelpers;
import quaternary.botaniatweaks.modules.shared.helper.RegHelpers;
import quaternary.botaniatweaks.etc.event.LexiconHandlerEvent;
import quaternary.botaniatweaks.modules.shared.lexi.DoubleCompatLexiconEntry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class AvaritiaCompat {
	public static LexiconEntry direCrateEntry = null;
	
	public static Block direCrate;
	
	public static void preinit() {		
		BotaniaTweaks.PROXY.registerSidedEventClasses(() -> CommonEvents.class, () -> ClientEvents.class);
	}
	
	public static class CommonEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			direCrate = new BlockDireCraftyCrate();
			reg.register(RegHelpers.createBlock(direCrate, "dire_crafty_crate"));
			
			GameRegistry.registerTileEntity(TileDireCraftyCrate.class, BotaniaTweaks.MODID + ":dire_crafty_crate");
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			reg.register(RegHelpers.createItemBlock(new ItemBlock(direCrate)));
		}
		
		@SubscribeEvent
		public static void lexicon(LexiconHandlerEvent e) {
			ItemStack direCrateStack = ModCompatUtil.getStackFor(direCrate.getRegistryName());
			ItemStack extremeTableStack = ModCompatUtil.getStackFor(new ResourceLocation("avaritia", "extreme_crafting_table"));
			ItemStack craftyCrateStack = ModCompatUtil.getStackFor(new ResourceLocation("botania", "opencrate"), 1);
			
			RecipeElvenTrade direCrateRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack[]{direCrateStack}, extremeTableStack, craftyCrateStack);
			
			direCrateEntry = new DoubleCompatLexiconEntry("botania_tweaks.lexicon.category.direCrate", BotaniaAPI.categoryDevices, BotaniaTweaks.NAME, Avaritia.MOD_NAME);
			direCrateEntry.setKnowledgeType(BotaniaAPI.elvenKnowledge);
			direCrateEntry.setIcon(direCrateStack);
			direCrateEntry.addPage(new PageText("botania_tweaks.lexicon.direCrate.0"));
			direCrateEntry.addPage(new PageElvenRecipe("botania_tweaks.lexicon.direCrate.subtitle", direCrateRecipe));
		}
	}
	
	public static class ClientEvents {
		@SubscribeEvent
		public static void models(ModelRegistryEvent e) {
			ClientHelpers.setModel(direCrate.getRegistryName().getResourcePath());
		}
	}
}
