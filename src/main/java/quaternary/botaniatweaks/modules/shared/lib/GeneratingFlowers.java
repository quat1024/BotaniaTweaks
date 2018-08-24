package quaternary.botaniatweaks.modules.shared.lib;

import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import quaternary.botaniatweaks.BotaniaTweaks;

import java.util.*;

public class GeneratingFlowers {
	
	private static final Set<FlowerData> flowers;
	private static final Map<String, FlowerData> classesToData;
	private static final Map<String, FlowerData> namesToData;
	
	private static final String BOTANIA_CLASS_PREFIX = "vazkii.botania.common.block.subtile.generating.SubTile";
	
	static {
		flowers = new HashSet<>();
		
		//MODDERS: If you are interested in me adding compat for your Botania generating flower, reach out!
		//I'd love to add an event or similar, but this gets hit during ASM time, which is way too early for that.
		//Just pass me the your mod ID, the name of your flower, its class name, and whether it's passive or not.
		//Your flower will gain access to some of my cool tweaks, like being able to set it to decay even if it's
		//not a passive flower, and for users to keep statistics on the amount of mana it generates, with pretty much
		//no effort on your part.
		//
		//While I've got you here - *please* direct Botania Tweaks-related crash reports to me first. This mod does an
		//ungodly amount of ASM transforming.
		
		flowers.add(new FlowerData("botania", "arcanerose", BOTANIA_CLASS_PREFIX + "ArcaneRose", false));
		flowers.add(new FlowerData("botania", "dandelifeon", BOTANIA_CLASS_PREFIX + "Dandelifeon", false));
		flowers.add(new FlowerData("botania", "endoflame", BOTANIA_CLASS_PREFIX + "Endoflame", false));
		flowers.add(new FlowerData("botania", "entropinnyum", BOTANIA_CLASS_PREFIX + "Entropinnyum", false));
		flowers.add(new FlowerData("botania", "gourmaryllis", BOTANIA_CLASS_PREFIX + "Gourmaryllis", false));
		flowers.add(new FlowerData("botania", "kekimurus", BOTANIA_CLASS_PREFIX + "Kekimurus", false));
		flowers.add(new FlowerData("botania", "munchdew", BOTANIA_CLASS_PREFIX + "Munchdew", false));
		flowers.add(new FlowerData("botania", "narslimmus", BOTANIA_CLASS_PREFIX + "Narslimmus", false));
		flowers.add(new FlowerData("botania", "rafflowsia", BOTANIA_CLASS_PREFIX + "Rafflowsia", false));
		flowers.add(new FlowerData("botania", "shulkmenot", BOTANIA_CLASS_PREFIX + "ShulkMeNot", false));
		flowers.add(new FlowerData("botania", "spectrolus", BOTANIA_CLASS_PREFIX + "Spectrolus", false));
		flowers.add(new FlowerData("botania", "thermalily", BOTANIA_CLASS_PREFIX + "Thermalily", false));
		flowers.add(new FlowerData("botania", "hydroangeas", BOTANIA_CLASS_PREFIX + "Hydroangeas", true));
		
		flowers.add(new FlowerData("floralchemy", "petropetunia", "com.minerarcana.floralchemy.block.flower.SubTilePetroPetunia", false));
		
		classesToData = new HashMap<>();
		namesToData = new HashMap<>();
		for(FlowerData data : flowers) {
			classesToData.put(data.className, data);
			namesToData.put(data.name, data);
		}
	}
	
	public static Collection<String> getAllFlowerClassesMayOrMayNotExist() {
		return classesToData.keySet();
	}
	
	public static Collection<FlowerData> getAllFlowerDatas() {
		return flowers;
	}
	
	public static FlowerData flowerDataFromName(String name) {
		return namesToData.get(name);
	}
	
	public static String flowerNameFromClass(String className) {
		return classesToData.get(className).name;
	}
	
	public static Collection<String> getAllFlowerNames() {
		return namesToData.keySet();
	}
	
	public static boolean hasFlowerNamed(String name) {
		return namesToData.keySet().contains(name);
	}
	
	public static class FlowerData {
		public FlowerData(String modId, String name, String className, boolean isPassive) {
			this.modId = modId;
			this.name = name;
			this.className = className;
			this.isPassive = isPassive;
		}
		
		public final String modId;
		public final String name;
		public final String className;
		public final boolean isPassive;
		private boolean isPresent = false;
		
		public boolean isPresent() {
			return isPresent;
		}
		
		public void markPresent() {
			isPresent = true;
		}
	}
	
	public static class PostInitHandler {
		//hidden stuff in a secret inner class to prevent classloading issues ;)
		public static void postinit() {
			for(FlowerData data : flowers) {
				if(Loader.isModLoaded(data.modId)) {
					if(classExists(data.className)) {
						data.markPresent();
					} else {
						BotaniaTweaks.LOG.error("Mod " + data.modId + " is loaded, but flower " + data.name + " is not present??");
						BotaniaTweaks.LOG.info("This is a bug in Botania Tweaks, not in " + data.modId + "!");
						BotaniaTweaks.LOG.info("Please report to me first so I can fix it! Thanks!");
					}
				}
			}
		}
		
		private static boolean classExists(String className) {
			try {
				return Class.forName(className) != null;
			} catch (ClassNotFoundException e) {
				return false;
			}
		}
	}
}
