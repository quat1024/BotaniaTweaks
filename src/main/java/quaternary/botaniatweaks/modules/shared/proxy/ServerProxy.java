package quaternary.botaniatweaks.modules.shared.proxy;

import net.minecraftforge.common.MinecraftForge;

import java.util.function.Supplier;

public class ServerProxy {
	public boolean shouldAddLexiconPages() {
		return false;
	}
	
	public void registerSidedEventClasses(Supplier<Class<?>> serverClass, Supplier<Class<?>> clientClass) {
		if(serverClass != null) MinecraftForge.EVENT_BUS.register(serverClass.get());
	}
}
