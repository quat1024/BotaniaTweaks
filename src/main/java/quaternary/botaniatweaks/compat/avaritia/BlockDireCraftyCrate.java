package quaternary.botaniatweaks.compat.avaritia;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.botaniatweaks.BotaniaTweaks;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

import javax.annotation.Nullable;

public class BlockDireCraftyCrate extends Block implements IWandHUD, IWandable, ILexiconable {
	public BlockDireCraftyCrate() {
		super(Material.WOOD);
		
		setHardness(2f);
		setSoundType(SoundType.WOOD);
		
		setRegistryName(new ResourceLocation(BotaniaTweaks.MODID, "dire_crafty_crate"));
		setUnlocalizedName(BotaniaTweaks.MODID + ".dire_crafty_crate");
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileDireCraftyCrate();
	}
	
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing enumFacing) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileDireCraftyCrate) {
			return ((TileDireCraftyCrate)tile).onWanded(world, player, stack);
		}
		
		return false;
	}
	
	//TODO Comparator signal?
	
	//Modified from BlockOpenCrate#renderHUD
	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileDireCraftyCrate) {
			TileDireCraftyCrate craft = (TileDireCraftyCrate) tile;
			
			int width = 160;
			int height = 160;
			int xc = res.getScaledWidth() / 2 + 20;
			int yc = res.getScaledHeight() / 2 - height / 2;
			
			Gui.drawRect(xc - 6, yc - 6, xc + width + 6, yc + height + 6, 0x22000000);
			Gui.drawRect(xc - 4, yc - 4, xc + width + 4, yc + height + 4, 0x22000000);
			
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					int index = i * 9 + j;
					int xp = xc + j * 18;
					int yp = yc + i * 18;
					
					Gui.drawRect(xp, yp, xp + 16, yp + 16, 0x22FFFFFF);
					
					ItemStack item = craft.getItemHandler().getStackInSlot(index);
					net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
					GlStateManager.enableRescaleNormal();
					mc.getRenderItem().renderItemAndEffectIntoGUI(item, xp, yp);
					net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
				}
			}
		}
	}
	
	@Override
	public LexiconEntry getEntry(World world, BlockPos blockPos, EntityPlayer entityPlayer, ItemStack itemStack) {
		return AvaritiaCompat.direCrateEntry;
	}
}
