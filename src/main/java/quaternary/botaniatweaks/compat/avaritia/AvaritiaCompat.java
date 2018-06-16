package quaternary.botaniatweaks.compat.avaritia;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.botaniatweaks.BotaniaTweaks;

public class AvaritiaCompat {
	public static void preinit() {
		MinecraftForge.EVENT_BUS.register(AvaritiaCompat.class);
		
		direCrate = new BlockDireCraftyCrate();
	}
	
	private static Block direCrate;
	
	@SubscribeEvent
	public static void blocks(RegistryEvent.Register<Block> b) {
		b.getRegistry().register(direCrate);
		
		GameRegistry.registerTileEntity(TileDireCraftyCrate.class, BotaniaTweaks.MODID + ":dire_crafty_crate");
	}
	
	@SubscribeEvent
	public static void items(RegistryEvent.Register<Item> e) {
		Item crateItem = new ItemBlock(direCrate);
		crateItem.setRegistryName(direCrate.getRegistryName());
		e.getRegistry().register(crateItem);
	}
	
	
}
