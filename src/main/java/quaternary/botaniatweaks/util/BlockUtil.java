package quaternary.botaniatweaks.util;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import quaternary.botaniatweaks.BotaniaTweaks;

public class BlockUtil {
	public static <B extends Block> B setName(B in, String name) {
		in.setRegistryName(new ResourceLocation(BotaniaTweaks.MODID, name));
		in.setUnlocalizedName(BotaniaTweaks.MODID + "." + name);
		return in;
	}
	
	public static <B extends Block> B setTab(B in) {
		in.setCreativeTab(BotaniaTweaks.TAB);
		return in;
	}
}
