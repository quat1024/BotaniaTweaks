package quaternary.botaniatweaks.recipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.StorageVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import java.util.*;

public class AgglomerationRecipes {
	public static ArrayList<AgglomerationRecipe> recipes = new ArrayList<>();
	
	public static void init() {
		//imitate the regular agglomeration recipe
		register(new AgglomerationRecipe(
			ImmutableList.of(manaResource(MANA_DIAMOND), manaResource(MANASTEEL), manaResource(MANA_PEARL)),
			manaResource(TERRASTEEL),
			500_000
		));
		
		//example recipes
		//TODO norelease remove these dumb recipes
		register(new AgglomerationRecipe(
			ImmutableList.of(new ItemStack(Items.APPLE), new ItemStack(Items.GOLDEN_APPLE)),
			new ItemStack(ModItems.infiniteFruit),
			50_000
		));
		
		register(new AgglomerationRecipe(
			ImmutableList.of(new ItemStack(Items.STONE_PICKAXE)),
			new ItemStack(ModItems.manasteelPick),
			50_000,
			0x0088FF,
			ModBlocks.storage.getDefaultState().withProperty(BotaniaStateProps.STORAGE_VARIANT, StorageVariant.MANASTEEL),
			ModBlocks.storage.getDefaultState().withProperty(BotaniaStateProps.STORAGE_VARIANT, StorageVariant.MANASTEEL),
			ModBlocks.storage.getDefaultState().withProperty(BotaniaStateProps.STORAGE_VARIANT, StorageVariant.MANASTEEL)
		));
	}
	
	public static void register(AgglomerationRecipe recipe) {
		recipes.add(recipe);
	}
	
	public static void unregister(AgglomerationRecipe recipe) {
		recipes.remove(recipe);
	}
	
	//TODO: Each agglomeration recipe matcher calls getBlockState 9 times.
	//That feels wasteful; can I pass in a list?
	public static Optional<AgglomerationRecipe> findMatchingRecipe(World w, BlockPos platePos, List<ItemStack> inputs) {
		for(AgglomerationRecipe recipe : recipes) {
			if(recipe.matches(w, platePos, inputs)) return Optional.of(recipe);
		}
		return Optional.empty();
	}
	
	public static boolean containsItem(ItemStack stack) {
		for(AgglomerationRecipe recipe : recipes) {
			for(ItemStack recipeStack : recipe.getRecipeStacks()) {
				if(ItemHandlerHelper.canItemStacksStack(stack, recipeStack)) {
					return true;
				}
			}
			
			for(String oreKey : recipe.getRecipeOreKeys()) {
				for(ItemStack oreStack : OreDictionary.getOres(oreKey)) {
					if(ItemHandlerHelper.canItemStacksStack(stack, oreStack)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	//METADATA GOD ITEMS: not even once
	static int MANASTEEL = 0;
	static int MANA_PEARL = 1;
	static int MANA_DIAMOND = 2;
	static int TERRASTEEL = 4;
	private static ItemStack manaResource(int meta) {
		return new ItemStack(ModItems.manaResource, 1, meta);
	}
}
