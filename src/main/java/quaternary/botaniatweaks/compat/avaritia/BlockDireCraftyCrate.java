package quaternary.botaniatweaks.compat.avaritia;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
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
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

import javax.annotation.Nullable;

public class BlockDireCraftyCrate extends Block implements IWandHUD, IWandable {
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
	
	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUD(Minecraft minecraft, ScaledResolution scaledResolution, World world, BlockPos blockPos) {
		//TODO
	}
}
