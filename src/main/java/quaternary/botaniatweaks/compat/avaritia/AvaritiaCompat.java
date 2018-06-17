package quaternary.botaniatweaks.compat.avaritia;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.lexi.DoubleCompatLexiconEntry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class AvaritiaCompat {
	public static void preinit() {
		MinecraftForge.EVENT_BUS.register(AvaritiaCompat.class);
		
		direCrate = new BlockDireCraftyCrate();
	}
	
	@GameRegistry.ItemStackHolder("botania_tweaks:dire_crafty_crate")
	public static final ItemStack DIRE_CRATE_STACK = ItemStack.EMPTY;
	
	@GameRegistry.ItemStackHolder("avaritia:extreme_crafting_table")
	public static final ItemStack DIRE_CRAFTING_TABLE_STACK = ItemStack.EMPTY;
	
	@GameRegistry.ItemStackHolder(value = "botania:opencrate", meta = 1)
	public static final ItemStack CRAFTY_CRATE_STACK = ItemStack.EMPTY;
	
	public static LexiconEntry direCrateEntry = null;
	
	public static void lexicon() {
		//yeah sure lets do this here too
		RecipeElvenTrade direCrateRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack[]{DIRE_CRATE_STACK}, DIRE_CRAFTING_TABLE_STACK, CRAFTY_CRATE_STACK);
		
		direCrateEntry = new DoubleCompatLexiconEntry("botania_tweaks.lexicon.category.direCrate", BotaniaAPI.categoryDevices, "Botania Tweaks", "Avaritia");
		direCrateEntry.setKnowledgeType(BotaniaAPI.elvenKnowledge);
		direCrateEntry.setIcon(DIRE_CRATE_STACK);
		direCrateEntry.addPage(new PageText("botania_tweaks.lexicon.direCrate.0"));
		direCrateEntry.addPage(new PageElvenRecipe("botania_tweaks.lexicon.direCrate.subtitle", direCrateRecipe));
	}
	
	private static Block direCrate;
	private static Item direCrateItem;
	
	@SubscribeEvent
	public static void blocks(RegistryEvent.Register<Block> b) {
		b.getRegistry().register(direCrate);
		
		GameRegistry.registerTileEntity(TileDireCraftyCrate.class, BotaniaTweaks.MODID + ":dire_crafty_crate");
	}
	
	@SubscribeEvent
	public static void items(RegistryEvent.Register<Item> e) {
		direCrateItem = new ItemBlock(direCrate);
		direCrateItem.setRegistryName(direCrate.getRegistryName());
		e.getRegistry().register(direCrateItem);
	}
	
	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {
		BotaniaTweaks.PROXY.registerModel(direCrateItem);
	}
}
