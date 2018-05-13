package quaternary.botaniatweaks.asm;

import quaternary.botaniatweaks.config.BotaniaTweaksConfig;

public class BotaniaTweakerHooks {
	public static int getPassiveDecayTime() {
		return BotaniaTweaksConfig.PASSIVE_DECAY_TIMER;
	}
	
	public static boolean shouldFlowerDecay(String name) {
		return BotaniaTweaksConfig.SHOULD_ALSO_BE_PASSIVE_MAP.getOrDefault(name, false);
	}
}
