package quaternary.botaniatweaks.etc;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemBlockRainbowBarf extends ItemBlock {
	public ItemBlockRainbowBarf(Block block) {
		super(block);
	}
	
	@Nullable
	@Override
	public FontRenderer getFontRenderer(ItemStack stack) {
		//oooo a getter with a side effect
		return RainbowBarfFontRenderer.get();
	}
}
