package quaternary.botaniatweaks.compat.botania.handler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import quaternary.botaniatweaks.compat.botania.advancement.ManagenStatisticsAdvancementHandler;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;

public class ManagenStatisticsAdvancementWorldTickHandler {
	@SubscribeEvent
	public static void update(TickEvent.WorldTickEvent e) {
		if(BotaniaTweaksConfig.MANA_GENERATION_STATISTICS && e.world.getTotalWorldTime() % 200 == 0) {
			ManagenStatisticsAdvancementHandler.handle(e.world);
		}
	}
}
