package quaternary.botaniatweaks.modules.botania.handler;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.botaniatweaks.asm.BotaniaTweakerHooks;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import quaternary.botaniatweaks.modules.botania.misc.IBotaniaTweaked;
import quaternary.botaniatweaks.modules.shared.lib.GeneratingFlowers;
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
				
				GeneratingFlowers.FlowerData data = GeneratingFlowers.flowerDataFromName(type);
				if(data == null) return;
				
				if(data.isPassive) {
					addTooltip = BotaniaConfig.DECAY_TIMES.get(type) != 72000;
				} else {
					addTooltip = BotaniaTweakerHooks.shouldFlowerDecay(type);
				}
				
				if(BotaniaConfig.ROSA_ARCANA_ORB_MULTIPLIER != 1 && type.equals("arcanerose")) addTooltip = true;
			} else {
				Block block = ((ItemBlock) item).getBlock();
				if(block instanceof IBotaniaTweaked) {
					addTooltip = ((IBotaniaTweaked) block).isTweaked();
				}
			}
		}
		
		if(addTooltip) {
			e.getToolTip().add(TextFormatting.DARK_GREEN + "" + TextFormatting.ITALIC + I18n.format("botania_tweaks.tweaked") + TextFormatting.RESET);
		}
	}
}
