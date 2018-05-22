package quaternary.botaniatweaks.etc;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IFlowerComponent;

/** Created through ASM patchery in TileAltar#getFlowerComponent. */
public class CatchallFlowerComponent implements IFlowerComponent {
	@Override
	public boolean canFit(ItemStack stack, IPetalApothecary apothecary) {
		return true;
	}
	
	@Override
	public int getParticleColor(ItemStack stack) {
		return 0xEE55DD; //idk
	}
}
