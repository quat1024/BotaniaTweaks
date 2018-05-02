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
import quaternary.botaniatweaks.block.BlockNerfedManaFluxfield;
import quaternary.botaniatweaks.config.BotaniaTweaksConfig;
import quaternary.botaniatweaks.dispense.BehaviorEnderAirDispenser;
import quaternary.botaniatweaks.tile.TileNerfedManaFluxfield;
import quaternary.botaniatweaks.util.BlockUtil;

import java.util.ArrayList;

@Mod(modid = BotaniaTweaks.MODID, name = BotaniaTweaks.NAME, version = BotaniaTweaks.VERSION, dependencies = BotaniaTweaks.DEPS)
public class BotaniaTweaks {
	public static final String MODID = "botania_tweaks";
	public static final String NAME = "Botania Tweaks";
	public static final String VERSION = "1.0.0";
	public static final String DEPS = "required-after:botania";
	
	public static final ArrayList<Block> BLOCKS = new ArrayList<>();
	public static final ArrayList<Item> ITEMS = new ArrayList<>();
	
	public static final CreativeTabs TAB = new CreativeTabs(MODID) {
		@GameRegistry.ItemStackHolder(MODID + ":tweaked_fluxfield")
		public final ItemStack icon = ItemStack.EMPTY;
		
		@Override
		public ItemStack getTabIconItem() {
			return icon;
		}
	};
	
	static {
		BLOCKS.add(BlockUtil.setName(new BlockNerfedManaFluxfield(), "tweaked_fluxfield"));
		
		for(Block b : BLOCKS) {
			b.setCreativeTab(TAB);
			ITEMS.add(new ItemBlock(b).setRegistryName(b.getRegistryName()));
		}
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
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
