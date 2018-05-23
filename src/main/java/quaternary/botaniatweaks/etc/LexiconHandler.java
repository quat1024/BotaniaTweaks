package quaternary.botaniatweaks.etc;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;

public class LexiconHandler {
	static LexiconPage spork = new PageCraftingRecipe("botania_tweaks.lexicon.spork", new ResourceLocation(BotaniaTweaks.MODID, "spork"));
	static LexiconPage regularCraft = new PageCraftingRecipe("botania_tweaks.lexicon.sporkspark", new ResourceLocation(BotaniaTweaks.MODID, "spork/corporeaspark_0_spork"));
	static LexiconPage masterCraft = new PageCraftingRecipe("botania_tweaks.lexicon.masterspork", new ResourceLocation(BotaniaTweaks.MODID, "spork/corporeaspark_1_spork"));
	
	public static void fixKnowledgeTypes() {
		fixKnowledgeType(LexiconData.corporea);
		fixKnowledgeType(LexiconData.corporeaFunnel);
		fixKnowledgeType(LexiconData.corporeaInterceptor);
		fixKnowledgeType(LexiconData.corporeaRetainer);
		
		if(BotaniaTweaksConfig.SPORK && BotaniaTweaks.PROXY.shouldAddLexiconPages()) {
			LexiconData.corporea.addPage(spork);
			LexiconData.corporea.addPage(regularCraft);
			LexiconData.corporea.addPage(masterCraft);
		} else {
			LexiconData.corporea.pages.removeIf(lexiconPage -> lexiconPage.equals(spork));
			LexiconData.corporea.pages.removeIf(lexiconPage -> lexiconPage.equals(regularCraft));
			LexiconData.corporea.pages.removeIf(lexiconPage -> lexiconPage.equals(masterCraft));
		}
	}
	
	static void fixKnowledgeType(LexiconEntry toFix) {
		toFix.setKnowledgeType(BotaniaTweaksConfig.SPORK ? BotaniaAPI.basicKnowledge : BotaniaAPI.elvenKnowledge);
	}
}
