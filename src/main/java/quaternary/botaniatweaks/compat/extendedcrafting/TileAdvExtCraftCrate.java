package quaternary.botaniatweaks.compat.extendedcrafting;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import quaternary.botaniatweaks.compat.shared.tile.AbstractTileCompatCrate;

import java.util.function.BiFunction;

public class TileAdvExtCraftCrate extends AbstractTileCompatCrate<IRecipe> {
	@Override
	public int getCrateWidth() {
		return 5;
	}
	
	@Override
	public int getCrateHeight() {
		return 5;
	}
	
	@Override
	@SuppressWarnings("unchecked") //Blake!!!!!!!!! where are your generics!!!!!11
	protected Iterable<IRecipe> getAllRecipes() {
		//TODO: does this handle tier-locked recipes properly
		return TableRecipeManager.getInstance().getRecipes();
	}
	
	@Override
	protected BiFunction<IRecipe, InventoryCrafting, Boolean> doesRecipeMatchFunc() {
		return (recipe, inv) -> recipe.matches(inv, world);
	}
	
	@Override
	protected BiFunction<IRecipe, InventoryCrafting, ItemStack> craftingResultFunc() {
		return (recipe, inv) -> recipe.getCraftingResult(inv);
	}
}
