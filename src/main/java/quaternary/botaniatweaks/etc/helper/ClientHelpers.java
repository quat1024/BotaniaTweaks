package quaternary.botaniatweaks.etc.helper;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import quaternary.botaniatweaks.BotaniaTweaks;

public class ClientHelpers {
	public static void setModel(String name) {
		Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(BotaniaTweaks.MODID, name));
		ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
	}
}
