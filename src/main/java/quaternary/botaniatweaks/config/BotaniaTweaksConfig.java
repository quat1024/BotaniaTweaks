package quaternary.botaniatweaks.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.botaniatweaks.BotaniaTweaks;

import java.io.File;
import java.util.HashMap;

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
	
	static Configuration config;
	
	public static void readConfig() {
		config = new Configuration(new File(Loader.instance().getConfigDir(), "botaniatweaks.cfg"), "1");
		config.load();
		
		//fluxfield
		MANA_SHOTS_PER_ENERGY_BURST = config.getInt("shotsPerBurst", "fluxfield", 1, 1, Integer.MAX_VALUE, "How many pulses from a mana spreader are needed to fire off a \"packet\" of FE?");
		
		FE_PER_ENERGY_BURST = config.getInt("fePerBurst", "fluxfield", 30, 1, Integer.MAX_VALUE, "How much FE is contained within a \"packet\"?");
		
		//decay
		PASSIVE_DECAY_TIMER = config.getInt("passiveDecayTimer", "decay", 72000, 1, 72000, "How many ticks until passive flowers decay? Can only be set *lower* than the default value. Muahaha.");
		
		for(ActiveGeneratingFlowers activeFlower : ActiveGeneratingFlowers.values()) {
			boolean should = config.getBoolean(activeFlower.name + "Decay", "decay.flowers", false, "Does the " + activeFlower.name + " experience passive decay?");
			SHOULD_ALSO_BE_PASSIVE_MAP.put(activeFlower.name, should);
		}
		
		//and the rest
		CREATE_ENDER_AIR_WITH_DISPENSER = config.getBoolean("enderAirDispenser", "general", true, "Can dispensers shoot glass bottles to turn them in to Ender Air in the End dimension? This allows for automation of Ender Air, which was not previously possible.");
		
		POTTED_TINY_POTATO = config.getBoolean("pottedTinyPotato", "general", true, "Can players place tiny potatoes in flower pots? Please don't disable this, it's very cute.");
		
		MANASTORM_SCALE_FACTOR = config.getFloat("manastormScaleFactor", "general", 8f, 1f, 15f, "The default mana output of the Manastorm Charge is multiplied by this amount. Setting this to a value higher than around ~1.38889ish allows for the \"Manastorm Reactor\" build to be profitable.");
		
		SUPER_ENTROPINNYUM = config.getBoolean("superEntropinnyum", "general", true, "Should the Entropinnyum generate 8x the mana it does by default? This makes it possible to run an Entropinnyum off of, for example, a cobbleworks; by default, the flint-to-gunpowder recipe is much too expensive to make another TNT.\n\nAlso I think this flower is way underpowered in general, but that's just me.");
		
		SUPER_SPECTROLUS = config.getBoolean("superSpectrolus", "general", true, "Should the Spectrolus generate 10x the mana it does by default? This makes it much cheaper to run; filling a mana pool only requires a little over five stacks of wool, not over a double chest's worth.");
		
		if(config.hasChanged()) config.save();
	}
	
	@SubscribeEvent
	public static void configChanged(ConfigChangedEvent e) {
		if(e.getModID().equals(BotaniaTweaks.MODID)) {
			readConfig();
		}
	}
}
