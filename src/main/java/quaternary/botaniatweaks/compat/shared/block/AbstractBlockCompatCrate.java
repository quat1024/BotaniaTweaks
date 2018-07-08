package quaternary.botaniatweaks.compat.shared.block;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.botaniatweaks.compat.shared.tile.AbstractTileCompatCrate;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

import javax.annotation.Nullable;

public abstract class AbstractBlockCompatCrate extends Block implements IWandHUD, IWandable, ILexiconable {
	@Override
	public abstract LexiconEntry getEntry(World world, BlockPos blockPos, EntityPlayer entityPlayer, ItemStack itemStack);
	
	@Nullable
	@Override
	public abstract TileEntity createTileEntity(World world, IBlockState state);
	
	public AbstractBlockCompatCrate() {
		super(Material.WOOD);
		setHardness(2);
		setSoundType(SoundType.WOOD);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing facing) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof AbstractTileCompatCrate) {
			return ((AbstractTileCompatCrate)tile).onWanded(world, player, stack);
		}
		
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof AbstractTileCompatCrate) {
			AbstractTileCompatCrate crate = (AbstractTileCompatCrate) tile;
			
			//TODO this magic number is an artifact of hardcoding it back when there was just 1 crate
			//Do the math to figure out the real width and height please.
			final int STACK_SPACING = 18;
			
			int width = crate.getCrateWidth() * STACK_SPACING - 2;
			int height = crate.getCrateHeight() * STACK_SPACING - 2;
			int xc = res.getScaledWidth() / 2 + 20;
			int yc = res.getScaledHeight() / 2 - height / 2;
			
			Gui.drawRect(xc - 6, yc - 6, xc + width + 6, yc + height + 6, 0x22000000);
			Gui.drawRect(xc - 4, yc - 4, xc + width + 4, yc + height + 4, 0x22000000);
			
			for(int i = 0; i < crate.getCrateWidth(); i++) {
				for(int j = 0; j < crate.getCrateHeight(); j++) {
					int index = i * crate.getCrateWidth() + j; //TODO width, or height? Most crates are square anyways so it won't matter
					int xp = xc + j * STACK_SPACING;
					int yp = yc + i * STACK_SPACING;
					
					Gui.drawRect(xp, yp, xp + 16, yp + 16, 0x22FFFFFF);
					
					ItemStack item = crate.getItemHandler().getStackInSlot(index);
					net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
					GlStateManager.enableRescaleNormal();
					mc.getRenderItem().renderItemAndEffectIntoGUI(item, xp, yp);
					net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
				}
			}
		}
	}
}
