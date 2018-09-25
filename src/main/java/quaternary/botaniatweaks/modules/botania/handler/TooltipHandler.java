package quaternary.botaniatweaks.modules.botania.handler;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.botaniatweaks.asm.BotaniaTweakerHooks;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import quaternary.botaniatweaks.modules.botania.misc.IBotaniaTweaked;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class TooltipHandler {
	@SubscribeEvent
	public static void handleTooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		Item item = stack.getItem();
		
		boolean addTooltip = false;
		
		if(item instanceof IBotaniaTweaked) {
			addTooltip = ((IBotaniaTweaked) item).isTweaked();
		} else if(item instanceof ItemBlock) {
			if(item instanceof ItemBlockSpecialFlower) {
				String type = ItemBlockSpecialFlower.getType(stack);
				addTooltip = BotaniaTweakerHooks.shouldFlowerDecay(type);
				
				if(BotaniaConfig.SUPER_SPECTROLUS && type.equals("spectrolus")) addTooltip = true;
				if(BotaniaConfig.ROSA_ARCANA_ORB_MULTIPLIER != 1 && type.equals("arcanerose")) addTooltip = true;
			} else {
				Block block = ((ItemBlock) item).getBlock();
				if(block instanceof IBotaniaTweaked) {
					addTooltip = ((IBotaniaTweaked) block).isTweaked();
				}
			}
		}
		
		if(addTooltip) {
			e.getToolTip().add(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocal("botania_tweaks.tweaked") + TextFormatting.RESET);
		}
	}
}
