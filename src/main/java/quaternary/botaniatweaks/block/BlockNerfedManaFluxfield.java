package quaternary.botaniatweaks.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.tile.TileNerfedManaFluxfield;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nullable;

public class BlockNerfedManaFluxfield extends Block implements ILexiconable {
	public BlockNerfedManaFluxfield() {
		super(Material.ROCK);
		this.setHardness(2.0F);
		this.setResistance(10.0F);
		setSoundType(SoundType.STONE);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
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
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileNerfedManaFluxfield) {
			IEnergyStorage energy = ((TileNerfedManaFluxfield) tile).energyHandler;
			return MathHelper.ceil(15 * ((float) energy.getEnergyStored() / energy.getMaxEnergyStored()));
		} else return 0; //oops
	}
	
	@Override
	public LexiconEntry getEntry(World world, BlockPos blockPos, EntityPlayer entityPlayer, ItemStack itemStack) {
		//just go to the vanilla fluxfield for now
		return LexiconData.rfGenerator;
	}
}
