package quaternary.botaniatweaks.compat.crafttweaker;

import com.blamejared.mtlib.helpers.InputHelper;
import com.google.common.collect.ImmutableList;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import quaternary.botaniatweaks.recipe.AgglomerationRecipe;
import quaternary.botaniatweaks.recipe.AgglomerationRecipes;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.botaniatweaks.Agglomeration")
@ZenRegister
public class CTAgglomeration {
	public static final String NAME = "Botania Tweaks Agglomeration";
	
	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient[] inputs, int cost) {
		ImmutableList<Object> ins = ImmutableList.of(InputHelper.toObjects(inputs));
		ItemStack out = InputHelper.toStack(output);
		CTHandler.ADD_ACTIONS.add(new AddAction(new AgglomerationRecipe(ins, out, cost)));
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
}
