package quaternary.botaniatweaks.modules.botania.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;

import java.util.List;

public class TNTDuplicatorDetectionWorldTickHander {
	@SubscribeEvent
	public static void update(TickEvent.WorldTickEvent e) {
		if(e.world.isRemote || e.phase == TickEvent.Phase.START) return;
		
		for(EntityTNTPrimed tnt : e.world.getEntities(EntityTNTPrimed.class, (ent) -> true)) {
			processTNTDupe(tnt);
		}
	}
	
	public static void processTNTDupe(EntityTNTPrimed tnt) {
		World world = tnt.world;
		if(tnt.getFuse() != 79) return;
		
		BlockPos tntPos = tnt.getPosition();
		
		int score = 0;
		
		//TNT duplicators usually involve slime blocks, detector rails, and minecarts
		Iterable<BlockPos.MutableBlockPos> nearbyPositions = BlockPos.getAllInBoxMutable(tntPos.add(-1, -1, -1), tntPos.add(1, 1, 1));
		
		for(BlockPos pos : nearbyPositions) {
			Block b = getBlockOrMovingBlock(world, pos);
			if(b == Blocks.SLIME_BLOCK) score++;
			if(b == Blocks.DETECTOR_RAIL) score += 5;
		}
		
		List<EntityMinecart> nearbyCarts = world.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(tntPos.add(-2, -2, -2), tntPos.add(2, 2, 2)));
		if(!nearbyCarts.isEmpty()) {
			score += 3;
			
			for(EntityMinecart cart : nearbyCarts) {
				Block atCart = getBlockOrMovingBlock(world, cart.getPosition());
				Block belowCart = getBlockOrMovingBlock(world, cart.getPosition().down());
				
				if(atCart == Blocks.DETECTOR_RAIL) score += 4;
				if(belowCart == Blocks.DETECTOR_RAIL) score += 4;
			}
		}
		
		if(score >= BotaniaConfig.TNT_DUPE_HEURISTIC) tnt.getTags().add("CheatyDupe");
	}
	
	static Block getBlockOrMovingBlock(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityPiston) {
			return ((TileEntityPiston) tile).getPistonState().getBlock();
		} else return world.getBlockState(pos).getBlock();
	}
}
