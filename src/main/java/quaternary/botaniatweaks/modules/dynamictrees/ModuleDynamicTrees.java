package quaternary.botaniatweaks.modules.dynamictrees;

import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.IModule;
import vazkii.botania.common.block.ModBlocks;

public class ModuleDynamicTrees implements IModule {
	public static boolean altGrassTrees = false;
	
	@Override
	public void readConfig(Configuration config) {
		Property proop = config.get("compat.dynamictrees", "customGrassTrees", true, "Should you be able to plant trees from Dynamic Trees on other Botania grasses?");
		proop.setRequiresMcRestart(true); //:thonk:
		altGrassTrees = proop.getBoolean();
	}
	
	@Override
	public void init() {
		if(altGrassTrees) {
			BotaniaTweaks.LOG.info("Expanding Dynamic Trees's definition of grass to include Botania variant grasses too...");
			DirtHelper.registerSoil(ModBlocks.altGrass, DirtHelper.DIRTLIKE);
		}
	}
}
