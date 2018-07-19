package quaternary.botaniatweaks.modules.avaritia;

import net.minecraftforge.common.config.Configuration;

public class AvaritiaConfig {
	public static boolean DIRE_CRAFTY_CRATE = true;
	
	public static void readConfig(Configuration config) {
		//compat
		DIRE_CRAFTY_CRATE = config.get("compat.avaritia", "direCraftyCrate", true, "Should the Dire Crafty Crate be registered?").setRequiresMcRestart(true).getBoolean();
	}
}
