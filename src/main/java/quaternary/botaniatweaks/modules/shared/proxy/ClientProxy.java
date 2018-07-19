package quaternary.botaniatweaks.modules.shared.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import quaternary.botaniatweaks.modules.botania.item.ItemBlockRainbowBarf;

import java.util.function.Supplier;

public class ClientProxy extends ServerProxy {
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
		if(serverClass != null) MinecraftForge.EVENT_BUS.register(serverClass.get());
		if(clientClass != null) MinecraftForge.EVENT_BUS.register(clientClass.get());
	}
}