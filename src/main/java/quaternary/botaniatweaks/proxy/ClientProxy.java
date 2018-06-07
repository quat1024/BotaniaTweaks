package quaternary.botaniatweaks.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import quaternary.botaniatweaks.etc.*;
import quaternary.botaniatweaks.tile.TileCompressedTinyPotato;
import quaternary.botaniatweaks.tile.render.RenderTileCompressedTinyPotato;

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
	public Item makeRainbowItem(Block b) {
		return new ItemBlockRainbowBarf(b);
	}
	
	@Override
	public void registerEvents() {
		MinecraftForge.EVENT_BUS.register(ClientEvents.class);
		MinecraftForge.EVENT_BUS.register(Events.class);
	}
}