package quaternary.botaniatweaks.modules.avaritia.tile;

import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import quaternary.botaniatweaks.modules.shared.tile.AbstractTileCompatCrate;

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
	protected boolean doesRecipeMatch(IExtremeRecipe recipe, InventoryCrafting inv) {
		return recipe.matches(inv, world);
	}
	
	@Override
	protected ItemStack getCraftingResult(IExtremeRecipe recipe, InventoryCrafting inv) {
		return recipe.getCraftingResult(inv);
	}
}
