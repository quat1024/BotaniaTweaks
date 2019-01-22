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
		ImmutableList.Builder<List<ItemStack>> inputs_ = ImmutableList.builder();
		
		//Itemstack inputs
		for(ItemStack stack : recipe.getRecipeStacks()) {
			inputs_.add(ImmutableList.of(stack));
		}
		
		//Ore key inputs
		for(String key : recipe.getRecipeOreKeys()) {
			inputs_.add(ImmutableList.copyOf(OreDictionary.getOres(key)));
		}
		
		//The three multiblock pieces
		multiblockCenterStack = MiscHelpers.stackFromState(recipe.multiblockCenter);
		multiblockEdgeStack = MiscHelpers.stackFromState(recipe.multiblockEdge);
		multiblockCornerStack = MiscHelpers.stackFromState(recipe.multiblockCorner);
		
		inputs_.add(ImmutableList.of(multiblockCenterStack));
		inputs_.add(ImmutableList.of(multiblockEdgeStack));
		inputs_.add(ImmutableList.of(multiblockCornerStack));
		
		ImmutableList.Builder<ItemStack> outputs_ = ImmutableList.builder();
		
		//Recipe output
		outputs_.add(recipe.getRecipeOutputCopy());
		
		//The multiblock replacements
		if(recipe.multiblockCenterReplace != null) {
			multiblockReplaceCenterStack = MiscHelpers.stackFromState(recipe.multiblockCenterReplace);
			outputs_.add(multiblockCenterStack);
		}
		
		if(recipe.multiblockEdgeReplace != null) {
			multiblockReplaceEdgeStack = MiscHelpers.stackFromState(recipe.multiblockEdgeReplace);
			outputs_.add(multiblockReplaceEdgeStack);
		}
		
		if(recipe.multiblockCornerReplace != null) {
			multiblockReplaceCornerStack = MiscHelpers.stackFromState(recipe.multiblockCornerReplace);
			outputs_.add(multiblockReplaceCornerStack);
		}
		
		inputs = inputs_.build();
		outputs = outputs_.build();
		
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
		HUDHandler.renderManaBar(35, 60, 0x0000FF, 0.75f, manaCost, TilePool.MAX_MANA);
		
		if(manaCost > 1_000_000) {
			int roughPoolCount = (250_000 * Math.round(manaCost / 250_000f)) / 1_000_000;
			Minecraft.getMinecraft().fontRenderer.drawString("x" + roughPoolCount, 140, 58, 0x000000);
		}
		
		GlStateManager.disableAlpha();
	}
}
