package quaternary.botaniatweaks.compat.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import quaternary.botaniatweaks.recipe.AgglomerationRecipe;

import java.util.*;

public class RecipeWrapperAgglomeration implements IRecipeWrapper {
	AgglomerationRecipe recipe;
	
	List<List<ItemStack>> inputs;
	List<ItemStack> outputs;
	
	public RecipeWrapperAgglomeration(AgglomerationRecipe recipe) {
		this.recipe = recipe;
		ImmutableList.Builder<List<ItemStack>> bob = ImmutableList.builder();
		
		//Itemstack inputs
		for(ItemStack stack : recipe.getRecipeStacks()) {
			bob.add(ImmutableList.of(stack));
		}
		
		//Ore key inputs
		for(String key : recipe.getRecipeOreKeys()) {
			bob.add(ImmutableList.copyOf(OreDictionary.getOres(key)));
		}
		
		//The three multiblock pieces
		bob.add(ImmutableList.of(stackFromState(recipe.multiblockCenter)));
		bob.add(ImmutableList.of(stackFromState(recipe.multiblockEdge)));
		bob.add(ImmutableList.of(stackFromState(recipe.multiblockCorner)));
		
		
		ImmutableList.Builder<ItemStack> joe = ImmutableList.builder();
		
		//Recipe output
		joe.add(recipe.getRecipeOutputCopy());
		
		//The multiblock replacements
		joe.add(stackFromState(recipe.multiblockCenterReplace));
		joe.add(stackFromState(recipe.multiblockEdgeReplace));
		joe.add(stackFromState(recipe.multiblockCornerReplace));
		
		inputs = bob.build();
		outputs = joe.build();
	}
	
	ItemStack stackFromState(IBlockState state) {
		if(state == null) return ItemStack.EMPTY;
		
		return state.getBlock().getItem(null, null, state); //Ugh
	}
	
	@Override
	public void getIngredients(IIngredients ing) {
		ing.setInputLists(ItemStack.class, inputs);
		ing.setOutputs(ItemStack.class, outputs);
	}
}
