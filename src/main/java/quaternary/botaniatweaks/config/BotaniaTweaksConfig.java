package quaternary.botaniatweaks.config;

import net.minecraftforge.common.config.Config;
import quaternary.botaniatweaks.BotaniaTweaks;

@Config(modid = BotaniaTweaks.MODID, name = BotaniaTweaks.NAME)
public class BotaniaTweaksConfig {
	@Config.Name("Fluxfield - Mana per energy burst")
	@Config.Comment("How many pulses from a mana spreader are needed to fire off a \"packet\" of FE?")
	@Config.RangeInt(min = 1)
	public static int MANA_SHOTS_PER_ENERGY_BURST = 1;
	
	@Config.Name("Fluxfield - FE in energy burst")
	@Config.Comment("How much FE is contained within a \"packet\"?")
	public static int FE_PER_ENERGY_BURST = 30;
	
	@Config.Name("Create Ender Air with Dispenser")
	@Config.Comment("Can dispensers shoot glass bottles to turn them in to Ender Air in the End dimension? This allows for automation of Ender Air, which was not previously possible.")
	public static boolean CREATE_ENDER_AIR_WITH_DISPENSER = true;
	
	@Config.Name("Potted Tiny Potato")
	@Config.Comment({"Can players place tiny potatoes in flower pots?","Please do not disable this, it's very cute."})
	public static boolean POTTED_TINY_POTATO = true;
}
