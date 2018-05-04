package quaternary.botaniatweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.botaniatweaks.block.BlockCustomAgglomerationPlate;
import quaternary.botaniatweaks.block.BlockNerfedManaFluxfield;
import quaternary.botaniatweaks.dispense.BehaviorEnderAirDispenser;
import quaternary.botaniatweaks.recipe.AgglomerationRecipes;
import quaternary.botaniatweaks.tile.TileCustomAgglomerationPlate;
import quaternary.botaniatweaks.tile.TileNerfedManaFluxfield;
import quaternary.botaniatweaks.util.BlockUtil;

import java.util.ArrayList;

@Mod(modid = BotaniaTweaks.MODID, name = BotaniaTweaks.NAME, version = BotaniaTweaks.VERSION, dependencies = BotaniaTweaks.DEPS)
public class BotaniaTweaks {
	public static final String MODID = "botania_tweaks";
	public static final String NAME = "Botania Tweaks";
	public static final String VERSION = "1.0.0";
	public static final String DEPS = "required-after:botania";
	
	public static final Logger LOG = LogManager.getLogger(NAME);
	
	public static final ArrayList<Block> BLOCKS = new ArrayList<>();
	public static final ArrayList<Item> ITEMS = new ArrayList<>();
	
	@GameRegistry.ItemStackHolder("botania:rfgenerator")
	public static final ItemStack icon = ItemStack.EMPTY;
	public static final CreativeTabs TAB = new CreativeTabs(MODID) {
		@Override
		public ItemStack getTabIconItem() {
			return icon;
		}
	};
	
	static {
		BLOCKS.add(BlockUtil.setName(new BlockNerfedManaFluxfield(), "rfGenerator"));
		BLOCKS.add(BlockUtil.setName(new BlockCustomAgglomerationPlate(), "terraPlate"));
		
		for(Block b : BLOCKS) {
			b.setCreativeTab(TAB);
			ITEMS.add(new ItemBlock(b).setRegistryName(b.getRegistryName()));
		}
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
		AgglomerationRecipes.init();
		
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.GLASS_BOTTLE, new BehaviorEnderAirDispenser(BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(Items.GLASS_BOTTLE)));
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
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			for(Item i : ITEMS) {
				reg.register(i);
			}
		}
	}
}
