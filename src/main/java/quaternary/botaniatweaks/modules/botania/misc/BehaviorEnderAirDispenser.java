package quaternary.botaniatweaks.modules.botania.misc;

import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import vazkii.botania.common.item.ModItems;

public class BehaviorEnderAirDispenser implements IBehaviorDispenseItem {
	IBehaviorDispenseItem vanillaBehavior;
	
	public BehaviorEnderAirDispenser(IBehaviorDispenseItem vanillaBehavior) {
		this.vanillaBehavior = vanillaBehavior;
	}
	
	@Override
	public ItemStack dispense(IBlockSource source, ItemStack dispensedStack) {
		if(!BotaniaConfig.CREATE_ENDER_AIR_WITH_DISPENSER) return vanillaBehavior.dispense(source, dispensedStack);
		
		boolean end = source.getWorld().provider.getDimension() == 1;
		if(end) {
			vanillaBehavior.dispense(source, new ItemStack(ModItems.manaResource, 1, 15));
			dispensedStack.shrink(1);
			return dispensedStack;
		} else {
			return vanillaBehavior.dispense(source, dispensedStack);
		}
	}
}
