package quaternary.botaniatweaks.modules.botania.lexi;

import net.minecraft.util.ResourceLocation;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import quaternary.botaniatweaks.modules.shared.lexi.PageTextCompat;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;

public class BotaniaLexiconHandler {
	public static void registerLexicon() {
		setBasicOrElvenKnowledge(BotaniaConfig.SPORK, LexiconData.corporea);
		setBasicOrElvenKnowledge(BotaniaConfig.SPORK, LexiconData.corporeaFunnel);
		setBasicOrElvenKnowledge(BotaniaConfig.SPORK, LexiconData.corporeaInterceptor);
		setBasicOrElvenKnowledge(BotaniaConfig.SPORK, LexiconData.corporeaRetainer);
		
		if(!BotaniaTweaks.PROXY.shouldAddLexiconPages()) return;
		
		if(BotaniaConfig.SPORK) {
			LexiconPage spork = new PageCraftingRecipe("botania_tweaks.lexicon.spork", new ResourceLocation(BotaniaTweaks.MODID, "spork"));
			LexiconPage regularCraft = new PageCraftingRecipe("botania_tweaks.lexicon.sporkspark", new ResourceLocation(BotaniaTweaks.MODID, "spork/corporeaspark_0_spork"));
			LexiconPage masterCraft = new PageCraftingRecipe("botania_tweaks.lexicon.masterspork", new ResourceLocation(BotaniaTweaks.MODID, "spork/corporeaspark_1_spork"));
			
			LexiconData.corporea.addPage(spork);
			LexiconData.corporea.addPage(regularCraft);
			LexiconData.corporea.addPage(masterCraft);
		}
		
		for(int i=1; i <= 8; i++) {
			PageCraftingRecipe potat = new PageCraftingRecipe("botania_tweaks.lexicon.potato." + i, new ResourceLocation(BotaniaTweaks.MODID, "compressed_tiny_potato_" + i));
			LexiconData.tinyPotato.addPage(potat);
		}
		
		if(BotaniaConfig.ADVANCED_CRAFTY_CRATE) {			
			LexiconData.craftCrate.addPage(new PageTextCompat("botania_tweaks.lexicon.advCrate.0", "Botania Tweaks"));
			LexiconData.craftCrate.addPage(new PageTextCompat("botania_tweaks.lexicon.advCrate.1", "Botania Tweaks"));
			
			if(BotaniaConfig.ADVANCED_CRAFTY_CRATE_HARDMODE) {
				LexiconData.craftCrate.addPage(new PageTextCompat("botania_tweaks.lexicon.advCrateHard", "Botania Tweaks"));
			}
		}
	}
	
	static void setBasicOrElvenKnowledge(boolean flag, LexiconEntry toFix) {
		toFix.setKnowledgeType(flag ? BotaniaAPI.basicKnowledge : BotaniaAPI.elvenKnowledge);
	}
}
