package quaternary.botaniatweaks.compat.botania;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.botaniatweaks.BotaniaTweaks;
import quaternary.botaniatweaks.compat.botania.advancement.ManagenStatisticsAdvancementHandler;
import quaternary.botaniatweaks.compat.botania.block.*;
import quaternary.botaniatweaks.compat.botania.item.ItemSpork;
import quaternary.botaniatweaks.compat.botania.misc.*;
import quaternary.botaniatweaks.compat.botania.handler.*;
import quaternary.botaniatweaks.compat.botania.net.PacketAdvancedCrateFX;
import quaternary.botaniatweaks.compat.botania.net.PacketCustomTerraPlate;
import quaternary.botaniatweaks.etc.helper.ClientHelpers;
import quaternary.botaniatweaks.etc.helper.RegHelpers;
import quaternary.botaniatweaks.compat.botania.lexi.BotaniaLexiconHandler;
import quaternary.botaniatweaks.compat.botania.tile.*;
import quaternary.botaniatweaks.compat.botania.tile.render.RenderTileCompressedTinyPotato;
import quaternary.botaniatweaks.net.*;

import java.util.ArrayList;
import java.util.List;

public class BotaniaCompat {
	public static void preinit() {
		BotaniaTweaks.PROXY.registerSidedEventClasses(() -> CommonEvents.class, () -> ClientEvents.class);
		
		MinecraftForge.EVENT_BUS.register(AutoCorporeaSparkPlaceBlockHandler.class);
		MinecraftForge.EVENT_BUS.register(SheepEatAltGrassJoinWorldHandler.class);
		MinecraftForge.EVENT_BUS.register(TNTDuplicatorDetectionWorldTickHander.class);
		MinecraftForge.EVENT_BUS.register(ManagenStatisticsAdvancementWorldTickHandler.class);
		MinecraftForge.EVENT_BUS.register(PotatoRightClickHandler.class);
		MinecraftForge.EVENT_BUS.register(BotaniaLexiconHandler.class);
		BotaniaTweaks.PROXY.registerSidedEventClasses(null, () -> TooltipHandler.class);
		
		BotaniaRegistryReplacements.registerOverrides();
	}
	
	public static void init() {
		ManagenStatisticsAdvancementHandler.init();
		
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.GLASS_BOTTLE, new BehaviorEnderAirDispenser(BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(Items.GLASS_BOTTLE)));
		
		BotaniaTweaksPacketHandler.NET.registerMessage(PacketCustomTerraPlate.Response.class, PacketCustomTerraPlate.class, 0, Side.CLIENT);
		BotaniaTweaksPacketHandler.NET.registerMessage(PacketAdvancedCrateFX.Response.class, PacketAdvancedCrateFX.class, 1, Side.CLIENT);
	}
	
	private static List<BlockCompressedTinyPotato> taters = new ArrayList<>(8);
	private static BlockPottedTinyPotato pottedTater;
	
	public static class CommonEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			for(int compressionLevel = 1; compressionLevel <= 8; compressionLevel++) {
				String regName = "compressed_tiny_potato_" + compressionLevel;
				BlockCompressedTinyPotato tater = RegHelpers.createBlock(new BlockCompressedTinyPotato(compressionLevel), regName);
				taters.add(tater);
				reg.register(tater);
			}
			
			pottedTater = RegHelpers.createBlock(new BlockPottedTinyPotato(), "potted_tiny_potato");
			reg.register(pottedTater);
			
			GameRegistry.registerTileEntity(TileNerfedManaFluxfield.class, BotaniaTweaks.MODID + ":tweaked_fluxfield");
			GameRegistry.registerTileEntity(TileCustomAgglomerationPlate.class, BotaniaTweaks.MODID + ":custom_agglomeration_plate");
			GameRegistry.registerTileEntity(TileCompressedTinyPotato.class, BotaniaTweaks.MODID + ":compressed_tiny_potato");
			GameRegistry.registerTileEntity(TileCustomCraftyCrate.class, BotaniaTweaks.MODID + ":custom_crafty_crate");
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			for(BlockCompressedTinyPotato tater : taters) {
				ItemBlock ib = tater.compressionLevel == 8 ? BotaniaTweaks.PROXY.makeRainbowItem(tater) : new ItemBlock(tater);
				reg.register(RegHelpers.createItemBlock(ib));
			}
			
			reg.register(RegHelpers.createItemBlock(new ItemBlock(pottedTater)));
			
			reg.register(RegHelpers.createItem(new ItemSpork(), "spork"));
		}
	}
	
	public static class ClientEvents {
		@SubscribeEvent
		public static void models(ModelRegistryEvent e) {
			//This whole method sucks dick. TODO make it not suck dick.
			
			for(BlockCompressedTinyPotato tater : taters) {
				ClientHelpers.setModel(tater.getRegistryName().getResourcePath());
			}
			
			ClientHelpers.setModel("spork");
			ClientHelpers.setModel("potted_tiny_potato");
			
			ClientRegistry.bindTileEntitySpecialRenderer(TileCompressedTinyPotato.class, new RenderTileCompressedTinyPotato());
		}
	}
}
