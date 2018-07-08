package quaternary.botaniatweaks.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import quaternary.botaniatweaks.etc.Events;

public class ServerProxy {
	public void registerTESR() {
		//No-op
	}
	
	public boolean shouldAddLexiconPages() {
		return false;
	}
	
	//I know this is stupid.
	//But there's a SideOnly bug(...?) where the FontRenderer
	//information is not fully stripped from ItemBlockRainbowBarf.
	//So it crashes on servers.
	public ItemBlock makeRainbowItem(Block b) {
		return new ItemBlock(b);
	}
	
	public void registerEvents() {
		MinecraftForge.EVENT_BUS.register(Events.class);
	}
}
