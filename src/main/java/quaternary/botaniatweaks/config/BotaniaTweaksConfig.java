package quaternary.botaniatweaks.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.asm.BotaniaTweakerHooks;
import quaternary.botaniatweaks.modules.shared.lib.GeneratingFlowers;

import java.io.File;
import java.util.*;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = BotaniaTweaks.MODID)
public class BotaniaTweaksConfig {
	public static int MANA_SHOTS_PER_ENERGY_BURST = 1;
	public static int FE_PER_ENERGY_BURST = 30;
	
	public static boolean CREATE_ENDER_AIR_WITH_DISPENSER = true;
	
	public static boolean POTTED_TINY_POTATO = true;
	
	public static int PASSIVE_DECAY_TIMER = 72000;
	public static HashMap<String, Boolean> SHOULD_ALSO_BE_PASSIVE_MAP = new HashMap<>();
	
	public static float MANASTORM_SCALE_FACTOR = 8;
	public static boolean SUPER_ENTROPINNYUM = true;
	public static boolean SUPER_SPECTROLUS = true;
	
	public static boolean AUTO_CORPOREA_SPARK = false;
	
	public static boolean EVERYTHING_APOTHECARY = true;
	
	public static boolean SHEEP_EAT_ALT_GRASS = true;
	
	public static EnumOrechidMode ORECHID_MODE = EnumOrechidMode.DEFAULT;
	
	public static boolean FORCE_VANILLA_TNT = false;
	public static boolean ALLOW_DUPLICATED_TNT = false;
	public static int TNT_DUPE_HEURISTIC = 8;
	
	public static boolean SPORK = true;
	
	public static boolean AGRICRAFT_DOOT = true;
	
	public static boolean ADVANCED_CRAFTY_CRATE = false;
	public static boolean ADVANCED_CRAFTY_CRATE_HARDMODE = false;
	public static int ADVANCED_CRATE_MANA_PER_ITEM = 160;
	
	public static boolean MANA_GENERATION_STATISTICS = false;
	
	static Configuration config;
	
	public static void initConfig() {
		config = new Configuration(new File(Loader.instance().getConfigDir(), "botaniatweaks.cfg"), "1");
		config.load();
		
		readConfig();
	}
	
