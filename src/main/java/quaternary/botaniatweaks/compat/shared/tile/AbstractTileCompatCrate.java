package quaternary.botaniatweaks.compat.shared.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.TileOpenCrate;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

public abstract class AbstractTileCompatCrate<T> extends TileOpenCrate {
	public abstract int getCrateWidth();
	public abstract int getCrateHeight();
	
	protected abstract Iterable<T> getAllRecipes();
	protected abstract BiFunction<T, InventoryCrafting, Boolean> doesRecipeMatchFunc();
	protected abstract BiFunction<T, InventoryCrafting, ItemStack> craftingResultFunc();
	
	protected int getCrateSize() {
		return getCrateWidth() * getCrateHeight();
	}
	
	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true){
			@Override
			protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
				return 1;
			}
			
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				//Disallow insertion into the hidden output slot
				return (slot == getCrateWidth() * getCrateHeight()) ? stack : super.insertItem(slot, stack, simulate);
			}
		};
	}
	
	int oldItemCount = 0;
	
	@Override
	public void update() {
		if(world.isRemote) return;
		
		if(canEject() && isCrateFull() && craft(true)) {
			ejectAll();
		}
		
		int newItemCount = countItems();
		if(oldItemCount != newItemCount) {
			oldItemCount = newItemCount;
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
			markDirty();
		}
	}
	
	boolean craft(boolean fullCheck) {
		if(fullCheck && !isCrateFull()) return false;
		
		InventoryCrafting inv = new InventoryCrafting(new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer entityPlayer) {
				return false;
			}
		}, getCrateWidth(), getCrateHeight());
		
		for(int i = 0; i < getCrateSize(); i++) {
			ItemStack crateStack = itemHandler.getStackInSlot(i);
			if(crateStack.isEmpty()) continue;
			
			//skip Crafting Placeholder
			if(crateStack.getItem() == ModItems.manaResource && crateStack.getItemDamage() == 11) continue;
			
			inv.setInventorySlotContents(i, crateStack);
		}
		
		for(T recipe : getAllRecipes()) {
			if(doesRecipeMatchFunc().apply(recipe, inv)) {
				ItemStack output = craftingResultFunc().apply(recipe, inv);
				
				itemHandler.setStackInSlot(getCrateSize(), output);
				
				for(int i = 0; i < getCrateSize(); i++) {
					ItemStack stack = itemHandler.getStackInSlot(i);
					if(stack.isEmpty()) continue;
					
					itemHandler.setStackInSlot(i, stack.getItem().getContainerItem(stack));
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	boolean isCrateFull() {
		for(int i = 0; i < getCrateSize(); i++) {
			if(itemHandler.getStackInSlot(i).isEmpty()) return false;
		}
		
		return true;
	}
	
	int countItems() {
		int count = 0;
		for(int i = 0; i < getCrateSize(); i++) {
			count += itemHandler.getStackInSlot(i).isEmpty() ? 0 : 1;
		}
		
		return count;
	}
	
	void ejectAll() {
		for(int i = 0; i < getCrateSize() + 1; i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			if(!stack.isEmpty()) eject(stack, false);
			itemHandler.setStackInSlot(i, ItemStack.EMPTY);
		}
		
		markDirty();
	}
	
	@Override
	public boolean onWanded(World world, EntityPlayer player, ItemStack stack) {
		if(!world.isRemote && canEject()) {
			craft(false);
			ejectAll();
		}
		
		return true;
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}
}
