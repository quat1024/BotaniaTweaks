package quaternary.botaniatweaks.asm;

import mezz.jei.api.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.asm.monkeypatch.IHardSpectrolus;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import quaternary.botaniatweaks.modules.botania.wsd.ManaStatisticsWsd;
import quaternary.botaniatweaks.modules.jei.BotaniaTweaksJeiPlugin;
import quaternary.botaniatweaks.modules.jei.ModuleJei;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.generating.SubTileSpectrolus;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
	
	/// hard spectrolus tweak
	
	public static void hardSpectrolusInitialize(SubTileSpectrolus tile) {
		//Always runs even in regular mode
		//Not a problem because the array is initialized so arr[0] = 0, arr[1] = 1
		//and the array lookup is a no-op
		((IHardSpectrolus) tile).btweaks$setWoolOrder(generateDefaultHardSpectrolusArray());
	}
	
	public static void hardSpectrolusWriteToPacketNBT(SubTileSpectrolus tile, NBTTagCompound tag) {
		if(!BotaniaConfig.ADVANCED_SPECTROLUS) return;
		
		tag.setIntArray("btweaks-woolorder", ((IHardSpectrolus) tile).btweaks$getWoolOrder());
	}
	
	public static void hardSpectrolusReadFromPacketNBT(SubTileSpectrolus tile, NBTTagCompound tag) {
		if(!BotaniaConfig.ADVANCED_SPECTROLUS) return;
		
		int[] order = tag.getIntArray("btweaks-woolorder");
		if(order.length != 16) order = generateDefaultHardSpectrolusArray();
		
		((IHardSpectrolus) tile).btweaks$setWoolOrder(order);
	}
	
	public static void hardSpectrolusOnBlockPlacedBy(SubTileSpectrolus tile, World world, ItemStack stack) {
		if(!BotaniaConfig.ADVANCED_SPECTROLUS) return;
		
		((IHardSpectrolus) tile).btweaks$setNextColor(ItemNBTHelper.getInt(stack, "btweaks-nextcolor", 0));
		
		int[] order = ItemNBTHelper.getIntArray(stack, "btweaks-woolorder");
		if(order.length != 16) {
			order = shuffleArray(generateDefaultHardSpectrolusArray(), world.rand);
		}
		((IHardSpectrolus) tile).btweaks$setWoolOrder(order);
	}
	
	public static void hardSpectrolusPopulateDropStackNBTs(SubTileSpectrolus tile, List<ItemStack> drops) {
		if(!BotaniaConfig.ADVANCED_SPECTROLUS) return;
		
		ItemNBTHelper.setInt(drops.get(0), "btweaks-nextcolor", ((IHardSpectrolus) tile).btweaks$getNextColor());
		ItemNBTHelper.setIntArray(drops.get(0), "btweaks-woolorder", ((IHardSpectrolus) tile).btweaks$getWoolOrder());
	}
	
	private static int[] generateDefaultHardSpectrolusArray() {
		int[] arr = new int[16];
		for(int i = 0; i < arr.length; i++) arr[i] = i;
		return arr;
	}
	
	private static int[] shuffleArray(int[] arr, Random rand) {
		//fisher-yates shuffle (why is there a primitive to do this on collections, but not arrays...)
		for(int i = arr.length - 1; i > 0; i--) {
			int index = rand.nextInt(i + 1);
			
			int tmp = arr[index];
			arr[index] = arr[i];
			arr[i] = tmp;
		}
		return arr;
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
	
	//Key damage tweak
	
	public static float getKeyDamage() {
		return 20f * BotaniaConfig.KEY_DAMAGE_SCALE;
	}
	
	public static class Jei {
		public static void patchCorporeaKeybind(IJeiRuntime runtime) {
			if(ModuleJei.FIX_CORPOREA_REQUEST_KEYBIND) {
				CorporeaInputHandler.jeiPanelSupplier = () -> {
					IJeiRuntime jeiRuntime = BotaniaTweaksJeiPlugin.jeiRuntime;
					
					Object thing = jeiRuntime.getIngredientListOverlay().getIngredientUnderMouse();
					
					//Here's the change
					if(thing == null) {
						thing = jeiRuntime.getBookmarkOverlay().getIngredientUnderMouse();
					}
					
					if(thing == null && Minecraft.getMinecraft().currentScreen == jeiRuntime.getRecipesGui()) {
						thing = jeiRuntime.getRecipesGui().getIngredientUnderMouse();
					}
					
					if(thing instanceof ItemStack) return (ItemStack) thing;
					else return null;
				};
			}
		}
	}
}
