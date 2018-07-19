package quaternary.botaniatweaks.modules.agricraft;

import net.minecraftforge.common.config.Configuration;

public class AgricraftConfig {
	public static boolean AGRICRAFT_DOOT = true;
	
	public static void readConfig(Configuration config) {
		//compat
		AGRICRAFT_DOOT = config.getBoolean("dootableAgricraft", "compat.agricraft", true, "Can the Horn of the Wild harvest crops from Agricraft?");
	}
}
