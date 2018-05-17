package quaternary.botaniatweaks.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.botaniatweaks.recipe.AgglomerationRecipe;
import quaternary.botaniatweaks.recipe.AgglomerationRecipes;

@JEIPlugin
public class BotaniaTweaksJeiPlugin implements IModPlugin {
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new RecipeCategoryCustomAgglomeration(registry.getJeiHelpers().getGuiHelper()));
	}
	
	@GameRegistry.ObjectHolder("botania:terraplate")
	public static final Item terraplate = Items.ACACIA_BOAT;
	
	@Override
	public void register(IModRegistry registry) {
		registry.handleRecipes(AgglomerationRecipe.class, RecipeWrapperAgglomeration::new, RecipeCategoryCustomAgglomeration.UID);
		
		registry.addRecipes(AgglomerationRecipes.recipes, RecipeCategoryCustomAgglomeration.UID);
		
		registry.addRecipeCatalyst(new ItemStack(terraplate), RecipeCategoryCustomAgglomeration.UID);
	}
}
