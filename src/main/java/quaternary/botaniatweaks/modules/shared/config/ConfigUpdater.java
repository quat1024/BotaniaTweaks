package quaternary.botaniatweaks.modules.shared.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.botaniatweaks.BotaniaTweaks;

public class ConfigUpdater {
	public static final int CONFIG_FILE_VERSION = 2;
	
	private static final Logger LOG = LogManager.getLogger(BotaniaTweaks.NAME + " Config Auto-Updater");
	
	private static void log(String message) {
		LOG.info(message);
	}
	
	static void updateConfig(Configuration config) {
		int version;
		try {
			version = Integer.parseInt(config.getLoadedConfigVersion());
		} catch (NumberFormatException e) {
			throw new RuntimeException("Problem parsing configuration file version number! \"" + config.getLoadedConfigVersion() + "\" doesn't look like a valid integer");
		}
		
		if(version > CONFIG_FILE_VERSION) {
			throw new IllegalArgumentException("This version " + version + " config file is from the future! I only know about config file versions up to " + CONFIG_FILE_VERSION + "!");
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
			boolean wasEnabled = config.getBoolean("superEntropinnyum", "balance", false, "");
			config.get("balance", "cheapFlintToPowder", false).set(wasEnabled);
			config.getCategory("balance").remove("superEntropinnyum");
		}
		
		//flipped this key around
		if(config.hasKey("balance.tnt", "allowEntropinnyumDuplicatedTNT")) {
			log("Flipping around the entropinnyum tnt config (from allow to deny)");
			boolean wasAllowed = config.getBoolean("allowEntropinnyumDuplicatedTNT", "balance.tnt", false, "");
			config.get("balance.tnt", "denyEntropinnyumDuplicatedTNT", false).set(!wasAllowed);
			config.getCategory("balance.tnt").remove("allowEntropinnyumDuplicatedTNT");
		}
		
		//totally removed these keys
		if(config.hasKey("balance", "corporeaSpork")) {
			log("Removing the corporea spork config option, since the spork was removed");
			config.getCategory("balance").remove("corporeaSpork");
		}
		
		if(config.hasKey("etc", "pottedTinyPotato")) {
			log("Removing the potted tiny potato config option (it's just always on now)");
			config.getCategory("etc").remove("pottedTinyPotato");
		}
	}
}
