package quaternary.botaniatweaks.modules;

public interface IModule {
	default void preinit() {}
	default void init() {}
	default void postinit(){}
	default void loadComplete(){}
}
