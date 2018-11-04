package quaternary.botaniatweaks.modules.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import quaternary.botaniatweaks.modules.botania.recipe.AgglomerationRecipe;
import quaternary.botaniatweaks.modules.shared.helper.MiscHelpers;
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
		multiblockCenterStack = MiscHelpers.stackFromState(recipe.multiblockCenter);
		multiblockEdgeStack = MiscHelpers.stackFromState(recipe.multiblockEdge);
		multiblockCornerStack = MiscHelpers.stackFromState(recipe.multiblockCorner);
		
		bob.add(ImmutableList.of(multiblockCenterStack));
		bob.add(ImmutableList.of(multiblockEdgeStack));
		bob.add(ImmutableList.of(multiblockCornerStack));
		
		ImmutableList.Builder<ItemStack> joe = ImmutableList.builder();
		
		//Recipe output
		joe.add(recipe.getRecipeOutputCopy());
		
		//The multiblock replacements
		multiblockReplaceCenterStack = MiscHelpers.stackFromState(recipe.multiblockCenterReplace);
		multiblockReplaceEdgeStack = MiscHelpers.stackFromState(recipe.multiblockEdgeReplace);
		multiblockReplaceCornerStack = MiscHelpers.stackFromState(recipe.multiblockCornerReplace);
		
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
	
	@Override
	public void getIngredients(IIngredients ing) {
		ing.setInputLists(VanillaTypes.ITEM, inputs);
		ing.setOutputs(VanillaTypes.ITEM, outputs);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		GlStateManager.enableAlpha();
		HUDHandler.renderManaBar(14, 60, 0x0000FF, 0.75f, manaCost, TilePool.MAX_MANA);
		GlStateManager.disableAlpha();
	}
}
