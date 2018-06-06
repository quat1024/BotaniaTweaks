package quaternary.botaniatweaks.etc;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.botaniatweaks.BotaniaTweaks;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSpork extends Item {
	public ItemSpork() {
		super();
		setRegistryName(BotaniaTweaks.MODID, "spork");
		setUnlocalizedName(BotaniaTweaks.MODID + ".spork");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.translateToLocal("botania_tweaks.spork.tooltip"));
	}
}
