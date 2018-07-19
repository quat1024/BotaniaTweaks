package quaternary.botaniatweaks.modules.botania.lexi;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;
import quaternary.botaniatweaks.modules.botania.recipe.AgglomerationRecipe;
import quaternary.botaniatweaks.modules.shared.helper.MiscHelpers;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.common.lexicon.page.PageRecipe;

import java.util.ArrayList;
import java.util.List;

public class PageCustomTerrasteel extends PageRecipe {
	private static final ResourceLocation terrasteelOverlay = new ResourceLocation("botania:textures/gui/terrasteelOverlay.png");
	
	private final AgglomerationRecipe recipe;
	
	List<List<ItemStack>> inputStacks = new ArrayList<>();
	
	public PageCustomTerrasteel(String unlocalizedName, AgglomerationRecipe recipe) {
		super(unlocalizedName);
		this.recipe = recipe;
		
		for(ItemStack stack : recipe.recipeStacks) {
			inputStacks.add(ImmutableList.of(stack));
		}
		
		for(String oreKey : recipe.recipeOreKeys) {
			inputStacks.add(MiscHelpers.getAllSubtypes(OreDictionary.getOres(oreKey)));
		}
	}
	
	@GameRegistry.ItemStackHolder("botania:terraplate")
	public static final ItemStack terraPlate = ItemStack.EMPTY;
	
	@Override
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		IBlockState multiblockCenter = recipe.multiblockCenter;
		IBlockState multiblockEdge = recipe.multiblockEdge;
		IBlockState multiblockCorner = recipe.multiblockCorner;
		
		ItemStack multiblockCenterStack = MiscHelpers.stackFromState(multiblockCenter);
		ItemStack multiblockEdgeStack = MiscHelpers.stackFromState(multiblockEdge);
		ItemStack multiblockCornerStack = MiscHelpers.stackFromState(multiblockCorner);
		
		//Draw multiblock
		GlStateManager.translate(0F, 0F, -10F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 103, multiblockCornerStack, false);
		
		GlStateManager.translate(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 7, gui.getTop() + 106, multiblockEdgeStack, false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 6, gui.getTop() + 106, multiblockEdgeStack, false);
		
		GlStateManager.translate(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 110, multiblockCenterStack, false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 14, gui.getTop() + 110, multiblockCornerStack, false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 13, gui.getTop() + 110, multiblockCornerStack, false);
		
		GlStateManager.translate(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 - 6, gui.getTop() + 114, multiblockEdgeStack, false);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 7, gui.getTop() + 114, multiblockEdgeStack, false);
		
		GlStateManager.translate(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8 + 1, gui.getTop() + 117, multiblockCornerStack, false);
		
		GlStateManager.translate(0F, 0F, 5F);
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 102, terraPlate, false);
		GlStateManager.translate(0F, 0F, -10F);
		
		//Draw inputs (in an arc around the multiblock)
		int circleCenterX = gui.getLeft() + gui.getWidth() / 2 - 8;
		int circleCenterY = gui.getTop() + 110;
		int circleRadius = 30;
		
		List<ItemStack> drawStacks = new ArrayList<>(inputStacks.size());
		int seconds = (int) (Minecraft.getSystemTime() / 1000);
		
		for(List<ItemStack> stackList : inputStacks) {
			drawStacks.add(stackList.get(seconds % stackList.size()));
		}
		
		double tau = 2 * Math.PI;
		double angleBetweenItems = tau / 10d;
		
		double angle = (tau * -.25) - ((drawStacks.size() - 1) / 2d * angleBetweenItems);
		
		for(ItemStack stack : drawStacks) {
			double xOffset = Math.cos(angle) * circleRadius;
			double yOffset = Math.sin(angle) * circleRadius;
			
			renderItem(gui, circleCenterX + xOffset, circleCenterY + yOffset, stack, false);
			
			angle += angleBetweenItems;
		}
		
		//Draw output
		renderItem(gui, gui.getLeft() + gui.getWidth() / 2 - 8, gui.getTop() + 30, recipe.recipeOutput, false);
		
		//Draw arrow
		Minecraft.getMinecraft().renderEngine.bindTexture(terrasteelOverlay);
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GlStateManager.disableBlend();
	}
	
	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		LexiconRecipeMappings.map(recipe.getRecipeOutputCopy(), entry, index);
	}
	
	@Override
	public List<ItemStack> getDisplayedRecipes() {
		return ImmutableList.of(recipe.getRecipeOutputCopy());
	}
}
