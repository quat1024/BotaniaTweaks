package quaternary.botaniatweaks.compat.botania.handler;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.botaniatweaks.compat.botania.misc.EntityAIEatAltGrass;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;

public class SheepEatAltGrassJoinWorldHandler {
	@SubscribeEvent
	public static void joinWorld(EntityJoinWorldEvent e) {
		if(BotaniaTweaksConfig.SHEEP_EAT_ALT_GRASS && e.getEntity() instanceof EntitySheep) {
			EntitySheep sheep = (EntitySheep) e.getEntity();
			
			sheep.tasks.addTask(5, new EntityAIEatAltGrass(sheep));
		}
	}
}
