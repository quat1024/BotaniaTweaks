package quaternary.botaniatweaks.modules.botania.block;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.shared.helper.MiscHelpers;
import vazkii.botania.common.block.ModBlocks;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BotaniaRegistryReplacements {	
	public static void registerOverrides() {
		BotaniaTweaks.LOG.info("Creating Botania Tweaks registry replacement blocks. Forge will print some safe-to-ignore warnings.");
		
		Map<String, Block> botaniaRegistryReplacements = new HashMap<>();
		
		botaniaRegistryReplacements.put("rfGenerator", new BlockNerfedManaFluxfield());
		botaniaRegistryReplacements.put("terraPlate", new BlockCustomAgglomerationPlate());
		botaniaRegistryReplacements.put("openCrate", new BlockCustomOpenCrate());
		botaniaRegistryReplacements.put("pylon", new BlockCustomManaPylon());
		
		//Classload botania's ModBlocks early.
		//This triggers all the blocks to be constructed because it's done at clinit.
		//That *also* triggers a bunch of setRegistryNames. I'm the active mod container,
		//so we do a little hackery to prevent that printing 1 million override warnings.
		Loader loader = Loader.instance();
		ModContainer myContainer = loader.activeModContainer();
		ModContainer botaniaContainer = MiscHelpers.getBotaniaModContainer();
		
		loader.setActiveModContainer(botaniaContainer);
		ModBlocks.cacophonium.getDefaultState(); //Do whatever, as long as it loads the class it's ok.
		loader.setActiveModContainer(myContainer);
		
		//Swoop in and patch the block references to the botania blocks, as soon as possible
		for(Map.Entry<String, Block> entry : botaniaRegistryReplacements.entrySet()) {
			try {
				String fieldName = entry.getKey();
				Block patchedBlock = entry.getValue();
				
				Field f = ReflectionHelper.findField(ModBlocks.class, fieldName);
				MiscHelpers.makeNonFinal(f);
				f.set(null, patchedBlock);
			} catch (Exception e) {
				throw new RuntimeException("There was a problem patching a Botania ModBlocks field", e);
			}
		}
	}
}
