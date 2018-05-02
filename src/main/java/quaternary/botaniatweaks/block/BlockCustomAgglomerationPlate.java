package quaternary.botaniatweaks.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import quaternary.botaniatweaks.tile.TileCustomAgglomerationPlate;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

//COPY from BlockTerraPlate.java. Changes noted.
public class BlockCustomAgglomerationPlate extends Block implements ILexiconable {
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 3.0/16, 1);
	
	public BlockCustomAgglomerationPlate() {
		super(Material.IRON);
		setHardness(3F);
		setResistance(10F);
		setSoundType(SoundType.METAL);
		BotaniaAPI.blacklistBlockFromMagnet(this, Short.MAX_VALUE);
	}
	
	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}
	
	//BOTANIATWEAKS (temp?) - remove onBlockActivated right-click-onto-plate convenience
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isPassable(IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		//BOTANIATWEAKS: new tile entity
		return new TileCustomAgglomerationPlate();
	}
	
	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.terrasteel;
	}
	
	//BOTANIATWEAKS: (temp) remove comparator override
	
	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
}
