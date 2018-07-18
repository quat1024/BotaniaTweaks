package quaternary.botaniatweaks.modules.botania.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.*;

public class AgglomerationRecipe {
	public final ImmutableList<ItemStack> recipeStacks;
	public final ImmutableList<String> recipeOreKeys;
	public final ItemStack recipeOutput;
	public final int manaCost;
	public final int color1;
	public final int color2;
	public final IBlockState multiblockCenter;
	public final IBlockState multiblockEdge;
	public final IBlockState multiblockCorner;
	@Nullable
	public final IBlockState multiblockCenterReplace;
	@Nullable
	public final IBlockState multiblockEdgeReplace;
	@Nullable
	public final IBlockState multiblockCornerReplace;
	
	final int totalInputs;
	
	private void verifyInputs(ImmutableList<Object> inputs) {
		if(inputs.isEmpty()) throw new IllegalArgumentException("Can't make empty agglomeration recipe");
		
		for(Object o : inputs) {
			if(o instanceof ItemStack || o instanceof String) continue;
			throw new IllegalArgumentException("illegal recipe input " + o);
		}
	}
	
	public AgglomerationRecipe(ImmutableList<Object> recipeInputs, ItemStack recipeOutput, int manaCost, int color1, int color2, IBlockState multiblockCenter, IBlockState multiblockEdge, IBlockState multiblockCorner, @Nullable IBlockState multiblockCenterReplace, @Nullable IBlockState multiblockEdgeReplace, @Nullable IBlockState multiblockCornerReplace) {
		verifyInputs(recipeInputs);
		
		ImmutableList.Builder<ItemStack> stackInputBuilder = new ImmutableList.Builder<>();
		ImmutableList.Builder<String> keyInputBuilder = new ImmutableList.Builder<>();
		
		for(Object o : recipeInputs) {
			if(o instanceof ItemStack) stackInputBuilder.add((ItemStack) o);
			else keyInputBuilder.add((String) o);
		}
		
		this.recipeStacks = stackInputBuilder.build();
		this.recipeOreKeys = keyInputBuilder.build();
		this.totalInputs = recipeStacks.size() + recipeOreKeys.size();
		
		this.recipeOutput = recipeOutput;
		this.manaCost = manaCost;
		this.color1 = color1;
		this.color2 = color2;
		
		this.multiblockCenter = multiblockCenter;
		this.multiblockEdge = multiblockEdge;
		this.multiblockCorner = multiblockCorner;
		
		this.multiblockCenterReplace = multiblockCenterReplace;
		this.multiblockEdgeReplace = multiblockEdgeReplace;
		this.multiblockCornerReplace = multiblockCornerReplace;
	}
	
	/////
	
	public boolean matches(World w, BlockPos platePos, List<ItemStack> inputs) {
		return multiblockMatches(w, platePos) && itemsMatch(inputs);
	}
	
	public boolean itemsMatch(List<ItemStack> userInputs) {
		//Early-exit if the input count is wrong anyways
		if(userInputs.size() != totalInputs) return false;
		
		List<ItemStack> userInputsCopy = new ArrayList<>(userInputs);
		List<ItemStack> recipeStacksCopy = new ArrayList<>(recipeStacks);
		List<String> recipeKeysCopy = new ArrayList<>(recipeOreKeys);
		
		//Check stack inputs
		Iterator<ItemStack> recipeStackIterator = recipeStacksCopy.iterator();
		while(recipeStackIterator.hasNext()) {
			ItemStack recipeStack = recipeStackIterator.next();
			
			Iterator<ItemStack> userInputStackIterator = userInputsCopy.iterator();
			while(userInputStackIterator.hasNext()) {
				ItemStack userInputStack = userInputStackIterator.next();
				
				if(ItemHandlerHelper.canItemStacksStack(recipeStack, userInputStack) && recipeStack.getCount() == userInputStack.getCount()) {
					recipeStackIterator.remove();
					userInputStackIterator.remove();
				}
			}
		}
		
		//Are all of the recipe item stacks present?
		if(!recipeStacksCopy.isEmpty()) return false;
		
		//Check ore dictionary key inputs
		Iterator<String> recipeKeyIterator = recipeKeysCopy.iterator();
		while(recipeKeyIterator.hasNext()) {
			NonNullList<ItemStack> ores = OreDictionary.getOres(recipeKeyIterator.next());
				
				nextOre:
			for(ItemStack ore : ores) {
				Iterator<ItemStack> userInputStackIterator = userInputsCopy.iterator();
				
				while(userInputStackIterator.hasNext()) {
					ItemStack userInputStack = userInputStackIterator.next();
					if(userInputStack.getCount() != 1) continue;
					
					if(ItemHandlerHelper.canItemStacksStack(ore, userInputStack)) {
						recipeKeyIterator.remove();
						userInputStackIterator.remove();
						break nextOre;
					}
				}
			}
		}
		
		//Have all of the itemstacks and orekeys been used?
		return recipeKeysCopy.isEmpty() && userInputsCopy.isEmpty();
	}
	
