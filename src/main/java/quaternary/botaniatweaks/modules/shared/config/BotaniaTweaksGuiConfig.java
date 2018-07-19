package quaternary.botaniatweaks.modules.shared.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import quaternary.botaniatweaks.modules.shared.config.BotaniaTweaksConfig;

import java.util.List;
import java.util.stream.Collectors;

public class BotaniaTweaksGuiConfig extends GuiConfig {
	public BotaniaTweaksGuiConfig(GuiScreen parent) {
		super(parent, getConfigElements(), BotaniaTweaks.MODID, false, false, BotaniaTweaks.NAME + " Config!");
	}
	
	//Adapted from Choonster's TestMod3. They say they adapted it from EnderIO "a while back".
	//http://www.minecraftforge.net/forum/topic/39880-110solved-make-config-options-show-up-in-gui/
	static List<IConfigElement> getConfigElements() {
		Configuration c = BotaniaTweaksConfig.config;
		//Don't look!
		return c.getCategoryNames().stream().filter(name -> !c.getCategory(name).isChild()).map(name -> new ConfigElement(c.getCategory(name).setLanguageKey(BotaniaTweaks.MODID + ".config." + name))).collect(Collectors.toList());
	}
}
