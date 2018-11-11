package quaternary.botaniatweaks.modules.botania.handler;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import vazkii.botania.common.block.ModBlocks;

public class TillAltGrassHandler {
	@SubscribeEvent
	public static void useHoe(UseHoeEvent e) {
		if(!BotaniaConfig.TILL_ALT_GRASS || e.getWorld().isRemote) return;
		
		World world = e.getWorld();
		BlockPos pos = e.getPos();
		
		if(world.getBlockState(pos).getBlock() == ModBlocks.altGrass && world.isAirBlock(pos.up())) {
			world.setBlockState(pos, Blocks.FARMLAND.getDefaultState());
			e.setResult(Event.Result.ALLOW);
		}
	}
}
