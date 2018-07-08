package quaternary.botaniatweaks.compat.avaritia;

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import quaternary.botaniatweaks.compat.shared.tile.AbstractTileCompatCrate;

import java.util.function.BiFunction;

public class TileDireCraftyCrate extends AbstractTileCompatCrate<IExtremeRecipe> {
	@Override
	public int getCrateWidth() {
		return 9;
	}
	
	@Override
	public int getCrateHeight() {
		return 9;
	}
	
	@Override
	protected Iterable<IExtremeRecipe> getAllRecipes() {
		return AvaritiaRecipeManager.EXTREME_RECIPES.values();
	}
	
	@Override
	protected BiFunction<IExtremeRecipe, InventoryCrafting, Boolean> doesRecipeMatchFunc() {
		return (recipe, inv) -> recipe.matches(inv, world);
	}
	
	@Override
	protected BiFunction<IExtremeRecipe, InventoryCrafting, ItemStack> craftingResultFunc() {
		return (recipe, inv) -> recipe.getCraftingResult(inv);
	}
}
