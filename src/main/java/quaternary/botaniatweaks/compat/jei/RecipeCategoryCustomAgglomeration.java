package quaternary.botaniatweaks.compat.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.botaniatweaks.BotaniaTweaks;

import java.util.List;

public class RecipeCategoryCustomAgglomeration implements IRecipeCategory {
	
	public static final String UID = "botaniatweaks.agglomeration";
	static final int WIDTH = 128;
	static final int HEIGHT = 128;
	
	final String localizedName;
	final IDrawable background;
	
	public RecipeCategoryCustomAgglomeration(IGuiHelper guiHelper) {
		localizedName = I18n.translateToLocal("botania_tweaks.jei.agglomeration.category");
		background = guiHelper.createDrawable(new ResourceLocation(BotaniaTweaks.MODID, "textures/ui/terrasteeloverlay.png"), 0, 0, WIDTH, HEIGHT);
	}
	
	@Override
	public String getUid() {
		return UID;
	}
	
	@Override
	public String getTitle() {
		return localizedName;
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
		
		RecipeWrapperAgglomeration wrapper = (RecipeWrapperAgglomeration) wrap; 
		
		IGuiItemStackGroup stacks = layout.getItemStacks();
		List<List<ItemStack>> ins = ings.getInputs(ItemStack.class);
		List<List<ItemStack>> itemInputs = ins.subList(0, ins.size() - 3);
		List<List<ItemStack>> outs = ings.getOutputs(ItemStack.class);
		int index = 0;
		
		//Set centered row of inputs
		
		int inputCount = itemInputs.size();
		
		int totalInputWidth = ITEM_WIDTH * inputCount + (ITEM_BUFFER * (inputCount - 1));
		
		int posX = WIDTH / 2 - totalInputWidth / 2;
		int posY = 0;
		for(List<ItemStack> in : itemInputs) {
			stacks.init(index, true, posX, posY);
			stacks.set(index, in);
			index++;
			posX += ITEM_WIDTH + ITEM_BUFFER;
		}
		
		posY += ITEM_HEIGHT * 2 + ITEM_BUFFER;
		
		//Set output item
		List<ItemStack> outItem = outs.get(0);
		stacks.init(index, false, WIDTH / 2 - ITEM_WIDTH / 2, posY);
		stacks.set(index, outItem);
		index++;
		
		posY += ITEM_HEIGHT * 4.5;
		
		//Set multiblock under plate
		
		index = setMultiblock(index, stacks, ImmutableList.of(wrapper.multiblockCenterStack), ImmutableList.of(wrapper.multiblockEdgeStack), ImmutableList.of(wrapper.multiblockCornerStack), WIDTH / 2 - ITEM_WIDTH * 3, posY, false, false, false);
		
		ItemStack centerReplace = wrapper.multiblockReplaceCenterStack;
		ItemStack edgeReplace = wrapper.multiblockReplaceEdgeStack;
		ItemStack cornerReplace = wrapper.multiblockReplaceCornerStack;
		
		boolean isCenterReplaced = true;
		boolean isEdgeReplaced = true;
		boolean isCornerReplaced = true;
		
		if(centerReplace == null) {
			isCenterReplaced = false;
			centerReplace = ItemStack.EMPTY;
		}
		
		if(edgeReplace == null) {
			isEdgeReplaced = false;
			edgeReplace = ItemStack.EMPTY;
		}
		
		if(cornerReplace == null) {
			isCornerReplaced = false;
			cornerReplace = ItemStack.EMPTY;
		}
		
		List<ItemStack> drawCenter = ImmutableList.of(isCenterReplaced ? centerReplace : wrapper.multiblockCenterStack);
		List<ItemStack> drawEdge = ImmutableList.of(isEdgeReplaced ? edgeReplace : wrapper.multiblockEdgeStack);
		List<ItemStack> drawCorner = ImmutableList.of(isCornerReplaced ? cornerReplace : wrapper.multiblockCornerStack);
		
		setMultiblock(index, stacks, drawCenter, drawEdge, drawCorner, WIDTH / 2 + ITEM_WIDTH * 3, posY, isCenterReplaced, isEdgeReplaced, isCornerReplaced);
	}
	
	static boolean empty(List<ItemStack> list) {
		return list.isEmpty();
	}
	
	@GameRegistry.ObjectHolder("botania:terraplate")
	public static final Item terraplate = Items.ACACIA_BOAT;
	
	int setMultiblock(int index, IGuiItemStackGroup stacks, List<ItemStack> center, List<ItemStack> edges, List<ItemStack> corners, int posX, int posY, boolean centerOutput, boolean edgeOutput, boolean cornerOutput) {
		stacks.init(index, false, posX - ITEM_WIDTH / 2, posY - MathHelper.floor(ITEM_HEIGHT * 2.5));
		stacks.set(index, new ItemStack(terraplate));
		index++;
		
		if(!empty(center)) {
			//the middle
			stacks.init(index, centerOutput, posX - ITEM_WIDTH / 2, posY - ITEM_HEIGHT / 2);
			stacks.set(index, center);
			index++;
		}
		
		if(!empty(edges)) {
			//the edges
			stacks.init(index, edgeOutput, posX - MathHelper.floor(ITEM_WIDTH * 1.5), posY - ITEM_HEIGHT);
			stacks.set(index, edges);
			index++;
			
			stacks.init(index, edgeOutput, posX + ITEM_WIDTH / 2, posY - ITEM_HEIGHT);
			stacks.set(index, edges);
			index++;
			
			stacks.init(index, edgeOutput, posX - MathHelper.floor(ITEM_WIDTH * 1.5), posY);
			stacks.set(index, edges);
			index++;
			
			stacks.init(index, edgeOutput, posX + ITEM_WIDTH / 2, posY);
			stacks.set(index, edges);
			index++;
		}
		
		if(!empty(corners)) {
			//the corners
			stacks.init(index, cornerOutput, posX - MathHelper.floor(ITEM_WIDTH * 2.5), posY - ITEM_HEIGHT / 2);
			stacks.set(index, corners);
			index++;
			
			stacks.init(index, cornerOutput, posX + MathHelper.floor(ITEM_WIDTH * 1.5), posY - ITEM_HEIGHT / 2);
			stacks.set(index, corners);
			index++;
			
			stacks.init(index, cornerOutput, posX - ITEM_WIDTH / 2, posY - MathHelper.floor(ITEM_HEIGHT * 1.5));
			stacks.set(index, corners);
			index++;
			
			stacks.init(index, cornerOutput, posX - ITEM_WIDTH / 2, posY + ITEM_HEIGHT / 2);
			stacks.set(index, corners);
			index++;
		}
		
		return index;
	}
}
