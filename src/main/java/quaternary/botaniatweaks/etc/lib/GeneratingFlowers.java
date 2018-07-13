package quaternary.botaniatweaks.etc.lib;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.*;

public class GeneratingFlowers {
	
	public static final Set<String> activeFlowerClasses;
	public static final Map<String, String> activeFlowerClassesToNames;
	
	public static final Set<String> flowerClasses;
	public static final Map<String, String> flowerClassesToNames;
	
	public static final List<String> flowerNames = new ArrayList<>();
	
	static {
		ImmutableMap.Builder<String, String> all = ImmutableMap.builder();
		ImmutableMap.Builder<String, String> activeOnly = ImmutableMap.builder();
		
		putBotaniaFlower(false, "ArcaneRose", all, activeOnly);
		putBotaniaFlower(false, "Dandelifeon", all, activeOnly);
		putBotaniaFlower(false, "Endoflame", all, activeOnly);
		putBotaniaFlower(false, "Entropinnyum", all, activeOnly);
		putBotaniaFlower(false, "Gourmaryllis", all, activeOnly);
		putBotaniaFlower(false, "Kekimurus", all, activeOnly);
		putBotaniaFlower(false, "Munchdew", all, activeOnly);
		putBotaniaFlower(false, "Narslimmus", all, activeOnly);
		putBotaniaFlower(false, "Rafflowsia", all, activeOnly);
		putBotaniaFlower(false, "ShulkMeNot", all, activeOnly);
		putBotaniaFlower(false, "Spectrolus", all, activeOnly);
		putBotaniaFlower(false, "Thermalily", all, activeOnly);
		putBotaniaFlower(true, "Hydroangeas", all, activeOnly);
		
		activeFlowerClassesToNames = activeOnly.build();
		activeFlowerClasses = ImmutableSet.copyOf(activeFlowerClassesToNames.keySet());
		
		flowerClassesToNames = all.build();
		flowerClasses = ImmutableSet.copyOf(flowerClassesToNames.keySet());
	}
	
	private static void putBotaniaFlower(boolean passive, String name, ImmutableMap.Builder<String, String> all, ImmutableMap.Builder<String, String> activeOnly) {
		String flowerName = name.toLowerCase(Locale.ROOT);
		flowerNames.add(flowerName);
		
		all.put("vazkii.botania.common.block.subtile.generating.SubTile" + name, flowerName);
		
		if(!passive) {
			activeOnly.put("vazkii.botania.common.block.subtile.generating.SubTile" + name, flowerName);
		}
	}
	
	public static String getFlowerName(String className) {
		return flowerClassesToNames.getOrDefault(className, "Unknown flower??");
	}
	
	public static boolean flowerExists(String flowerName) {
		return flowerNames.contains(flowerName);
	}
}
