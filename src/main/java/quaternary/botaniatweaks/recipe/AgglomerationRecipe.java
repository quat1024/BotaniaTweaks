package quaternary.botaniatweaks.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.block.ModBlocks;

import java.util.List;

public class AgglomerationRecipe {
	final ImmutableList<ItemStack> recipeStacks;
	final ImmutableList<String> recipeOreKeys;
	final ItemStack recipeOutput;
	final int manaCost;
	final int color;
	final IBlockState multiblockCenter;
	final IBlockState multiblockEdges;
	final IBlockState multiblockCorners;
	
	final int totalInputs;
	
	private void verifyInputs(ImmutableList<Object> inputs) {
		if(inputs.isEmpty()) throw new IllegalArgumentException("Can't make empty agglomeration recipe");
		
		for(Object o : inputs) {
			if(o instanceof ItemStack || o instanceof String) continue;
			throw new IllegalArgumentException("illegal recipe input " + o);
		}
	}
	
	public AgglomerationRecipe(ImmutableList<Object> recipeInputs, ItemStack recipeOutput, int manaCost, int color, IBlockState multiblockCenter, IBlockState multiblockEdges, IBlockState multiblockCorners) {
		verifyInputs(recipeInputs);
		
		ImmutableList.Builder<ItemStack> stackInputBuilder = new ImmutableList.Builder<>();
		ImmutableList.Builder<String> keyInputBuilder = new ImmutableList.Builder<>();
		
		for(Object o : recipeInputs) {
			if(o instanceof ItemStack) stackInputBuilder.add((ItemStack)o);
			else keyInputBuilder.add((String) o);
		}
		
		this.recipeStacks = stackInputBuilder.build();
		this.recipeOreKeys = keyInputBuilder.build();
		this.totalInputs = recipeStacks.size() + recipeOreKeys.size();
		
		this.recipeOutput = recipeOutput;
		this.manaCost = manaCost;
		this.color = color;
		this.multiblockCenter = multiblockCenter;
		this.multiblockEdges = multiblockEdges;
		this.multiblockCorners = multiblockCorners;
	}
	
	//defines the corners to be the same as the center (like the vanilla livingrock/lapisblock recipe)
	public AgglomerationRecipe(ImmutableList<Object> recipeInputs, ItemStack recipeOutput, int manaCost, int color, IBlockState multiblockCenter, IBlockState multiblockEdges) {
		this(recipeInputs, recipeOutput, manaCost, color, multiblockCenter, multiblockEdges, multiblockCenter);
	}
	
	public AgglomerationRecipe(ImmutableList<Object> recipeInputs, ItemStack recipeOutput, int manaCost, int color) {
		this(recipeInputs, recipeOutput, manaCost, color, ModBlocks.livingrock.getDefaultState(), Blocks.LAPIS_BLOCK.getDefaultState(), ModBlocks.livingrock.getDefaultState());
	}
	
	public AgglomerationRecipe(ImmutableList<Object> recipeInputs, ItemStack recipeOutput, int manaCost) {
		this(recipeInputs, recipeOutput, manaCost, 0x00FF00); //TODO determine the actual default color
	}
	
	/////
	
	public boolean matches(World w, BlockPos platePos, List<ItemStack> inputs) {
		return multiblockMatches(w, platePos) && itemsMatch(inputs);
	}
	
	public boolean itemsMatch(List<ItemStack> inputs) {
		int matches = 0;
		
		//Check stack inputs
		for(ItemStack recipeStack : recipeStacks) {
			for(ItemStack inputStack : inputs) {
				if(ItemHandlerHelper.canItemStacksStack(recipeStack, inputStack)) {
					matches++;
					break; //try the next input stack
				}
			}
		}
		
		if(matches == totalInputs) return true;
		
		//Check recipe inputs that are ore dictionary keys
		for(String oreKey : recipeOreKeys) {
			NonNullList<ItemStack> ores = OreDictionary.getOres(oreKey);
			
			for(ItemStack inputStack : inputs) {
				if(inputStack.getCount() != 1) continue;
				
				for(ItemStack ore : ores) {
					if(ItemHandlerHelper.canItemStacksStack(ore, inputStack)) {
						matches++;
						break; //try the next input stack
					}
				}
			}
		}
		
		return matches == totalInputs;
	}
	
	public boolean multiblockMatches(World w, BlockPos platePos) {
		BlockPos multiblockPos = platePos.down();
		if(!w.getBlockState(multiblockPos).equals(multiblockCenter)) return false;
		for(EnumFacing nesw : EnumFacing.HORIZONTALS) {
			BlockPos horizOffset = multiblockPos.offset(nesw);
			if(!w.getBlockState(horizOffset).equals(multiblockEdges)) return false;
			
			BlockPos cornerOffset = horizOffset.offset(nesw.rotateY());
			if(!w.getBlockState(cornerOffset).equals(multiblockCorners)) return false;
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
	
	public int getColor() {
		return color;
	}
	
	public IBlockState getMultiblockCenter() {
		return multiblockCenter;
	}
	
	public IBlockState getMultiblockEdges() {
		return multiblockEdges;
	}
	
	public IBlockState getMultiblockCorners() {
		return multiblockCorners;
	}
}
