package quaternary.botaniatweaks.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import quaternary.botaniatweaks.etc.MathUtil;
import vazkii.botania.common.Botania;

public class PacketCustomTerraPlate implements IMessage {
	int x, y, z, color1, color2;
	float progress;
	
	public PacketCustomTerraPlate() {
	}
	
	public PacketCustomTerraPlate(BlockPos pos, int color1, int color2, float progress) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.color1 = color1;
		this.color2 = color2;
		this.progress = progress;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(color1);
		buf.writeInt(color2);
		buf.writeFloat(progress);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		color1 = buf.readInt();
		color2 = buf.readInt();
		progress = buf.readFloat();
	}
	
	public static class Response implements IMessageHandler<PacketCustomTerraPlate, IMessage> {
		@Override
		public IMessage onMessage(PacketCustomTerraPlate message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				//Adapted from PacketBotaniaEffect case 8
				
				int ticks = (int) MathHelper.clamp(message.progress * 100, 0, 100);
				//BotaniaTweaks.LOG.info(message.progress);
				int totalSpiritCount = 3;
				double tickIncrement = 360.0D / (double) totalSpiritCount;
				int speed = 5;
				double wticks = (double) (ticks * speed) - tickIncrement;
				double r = Math.sin((double) (ticks - 100) / 10.0D) * 2.0D;
				double g = Math.sin(wticks * Math.PI / 180.0D * 0.55D);
				
				for(int ix = 0; ix < totalSpiritCount; ++ix) {
					double x = message.x + Math.sin(wticks * Math.PI / 180.0D) * r + 0.5D;
					double y = message.y + 0.25D + Math.abs(r) * 0.7D;
					double zx = message.z + Math.cos(wticks * Math.PI / 180.0D) * r + 0.5D;
					wticks += tickIncrement;
					
					int color = MathUtil.lerpColor(message.color1, message.color2, message.progress);
					float red = ((color & 0xFF0000) >> 16) / 255f;
					float green = ((color & 0x00FF00) >> 8) / 255f;
					float blue = (color & 0x0000FF) / 255f;
					
					Botania.proxy.wispFX(x, y, zx, red, green, blue, 0.85F, (float) g * 0.05F, 0.25F);
					Botania.proxy.wispFX(x, y, zx, red, green, blue, (float) Math.random() * 0.1F + 0.1F, (float) (Math.random() - 0.5D) * 0.05F, (float) (Math.random() - 0.5D) * 0.05F, (float) (Math.random() - 0.5D) * 0.05F, 0.9F);
					if(ticks == 100) {
						for(int j = 0; j < 15; ++j) {
							Botania.proxy.wispFX(message.x + 0.5D, message.y + 0.5D, message.z + 0.5D, red, green, blue, (float) Math.random() * 0.15F + 0.15F, (float) (Math.random() - 0.5D) * 0.125F, (float) (Math.random() - 0.5D) * 0.125F, (float) (Math.random() - 0.5D) * 0.125F);
						}
					}
				}
			});
			
			return null;
		}
	}
}
