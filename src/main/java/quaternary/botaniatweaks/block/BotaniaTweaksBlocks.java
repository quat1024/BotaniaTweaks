package quaternary.botaniatweaks.block;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.etc.Util;
import vazkii.botania.common.block.ModBlocks;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BotaniaTweaksBlocks {
	public static void registerBlocks(IForgeRegistry<Block> reg) {
		registerNonOverrides(reg);
	}
	
	public static <T extends Block> T createBlock(T block, String name) {
		block.setRegistryName(new ResourceLocation(BotaniaTweaks.MODID, name));
		block.setUnlocalizedName(BotaniaTweaks.MODID + "." + name);
		block.setCreativeTab(BotaniaTweaks.TAB);
		
		return block;
	}
	
	private static void registerNonOverrides(IForgeRegistry<Block> reg) {
		for(int compressionLevel = 1; compressionLevel <= 8; compressionLevel++) {
			String regName = "compressed_tiny_potato_" + compressionLevel;
			reg.register(createBlock(new BlockCompressedTinyPotato(compressionLevel), regName));
		}
		
		reg.register(createBlock(new BlockPottedTinyPotato(), "potted_tiny_potato"));
	}
	
	public static void registerOverrides() {
		BotaniaTweaks.LOG.info("Creating Botania Tweaks registry replacement blocks. Forge will print some safe-to-ignore warnings.");
		
		Map<String, Block> botaniaRegistryReplacements = new HashMap<>();
		
		botaniaRegistryReplacements.put("rfGenerator", new BlockNerfedManaFluxfield());
		botaniaRegistryReplacements.put("terraPlate", new BlockCustomAgglomerationPlate());
		botaniaRegistryReplacements.put("openCrate", new BlockCustomOpenCrate());
		
		//Classload botania's ModBlocks early.
		//This triggers all the blocks to be constructed because it's done at clinit.
		//That *also* triggers a bunch of setRegistryNames. I'm the active mod container,
		//so we do a little hackery to prevent that printing 1 million override warnings.
		Loader loader = Loader.instance();
		ModContainer myContainer = loader.activeModContainer();
		ModContainer botaniaContainer = Util.getBotaniaModContainer();
		
		loader.setActiveModContainer(botaniaContainer);
		ModBlocks.cacophonium.getDefaultState(); //Do whatever, as long as it loads the class it's ok.
		loader.setActiveModContainer(myContainer);
		
		//Swoop in and patch the block references to the botania blocks, as soon as possible
		for(Map.Entry<String, Block> entry : botaniaRegistryReplacements.entrySet()) {
			try {
				String fieldName = entry.getKey();
				Block patchedBlock = entry.getValue();
				
				Field f = ReflectionHelper.findField(ModBlocks.class, fieldName);
				Util.makeNonFinal(f);
				f.set(null, patchedBlock);
			} catch (Exception e) {
				throw new RuntimeException("There was a problem patching a Botania ModBlocks field", e);
			}
		}
	}
}
