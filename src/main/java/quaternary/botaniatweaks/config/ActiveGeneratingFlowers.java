package quaternary.botaniatweaks.config;

import java.util.*;

//Be careful about classloading here.
public enum ActiveGeneratingFlowers {
	ARCANEROSE("vazkii.botania.common.block.subtile.generating.SubTileArcaneRose", "arcanerose"),
	DANDELIFEON("vazkii.botania.common.block.subtile.generating.SubTileDandelifeon", "dandelifeon"),
	ENDOFLAME("vazkii.botania.common.block.subtile.generating.SubTileEndoflame", "endoflame"),
	ENTROPINNYUM("vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum", "entropinnyum"),
	GOURMARYLLIS("vazkii.botania.common.block.subtile.generating.SubTileGourmaryllis", "gourmaryllis"),
	KEKIMURUS("vazkii.botania.common.block.subtile.generating.SubTileKekimurus", "kekimurus"),
	MUNCHDEW("vazkii.botania.common.block.subtile.generating.SubTileMunchdew", "munchdew"),
	NARSLIMMUS("vazkii.botania.common.block.subtile.generating.SubTileNarslimmus", "narslimmus"),
	RAFFLOWSIA("vazkii.botania.common.block.subtile.generating.SubTileRafflowsia", "rafflowsia"),
	SHULKMENOT("vazkii.botania.common.block.subtile.generating.SubTileShulkMeNot", "shulkmenot"),
	SPECTROLUS("vazkii.botania.common.block.subtile.generating.SubTileSpectrolus", "spectrolus"),
	THERMALILY("vazkii.botania.common.block.subtile.generating.SubTileThermalily", "thermalily");
	
	public String className;
	public String name;
	
	public static List<String> allClasses = new ArrayList<>();
	public static HashMap<String, String> classToNamesMap = new HashMap<>();
	
	ActiveGeneratingFlowers(String className, String name) {
		this.className = className;
		this.name = name;
	}
	
	static {
		for(ActiveGeneratingFlowers flower : ActiveGeneratingFlowers.values()) {
			allClasses.add(flower.className);
			classToNamesMap.put(flower.className, flower.name);
		}
	}
}
