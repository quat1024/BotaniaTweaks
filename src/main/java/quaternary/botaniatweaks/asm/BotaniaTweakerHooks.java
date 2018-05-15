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
	
	/// manastorm tweak
	
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
	
	/// entro tweak
	
	public static int getEntropinnyumMaxMana() {
		//6500 is the default (check subtileentropinnyum)
		return 6500 * (BotaniaTweaksConfig.SUPER_ENTROPINNYUM ? 8 : 1); 
	}
	
	//spectro tweak
	
	public static int getSpectrolusManaPerWool() {
		//300 is the default (check subtilespectrolus)
		return 300 * (BotaniaTweaksConfig.SUPER_SPECTROLUS ? 10 : 1);
	}
}
