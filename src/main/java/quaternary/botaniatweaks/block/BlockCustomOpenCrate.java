package quaternary.botaniatweaks.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import quaternary.botaniatweaks.etc.IBotaniaReplaced;
import quaternary.botaniatweaks.tile.TileCustomCraftyCrate;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CrateVariant;
import vazkii.botania.common.block.BlockOpenCrate;
import vazkii.botania.common.block.tile.TileOpenCrate;

import javax.annotation.Nonnull;

public class BlockCustomOpenCrate extends BlockOpenCrate implements IBotaniaReplaced {
	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		if(state.getValue(BotaniaStateProps.CRATE_VARIANT) == CrateVariant.OPEN) return new TileOpenCrate();
		else return new TileCustomCraftyCrate();
	}
}
