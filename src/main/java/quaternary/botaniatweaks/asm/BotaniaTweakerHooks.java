package quaternary.botaniatweaks.asm;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.capability.IFluidHandler;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import quaternary.botaniatweaks.modules.botania.misc.CatchallFlowerComponent;
import quaternary.botaniatweaks.modules.botania.wsd.ManaStatisticsWsd;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.Botania;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unused") //Everything here called through ASM
public class BotaniaTweakerHooks {
	
	/// decay tweak
	
	public static int getPassiveDecayTime() {
		return BotaniaConfig.PASSIVE_DECAY_TIMER;
	}
	
	public static boolean shouldFlowerDecay(String name) {
		return BotaniaConfig.SHOULD_ALSO_BE_PASSIVE_MAP.getOrDefault(name, false);
	}
	
	/// manastorm tweak
	
	public static int getManastormBurstMana() {
		return MathHelper.floor(120 * BotaniaConfig.MANASTORM_SCALE_FACTOR);
	}
	
	public static int getManastormBurstStartingMana() {
		return MathHelper.floor(340 * BotaniaConfig.MANASTORM_SCALE_FACTOR);
	}
	
	//Is this loss
	public static float getManastormBurstLossjpgPerTick() {
		return BotaniaConfig.MANASTORM_SCALE_FACTOR;
	}
	
	/// rosa arcana tweak
	
	public static int getRosaArcanaXPOrbMana() {
		//35 is the default (check subtilearcanerose)
		return (int) (35 * BotaniaConfig.ROSA_ARCANA_ORB_MULTIPLIER);
	}
	
	/// orechid tweak
	
	public static boolean orechidGog = Botania.gardenOfGlassLoaded;
	
	/// entro tnt duplication tweak
	
	public static List<EntityTNTPrimed> processTNTList(List<EntityTNTPrimed> inList) {
		Iterator<EntityTNTPrimed> it = inList.iterator();
		while(it.hasNext()) {
			EntityTNTPrimed tnt = it.next();
			if(BotaniaConfig.DENY_DUPLICATED_TNT && tnt.getTags().contains("CheatyDupe")) {
				if(tnt.getFuse() == 1) doTNTSilliness(tnt);
				
				it.remove();
				continue;
			}
			
			if(BotaniaConfig.FORCE_VANILLA_TNT && !tnt.getClass().equals(EntityTNTPrimed.class)) {
				it.remove();
			}
		}
		return inList;
	}
	
	static void doTNTSilliness(EntityTNTPrimed tnt) {
		try {
			NBTTagCompound fireworkNBT = JsonToNBT.getTagFromJson("{Fireworks:{Flight:1b,Explosions:[{Type:2b,Trail:1b,Colors:[I;15790320]},{Type:1b,Colors:[I;11743532],Flicker:1b}]}}");
			
			ItemStack fireworkItem = new ItemStack(Items.FIREWORKS, 1, 0);
			fireworkItem.setTagCompound(fireworkNBT);
			EntityFireworkRocket firework = new EntityFireworkRocket(tnt.world, tnt.posX, tnt.posY, tnt.posZ, fireworkItem);
			firework.lifetime = 1; //Kaboom!
			tnt.world.spawnEntity(firework);
			
			List<EntityPlayer> nearbyPlayers = tnt.world.playerEntities.stream().filter(player -> player.getDistanceSq(tnt) < 25 * 25).collect(Collectors.toList());
			
			for(EntityPlayer p : nearbyPlayers) {
				TextComponentTranslation flowerName = new TextComponentTranslation("tile.botania:flower.entropinnyum.name");
				TextComponentTranslation niceTry = new TextComponentTranslation("botania_tweaks.entrodupe.nicetry");
				TextComponentTranslation chatString = new TextComponentTranslation("chat.type.text", flowerName, niceTry);
				p.sendMessage(chatString);
			}
			
			tnt.setDead(); //No EXPLOSION!!
		} catch(NBTException bleh) {
			// :D
		}
	}
	
	//Mana statistics
	
	private static String lastFlowerName = null;
	private static int oldMana = 0;
	
	public static void beginManaStatSection(String flowerName, SubTileGenerating flower, int oldMana_) {
		if(!BotaniaConfig.MANA_GENERATION_STATISTICS || flower.getWorld().isRemote) return;
		flowerName = fixThermalilyFlowerName(flowerName, flower);
		
		lastFlowerName = flowerName;
		oldMana = oldMana_;
	}
	
	public static void endManaStatSection(String flowerName, SubTileGenerating flower, int newMana) {
		if(!BotaniaConfig.MANA_GENERATION_STATISTICS ||flower.getWorld().isRemote) return;
		flowerName = fixThermalilyFlowerName(flowerName, flower);
		
		int manaDifference = newMana - oldMana;
		
		//Try to figure out how much mana the flower has generated
		if(flower.canGeneratePassively()) {
			manaDifference += (flower.getWorld().getTotalWorldTime() % flower.getDelayBetweenPassiveGeneration()) == 0 ? flower.getValueForPassiveGeneration() : 0;
		}
		
		if(manaDifference != 0) {
			ManaStatisticsWsd wsd = ManaStatisticsWsd.get(flower.getWorld());
			wsd.trackMana(flowerName, manaDifference);
		}
		
		lastFlowerName = null;
		oldMana = 0;
	}
	
	private static String fixThermalilyFlowerName(String flowerName, SubTileGenerating flower) {
		//Thermalilies extend Hydroangei but don't override onUpdate.
		//When I insert the string "hydroangeas" into that flower's onUpdate with asm
		//any mana from thermalilies gets mistakenly attributed to hydroangei.
		
		//I don't think any other flowers extend each other
		if(flowerName.equals("hydroangeas")) {
			String className = flower.getClass().getName();
			if(className.endsWith("Thermalily")) return "thermalily";
			else return "hydroangeas";
		} else return flowerName;
	}
	
	//Creative pool size tweak
	
	public static int getCreativePoolSize() {
		return BotaniaConfig.CREATIVE_POOL_SIZE;
	}
}
