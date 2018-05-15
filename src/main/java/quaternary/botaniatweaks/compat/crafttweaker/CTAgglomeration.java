package quaternary.botaniatweaks.compat.crafttweaker;

import com.blamejared.mtlib.helpers.InputHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.*;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.mc1120.item.MCItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import quaternary.botaniatweaks.recipe.AgglomerationRecipe;
import quaternary.botaniatweaks.recipe.AgglomerationRecipes;
import stanhebben.zenscript.annotations.*;
import vazkii.botania.common.block.ModBlocks;

@ZenClass("mods.botaniatweaks.Agglomeration")
@ZenRegister
public class CTAgglomeration {
	public static final String NAME = "Botania Tweaks Agglomeration";
	
	@ZenMethod
	public static void removeDefaultRecipe() {
		CTHandler.REMOVE_ACTIONS.add(new RemoveAction(AgglomerationRecipes.defaultRecipe));
	}
	
	//The easy way
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient[] inputs, @Optional Integer manaCostIn, @Optional Integer colorIn, @Optional IIngredient center, @Optional IIngredient edge, @Optional IIngredient corner, @Optional Boolean consumeCenter, @Optional Boolean consumeEdge, @Optional Boolean consumeCorner) {
		CTHandler.ADD_ACTIONS.add(new AddAction(buildAgglomerationRecipe(output, inputs, manaCostIn, colorIn, center, edge, corner, consumeCenter, consumeEdge, consumeCorner)));
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack output, IIngredient[] inputs, @Optional Integer manaCostIn, @Optional Integer colorIn, @Optional IIngredient center, @Optional IIngredient edge, @Optional IIngredient corner, @Optional Boolean consumeCenter, @Optional Boolean consumeEdge, @Optional Boolean consumeCorner) {
		CTHandler.REMOVE_ACTIONS.add(new RemoveAction(buildAgglomerationRecipe(output, inputs, manaCostIn, colorIn, center, edge, corner, consumeCenter, consumeEdge, consumeCorner)));
	}
	
	//The companion object way
	@ZenMethod
	public static void addRecipe(CTAgglomerationRecipe recipe) {
		CTHandler.ADD_ACTIONS.add(new AddAction(recipe.toAgglomerationRecipe()));
	}
	
	@ZenMethod
	public static void removeRecipe(CTAgglomerationRecipe recipe) {
		CTHandler.REMOVE_ACTIONS.add(new RemoveAction(recipe.toAgglomerationRecipe()));
	}
	
	//Companion objects
	@ZenClass("mods.botaniatweaks.AgglomerationRecipe")
	@ZenRegister
	public static class CTAgglomerationRecipe {
		@ZenProperty
		public IItemStack output;
		@ZenProperty
		public IIngredient[] inputs;
		@ZenProperty
		public int manaCost = 500_000;
		@ZenProperty
		public int color = 0x00FF00; //todo accurate color
		@ZenProperty
		public CTAgglomerationMultiblock multiblock = new CTAgglomerationMultiblock();
		
		@ZenMethod
		public static CTAgglomerationRecipe create() {
			return new CTAgglomerationRecipe();
		}
		
		//chainable methods
		@ZenMethod
		public CTAgglomerationRecipe output(IItemStack output) {
			this.output = output;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationRecipe input(IIngredient[] inputs) {
			this.inputs = inputs;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationRecipe manaCost(int manaCost) {
			this.manaCost = manaCost;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationRecipe color(int color) {
			this.color = color;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationRecipe multiblock(CTAgglomerationMultiblock multiblock) {
			this.multiblock = multiblock;
			return this;
		}
		
		//internal use !
		private AgglomerationRecipe toAgglomerationRecipe() {
			return buildAgglomerationRecipe(output, inputs, manaCost, color, multiblock.center, multiblock.edge, multiblock.corner, multiblock.consumeCenter, multiblock.consumeEdge, multiblock.consumeCorner);
		}
	}
	
	@ZenClass("mods.botaniatweaks.AgglomerationMultiblock")
	@ZenRegister
	public static class CTAgglomerationMultiblock {
		@ZenProperty
		public IIngredient center = mcStackFromBlock(ModBlocks.livingrock);
		@ZenProperty
		public IIngredient edge = mcStackFromBlock(Blocks.LAPIS_BLOCK);
		@ZenProperty
		public IIngredient corner = mcStackFromBlock(ModBlocks.livingrock);
		@ZenProperty
		public boolean consumeCenter = false;
		@ZenProperty
		public boolean consumeEdge = false;
		@ZenProperty
		public boolean consumeCorner = false;
		
		
		@ZenMethod
		public static CTAgglomerationMultiblock create() {
			return new CTAgglomerationMultiblock();
		}
		
		//chainable methods
		@ZenMethod
		public CTAgglomerationMultiblock center(IIngredient center) {
			this.center = center;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock edge(IIngredient edge) {
			this.edge = edge;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock corner(IIngredient corner) {
			this.corner = corner;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock consumeCenter(@Optional Boolean consumeCenter) {
			this.consumeCenter = consumeCenter == null ? true : consumeCenter;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock consumeEdge(@Optional Boolean consumeEdge) {
			this.consumeEdge = consumeEdge == null ? true : consumeEdge;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock consumeCorner(@Optional Boolean consumeCorner) {
			this.consumeCorner = consumeCorner == null ? true : consumeCorner;
			return this;
		}
	} 
	
	public static class AddAction implements IAction {
		AgglomerationRecipe recipe;
		
		public AddAction(AgglomerationRecipe recipe) {
			this.recipe = recipe;
		}
		
		@Override
		public void apply() {
			AgglomerationRecipes.register(recipe);
		}
		
		@Override
		public String describe() {
			return "adding agglomeration recipe todo fill this out";
		}
	}
	
	public static class RemoveAction implements IAction {
		AgglomerationRecipe recipe;
		
		public RemoveAction(AgglomerationRecipe recipe) {
			this.recipe = recipe;
		}
		
		@Override
		public void apply() {
			AgglomerationRecipes.unregister(recipe);
		}
		
		@Override
		public String describe() {
			return "removing an agglomeration recipe i guess";
		}
	}
	
	//HELPERS!
	//because ct IBlockStates are kinda useless
	private static IBlockState toMinecraftBlockstate(IIngredient ing) {
		Object obj = InputHelper.toObject(ing);
		
		// >.>
		if(obj instanceof ItemStack) {
			ItemStack stack = (ItemStack) obj;
			return Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata());
		}
		
		// <.<
		if(obj instanceof ILiquidStack) {
			return InputHelper.toFluid((ILiquidStack) obj).getFluid().getBlock().getDefaultState();
		}
		
		throw new IllegalArgumentException("Invalid multiblock item: " + ing);
	}
	
	private static MCItemStack mcStackFromBlock(Block b) {
		return new MCItemStack(new ItemStack(Item.getItemFromBlock(b)));
	}
	
	private static AgglomerationRecipe buildAgglomerationRecipe(IItemStack output, IIngredient[] inputs, Integer manaCostIn, Integer colorIn, IIngredient center, IIngredient edge, IIngredient corner, Boolean consumeCenter, Boolean consumeEdge, Boolean consumeCorner) {
		Preconditions.checkNotNull(output, "Output must be defined!");
		Preconditions.checkNotNull(inputs, "Inputs must be defined!");
		
		ImmutableList<Object> ins = ImmutableList.copyOf(InputHelper.toObjects(inputs));
		ItemStack out = InputHelper.toStack(output);
		
		int manaCost = 500_000;
		if(manaCostIn != null) manaCost = manaCostIn;
		
		int color = 0x00FF00; //todo sane default
		if(colorIn != null) color = colorIn;
		
		IBlockState centerState = ModBlocks.livingrock.getDefaultState();
		IBlockState edgeState = Blocks.LAPIS_BLOCK.getDefaultState();
		IBlockState cornerState = ModBlocks.livingrock.getDefaultState();
		if(edge == null && corner == null && center != null) { //only one defined
			centerState = edgeState = cornerState = toMinecraftBlockstate(center);
		} else if(corner == null && center != null) { //default to checkerboard
			centerState = cornerState = toMinecraftBlockstate(center);
			edgeState = toMinecraftBlockstate(edge);
		} else if (edge != null && center != null){ //all 3 defined
			centerState = toMinecraftBlockstate(center);
			edgeState = toMinecraftBlockstate(edge);
			cornerState = toMinecraftBlockstate(corner);
		}
		
		boolean conCenter = consumeCenter == null ? false : consumeCenter;
		boolean conEdge = consumeEdge == null ? false : consumeEdge;
		boolean conCorner = consumeCorner == null ? false : consumeCorner;
		
		return new AgglomerationRecipe(ins, out, manaCost, color, centerState, edgeState, cornerState, conCenter, conEdge, conCorner);
	}
}
