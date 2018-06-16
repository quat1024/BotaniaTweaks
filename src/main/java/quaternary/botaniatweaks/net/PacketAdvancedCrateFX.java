package quaternary.botaniatweaks.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import vazkii.botania.common.Botania;

import java.util.Random;

public class PacketAdvancedCrateFX implements IMessage {
	BlockPos pos;
	float idealMana;
	float usedMana;
	int itemCount;
	
	public PacketAdvancedCrateFX() {}
	
	public PacketAdvancedCrateFX(BlockPos pos, float idealMana, float usedMana, int itemCount) {
		this.pos = pos;
		this.idealMana = idealMana;
		this.usedMana = usedMana;
		this.itemCount = itemCount;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		//you're welcome, cubic chunks
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		
		buf.writeFloat(idealMana);
		buf.writeFloat(usedMana);
		buf.writeInt(itemCount);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		
		idealMana = buf.readFloat();
		usedMana = buf.readFloat();
		itemCount = buf.readInt();
	}
	
	public static class Response implements IMessageHandler<PacketAdvancedCrateFX, IMessage> {
		static Random blah = new Random();
		
		@Override
		public IMessage onMessage(PacketAdvancedCrateFX p, MessageContext messageContext) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				float redThreshold = p.usedMana == 0 ? 0 : (p.idealMana / p.usedMana) * p.itemCount;
				
				for(int i=0; i < Math.max(1, p.itemCount); i++) {
					float spx = (blah.nextFloat() - .5f) / 10f;
					float spz = (blah.nextFloat() - .5f) / 10f;
					
					float r = (i >= redThreshold ? 1 : 86 / 255f);
					float g = (i >= redThreshold ? 0 : 216 / 255f);
					float b = (i >= redThreshold ? 0 : 162 / 255f);
					
					Botania.proxy.wispFX(p.pos.getX() + blah.nextDouble(), p.pos.getY() + 1, p.pos.getZ() + blah.nextDouble(), r, g, b, blah.nextFloat() * 0.7f, spx, 0.01f + blah.nextFloat() * 0.1f, spz, blah.nextFloat());
				}
			});
			
			return null;
		}
	}
}
