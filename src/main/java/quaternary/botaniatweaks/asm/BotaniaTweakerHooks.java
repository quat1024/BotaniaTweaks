package quaternary.botaniatweaks.asm;

import net.minecraft.util.math.MathHelper;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;

@SuppressWarnings("unused") //Everything here called through ASM
public class BotaniaTweakerHooks {
	public static int getPassiveDecayTime() {
		return BotaniaTweaksConfig.PASSIVE_DECAY_TIMER;
	}
	
	public static boolean shouldFlowerDecay(String name) {
		return BotaniaTweaksConfig.SHOULD_ALSO_BE_PASSIVE_MAP.getOrDefault(name, false);
	}
	
	public static int getManastormBurstMana() {
		return MathHelper.floor(120 * BotaniaTweaksConfig.MANASTORM_SCALE_FACTOR);
	}
	
	public static int getManastormBurstStartingMana() {
		return MathHelper.floor(340 * BotaniaTweaksConfig.MANASTORM_SCALE_FACTOR);
	}
	
	//Is this loss
	public static float getManastormBurstLossjpgPerTick() {
		return BotaniaTweaksConfig.MANASTORM_SCALE_FACTOR;
	}
}
