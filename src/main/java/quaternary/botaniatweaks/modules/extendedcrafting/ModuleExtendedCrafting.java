package quaternary.botaniatweaks.modules.extendedcrafting;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.IModule;
import quaternary.botaniatweaks.modules.extendedcrafting.tile.AbstractTileExtCraftCrate;
import quaternary.botaniatweaks.modules.shared.block.BlockCompatCrate;
import quaternary.botaniatweaks.modules.shared.helper.*;
import quaternary.botaniatweaks.modules.shared.lexi.DoubleCompatLexiconEntry;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageText;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModuleExtendedCrafting implements IModule {
	public static LexiconEntry extCrateEntry;
	
	public static Block basicExtCrate;
	public static Block advExtCrate;
	public static Block eliteExtCrate;
	public static Block ultExtCrate;
	
	public static boolean areCraftersEnabled() {
		return ModBlocks.blockBasicTable.isEnabled();
	}
	
	@Override
	public void preinit() {
		BotaniaTweaks.PROXY.registerSidedEventClasses(() -> CommonEvents.class, () -> ClientEvents.class);
	}
	
	@Override
	public void postinit() {
		if(!areCraftersEnabled())
			return;

		List<RecipeElvenTrade> elvenRecipes = new ArrayList<>();
		extCrateEntry = new DoubleCompatLexiconEntry("botania_tweaks.lexicon.category.extCrates", BotaniaAPI.categoryDevices, BotaniaTweaks.NAME, ExtendedCrafting.NAME);

		BiConsumer<Block, Block> elvenRecipeFunc = (extCrate, extTable) -> {
			ItemStack extCrateStack = new ItemStack(Item.getItemFromBlock(extCrate));
			ItemStack extTableStack = new ItemStack(Item.getItemFromBlock(extTable));
			ItemStack crateStack = ModCompatUtil.getStackFor(new ResourceLocation("botania", "opencrate"), 1);
			
			elvenRecipes.add(BotaniaAPI.registerElvenTradeRecipe(extCrateStack, extTableStack, crateStack));
			LexiconRecipeMappings.map(extCrateStack, extCrateEntry, 0);
		};
		
		elvenRecipeFunc.accept(basicExtCrate, ModBlocks.blockBasicTable);
		elvenRecipeFunc.accept(advExtCrate, ModBlocks.blockAdvancedTable);
		elvenRecipeFunc.accept(eliteExtCrate, ModBlocks.blockEliteTable);
		elvenRecipeFunc.accept(ultExtCrate, ModBlocks.blockUltimateTable);
		
		extCrateEntry.setKnowledgeType(BotaniaAPI.elvenKnowledge);
		extCrateEntry.setIcon(new ItemStack(Item.getItemFromBlock(ultExtCrate)));
		extCrateEntry.addPage(new PageText("botania_tweaks.lexicon.extCrates.0"));
		for(int i = 0; i < elvenRecipes.size(); i++) {
			extCrateEntry.addPage(new PageElvenRecipe("botania_tweaks.lexicon.extCrates.subtitle." + i, elvenRecipes.get(i)));
		}
	}
	
	public static class CommonEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			Supplier<LexiconEntry> entry = () -> extCrateEntry;
			basicExtCrate = new BlockCompatCrate(entry, AbstractTileExtCraftCrate.Basic::new);
			advExtCrate = new BlockCompatCrate(entry, AbstractTileExtCraftCrate.Advanced::new);
			eliteExtCrate = new BlockCompatCrate(entry, AbstractTileExtCraftCrate.Elite::new);
			ultExtCrate = new BlockCompatCrate(entry, AbstractTileExtCraftCrate.Ultimate::new);
			
			reg.register(RegHelpers.createBlock(basicExtCrate, "basic_extended_crafty_crate"));
			reg.register(RegHelpers.createBlock(advExtCrate, "advanced_extended_crafty_crate"));
			reg.register(RegHelpers.createBlock(eliteExtCrate, "elite_extended_crafty_crate"));
			reg.register(RegHelpers.createBlock(ultExtCrate, "ultimate_extended_crafty_crate"));
			
			GameRegistry.registerTileEntity(AbstractTileExtCraftCrate.Basic.class, new ResourceLocation(BotaniaTweaks.MODID, "basic_ext_crafty_crate"));
			GameRegistry.registerTileEntity(AbstractTileExtCraftCrate.Advanced.class, new ResourceLocation(BotaniaTweaks.MODID, "adv_ext_crafty_crate"));
			GameRegistry.registerTileEntity(AbstractTileExtCraftCrate.Elite.class, new ResourceLocation(BotaniaTweaks.MODID, "elite_ext_crafty_crate"));
			GameRegistry.registerTileEntity(AbstractTileExtCraftCrate.Ultimate.class, new ResourceLocation(BotaniaTweaks.MODID, "ult_ext_crafty_crate"));
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			reg.register(RegHelpers.createItemBlock(new ItemBlock(basicExtCrate)));
			reg.register(RegHelpers.createItemBlock(new ItemBlock(advExtCrate)));
			reg.register(RegHelpers.createItemBlock(new ItemBlock(eliteExtCrate)));
			reg.register(RegHelpers.createItemBlock(new ItemBlock(ultExtCrate)));
		}
	}
	
	public static class ClientEvents {
		@SubscribeEvent
		public static void models(ModelRegistryEvent e) {
			ClientHelpers.setModel(basicExtCrate.getRegistryName().getPath());
			ClientHelpers.setModel(advExtCrate.getRegistryName().getPath());
			ClientHelpers.setModel(eliteExtCrate.getRegistryName().getPath());
			ClientHelpers.setModel(ultExtCrate.getRegistryName().getPath());
		}
	}
}