	public static void readConfig() {
		//balance
		MANASTORM_SCALE_FACTOR = config.getFloat("manastormScaleFactor", "balance", 8f, 1f, 15f, "The default mana output of the Manastorm Charge is multiplied by this amount. Setting this to a value higher than around ~1.38889ish allows for the \"Manastorm Reactor\" build to be profitable.");
		
		SUPER_ENTROPINNYUM = config.getBoolean("superEntropinnyum", "balance", true, "Should the Entropinnyum generate 8x the mana it does by default? This makes it possible to run an Entropinnyum off of, for example, a cobbleworks; by default, the flint-to-gunpowder recipe is much too expensive to make another TNT.\n\nAlso I think this flower is way underpowered in general, but that's just me.");
		
		SUPER_SPECTROLUS = config.getBoolean("superSpectrolus", "balance", true, "Should the Spectrolus generate 10x the mana it does by default? This makes it much cheaper to run; filling a mana pool only requires a little over five stacks of wool, not over a double chest's worth.");
		
		ORECHID_MODE = getEnum(config, "cheapOrechid", "balance", EnumOrechidMode.DEFAULT, "How does the Orechid determine its cost and speed to run?", mode -> {
			switch (mode) {
				case DEFAULT: return "The Orechid will be cheap if Garden of Glass is loaded.";
				case FORCE_GOG: return "The Orechid will always be cheap to run, even if Garden of Glass is not present.";
				case FORCE_NO_GOG: return "The Orechid will be expensive to run, even in Garden of Glass mode.";
				default: return "h"; //h
			}
		}, EnumOrechidMode.class);
		
		BotaniaTweakerHooks.orechidGog = ORECHID_MODE.isGog();
		
		SPORK = config.get("balance", "corporeaSpork", true, "Should crafting recipes with the Spork be enabled? These recipes provide more expensive paths to corporea sparks, but are available earlier in the game (they don't require going to the End or elven technology).").setRequiresMcRestart(true).getBoolean();
		
		ADVANCED_CRAFTY_CRATE = config.getBoolean("advancedCraftyCrate", "balance.craftyCrate", false, "Should the Crafty Crate require mana to craft items?");
		
		ADVANCED_CRAFTY_CRATE_HARDMODE = config.getBoolean("advancedCraftyCrateHardMode", "balance.craftyCrate", false, "If true, the crate will immediately empty itself if it tries to perform a craft and does not have enough mana; if false, the craft will simply wait until it does. Makes it really hard to automate, good luck!");
		
		ADVANCED_CRATE_MANA_PER_ITEM = config.getInt("crateManaPerItem", "balance.craftyCrate", 160, 1, Integer.MAX_VALUE, "How much mana does the crafty crate use per-item in the recipe (empty slots and Crafting Placeholders don't count?) Default value is one burst from a redstone spreader.");
		
		//tnt
		ALLOW_DUPLICATED_TNT = config.getBoolean("allowEntropinnyumDuplicatedTNT", "balance.tnt", false, "Should the Entropinnyum accept TNT that came from a vanilla-style TNT duplicator device?");
		
		TNT_DUPE_HEURISTIC = config.getInt("tntDupeDetectionHeuristic", "balance.tnt", 10, 1, Integer.MAX_VALUE, "The TNT duplicator detection uses a score/heuristic system to detect duplicated TNT. Set this number higher if you get false positives.");
		
		FORCE_VANILLA_TNT = config.getBoolean("forceVanillaTNT", "balance.tnt", false, "Should the Entropinnyum only accept vanilla TNT entities?");
		
		//decay
		PASSIVE_DECAY_TIMER = config.getInt("passiveDecayTimer", "balance.decay", 72000, 1, 72000, "How many ticks until passive flowers decay? Can only be set *lower* than the default value. Muahaha.");
		
		for(String flower : GeneratingFlowers.activeFlowerClassesToNames.values()) {
			boolean should = config.getBoolean(flower + "Decay", "balance.decay.flowers", false, "Does the " + flower + " experience passive decay?");
			SHOULD_ALSO_BE_PASSIVE_MAP.put(flower, should);
		}
		
		//fluxfield
		MANA_SHOTS_PER_ENERGY_BURST = config.getInt("shotsPerBurst", "balance.fluxfield", 1, 1, Integer.MAX_VALUE, "How many pulses from a mana spreader are needed to fire off a \"packet\" of FE?");
		
		FE_PER_ENERGY_BURST = config.getInt("fePerBurst", "balance.fluxfield", 1600, 1, Integer.MAX_VALUE, "How much FE is contained within a \"packet\"?");
		
		//etc
		CREATE_ENDER_AIR_WITH_DISPENSER = config.getBoolean("enderAirDispenser", "etc", true, "Can dispensers shoot glass bottles to turn them in to Ender Air in the End dimension? This allows for automation of Ender Air, which was not previously possible.");
		
		POTTED_TINY_POTATO = config.getBoolean("pottedTinyPotato", "etc", true, "Can players place tiny potatoes in flower pots? Please don't disable this, it's very cute.");
		
		AUTO_CORPOREA_SPARK = config.getBoolean("autoCorporeaSpark", "etc", false, "If true, placing a corporea-related block will automatically decorate it with corporea sparks and floral powder, unless you're sneaking.");
		
		EVERYTHING_APOTHECARY = config.getBoolean("unlockApothecary", "etc", true, "If true, any item is allowed to enter the Petal Apothecary, not just petals, runes, and manaresources. Great for modpacks.");
		
		SHEEP_EAT_ALT_GRASS = config.getBoolean("sheepEatCustomGrass", "etc", true, "Can sheep eat the custom Botania grass blocks to regrow their wool?");
		
		MANA_GENERATION_STATISTICS = config.getBoolean("keepManaGenerationStatistics", "etc", false, "Should Botania Tweaks keep statistics on the total amount of mana generated, across all flowers and dimensions?");
		
		//compat
		AGRICRAFT_DOOT = config.getBoolean("dootableAgricraft", "compat", true, "Can the Horn of the Wild harvest crops from Agricraft?");
		
		if(config.hasChanged()) config.save();
	}
	
	private static <T extends Enum> T getEnum(Configuration config, String configName, String configCategory, T defaultValue, String configDescription, Function<T, String> describerFunction, Class<T> enumClass) {
		//FEAR MY TERRIBLE FUNCTIONAL BULLSHIT, HAHAHAAA
		//just pretend the inside of this method doesn't exist, because it's otherwise a great utility function
		
		T[] enumConstants = enumClass.getEnumConstants();
		
		String[] enumNames = Arrays.stream(enumConstants).map(T::toString).toArray(String[]::new);
		
		String configAndValueDescription = configDescription + "\n" + Arrays.stream(enumConstants).map(t -> "\"" + t.toString() + "\": " + describerFunction.apply(t)).reduce((one, two) -> one + '\n' + two).get();
		
		String userProvidedString = config.getString(configName, configCategory, defaultValue.toString(), configAndValueDescription, enumNames);
		
		Optional<T> userEnum = Arrays.stream(enumConstants).filter(t -> t.toString().equals(userProvidedString)).findAny();
		
		if(userEnum.isPresent()) return userEnum.get();
		else throw new IllegalArgumentException("\"" + userProvidedString + "\" is not a valid value for config option " + configName + "! See the config file for details");
	}
	
	@SubscribeEvent
	public static void configChanged(ConfigChangedEvent e) {
		if(e.getModID().equals(BotaniaTweaks.MODID)) {
			readConfig();
		}
	}
	
	public enum EnumOrechidMode {
		DEFAULT,
		FORCE_GOG,
		FORCE_NO_GOG;
		
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
		
		public boolean isGog() {
			switch (this) {
				case DEFAULT: return Loader.isModLoaded("gardenofglass"); //Can't use Botania.gardenOfGlassLoaded since I run before Botania's preinit populates that field
				case FORCE_GOG: return true;
				case FORCE_NO_GOG: return false;
			}
			
			return false; //Hhhh
		}
	}
}
