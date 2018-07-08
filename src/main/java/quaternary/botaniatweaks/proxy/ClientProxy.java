package quaternary.botaniatweaks.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import quaternary.botaniatweaks.etc.*;
import quaternary.botaniatweaks.item.ItemBlockRainbowBarf;
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
	public ItemBlock makeRainbowItem(Block b) {
		return new ItemBlockRainbowBarf(b);
	}
	
	@Override
	public void registerEvents() {
		MinecraftForge.EVENT_BUS.register(ClientEvents.class);
		MinecraftForge.EVENT_BUS.register(Events.class);
	}
}