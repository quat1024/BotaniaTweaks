package quaternary.botaniatweaks.modules.agricraft;

import net.minecraftforge.common.config.Configuration;
import quaternary.botaniatweaks.modules.IModule;

public class ModuleAgricraft implements IModule {
	@Override
	public void readConfig(Configuration config) {
		AgricraftConfig.readConfig(config);
	}
}
