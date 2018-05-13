package quaternary.botaniatweaks.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.botaniatweaks.BotaniaTweaks;

import java.util.ArrayList;
import java.util.List;

public class CTHandler {
	public static List<IAction> ADD_ACTIONS = new ArrayList<>();
	public static List<IAction> REMOVE_ACTIONS = new ArrayList<>();
	
	public static void init() {
		try {
			REMOVE_ACTIONS.forEach(CraftTweakerAPI::apply);
			ADD_ACTIONS.forEach(CraftTweakerAPI::apply);
		} catch (Exception err) {
			CraftTweakerAPI.logError("There was a problem applying a Botania Tweaks action");
			err.printStackTrace();
		}
	}
}