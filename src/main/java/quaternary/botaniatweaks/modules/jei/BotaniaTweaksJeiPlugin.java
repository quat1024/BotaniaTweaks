package quaternary.botaniatweaks.modules.jei;

import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import quaternary.botaniatweaks.modules.botania.recipe.AgglomerationRecipe;
import quaternary.botaniatweaks.modules.botania.recipe.AgglomerationRecipes;

@JEIPlugin
public class BotaniaTweaksJeiPlugin implements IModPlugin {
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new RecipeCategoryCustomAgglomeration(registry.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	public void register(IModRegistry registry) {
		registry.handleRecipes(AgglomerationRecipe.class, RecipeWrapperAgglomeration::new, RecipeCategoryCustomAgglomeration.UID);
		
		registry.addRecipes(AgglomerationRecipes.recipes, RecipeCategoryCustomAgglomeration.UID);
		
		Item terraPlate = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "terraplate"));
		registry.addRecipeCatalyst(new ItemStack(terraPlate), RecipeCategoryCustomAgglomeration.UID);
	}
}
