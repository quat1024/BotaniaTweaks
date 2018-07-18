package quaternary.botaniatweaks.modules.botania.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;
import quaternary.botaniatweaks.modules.botania.misc.IBotaniaTweaked;
import quaternary.botaniatweaks.modules.botania.tile.TileNerfedManaFluxfield;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.common.block.mana.BlockRFGenerator;

import javax.annotation.Nullable;

public class BlockNerfedManaFluxfield extends BlockRFGenerator implements ILexiconable, IBotaniaTweaked {
	public BlockNerfedManaFluxfield() {
		
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileNerfedManaFluxfield();
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@CapabilityInject(IEnergyStorage.class)
	public static final Capability<IEnergyStorage> ENERGY_CAP = null;
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileNerfedManaFluxfield) {
			IEnergyStorage energy = tile.getCapability(ENERGY_CAP, null);
			if(energy != null) {
				return MathHelper.ceil(15 * ((float) energy.getEnergyStored() / energy.getMaxEnergyStored()));
			}
		}
		
		return 0; //oops
	}
	
	@Override
	public boolean isTweaked() {
		return true;
	}
}
