package quaternary.botaniatweaks.modules.shared.helper;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModCompatUtil {
	public static ItemStack getStackFor(ResourceLocation res) {
		return getStackFor(res, 0);
	}
	
	public static ItemStack getStackFor(ResourceLocation res, int meta) {
		Item i = ForgeRegistries.ITEMS.getValue(res);
		return new ItemStack(i, 1, meta);
	}
}
