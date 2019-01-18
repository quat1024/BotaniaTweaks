package quaternary.botaniatweaks.modules.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.botania.recipe.AgglomerationRecipe;
import quaternary.botaniatweaks.modules.botania.recipe.AgglomerationRecipes;
import quaternary.botaniatweaks.modules.extendedcrafting.ModuleExtendedCrafting;
import quaternary.botaniatweaks.modules.shared.helper.ModCompatUtil;

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

		if(Loader.isModLoaded("extendedcrafting")) {
			if (ModuleExtendedCrafting.areCraftersEnabled()) {
				registry.addRecipeCatalyst(ModCompatUtil.getStackFor(new ResourceLocation(BotaniaTweaks.MODID, "basic_extended_crafty_crate")), "extendedcrafting:table_crafting_3x3");
				registry.addRecipeCatalyst(ModCompatUtil.getStackFor(new ResourceLocation(BotaniaTweaks.MODID, "advanced_extended_crafty_crate")), "extendedcrafting:table_crafting_5x5");
				registry.addRecipeCatalyst(ModCompatUtil.getStackFor(new ResourceLocation(BotaniaTweaks.MODID, "elite_extended_crafty_crate")), "extendedcrafting:table_crafting_7x7");
				registry.addRecipeCatalyst(ModCompatUtil.getStackFor(new ResourceLocation(BotaniaTweaks.MODID, "ultimate_extended_crafty_crate")), "extendedcrafting:table_crafting_9x9");
			} else {
				IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
				blacklist.addIngredientToBlacklist(ModCompatUtil.getStackFor(new ResourceLocation(BotaniaTweaks.MODID, "basic_extended_crafty_crate")));
				blacklist.addIngredientToBlacklist(ModCompatUtil.getStackFor(new ResourceLocation(BotaniaTweaks.MODID, "advanced_extended_crafty_crate")));
				blacklist.addIngredientToBlacklist(ModCompatUtil.getStackFor(new ResourceLocation(BotaniaTweaks.MODID, "elite_extended_crafty_crate")));
				blacklist.addIngredientToBlacklist(ModCompatUtil.getStackFor(new ResourceLocation(BotaniaTweaks.MODID, "ultimate_extended_crafty_crate")));
			}
		}

		if(Loader.isModLoaded("avaritia")) {
			registry.addRecipeCatalyst(ModCompatUtil.getStackFor(new ResourceLocation(BotaniaTweaks.MODID, "dire_crafty_crate")), "Avatitia.Extreme");
		}
	}
}
