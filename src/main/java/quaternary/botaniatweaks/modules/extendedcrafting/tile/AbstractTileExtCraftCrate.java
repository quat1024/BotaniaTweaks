package quaternary.botaniatweaks.modules.extendedcrafting.tile;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import quaternary.botaniatweaks.modules.shared.tile.AbstractTileCompatCrate;

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
	
	public static class Basic extends AbstractTileExtCraftCrate {
		@Override
		int getSize() {
			return 3;
		}
	}
	
	public static class Advanced extends AbstractTileExtCraftCrate {
		@Override
		int getSize() {
			return 5;
		}
	}
	
	public static class Elite extends AbstractTileExtCraftCrate {
		@Override
		int getSize() {
			return 7;
		}
	}
	
	public static class Ultimate extends AbstractTileExtCraftCrate {
		@Override
		int getSize() {
			return 9;
		}
	}
}
