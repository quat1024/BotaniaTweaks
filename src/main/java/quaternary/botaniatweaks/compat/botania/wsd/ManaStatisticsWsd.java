package quaternary.botaniatweaks.compat.botania.wsd;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class ManaStatisticsWsd extends WorldSavedData {
	public ManaStatisticsWsd() {
		this(STAT_NAME);
	}
	
	public ManaStatisticsWsd(String name) {
		super(name);
	}
	
	public static final String STAT_NAME = "botaniatweaks_mana_gen_stats";
	
	private Map<String, Long> totalFlowerManaMap = new HashMap<>();
	private long totalManaEver = 0;
	
	public static ManaStatisticsWsd get(World world) {
		MapStorage mapStorage = world.getMapStorage();
		ManaStatisticsWsd inst = (ManaStatisticsWsd) mapStorage.getOrLoadData(ManaStatisticsWsd.class, STAT_NAME);
		
		if(inst == null) {
			inst = new ManaStatisticsWsd();
			mapStorage.setData(STAT_NAME, inst);
		}
		
		return inst;
	}
	
	public void trackMana(String flowerName, int newMana) {
		totalManaEver += newMana;
		
		long perFlowerMana = totalFlowerManaMap.getOrDefault(flowerName, 0L);
		perFlowerMana += newMana;
		totalFlowerManaMap.put(flowerName, perFlowerMana);
		
		markDirty();
	}
	
	public long getTotalFlowerMana(String flowerName) {
		return totalFlowerManaMap.getOrDefault(flowerName, 0L);
	}
	
	public long getTotalMana() {
		return totalManaEver;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setLong("TotalManaEver", totalManaEver);
		
		NBTTagList list = new NBTTagList();
		for(Map.Entry<String, Long> entry : totalFlowerManaMap.entrySet()) {
			NBTTagCompound cmp = new NBTTagCompound();
			cmp.setString("Flower", entry.getKey());
			cmp.setLong("Mana", entry.getValue());
			list.appendTag(cmp);
		}
		
		nbt.setTag("PerFlowerManaEver", list);
		
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		totalManaEver = nbt.getLong("TotalManaEver");
		
		NBTTagList list = nbt.getTagList("PerFlowerManaEver", Constants.NBT.TAG_COMPOUND);
		
		totalFlowerManaMap.clear();
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound cmp = list.getCompoundTagAt(i);
			
			totalFlowerManaMap.put(cmp.getString("Flower"), cmp.getLong("Mana"));
		}
	}
}
