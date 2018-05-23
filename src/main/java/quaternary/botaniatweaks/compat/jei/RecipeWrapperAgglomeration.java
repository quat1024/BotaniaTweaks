package quaternary.botaniatweaks.compat.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import quaternary.botaniatweaks.recipe.AgglomerationRecipe;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nullable;
import java.util.List;

public class RecipeWrapperAgglomeration implements IRecipeWrapper {
	AgglomerationRecipe recipe;
	
	List<List<ItemStack>> inputs;
	List<ItemStack> outputs;
	
	ItemStack multiblockCenterStack;
	ItemStack multiblockEdgeStack;
	ItemStack multiblockCornerStack;
	
	//yeah i know it's stupid
	
	@Nullable
	ItemStack multiblockReplaceCenterStack;
	@Nullable
	ItemStack multiblockReplaceEdgeStack;
	@Nullable
	ItemStack multiblockReplaceCornerStack;
	
	int manaCost;
	
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
		multiblockCenterStack = stackFromState(recipe.multiblockCenter);
		multiblockEdgeStack = stackFromState(recipe.multiblockEdge);
		multiblockCornerStack = stackFromState(recipe.multiblockCorner);
		
		bob.add(ImmutableList.of(multiblockCenterStack));
		bob.add(ImmutableList.of(multiblockEdgeStack));
		bob.add(ImmutableList.of(multiblockCornerStack));
		
		ImmutableList.Builder<ItemStack> joe = ImmutableList.builder();
		
		//Recipe output
		joe.add(recipe.getRecipeOutputCopy());
		
		//The multiblock replacements
		multiblockReplaceCenterStack = stackFromState(recipe.multiblockCenterReplace);
		multiblockReplaceEdgeStack = stackFromState(recipe.multiblockEdgeReplace);
		multiblockReplaceCornerStack = stackFromState(recipe.multiblockCornerReplace);
		
		if(multiblockReplaceCenterStack != null) {
			joe.add(multiblockCenterStack);
		}
		
		if(multiblockReplaceEdgeStack != null) {
			joe.add(multiblockCenterStack);
		}
		
		if(multiblockReplaceCornerStack != null) {
			joe.add(multiblockCenterStack);
		}
		
		inputs = bob.build();
		outputs = joe.build();
		
		manaCost = recipe.manaCost;
	}
	
	ItemStack stackFromState(IBlockState state) {
		if(state == null) return null;
		
		try {
			return state.getBlock().getItem(null, null, state); //Ugh
		} catch(Exception e) {
			return ItemStack.EMPTY;
		}
	}
	
	@Override
	public void getIngredients(IIngredients ing) {
		ing.setInputLists(ItemStack.class, inputs);
		ing.setOutputs(ItemStack.class, outputs);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		GlStateManager.enableAlpha();
		HUDHandler.renderManaBar(14, 60, 0x0000FF, 0.75f, manaCost, TilePool.MAX_MANA);
		GlStateManager.disableAlpha();
	}
}
