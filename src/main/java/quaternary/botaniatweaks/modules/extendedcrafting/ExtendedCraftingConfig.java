package quaternary.botaniatweaks.modules.extendedcrafting;

import net.minecraftforge.common.config.Configuration;

public class ExtendedCraftingConfig {
	public static boolean EXTENDED_CRAFTY_CRATES = true;
	
	public static void readConfig(Configuration config) {
		//compat
		EXTENDED_CRAFTY_CRATES = config.get("compat.extendedcrafting", "extendedCraftyCrates", true, "Should the Extended Crafty Crates be registered?").setRequiresMcRestart(true).getBoolean();
	}
}