	public boolean multiblockMatches(World w, BlockPos platePos) {
		BlockPos multiblockPos = platePos.down();
		if(!w.getBlockState(multiblockPos).equals(multiblockCenter)) return false;
		for(EnumFacing nesw : EnumFacing.HORIZONTALS) {
			BlockPos horizOffset = multiblockPos.offset(nesw);
			if(!w.getBlockState(horizOffset).equals(multiblockEdge)) return false;
			
			BlockPos cornerOffset = horizOffset.offset(nesw.rotateY());
			if(!w.getBlockState(cornerOffset).equals(multiblockCorner)) return false;
		}
		
		return true;
	}
	
	//// Yeet
	
	
	public ImmutableList<ItemStack> getRecipeStacks() {
		return recipeStacks;
	}
	
	public ImmutableList<String> getRecipeOreKeys() {
		return recipeOreKeys;
	}
	
	public int getManaCost() {
		return manaCost;
	}
	
	public ItemStack getRecipeOutputCopy() {
		return recipeOutput.copy();
	}
	
	////
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof AgglomerationRecipe)) return false;
		AgglomerationRecipe other = (AgglomerationRecipe) obj;
		
		//the easy ones
		if(other.manaCost != manaCost) return false;
		//if(other.color1 != color1) return false; //Don't check color
		//if(other.color2 != color2) return false;
		if(other.multiblockCenter != multiblockCenter) return false;
		if(other.multiblockEdge != multiblockEdge) return false;
		if(other.multiblockCorner != multiblockCorner) return false;
		if(other.multiblockCenterReplace != multiblockCenterReplace) return false;
		if(other.multiblockEdgeReplace != multiblockEdgeReplace) return false;
		if(other.multiblockCornerReplace != multiblockCornerReplace) return false;
		
		if(!ItemStack.areItemStacksEqual(other.recipeOutput, recipeOutput)) return false;
		
		if(!new HashSet<>(other.recipeOreKeys).equals(new HashSet<>(recipeOreKeys))) return false;
		
		//the tricky one - deep compare these two lists
		List<ItemStack> myStackCopy = new ArrayList<>(recipeStacks);
		for(ItemStack otherStack : other.recipeStacks) {
			myStackCopy.removeIf(stack -> ItemStack.areItemStacksEqual(stack, otherStack));
		}
		
		return myStackCopy.size() == 0;
	}
	
	@Override
	public String toString() {
		return "AgglomerationRecipe{" +
						"recipeStacks=" + recipeStacks +
						", recipeOreKeys=" + recipeOreKeys +
						", recipeOutput=" + recipeOutput +
						", manaCost=" + manaCost +
						", color1=" + color1 +
						", color2=" + color2 +
						", multiblockCenter=" + multiblockCenter +
						", multiblockEdge=" + multiblockEdge +
						", multiblockCorner=" + multiblockCorner +
						", multiblockCenterReplace=" + multiblockCenterReplace +
						", multiblockEdgeReplace=" + multiblockEdgeReplace +
						", multiblockCornerReplace=" + multiblockCornerReplace +
						", totalInputs=" + totalInputs +
						'}';
	}
}
