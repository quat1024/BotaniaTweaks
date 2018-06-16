package quaternary.botaniatweaks.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import vazkii.botania.common.Botania;

import java.util.Random;

public class PacketAdvancedCrateFX implements IMessage {
	BlockPos pos;
	
	public PacketAdvancedCrateFX() {}
	
	public PacketAdvancedCrateFX(BlockPos pos) {
		this.pos = pos;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		//you're welcome, cubic chunks
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	public static class Response implements IMessageHandler<PacketAdvancedCrateFX, IMessage> {
		static Random blah = new Random();
		
		@Override
		public IMessage onMessage(PacketAdvancedCrateFX p, MessageContext messageContext) {
			Minecraft.getMinecraft().addScheduledTask(() -> {				
				for(int i=0; i < 8; i++) {
					double posx = p.pos.getX() + blah.nextDouble();
					double posy = p.pos.getY() + 1;
					double posz = p.pos.getZ() + blah.nextDouble();
					
					float r = 86 / 255f;
					float g = 216 / 255f;
					float b = 162 / 255f;
					
					float radius = blah.nextFloat() * 0.7f;
					
					float spx = (blah.nextFloat() - .5f) / 10f;
					float spy = 0.01f + blah.nextFloat() * 0.1f;
					float spz = (blah.nextFloat() - .5f) / 10f;
					
					float weirdness = blah.nextFloat() + .01f;
					
					Botania.proxy.wispFX(posx, posy, posz, r, g, b, radius, spx, spy, spz, weirdness);
				}
			});
			
			return null;
		}
	}
}
