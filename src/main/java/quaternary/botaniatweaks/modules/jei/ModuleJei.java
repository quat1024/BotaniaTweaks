package quaternary.botaniatweaks.modules.jei;

import net.minecraftforge.common.config.Configuration;
import quaternary.botaniatweaks.modules.IModule;

public class ModuleJei implements IModule {
	public static boolean FIX_CORPOREA_REQUEST_KEYBIND = true;
	
	@Override
	public void readConfig(Configuration config) {
		//No need to break this out into a separate config class for a single bool...
		FIX_CORPOREA_REQUEST_KEYBIND = config.get("compat.jei", "fixCorporeaRequestKeybind", true, "If 'true', you will be able to use the 'Corporea Request' keybind to click on JEI bookmarks. (This is on by default, unlike basically everything else in the mod, because it doesn't hurt.)").getBoolean();
	}
}
