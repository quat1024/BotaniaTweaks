package quaternary.botaniatweaks.modules.shared.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Supplier;

public class ServerProxy {
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
	
	public void registerSidedEventClasses(Supplier<Class> serverClass, Supplier<Class> clientClass) {
		if(serverClass != null) MinecraftForge.EVENT_BUS.register(serverClass.get());
	}
}
