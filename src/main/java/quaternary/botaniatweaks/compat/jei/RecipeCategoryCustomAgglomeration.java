package quaternary.botaniatweaks.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import quaternary.botaniatweaks.BotaniaTweaks;

import java.util.List;

public class RecipeCategoryCustomAgglomeration implements IRecipeCategory {
	
	public static final String UID = "botaniatweaks.agglomeration";
	static final int WIDTH = 145;
	static final int HEIGHT = 250;
	
	final String localizedName;
	final IDrawable background;
	
	public RecipeCategoryCustomAgglomeration(IGuiHelper guiHelper) {
		localizedName = I18n.translateToLocal("botaniatweaks.jei.category.agglomeration");
		background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
	}
	
	@Override
	public String getUid() {
		return UID;
	}
	
	@Override
	public String getTitle() {
		return I18n.translateToLocal("botaniatweaks.jei.agglomeration.category");
	}
	
	@Override
	public String getModName() {
		return BotaniaTweaks.NAME;
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	static final int ITEM_WIDTH = 16;
	static final int ITEM_HEIGHT = 16;
	static final int ITEM_BUFFER = 4;
	
	@Override
	public void setRecipe(IRecipeLayout layout, IRecipeWrapper wrap, IIngredients ings) {
		if(!(wrap instanceof RecipeWrapperAgglomeration)) return;
		
		IGuiItemStackGroup stacks = layout.getItemStacks();
		List<List<ItemStack>> ins = ings.getInputs(ItemStack.class);
		List<List<ItemStack>> itemInputs = ins.subList(0, ins.size() - 3);
		List<List<ItemStack>> multiblock = ins.subList(ins.size() - 3, ins.size());
		List<List<ItemStack>> outs = ings.getOutputs(ItemStack.class);
		List<List<ItemStack>> multiblockReplacements = outs.subList(outs.size() - 3, outs.size());
		int index = 0;
		
		//Set centered row of inputs
		
		int inputCount = itemInputs.size();
		
		int totalInputWidth = ITEM_WIDTH * inputCount + (ITEM_BUFFER * (inputCount - 1));
		
		int posX = WIDTH / 2 - totalInputWidth / 2;
		int posY = 40;
		for(List<ItemStack> in : itemInputs) {
			stacks.init(index, true, posX, posY);
			stacks.set(index, in);
			index++;
			posX += ITEM_WIDTH + ITEM_BUFFER;
		}
		
		posY += ITEM_HEIGHT + ITEM_BUFFER * 2;
		
		//Set output item
		List<ItemStack> outItem = outs.get(0);
		stacks.init(index, false, WIDTH / 2 - ITEM_WIDTH / 2, posY);
		stacks.set(index, outItem);
		index++;
		
		//Set multiblock under plate
		posY += ITEM_HEIGHT * 3 + ITEM_BUFFER;
		index = setMultiblock(index, stacks, multiblock.get(0), multiblock.get(1), multiblock.get(2), WIDTH / 2, posY, false);
		
		//Set multiblock replacements
		posY += ITEM_HEIGHT * 3 + ITEM_BUFFER;
		setMultiblock(index, stacks, multiblockReplacements.get(0), multiblockReplacements.get(1), multiblockReplacements.get(2), WIDTH / 2, posY, false);
	}
	
	int setMultiblock(int index, IGuiItemStackGroup stacks, List<ItemStack> center, List<ItemStack> edges, List<ItemStack> corners, int posX, int posY, boolean output) {
		if(!center.isEmpty() && !center.get(0).isEmpty()) {
			//the middle
			stacks.init(index, output, posX - ITEM_WIDTH / 2, posY - ITEM_HEIGHT / 2);
			stacks.set(index, center);
			index++;
		}
		
		if(!edges.isEmpty() && !edges.get(0).isEmpty()) {
			//the edges
			stacks.init(index, output, posX - MathHelper.floor(ITEM_WIDTH * 1.5), posY - ITEM_HEIGHT);
			stacks.set(index, edges);
			index++;
			
			stacks.init(index, output, posX + ITEM_WIDTH / 2, posY - ITEM_HEIGHT);
			stacks.set(index, edges);
			index++;
			
			stacks.init(index, output, posX - MathHelper.floor(ITEM_WIDTH * 1.5), posY);
			stacks.set(index, edges);
			index++;
			
			stacks.init(index, output, posX + ITEM_WIDTH / 2, posY);
			stacks.set(index, edges);
			index++;
		}
		
		if(!corners.isEmpty() && !corners.get(0).isEmpty()) {
			//the corners
			stacks.init(index, output, posX - MathHelper.floor(ITEM_WIDTH * 2.5), posY - ITEM_HEIGHT / 2);
			stacks.set(index, corners);
			index++;
			
			stacks.init(index, output, posX + MathHelper.floor(ITEM_WIDTH * 1.5), posY - ITEM_HEIGHT / 2);
			stacks.set(index, corners);
			index++;
			
			stacks.init(index, output, posX - ITEM_WIDTH / 2, posY - MathHelper.floor(ITEM_HEIGHT * 1.5));
			stacks.set(index, corners);
			index++;
			
			stacks.init(index, output, posX - ITEM_WIDTH / 2, posY + ITEM_HEIGHT / 2);
			stacks.set(index, corners);
			index++;
		}
		
		return index;
	}
}
