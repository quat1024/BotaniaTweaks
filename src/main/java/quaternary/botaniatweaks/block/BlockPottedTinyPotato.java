package quaternary.botaniatweaks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.util.MathUtil;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockTinyPotato;

//Much of this class is retyped from BlockFlowerPot.class
//Didn't want to bring along all the BlockContainer crap though
@Mod.EventBusSubscriber(modid = BotaniaTweaks.MODID)
public class BlockPottedTinyPotato extends Block {
	static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);
	
	public static final PropertyEnum<EnumFacing> POTATO_FACING = PropertyEnum.create("potato_facing", EnumFacing.class, EnumFacing.HORIZONTALS);
	
	public BlockPottedTinyPotato() {
		super(Material.CIRCUITS);
		setRegistryName(new ResourceLocation(BotaniaTweaks.MODID, "potted_tiny_potato"));
		
		setDefaultState(getDefaultState().withProperty(POTATO_FACING, EnumFacing.NORTH));
	}
	
	@GameRegistry.ObjectHolder(BotaniaTweaks.MODID + ":potted_tiny_potato")
	public static final Block INST = Blocks.AIR;
	
	@SubscribeEvent
	public static void rightClick(PlayerInteractEvent.RightClickBlock e) {
		if(!BotaniaTweaksConfig.POTTED_TINY_POTATO) return; // :(
		
		Block clickedBlock = e.getWorld().getBlockState(e.getPos()).getBlock();
		if(clickedBlock instanceof BlockFlowerPot) {
			ItemStack held = e.getEntityPlayer().getHeldItem(e.getHand());
			if(held.getItem() instanceof ItemBlock && ((ItemBlock)held.getItem()).getBlock() instanceof BlockTinyPotato) {
				if(!e.getEntityPlayer().isCreative()) held.shrink(1);
				e.getWorld().setBlockState(e.getPos(), INST.getDefaultState().withProperty(POTATO_FACING, e.getEntityPlayer().getHorizontalFacing().getOpposite()));
				
				//Prevent the potato from popping right back out again
				e.setUseBlock(Event.Result.DENY);
				//Prevent the player from placing another potato if they hold more than one
				e.setUseItem(Event.Result.DENY);
			}
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FLOWER_POT_AABB;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
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
	
	//change: override getpickblock to the potato instead of getItem stuff
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(ModBlocks.tinyPotato);
	}
	
	//no need for canplaceblockat since there's no block
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(new ItemStack(Blocks.FLOWER_POT));
		drops.add(new ItemStack(ModBlocks.tinyPotato));
	}
	
	//and the blockstate boiler plate
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, POTATO_FACING);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(POTATO_FACING, EnumFacing.getHorizontal(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(POTATO_FACING).getHorizontalIndex();
	}
}
