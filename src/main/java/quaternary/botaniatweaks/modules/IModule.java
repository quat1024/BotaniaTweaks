package quaternary.botaniatweaks.modules;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public interface IModule {
	default void preinit() {}
	default void readConfig(Configuration config) {}
	default void init() {}
	default void postinit() {}
	default void loadComplete() {}
	default void serverStarting(FMLServerStartingEvent e){}
}
