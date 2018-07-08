package quaternary.botaniatweaks.compat.avaritia;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import quaternary.botaniatweaks.compat.shared.block.AbstractBlockCompatCrate;
import vazkii.botania.api.lexicon.LexiconEntry;

import javax.annotation.Nullable;

public class BlockDireCraftyCrate extends AbstractBlockCompatCrate {
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileDireCraftyCrate();
	}
	
	@Override
	public LexiconEntry getEntry(World world, BlockPos blockPos, EntityPlayer entityPlayer, ItemStack itemStack) {
		return AvaritiaCompat.direCrateEntry;
	}
}
