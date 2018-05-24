package quaternary.botaniatweaks.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name("Botania Tweaks Core")
@IFMLLoadingPlugin.TransformerExclusions({"quaternary.botaniatweaks"/*, "net.minecraft."*/})
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1337)
public class BotaniaTweakerPlugin implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"quaternary.botaniatweaks.asm.BotaniaTweakerTransformer"};
	}
	
	@Override
	public String getModContainerClass() {
		return "quaternary.botaniatweaks.asm.BotaniaTweakerContainer";
	}
	
	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		
	}
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
