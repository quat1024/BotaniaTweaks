package quaternary.botaniatweaks.modules.botania.advancement;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import quaternary.botaniatweaks.modules.botania.wsd.ManaStatisticsWsd;

public class ManagenStatisticsAdvancementHandler {
	public static final FlowerGeneratedManaCriterionTrigger FLOWER_GENERATED_MANA_TRIGGER = new FlowerGeneratedManaCriterionTrigger();
	public static final TotalGeneratedManaCriterionTrigger TOTAL_GENERATED_MANA_TRIGGER = new TotalGeneratedManaCriterionTrigger();
	
	public static void init() {
		CriteriaTriggers.register(FLOWER_GENERATED_MANA_TRIGGER);
		CriteriaTriggers.register(TOTAL_GENERATED_MANA_TRIGGER);
	}
	
	public static void handle(World w) {
		if(!(w instanceof WorldServer)) return;
		WorldServer ws = (WorldServer) w;
		
		ManaStatisticsWsd wsd = ManaStatisticsWsd.get(w);
		
		for(EntityPlayer player : ws.playerEntities) {
			if(!(player instanceof EntityPlayerMP)) continue;
			
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			FLOWER_GENERATED_MANA_TRIGGER.trigger(playerMP, wsd);
			TOTAL_GENERATED_MANA_TRIGGER.trigger(playerMP, wsd);
		}
	}
}
