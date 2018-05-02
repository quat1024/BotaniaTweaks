package quaternary.botaniatweaks.config;

import net.minecraftforge.common.config.Config;
import quaternary.botaniatweaks.BotaniaTweaks;

@Config(modid = BotaniaTweaks.MODID, name = BotaniaTweaks.NAME)
public class BotaniaTweaksConfig {
	@Config.Name("mana_per_energy_burst")
	@Config.Comment("How many pulses from a mana spreader are needed to fire off a \"packet\" of FE?\n\nFor the dirty cheaters out there who look at numerical mana amounts, multiply this number by 160.")
	public static int MANA_SHOTS_PER_ENERGY_BURST = 1;
	
	@Config.Name("fe_per_energy_burst")
	@Config.Comment("How much FE is contained within one of these \"packets\"?\n\nCan't be set to a value that would make it better than the vanilla fluxfield. Muahaha.")
	@Config.RangeInt(min = 0, max = 1600)
	public static int FE_PER_ENERGY_BURST = 30;
	
	@Config.Name("create_ender_air_with_dispenser")
	@Config.Comment("Can dispensers shoot glass bottles to turn them in to Ender Air in the End dimension? This allows for automation of Ender Air, which was not previously possible.")
	public static boolean CREATE_ENDER_AIR_WITH_DISPENSER;
}
