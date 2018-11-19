package quaternary.botaniatweaks.modules.botania.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.fluids.UniversalBucket;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;
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

    public int getBucketCount () {
        // presume buckets aren't going to be in the ore dictionary
        int count = 0;

        for (ItemStack stack : recipeStacks) {
            Item item = stack.getItem();
            if (item instanceof UniversalBucket || item == Items.WATER_BUCKET || item == Items.MILK_BUCKET|| item == Items.LAVA_BUCKET) count++;
        }

        return count;
    }

    public static boolean areItemStacksEqual (@Nonnull ItemStack a, @Nonnull ItemStack b) {
        if (a.isEmpty() || b.isEmpty() || a.getItem() != b.getItem())
            return false;

        if (a.getHasSubtypes() && a.getMetadata() != b.getMetadata())
            return false;

        if (a.hasTagCompound() != b.hasTagCompound())
            return false;

        return (!a.hasTagCompound() || a.getTagCompound().equals(b.getTagCompound()));
    }
	
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
		if(userInputs.size() == 0 || userInputs.size() != totalInputs) return false;
		
		int usedRecipeStackCount = 0;
		int usedOreKeyCount = 0;
		boolean[] usedUserInputs = new boolean[userInputs.size()];
		
		//ensure all recipe stacks are satisfied
		for(ItemStack recipeStack : recipeStacks) {
			for(int i = 0; i < userInputs.size(); i++) {
				if(usedUserInputs[i]) continue; //already matched against a recipe item, don't consume again
				
				ItemStack userInputStack = userInputs.get(i);
				if(ItemHandlerHelper.canItemStacksStack(recipeStack, userInputStack) && recipeStack.getCount() == userInputStack.getCount() || !recipeStack.isStackable() && areItemStacksEqual(recipeStack, userInputStack)) {
					usedRecipeStackCount++;
					usedUserInputs[i] = true;
				}
			}
		}
		
		//user did not supply all of the required item stacks
		if(usedRecipeStackCount != recipeStacks.size()) return false;
		
		//ensure all recipe ore dictionary keys are satisfied
		for(String key : recipeOreKeys) {
			List<ItemStack> matchingOres = OreDictionary.getOres(key);
			for(ItemStack oreStack : matchingOres) {
				for(int i = 0; i < userInputs.size(); i++) {
					if(usedUserInputs[i]) continue;
					
					ItemStack userInputStack = userInputs.get(i);
					if(ItemHandlerHelper.canItemStacksStack(oreStack, userInputStack) && userInputStack.getCount() == 1) {
						usedOreKeyCount++;
						usedUserInputs[i] = true;
					}
				}
			}
		}
		
		return usedOreKeyCount == recipeOreKeys.size();
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
