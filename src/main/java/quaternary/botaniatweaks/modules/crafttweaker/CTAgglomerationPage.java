package quaternary.botaniatweaks.modules.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import quaternary.botaniatweaks.modules.botania.lexi.PageCustomTerrasteel;
import quaternary.botaniatweaks.modules.botania.recipe.AgglomerationRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;

import javax.annotation.Nullable;
import java.util.List;

@ZenClass("mods.botaniatweaks.AgglomerationPage")
@ZenRegister
public class CTAgglomerationPage {
	@ZenMethod
	public static void add(String unlocalizedName, String entry, int pageNumber, CTAgglomeration.CTAgglomerationRecipe recipe) {
		ModuleCrafttweaker.LATE_ACTIONS.add(new AddAction(unlocalizedName, entry, pageNumber, recipe.toAgglomerationRecipe()));
	}
	
	@ZenMethod
	public static void add(String unlocalizedName, String entry, int pageNumber,
	 				IItemStack output, 
					IIngredient[] inputs,
					@Nullable Integer manaCostIn,
					@Nullable Integer color1In,
					@Nullable Integer color2In,
					@Nullable IIngredient center,
					@Nullable IIngredient edge,
					@Nullable IIngredient corner,
					@Nullable IIngredient centerReplace,
					@Nullable IIngredient edgeReplace,
					@Nullable IIngredient cornerReplace
	) {
		ModuleCrafttweaker.LATE_ACTIONS.add(new AddAction(unlocalizedName, entry, pageNumber, CTAgglomeration.buildAgglomerationRecipe(output, inputs, manaCostIn, color1In, color2In, center, edge, corner, centerReplace, edgeReplace, cornerReplace)));
	}
	
	static class AddAction implements IAction {
		final String unlocalizedName;
		final String entryName;
		final int pageNumber;
		final AgglomerationRecipe recipe;
		
		public AddAction(String unlocalizedName, String entryName, int pageNumber, AgglomerationRecipe recipe) {
			this.unlocalizedName = unlocalizedName;
			this.entryName = entryName;
			this.pageNumber = pageNumber;
			this.recipe = recipe;
		}
		
		@Override
		public void apply() {
			LexiconEntry entry = findEntry(entryName);
			
			if(pageNumber > entry.pages.size()) {
				CraftTweakerAPI.logError("Page number " + pageNumber + " out of range for entry " + entryName + ", which only has " + entry.pages.size() + " pages");
			}
			
			entry.pages.add(pageNumber, new PageCustomTerrasteel(unlocalizedName, recipe));
		}
		
		@Override
		public String describe() {
			return "Adding an Agglomeration Page for " + recipe.toString();
		}
	}
	
	static LexiconEntry findEntry(String entryName) {
		List<LexiconEntry> entries = BotaniaAPI.getAllEntries();
		for(LexiconEntry entry : entries) {
			if(entry.getUnlocalizedName().equalsIgnoreCase(entryName)) return entry;
		}
		
		throw new IllegalArgumentException("Cannot find a lexicon entry with the name " + entryName);
	}
}
