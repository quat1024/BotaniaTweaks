package quaternary.botaniatweaks.compat.extendedcrafting;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import quaternary.botaniatweaks.compat.shared.tile.AbstractTileCompatCrate;

import java.util.function.BiFunction;

public class TileUltExtCraftCrate extends AbstractTileExtCraftCrate {
	@Override
	int getSize() {
		return 9;
	}
}
