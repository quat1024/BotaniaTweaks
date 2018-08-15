package quaternary.botaniatweaks.modules.shared.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import quaternary.botaniatweaks.BotaniaTweaks;

public class ConfigUpdater {
	public static int CONFIG_FILE_VERSION = 2;
	
	private static void log(String message) {
		BotaniaTweaks.LOG.info("Config updater says: " + message);
	}
	
	static void updateConfig(Configuration config) {
		int version;
		try {
			version = Integer.parseInt(config.getLoadedConfigVersion());
		} catch (NumberFormatException e) {
			throw new RuntimeException("Problem parsing configuration file version - did you mess with it? :P");
		}
		
		if(version > CONFIG_FILE_VERSION) {
			throw new IllegalArgumentException("This config file is from the future! This version of Botania Tweaks only knows about config file versions up to " + CONFIG_FILE_VERSION + "!");
		}
		
		boolean dirtyConfig = false;
		
		if(version == 1) {
			updatev1tov2(config);
			version = 2;
			dirtyConfig = true;
		}
		
		if(dirtyConfig) {
			BotaniaTweaksConfig.readConfig();
		}
	}
	
	static void updatev1tov2(Configuration config) {
		log("Updating version 1 config to version 2");
		//dootable agricraft to new location
		if(config.hasKey("compat", "dootableAgricraft")) {
			if(Loader.isModLoaded("agricraft")) {
				log("Moving the dootable Agricraft config to its new location");
				config.moveProperty("compat", "dootableAgricraft", "compat.agricraft");
			} else {
				log("Removing the dootable Agricraft config since you don't have it loaded");
				config.getCategory("compat").remove("dootableAgricraft");
			}
		}
		
		//replaced this tweak with another different one! figured if you enabled one you'd be interested in the other
		if(config.hasKey("balance", "superEntropinnyum")) {
			log("Replacing the super entropinnyum tweak with the \"cheap flint-to-gunpowder\" tweak");
			//TODO implement that
			boolean wasEnabled = config.getBoolean("superEntropinnyum", "balance", false, "");
			config.get("balance", "cheapFlintToPowder", false).set(wasEnabled);
			config.getCategory("balance").remove("superEntropinnyum");
		}
		
		//totally removed this one
		if(config.hasKey("balance", "corporeaSpork")) {
			config.getCategory("balance").remove("corporeaSpork");
		}
	}
}
