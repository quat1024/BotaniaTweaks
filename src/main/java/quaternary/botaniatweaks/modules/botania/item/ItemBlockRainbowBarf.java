package quaternary.botaniatweaks.modules.botania.item;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.botaniatweaks.modules.shared.etc.RainbowBarfFontRenderer;

import javax.annotation.Nullable;

public class ItemBlockRainbowBarf extends ItemBlock {
	public ItemBlockRainbowBarf(Block block) {
		super(block);
	}
	
	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(ItemStack stack) {
		//oooo a getter with a side effect
		return RainbowBarfFontRenderer.get();
	}
}
