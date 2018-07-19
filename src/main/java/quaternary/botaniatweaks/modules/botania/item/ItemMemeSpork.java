package quaternary.botaniatweaks.modules.botania.item;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.botaniatweaks.modules.shared.etc.RainbowBarfFontRenderer;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMemeSpork extends Item {
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag mistake) {
		tooltip.add(I18n.translateToLocal("botania_tweaks.meme_spork.tooltip"));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.attackEntityFrom(DamageSource.OUT_OF_WORLD, 13371337f);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(ItemStack stack) {
		return RainbowBarfFontRenderer.get();
	}
}
