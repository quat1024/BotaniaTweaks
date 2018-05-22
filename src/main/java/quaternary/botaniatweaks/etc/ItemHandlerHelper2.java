package quaternary.botaniatweaks.etc;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class ItemHandlerHelper2 {
	@Nonnull
	public static ItemStack filteredExtract(IItemHandler dest, @Nonnull ItemStack stackToExtract, int count, boolean simulate) {
		if (dest == null || stackToExtract.isEmpty()) return ItemStack.EMPTY;
		
		ItemStack runningExtract = ItemStack.EMPTY;
		
		for (int i = 0; i < dest.getSlots(); i++) {
			ItemStack removedStack = dest.extractItem(i, count, true);
			if(removedStack.isEmpty()) continue;
			
			if(ItemHandlerHelper.canItemStacksStack(stackToExtract, removedStack)) {
				if(!simulate) removedStack = dest.extractItem(i, count, false);
				if(runningExtract.isEmpty()) runningExtract = removedStack.copy();
				else runningExtract.setCount(runningExtract.getCount() + removedStack.getCount());
				
				count -= removedStack.getCount();
				if(count <= 0) break;				
			}
		}
		
		return stackToExtract;
	}
}

