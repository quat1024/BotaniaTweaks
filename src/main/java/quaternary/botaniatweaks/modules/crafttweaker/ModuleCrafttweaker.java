package quaternary.botaniatweaks.modules.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import quaternary.botaniatweaks.modules.IModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleCrafttweaker implements IModule {
	public static final List<IAction> ACTIONS = new ArrayList<>();
	public static final List<IAction> LATE_ACTIONS = new ArrayList<>();
	
	@Override
	public void postinit() {
		run(ACTIONS);
	}
	
	@Override
	public void loadComplete() {
		run(LATE_ACTIONS);
	}
	
	private void run(List<IAction> stuff2Do) {
		try {
			stuff2Do.forEach(CraftTweakerAPI::apply);
		} catch(RuntimeException e) {
			CraftTweakerAPI.getLogger().logError("There was some sorta problem applying a Botania tweaks action", e);
		}
	}
}
