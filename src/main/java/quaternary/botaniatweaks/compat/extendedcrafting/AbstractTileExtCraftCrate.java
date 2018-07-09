package quaternary.botaniatweaks.compat.extendedcrafting;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import quaternary.botaniatweaks.compat.shared.tile.AbstractTileCompatCrate;

public abstract class AbstractTileExtCraftCrate extends AbstractTileCompatCrate<IRecipe> {
	abstract int getSize();
	
	@Override
	public int getCrateWidth() {
		return getSize();
	}
	
	@Override
	public int getCrateHeight() {
		return getSize();
	}
	
	@Override
	@SuppressWarnings("unchecked") //Blake!!!!!!!!! where are your generics!!!!!11
	protected Iterable<IRecipe> getAllRecipes() {
		//TODO: does this handle tier-locked recipes properly
		return TableRecipeManager.getInstance().getRecipes();
	}
	
	@Override
	protected boolean doesRecipeMatch(IRecipe recipe, InventoryCrafting inv) {
		return recipe.matches(inv, world);
	}
	
	@Override
	protected ItemStack getCraftingResult(IRecipe recipe, InventoryCrafting inv) {
		return recipe.getCraftingResult(inv);
	}
}
