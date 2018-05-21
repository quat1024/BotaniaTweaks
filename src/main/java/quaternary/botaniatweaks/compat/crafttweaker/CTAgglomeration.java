package quaternary.botaniatweaks.compat.crafttweaker;

import com.blamejared.mtlib.helpers.InputHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
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

import javax.annotation.Nullable;

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
	public static void addRecipe(
					IItemStack output,
					IIngredient[] inputs,
					@Optional Integer manaCost,
					@Optional Integer color1,
					@Optional Integer color2,
					@Optional IIngredient center, //Usually I hate formatting like this but uh, yeah.
					@Optional IIngredient edge,   //Any guesses as to why I added the companion object way?
					@Optional IIngredient corner,
					@Optional IIngredient centerReplace,
					@Optional IIngredient edgeReplace,
					@Optional IIngredient cornerReplace
	) {
		CTHandler.ADD_ACTIONS.add(new AddAction(buildAgglomerationRecipe(output, inputs, manaCost, color1, color2, center, edge, corner, centerReplace, edgeReplace, cornerReplace)));
	}
	
	@ZenMethod
	public static void removeRecipe(
					IItemStack output,
					IIngredient[] inputs,
					@Optional Integer manaCost,
					@Optional Integer color1,
					@Optional Integer color2,
					@Optional IIngredient center,
					@Optional IIngredient edge,
					@Optional IIngredient corner,
					@Optional IIngredient centerReplace,
					@Optional IIngredient edgeReplace,
					@Optional IIngredient cornerReplace
	) {
		CTHandler.REMOVE_ACTIONS.add(new RemoveAction(buildAgglomerationRecipe(output, inputs, manaCost, color1, color2, center, edge, corner, centerReplace, edgeReplace, cornerReplace)));
	}
	
	//The way that's kinda in between the two
	@ZenMethod
	public static void addRecipe(
					IItemStack output,
					IIngredient[] inputs,
					@Optional Integer manaCost,
					@Optional Integer color1,
					@Optional Integer color2,
					@Optional CTAgglomerationMultiblock multiblock
	) {
		addRecipe(output, inputs, manaCost, color1, color2, multiblock.center, multiblock.edge, multiblock.corner, multiblock.centerReplace, multiblock.edgeReplace, multiblock.cornerReplace);
	}
	
	@ZenMethod
	public static void removeRecipe(
					IItemStack output,
					IIngredient[] inputs,
					@Optional Integer manaCost,
					@Optional Integer color1,
					@Optional Integer color2,
					@Optional CTAgglomerationMultiblock multiblock
	) {
		removeRecipe(output, inputs, manaCost, color1, color2, multiblock.center, multiblock.edge, multiblock.corner, multiblock.centerReplace, multiblock.edgeReplace, multiblock.cornerReplace);
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
		public int color1 = 0x0000FF; //todo accurate color
		@ZenProperty
		public int color2 = 0x00FF00; //todo accurate color
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
		public CTAgglomerationRecipe inputs(IIngredient[] inputs) {
			this.inputs = inputs;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationRecipe manaCost(int manaCost) {
			this.manaCost = manaCost;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationRecipe color1(int color1) {
			this.color1 = color1;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationRecipe colo2(int color2) {
			this.color2 = color2;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationRecipe multiblock(CTAgglomerationMultiblock multiblock) {
			this.multiblock = multiblock;
			return this;
		}
		
		//internal use !
		private AgglomerationRecipe toAgglomerationRecipe() {
			return buildAgglomerationRecipe(
							output,
							inputs,
							manaCost,
							color1,
							color2,
							multiblock.center,
							multiblock.edge,
							multiblock.corner,
							multiblock.centerReplace,
							multiblock.edgeReplace,
							multiblock.cornerReplace
			);
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
		public IIngredient centerReplace = null;
		@ZenProperty
		public IIngredient edgeReplace = null;
		@ZenProperty
		public IIngredient cornerReplace = null;
		
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
		public CTAgglomerationMultiblock all(IIngredient all) {
			this.center = this.corner = this.edge = all;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock checker(IIngredient five, IIngredient four) {
			this.center = this.corner = five;
			this.edge = four;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock centerReplace(IIngredient centerReplace) {
			this.centerReplace = centerReplace;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock edgeReplace(IIngredient edgeReplace) {
			this.edgeReplace = edgeReplace;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock cornerReplace(IIngredient cornerReplace) {
			this.cornerReplace = cornerReplace;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock allReplace(IIngredient allReplace) {
			this.centerReplace = this.cornerReplace = this.edgeReplace = allReplace;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock checkerReplace(IIngredient fiveReplace, IIngredient fourReplace) {
			this.centerReplace = this.cornerReplace = fiveReplace;
			this.edgeReplace = fourReplace;
			return this;
		}
		
		//shorthands
		@ZenMethod
		public CTAgglomerationMultiblock consumeCenter() {
			this.centerReplace = MCItemStack.EMPTY;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock consumeEdge() {
			this.edgeReplace = MCItemStack.EMPTY;
			return this;
		}
		
		@ZenMethod
		public CTAgglomerationMultiblock consumeCorner() {
			this.cornerReplace = MCItemStack.EMPTY;
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
		if(ing == null) return null;
		
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
	
	private static AgglomerationRecipe buildAgglomerationRecipe(
					IItemStack output,
					IIngredient[] inputs,
					@Nullable Integer manaCostIn,
					@Nullable Integer color1In,
					@Nullable Integer color2In,
					@Nullable IIngredient center,
					@Nullable IIngredient edge,
					@Nullable IIngredient corner,
					@Nullable IIngredient centerReplace,
					@Nullable IIngredient edgeReplace,
					@Nullable IIngredient cornerReplace
	) {
		//Validation
		Preconditions.checkNotNull(output, "Recipe output must be defined!");
		Preconditions.checkNotNull(inputs, "Recipe inputs must be defined!");
		
		int providedMultiblockStates = 0;
		providedMultiblockStates += center == null ? 0 : 1;
		providedMultiblockStates += edge == null ? 0 : 1;
		providedMultiblockStates += corner == null ? 0 : 1;
		Preconditions.checkArgument(providedMultiblockStates == 0 || providedMultiblockStates == 3, "The multiblock must be completely defined or not defined at all!");
		
		//actual recipe figuring
		ImmutableList<Object> ins = ImmutableList.copyOf(InputHelper.toObjects(inputs));
		ItemStack out = InputHelper.toStack(output);
		
		int manaCost = 500_000;
		if(manaCostIn != null) manaCost = manaCostIn;
		
		int color1 = 0x0000FF; //todo sane default color
		if(color1In != null) color1 = color1In;
		
		int color2 = 0x00FF00; //todo sane default color
		if(color2In != null) color2 = color2In;
		
		IBlockState centerState;
		IBlockState edgeState;
		IBlockState cornerState;
		
		if(providedMultiblockStates == 0) {
			centerState = ModBlocks.livingrock.getDefaultState();
			edgeState = Blocks.LAPIS_BLOCK.getDefaultState();
			cornerState = ModBlocks.livingrock.getDefaultState();
		} else { // == 3
			centerState = toMinecraftBlockstate(center);
			edgeState = toMinecraftBlockstate(edge);
			cornerState = toMinecraftBlockstate(corner);
		}
		
		IBlockState centerStateReplace = toMinecraftBlockstate(centerReplace);
		IBlockState edgeStateReplace = toMinecraftBlockstate(edgeReplace);
		IBlockState cornerStateReplace = toMinecraftBlockstate(cornerReplace);
		
		return new AgglomerationRecipe(ins, out, manaCost, color1, color2, centerState, edgeState, cornerState, centerStateReplace, edgeStateReplace, cornerStateReplace);
	}
}
