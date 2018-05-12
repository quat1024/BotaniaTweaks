package quaternary.botaniatweaks.asm;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.*;
import quaternary.botaniatweaks.BotaniaTweaks;

public class BotaniaTweakerContainer extends DummyModContainer {
	public BotaniaTweakerContainer() {
		super(new ModMetadata());
		ModMetadata mod = getMetadata();
		mod.name = "Botania Tweaks Core";
		mod.modId = "botania_tweaks_core";
		mod.description = "Small ASM hackery to Botania.";
		mod.authorList = ImmutableList.of("quaternary");
		mod.version = "-100";
		mod.parent = BotaniaTweaks.MODID;
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true;
	}
}
