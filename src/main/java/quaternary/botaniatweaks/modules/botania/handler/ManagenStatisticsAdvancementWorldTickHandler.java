package quaternary.botaniatweaks.modules.botania.handler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import quaternary.botaniatweaks.modules.botania.advancement.ManagenStatisticsAdvancementHandler;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;

public class ManagenStatisticsAdvancementWorldTickHandler {
	@SubscribeEvent
	public static void update(TickEvent.WorldTickEvent e) {
		if(BotaniaConfig.MANA_GENERATION_STATISTICS && e.world.getTotalWorldTime() % 200 == 0) {
			ManagenStatisticsAdvancementHandler.handle(e.world);
		}
	}
}
