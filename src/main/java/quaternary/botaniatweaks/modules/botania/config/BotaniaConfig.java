package quaternary.botaniatweaks.modules.botania.config;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import quaternary.botaniatweaks.asm.BotaniaTweakerHooks;
import quaternary.botaniatweaks.modules.shared.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.modules.shared.lib.GeneratingFlowers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BotaniaConfig {
	public static int MANA_SHOTS_PER_ENERGY_BURST = 1;
	public static int FE_PER_ENERGY_BURST = 30;
	
	public static boolean PROCESS_CUSTOM_AGGLO_STACKS = true;
	
	public static boolean CREATE_ENDER_AIR_WITH_DISPENSER;
	
	public static int PASSIVE_DECAY_TIMER = 72000;
	public static HashMap<String, Boolean> SHOULD_ALSO_BE_PASSIVE_MAP = new HashMap<>();
	
	public static float MANASTORM_SCALE_FACTOR;
	
	public static float ROSA_ARCANA_ORB_MULTIPLIER;
	
	public static boolean AUTO_CORPOREA_SPARK;
	public static List<ResourceLocation> CORPORETIC_BLOCKS;
	
	public static boolean SHEEP_EAT_ALT_GRASS;
	
	public static EnumOrechidMode ORECHID_MODE = EnumOrechidMode.DEFAULT;
	public static boolean NON_GOG_WATER_BOWL;
	
	public static boolean FORCE_VANILLA_TNT;
	public static boolean DENY_DUPLICATED_TNT;
	public static int TNT_DUPE_HEURISTIC;
	
	public static boolean ADVANCED_CRAFTY_CRATE;
	public static boolean ADVANCED_CRAFTY_CRATE_HARDMODE;
	public static int ADVANCED_CRATE_MANA_PER_ITEM;
	
	public static boolean MANA_GENERATION_STATISTICS;
	
	public static int CREATIVE_POOL_SIZE = 1_000_000;
	
	public static void readConfig(Configuration config) {
		//balance
		MANASTORM_SCALE_FACTOR = config.getFloat("manastormScaleFactor", "balance", 1f, 1f, 15f, "The default mana output of the Manastorm Charge is multiplied by this amount. Setting this to a value higher than around ~1.38889ish allows for the \"Manastorm Reactor\" build to be profitable.");
		
		ROSA_ARCANA_ORB_MULTIPLIER = config.getFloat("roseArcanaOrbMultiplier", "balance", 1, 1, 8, "A multiplier for the amount of mana that the Rosa Arcana produces when soaking up XP orbs from the world. This doesn't affect the amount of mana obtained from the XP bars of nearby players; only in-world orb entities are affected.");
		
		ORECHID_MODE = BotaniaTweaksConfig.getEnum(config, "cheapOrechid", "balance", EnumOrechidMode.DEFAULT, "How does the Orechid determine its cost and speed to run?", mode -> {
			switch (mode) {
				case DEFAULT: return "The Orechid will be cheap if Garden of Glass is loaded.";
				case FORCE_GOG: return "The Orechid will always be cheap to run, even if Garden of Glass is not present.";
				case FORCE_NO_GOG: return "The Orechid will be expensive to run, even in Garden of Glass mode.";
				default: return "h"; //h
			}
		}, EnumOrechidMode.class);
		
		BotaniaTweakerHooks.orechidGog = ORECHID_MODE.isGog();
		
		ADVANCED_CRAFTY_CRATE = config.getBoolean("advancedCraftyCrate", "balance.craftyCrate", false, "Should the Crafty Crate require mana to craft items?");
		
		ADVANCED_CRAFTY_CRATE_HARDMODE = config.getBoolean("advancedCraftyCrateHardMode", "balance.craftyCrate", false, "If true, the crate will immediately empty itself if it tries to perform a craft and does not have enough mana; if false, the craft will simply wait until it does. Makes it really hard to automate, good luck!");
		
		ADVANCED_CRATE_MANA_PER_ITEM = config.getInt("crateManaPerItem", "balance.craftyCrate", 160, 1, Integer.MAX_VALUE, "How much mana does the crafty crate use per-item in the recipe (empty slots and Crafting Placeholders don't count?) Default value is one burst from a redstone spreader.");
		
		//tnt
		DENY_DUPLICATED_TNT = config.getBoolean("denyEntropinnyumDuplicatedTNT", "balance.tnt", false, "Should the Entropinnyum block TNT that came from a vanilla-style TNT duplicator device?");
		
		TNT_DUPE_HEURISTIC = config.getInt("tntDupeDetectionHeuristic", "balance.tnt", 10, 1, Integer.MAX_VALUE, "The TNT duplicator detection uses a score/heuristic system to detect duplicated TNT. Set this number higher if you get false positives.");
		
		FORCE_VANILLA_TNT = config.getBoolean("forceVanillaTNT", "balance.tnt", false, "Should the Entropinnyum only accept vanilla TNT entities?");
		
		//decay
		PASSIVE_DECAY_TIMER = config.getInt("passiveDecayTimer", "balance.decay", 72000, 1, 72000, "How many ticks until passive flowers decay? Can only be set *lower* than the default value. Muahaha.");
		
		for(GeneratingFlowers.FlowerData data : GeneratingFlowers.getAllFlowerDatas()) {
			if(data.isPassive) continue;
			
			String flowerName = data.name;
			String flowerMod = data.modId;
			boolean shouldIt = config.getBoolean(flowerName + "Decay", "balance.decay.flowers", false, String.format("Does the %s, from %s, experience passive decay?", flowerName, flowerMod));
			SHOULD_ALSO_BE_PASSIVE_MAP.put(flowerName, shouldIt);
		}
		
		//fluxfield
		MANA_SHOTS_PER_ENERGY_BURST = config.getInt("shotsPerBurst", "balance.fluxfield", 1, 1, Integer.MAX_VALUE, "How many pulses from a mana spreader are needed to fire off a \"packet\" of FE?");
		
		FE_PER_ENERGY_BURST = config.getInt("fePerBurst", "balance.fluxfield", 1600, 1, Integer.MAX_VALUE, "How much FE is contained within a \"packet\"?");
		
		//etc
		CREATE_ENDER_AIR_WITH_DISPENSER = config.getBoolean("enderAirDispenser", "etc", false, "Can dispensers shoot glass bottles to turn them in to Ender Air in the End dimension? This allows for automation of Ender Air, which was not previously possible.");
		
		AUTO_CORPOREA_SPARK = config.getBoolean("autoCorporeaSpark", "etc", false, "If true, placing a corporea-related block will automatically decorate it with corporea sparks and floral powder, unless you're sneaking.");
		
		String[] corporeticBlockStringNames = config.getStringList("corporeticBlocks", "etc", new String[] {
			"botania:corporeaindex",
			"botania:corporeafunnel",
			"botania:corporeainterceptor",
			"botania:corporeacrystalcube",
			"incorporeal:corporea_liar"
		}, "The blocks considered \"corporea-related\" for the \"autoCorporeaSpark\" feature.");
		
		CORPORETIC_BLOCKS = new LinkedList<>();
		for(String name : corporeticBlockStringNames) {
			CORPORETIC_BLOCKS.add(new ResourceLocation(name));
		}
		
		SHEEP_EAT_ALT_GRASS = config.getBoolean("sheepEatCustomGrass", "etc", false, "Can sheep eat the custom Botania grass blocks to regrow their wool?");
		
		MANA_GENERATION_STATISTICS = config.getBoolean("keepManaGenerationStatistics", "etc", false, "Should Botania Tweaks keep statistics on the total amount of mana generated, across all flowers and dimensions?");
		
		CREATIVE_POOL_SIZE = (int) (1_000_000 * config.getFloat("guiltyPoolManaMultiplier", "etc", 1, 0, 1.06f, "This number acts as a multiplier for how much mana is in the Guilty Mana Pool. Setting higher than 1 allows for creating \"creative pool only\" mana infusion recipes, by adding recipes using more than an ordinary pool can hold."));
		
		NON_GOG_WATER_BOWL = config.getBoolean("nonGogWaterBowl", "etc", false, "Should the water bowl mechanic, where you right click on water with a bowl to create a filled bowl, be available outside of Garden of Glass mode?");
		
		PROCESS_CUSTOM_AGGLO_STACKS = config.getBoolean("doCustomAggglomerationPreprocessing", "etc", true, "If \"true\", the custom terrestrial agglomeration plate will try to \"collect\" the items on top of it, resolving issues relating to \"2x apple\" being treated differently from \"apple, apple\". This operation can be expensive, so here is your option to disable it, if you have a lot of agglomeration recipes that don't use this feature.\nThis is kind of a hack fix; if I get around to writing a more performant agglomeration recipe matcher, I'll remove this option.");
		
		if(config.hasChanged()) config.save();
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
