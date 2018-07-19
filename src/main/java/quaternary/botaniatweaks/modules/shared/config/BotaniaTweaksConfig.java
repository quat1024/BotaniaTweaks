package quaternary.botaniatweaks.modules.shared.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.modules.IModule;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = BotaniaTweaks.MODID)
public class BotaniaTweaksConfig {
	public static Configuration config;
	
	public static void initConfig() {
		config = new Configuration(new File(Loader.instance().getConfigDir(), "botaniatweaks.cfg"), "1");
		config.load();
		
		readConfig();
	}
	
	public static void readConfig() {
		for(IModule m : BotaniaTweaks.modules) {
			m.readConfig(config);
		}
		
		if(config.hasChanged()) config.save();
	}
	
	public static <T extends Enum> T getEnum(Configuration config, String configName, String configCategory, T defaultValue, String configDescription, Function<T, String> describerFunction, Class<T> enumClass) {
		//FEAR MY TERRIBLE FUNCTIONAL BULLSHIT, HAHAHAAA
		//just pretend the inside of this method doesn't exist, because it's otherwise a great utility function
		
		T[] enumConstants = enumClass.getEnumConstants();
		
		String[] enumNames = Arrays.stream(enumConstants).map(T::toString).toArray(String[]::new);
		
		String configAndValueDescription = configDescription + "\n" + Arrays.stream(enumConstants).map(t -> "\"" + t.toString() + "\": " + describerFunction.apply(t)).reduce((one, two) -> one + '\n' + two).get();
		
		String userProvidedString = config.getString(configName, configCategory, defaultValue.toString(), configAndValueDescription, enumNames);
		
		Optional<T> userEnum = Arrays.stream(enumConstants).filter(t -> t.toString().equals(userProvidedString)).findAny();
		
		if(userEnum.isPresent()) return userEnum.get();
		else throw new IllegalArgumentException("\"" + userProvidedString + "\" is not a valid value for config option " + configName + "! See the config file for details");
	}
	
	@SubscribeEvent
	public static void configChanged(ConfigChangedEvent e) {
		if(e.getModID().equals(BotaniaTweaks.MODID)) {
			readConfig();
		}
	}
}
