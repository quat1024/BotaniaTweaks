package quaternary.botaniatweaks.modules.botania.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.shared.helper.MathUtil;
import vazkii.botania.common.block.ModBlocks;

//Much of this class is retyped from BlockFlowerPot.class
//Didn't want to bring along all the BlockContainer crap though
@Mod.EventBusSubscriber(modid = BotaniaTweaks.MODID)
public class BlockPottedTinyPotato extends Block {
	static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);
	
	public static final PropertyEnum<EnumFacing> POTATO_FACING = PropertyEnum.create("potato_facing", EnumFacing.class, EnumFacing.HORIZONTALS);
	
	public BlockPottedTinyPotato() {
		super(Material.CIRCUITS);
		
		setDefaultState(getDefaultState().withProperty(POTATO_FACING, EnumFacing.NORTH));
	}
	
	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FLOWER_POT_AABB;
	}
	
	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	//changed: remove and pet the potato!!!
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(hand != EnumHand.MAIN_HAND) return false;
		
		if(player.isSneaking()) {
			double x = pos.getX() + MathUtil.rangeRemap(world.rand.nextDouble(), 0, 1, FLOWER_POT_AABB.minX, FLOWER_POT_AABB.maxX);
			double y = pos.getY() + FLOWER_POT_AABB.maxY + (double) 3 / 16;
			double z = pos.getZ() + MathUtil.rangeRemap(world.rand.nextDouble(), 0, 1, FLOWER_POT_AABB.minZ, FLOWER_POT_AABB.maxZ);
			world.spawnParticle(EnumParticleTypes.HEART, x, y, z, 0, 0, 0);
		} else {
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModBlocks.tinyPotato));
			world.setBlockState(pos, Blocks.FLOWER_POT.getDefaultState());
		}
		return true;
	}
	
	@Override
	@Deprecated
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(POTATO_FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	//no need for canplaceblockat since there's no block
	
	//removed getDrops since there's an actual item for this
	
	//and the blockstate boiler plate
	@Override
	@Deprecated
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, POTATO_FACING);
	}
	
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(POTATO_FACING, EnumFacing.byHorizontalIndex(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(POTATO_FACING).getHorizontalIndex();
	}
	
	//bleh
	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
