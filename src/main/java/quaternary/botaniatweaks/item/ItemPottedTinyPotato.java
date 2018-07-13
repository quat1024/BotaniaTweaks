package quaternary.botaniatweaks.item;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import javax.annotation.Nullable;

public class ItemPottedTinyPotato extends ItemBlock {
	public ItemPottedTinyPotato(Block block) {
		super(block);
	}
	
	@Nullable
	@Override
	public Item getContainerItem() {
		return Items.FLOWER_POT;
	}
}
