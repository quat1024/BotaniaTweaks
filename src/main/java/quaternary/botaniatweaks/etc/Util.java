package quaternary.botaniatweaks.etc;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Util {
	private static ModContainer botaniaModContainer = null;
	
	public static ModContainer getBotaniaModContainer() {
		if(botaniaModContainer != null) return botaniaModContainer;
		
		for(ModContainer container : Loader.instance().getActiveModList()) {
			if(container.getModId().equals("botania")) {
				botaniaModContainer = container; break;
			}
		}
		
		if(botaniaModContainer == null) throw new RuntimeException("Couldn't find Botania's mod container, is it not present? Whats goin on here");
		
		return botaniaModContainer;
	}
	
	public static void makeNonFinal(Field f) {
		Field fModifiers;
		try {
			fModifiers = f.getClass().getDeclaredField("modifiers");
			fModifiers.setAccessible(true);
			fModifiers.set(f, f.getModifiers() & ~Modifier.FINAL);
		} catch (Exception e) {
			throw new RuntimeException("java machine broke", e);
		}
	}
}
