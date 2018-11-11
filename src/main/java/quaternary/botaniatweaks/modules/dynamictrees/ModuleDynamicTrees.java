package quaternary.botaniatweaks.modules.dynamictrees;

import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.IModule;
import vazkii.botania.common.block.ModBlocks;

import java.util.HashSet;

public class ModuleDynamicTrees implements IModule {
	public static boolean altGrassTrees = false;
	
	@Override
	public void readConfig(Configuration config) {
		Property proop = config.get("compat.dynamictrees", "altGrassTrees", true, "Should you be able to plant trees from Dynamic Trees on other Botania grasses?");
		proop.setRequiresMcRestart(true); //:thonk:
		altGrassTrees = proop.getBoolean();
	}
	
	@Override
	public void init() {
		if(altGrassTrees) {
			BotaniaTweaks.LOG.info("Expanding Dynamic Trees's definition of grass to include Botania variant grasses too...");
			//Unfortunately there's a way to *add* blocks to the list of acceptable soils
			//But not really a way to *test* whether a block is an acceptable soil or not.
			//(besides a function that takes a world and a pos.)
			//What I'm trying to do is say, for every tree that can be planted on grass, also
			//allow that tree to be planted on Botania variant grass. Maybe there exist trees
			//that are planted on things other than grass and I don't want to make those
			//plantable on the botania variant grasses.
			
			//Luckily there's a secret little HashSet in the Species class!!!
			GameRegistry.findRegistry(Species.class).forEach(s -> {
				try {
					HashSet<Block> treeSoils = ReflectionHelper.getPrivateValue(Species.class, s, "soilList");
					if(treeSoils.contains(Blocks.GRASS)) {
						treeSoils.add(ModBlocks.altGrass);
					}
				} catch(Exception e) {
					//ok just assume it can be planted on soil
					BotaniaTweaks.LOG.error("Problem doing hacky dynamic trees reflective access stuff. More details follow. For now I'll just assume that the sapling " + s.getRegistryName() + " can in fact be planted on botania grass variants. Pls report this as a bug to Botania Tweaks if you see this. Ok the actual error:", e);
					s.addAcceptableSoil(ModBlocks.altGrass);
				}
			});
		}
	}
}
