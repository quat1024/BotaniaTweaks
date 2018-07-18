package quaternary.botaniatweaks.compat.botania.advancement;

import com.google.gson.*;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.compat.botania.wsd.ManaStatisticsWsd;

import java.util.*;

public class TotalGeneratedManaCriterionTrigger implements ICriterionTrigger<TotalGeneratedManaCriterionTrigger.Instance> {
	private static final ResourceLocation RESLOC = new ResourceLocation(BotaniaTweaks.MODID, "total_generated_mana");
	private final Map<PlayerAdvancements, Listeners> listeners = new HashMap<>();
	
	@Override
	public ResourceLocation getId() {
		return RESLOC;
	}
	
	@Override
	public void addListener(PlayerAdvancements adv, Listener<Instance> listener) {
		Listeners l = listeners.computeIfAbsent(adv, Listeners::new);
		l.add(listener);
	}
	
	@Override
	public void removeListener(PlayerAdvancements adv, Listener<Instance> listener) {
		Listeners l = listeners.get(adv);
		if(l != null) {
			listeners.remove(l);
			if(l.isEmpty()) {
				listeners.remove(adv);
			}
		}
	}
	
	@Override
	public void removeAllListeners(PlayerAdvancements adv) {
		listeners.remove(adv);
	}
	
	@Override
	public Instance deserializeInstance(JsonObject json, JsonDeserializationContext ctx) {
		long neededMana = (int) (JsonUtils.getFloat(json, "pools") * 1_000_000);
		
		if (neededMana < 0) {
			throw new JsonSyntaxException("Advancement with negative mana??????? what????");
		} else {
			return new Instance(neededMana);
		}
	}
	
	public void trigger(EntityPlayerMP player, ManaStatisticsWsd wsd) {
		Listeners l = listeners.get(player.getAdvancements());
		if(l != null) {
			l.trigger(wsd);
		}
	}
	
	public static class Listeners {
		private final PlayerAdvancements adv;
		private final Set<Listener<Instance>> listeners = new HashSet<>();
		
		public Listeners(PlayerAdvancements adv) {
			this.adv = adv;
		}
		
		public boolean isEmpty() {
			return listeners.isEmpty();
		}
		
		public void add(Listener<Instance> listener) {
			listeners.add(listener);
		}
		
		public void remove(Listener<Instance> listener) {
			listeners.remove(listener);
		}
		
		public void trigger(ManaStatisticsWsd data) {
			List<Listener<Instance>> successfulInstances = new ArrayList<>();
			
			for(Listener<Instance> l : listeners) {
				if(l.getCriterionInstance().test(data)) {
					successfulInstances.add(l);
				}
			}
			
			for(Listener<Instance> l : successfulInstances) {
				l.grantCriterion(adv);
			}
		}
	} 
	
	public static class Instance extends AbstractCriterionInstance {
		private final long neededMana;
		
		public Instance(long neededMana) {
			super(RESLOC);
			
			this.neededMana = neededMana;
		}
		
		public boolean test(ManaStatisticsWsd data) {
			return data.getTotalMana() > neededMana;
		}
	}
}
