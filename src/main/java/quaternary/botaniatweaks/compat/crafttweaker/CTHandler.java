package quaternary.botaniatweaks.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class CTHandler {
	public static List<IAction> ADD_ACTIONS = new ArrayList<>();
	
	@Mod.EventHandler
	public void loadComplete(FMLLoadCompleteEvent e) {
		try {
			ADD_ACTIONS.forEach(CraftTweakerAPI::apply);
		} catch (Exception err) {
			CraftTweakerAPI.logError("There was a problem applying a Botania Tweaks action");
			err.printStackTrace();
		}
	}
}
