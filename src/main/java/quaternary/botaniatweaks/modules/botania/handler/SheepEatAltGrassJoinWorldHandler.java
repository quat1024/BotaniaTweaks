package quaternary.botaniatweaks.modules.botania.handler;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.botaniatweaks.modules.botania.misc.EntityAIEatAltGrass;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;

public class SheepEatAltGrassJoinWorldHandler {
	@SubscribeEvent
	public static void joinWorld(EntityJoinWorldEvent e) {
		if(BotaniaConfig.SHEEP_EAT_ALT_GRASS && e.getEntity() instanceof EntitySheep) {
			EntitySheep sheep = (EntitySheep) e.getEntity();
			
			sheep.tasks.addTask(5, new EntityAIEatAltGrass(sheep));
		}
	}
}
