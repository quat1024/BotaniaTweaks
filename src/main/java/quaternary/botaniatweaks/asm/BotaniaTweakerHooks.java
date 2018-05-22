package quaternary.botaniatweaks.asm;

import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.capability.IFluidHandler;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.etc.CatchallFlowerComponent;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.common.Botania;

import java.util.regex.Pattern;

@SuppressWarnings("unused") //Everything here called through ASM
public class BotaniaTweakerHooks {
	
	/// decay tweak
	
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
	
	/// spectro tweak
	
	public static int getSpectrolusManaPerWool() {
		//300 is the default (check subtilespectrolus)
		return 300 * (BotaniaTweaksConfig.SUPER_SPECTROLUS ? 10 : 1);
	}
	
	/// apothecary tweak
	
	//Copy from TileAltar
	private static final Pattern SEED_PATTERN = Pattern.compile("(?:(?:(?:[A-Z-_.:]|^)seed)|(?:(?:[a-z-_.:]|^)Seed))(?:[sA-Z-_.:]|$)");
	
	@CapabilityInject(IFluidHandler.class)
	public static final Capability<IFluidHandler> FLUID_CAP = null;
	
	public static IFlowerComponent getFlowerComponent(IFlowerComponent comp, ItemStack stack) {
		//If the tweak is disabled, or if Botania has already chosen a good flower component, just don't change anything
		if(!BotaniaTweaksConfig.EVERYTHING_APOTHECARY || comp != null) return comp;
		
		//If it's a seed, don't allow it in, since yknow it has to complete the craft
		if(SEED_PATTERN.matcher(stack.getUnlocalizedName()).find()) return null;
		
		//Don't allow buckets in since it's annoying when the empty bucket goes in
		if(stack.getItem() instanceof ItemBucket) return null;
		if(stack.hasCapability(FLUID_CAP, null)) return null;
		
		//K cool
		return new CatchallFlowerComponent();
	}
	
	/// orechid tweak
	
	public static boolean orechidGog = Botania.gardenOfGlassLoaded;
	
	public static void onConfigChanged() {
		switch(BotaniaTweaksConfig.ORECHID_MODE) {
			case DEFAULT: orechidGog = Botania.gardenOfGlassLoaded; break;
			case FORCE_GOG: orechidGog = true; break;
			case FORCE_NO_GOG: orechidGog = false; break;
		}
	}
}
