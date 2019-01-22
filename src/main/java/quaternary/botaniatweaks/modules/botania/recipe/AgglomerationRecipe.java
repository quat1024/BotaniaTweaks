package quaternary.botaniatweaks.modules.botania.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.state.BotaniaStateProps;

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
	
	public boolean matches(World w, BlockPos platePos, List<ItemStack> inputs, IBlockState belowEarly, IBlockState edgeEarly, IBlockState cornerEarly) {
		return multiblockMatches(w, platePos, belowEarly, edgeEarly, cornerEarly) && itemsMatch(inputs);
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
				if(compareStacks(recipeStack, userInputStack) && recipeStack.getCount() == userInputStack.getCount()) {
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
					if(compareStacks(oreStack, userInputStack) && userInputStack.getCount() == 1) {
						usedOreKeyCount++;
						usedUserInputs[i] = true;
					}
				}
			}
		}
		
		return usedOreKeyCount == recipeOreKeys.size();
	}
	
	private EnumFacing[] EAST_SOUTH_WEST = new EnumFacing[] {
		EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST
	};
	
	public boolean multiblockMatches(World w, BlockPos platePos, IBlockState belowEarly, IBlockState edgeEarly, IBlockState cornerEarly) {
		//early-exit if the multiblock isn't even close to correct
		if(!areStatesSimilar(belowEarly, multiblockCenter)) return false;
		if(!areStatesSimilar(edgeEarly, multiblockEdge)) return false;
		if(!areStatesSimilar(cornerEarly, multiblockCorner)) return false;
		
		//check that the multiblock is well-formed (not just one but all 4 match up)
		//logic closely tied to TileCustomAgglomerationPlate stuff, so watch out
		//i intentionally do not check north or northeast since tilecustomagglomerationplate already did
		BlockPos multiblockPos = platePos.down();
		for(EnumFacing esw : EAST_SOUTH_WEST) {
			BlockPos horizOffset = multiblockPos.offset(esw);
			if(!areStatesSimilar(w.getBlockState(horizOffset), multiblockEdge)) return false;
			
			BlockPos cornerOffset = horizOffset.offset(esw.rotateY());
			if(!areStatesSimilar(w.getBlockState(cornerOffset), multiblockCorner)) return false;
		}
		
		return true;
	}
	
	private boolean areStatesSimilar(IBlockState a, IBlockState b) {
		if(a.getBlock() != b.getBlock()) return false;
		else return equalizeDirectionProperties(a).equals(equalizeDirectionProperties(b));
	}
	
	private IBlockState equalizeDirectionProperties(IBlockState state) {
		Collection<IProperty<?>> props = state.getPropertyKeys();
		if(props.contains(BlockDirectional.FACING)) return state.withProperty(BlockDirectional.FACING, EnumFacing.NORTH);
		if(props.contains(BotaniaStateProps.FACING)) return state.withProperty(BotaniaStateProps.FACING, EnumFacing.NORTH);
		
		for(IProperty<?> prop : props) {
			if(prop.getValueClass() != EnumFacing.class) continue;
			if(!prop.getAllowedValues().contains(EnumFacing.NORTH)) continue;
			state = state.withProperty((IProperty<EnumFacing>) prop, EnumFacing.NORTH);
		}
		
		return state;
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
	
	//see RecipePetals#compareStacks
	private static boolean compareStacks(ItemStack recipe, ItemStack supplied) {
		if(recipe.isEmpty() || supplied.isEmpty()) return false;
		if(recipe.getItem() != supplied.getItem()) return false;
		if(recipe.getItemDamage() != supplied.getItemDamage()) return false;
		
		if(!recipe.hasTagCompound()) return true;
		if(!supplied.hasTagCompound()) return false;
		
		NBTTagCompound merged = supplied.getTagCompound().copy();
		merged.merge(recipe.getTagCompound());
		return supplied.getTagCompound().equals(merged);
	}
}
