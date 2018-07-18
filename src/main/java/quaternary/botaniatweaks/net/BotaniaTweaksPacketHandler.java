package quaternary.botaniatweaks.net;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;
import quaternary.botaniatweaks.BotaniaTweaks;

public class BotaniaTweaksPacketHandler {
	public static final SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(BotaniaTweaks.MODID);
	
	public static void sendToAllAround(IMessage message, World w, BlockPos point) {
		NET.sendToAllAround(message, new NetworkRegistry.TargetPoint(w.provider.getDimension(), point.getX(), point.getY(), point.getZ(), 64));
	}
}
