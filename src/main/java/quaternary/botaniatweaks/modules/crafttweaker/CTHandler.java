package quaternary.botaniatweaks.modules.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class CTHandler {
	public static List<IAction> ADD_ACTIONS = new ArrayList<>();
	public static List<IAction> REMOVE_ACTIONS = new ArrayList<>();
	
	public static void init() {
		try {
			ADD_ACTIONS.forEach(CraftTweakerAPI::apply);
			REMOVE_ACTIONS.forEach(CraftTweakerAPI::apply);
		} catch(Exception e) {
			CraftTweakerAPI.logError("There was a problem applying a Botania Tweaks action");
			//print the error to the log
			StringWriter out = new StringWriter();
			e.printStackTrace(new PrintWriter(out));
			CraftTweakerAPI.logError(out.toString());
		}
	}
}
