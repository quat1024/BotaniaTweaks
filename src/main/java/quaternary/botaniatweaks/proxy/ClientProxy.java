package quaternary.botaniatweaks.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import quaternary.botaniatweaks.item.ItemBlockRainbowBarf;
import quaternary.botaniatweaks.tile.TileCompressedTinyPotato;
import quaternary.botaniatweaks.tile.render.RenderTileCompressedTinyPotato;

import java.util.function.Supplier;

public class ClientProxy extends ServerProxy {
	@Override
	public void registerTESR() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileCompressedTinyPotato.class, new RenderTileCompressedTinyPotato());
	}
	
	@Override
	public boolean shouldAddLexiconPages() {
		return true;
	}
	
	@Override
	public ItemBlock makeRainbowItem(Block b) {
		return new ItemBlockRainbowBarf(b);
	}
	
	@Override
	public void registerSidedEventClasses(Supplier<Class> serverClass, Supplier<Class> clientClass) {
		MinecraftForge.EVENT_BUS.register(serverClass.get());
		MinecraftForge.EVENT_BUS.register(clientClass.get());
	}
}