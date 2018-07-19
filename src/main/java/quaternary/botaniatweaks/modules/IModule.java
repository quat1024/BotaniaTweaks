package quaternary.botaniatweaks.modules;

import net.minecraftforge.common.config.Configuration;

public interface IModule {
	default void preinit() {}
	default void readConfig(Configuration config) {}
	default void init() {}
	default void postinit(){}
	default void loadComplete(){}
}
