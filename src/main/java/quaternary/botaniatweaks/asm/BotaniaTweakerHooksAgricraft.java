package quaternary.botaniatweaks.asm;

import vazkii.botania.api.item.IHornHarvestable;

public class BotaniaTweakerHooksAgricraft {
	public static boolean canHornHarvestCrop(IHornHarvestable.EnumHornType dooter) {
		return dooter == IHornHarvestable.EnumHornType.WILD;
	}
}
