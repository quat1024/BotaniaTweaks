package quaternary.botaniatweaks;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.botaniatweaks.block.*;
import quaternary.botaniatweaks.compat.crafttweaker.CTHandler;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.dispense.BehaviorEnderAirDispenser;
import quaternary.botaniatweaks.proxy.ServerProxy;
import quaternary.botaniatweaks.recipe.AgglomerationRecipes;
import quaternary.botaniatweaks.tile.*;
import quaternary.botaniatweaks.tile.render.RenderTileCompressedTinyPotato;
import vazkii.botania.common.item.block.ItemBlockMod;

import java.util.*;

@Mod(modid = BotaniaTweaks.MODID, name = BotaniaTweaks.NAME, version = BotaniaTweaks.VERSION, dependencies = BotaniaTweaks.DEPS, guiFactory = "quaternary.botaniatweaks.config.BotaniaTweaksGuiFactory")
public class BotaniaTweaks {
	public static final String MODID = "botania_tweaks";
	public static final String NAME = "Botania Tweaks";
	public static final String VERSION = "1.0.0";
	public static final String DEPS = "required-after:botania";
	
	public static final Logger LOG = LogManager.getLogger(NAME);
	
	public static final ArrayList<Block> BLOCKS = new ArrayList<>();
	public static final ArrayList<Item> ITEMS = new ArrayList<>();
	public static final ArrayList<Block> POTATOES = new ArrayList<>();
	
	@SidedProxy(clientSide = "quaternary.botaniatweaks.proxy.ClientProxy", serverSide = "quaternary.botaniatweaks.proxy.ServerProxy")
	public static ServerProxy PROXY;
	
	static {
		//Overrides
		BLOCKS.add(new BlockNerfedManaFluxfield());
		BLOCKS.add(new BlockCustomAgglomerationPlate());
		
		for(Block b : BLOCKS) {
			Item i = new ItemBlockMod(b).setRegistryName(b.getRegistryName());
			ITEMS.add(i);
		}
		
		//Other blocks and items
		for(int i = 1; i <= 8; i++) {
			POTATOES.add(new BlockCompressedTinyPotato(i));
		}
		
		for(Block b : POTATOES) {
			BLOCKS.add(b);
			Item i = new ItemBlock(b).setRegistryName(b.getRegistryName());
			ITEMS.add(i);
		}
		
		BLOCKS.add(new BlockPottedTinyPotato());
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
		AgglomerationRecipes.init();
		BotaniaTweaksConfig.initConfig();
		
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.GLASS_BOTTLE, new BehaviorEnderAirDispenser(BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(Items.GLASS_BOTTLE)));
	}
	
	@Mod.EventHandler
	public static void loadComplete(FMLLoadCompleteEvent e) {
		if(Loader.isModLoaded("crafttweaker")) {
			CTHandler.init();
		}
	}
	
	@Mod.EventBusSubscriber
	public static class CommonEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			for(Block b : BLOCKS) {
				reg.register(b);
			}
			
			GameRegistry.registerTileEntity(TileNerfedManaFluxfield.class, MODID + ":tweaked_fluxfield");
			GameRegistry.registerTileEntity(TileCustomAgglomerationPlate.class, MODID + ":custom_agglomeration_plate");
			GameRegistry.registerTileEntity(TileCompressedTinyPotato.class, MODID + ":compressed_tiny_potato");
			
			//While we're at it
			PROXY.registerTESR();
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			for(Item i : ITEMS) {
				reg.register(i);
			}
		}
	}
	
	@Mod.EventBusSubscriber
	public static class ClientEvents {
		@SubscribeEvent
		public static void model(ModelRegistryEvent e) {
			for(Item i : ITEMS) {
				if(i instanceof ItemBlockMod) continue;
				ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
			}
		}
	}
}
