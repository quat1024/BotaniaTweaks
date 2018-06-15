package quaternary.botaniatweaks.etc;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.block.BlockCustomAgglomerationPlate;
import quaternary.botaniatweaks.block.BlockNerfedManaFluxfield;

@SideOnly(Side.CLIENT)
public class ClientEvents {
	@SubscribeEvent
	public static void tooltip(ItemTooltipEvent e) {
		Item i = e.getItemStack().getItem();
		boolean addTooltip = false;
		
		if(i instanceof IBotaniaReplaced) {
			addTooltip = true;
		} else	if(i instanceof ItemBlock) {
			Block b = ((ItemBlock)i).getBlock();
			if(b instanceof IBotaniaReplaced) {
				addTooltip = true;
			}
		}
		
		if(addTooltip) {
			e.getToolTip().add(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocal("botania_tweaks.tweaked") + TextFormatting.RESET);
		}
	}
}
