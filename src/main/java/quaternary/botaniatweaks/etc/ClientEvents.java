package quaternary.botaniatweaks.etc;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.botaniatweaks.asm.BotaniaTweakerHooks;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

@SideOnly(Side.CLIENT)
public class ClientEvents {
	@SubscribeEvent
	public static void tooltip(ItemTooltipEvent e) {
		ItemStack stack = e.getItemStack();
		Item item = stack.getItem();
		
		boolean addTooltip = false;
		
		if(item instanceof IBotaniaTweaked) {
			addTooltip = ((IBotaniaTweaked) item).isTweaked();
		} else if(item instanceof ItemBlock) {
			if(item instanceof ItemBlockSpecialFlower) {
				String type = ItemBlockSpecialFlower.getType(stack);
				addTooltip = BotaniaTweakerHooks.shouldFlowerDecay(type);
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
