package quaternary.botaniatweaks.modules.botania.command;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import quaternary.botaniatweaks.modules.botania.wsd.ManaStatisticsWsd;
import quaternary.botaniatweaks.modules.shared.lib.GeneratingFlowers;

public class CommandDebugManaGenerationStats extends CommandBase {
	@Override
	public String getName() {
		return "botaniatweaks-debug-stats";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "botania_tweaks.commands.debug_stats.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(!BotaniaConfig.MANA_GENERATION_STATISTICS) {
			throw new CommandException("botania_tweaks.commands.debug_stats.disabled");
		}
		
		ManaStatisticsWsd wsd = ManaStatisticsWsd.get(server.getEntityWorld());
		
		send(sender, "start");
		for(String flowerName : GeneratingFlowers.flowerNames) {
			send(sender, "flower", flowerName, wsd.getTotalFlowerMana(flowerName));
		}
		send(sender, "total", wsd.getTotalMana());
	}
	
	private static void send(ICommandSender sender, String component, Object... args) {
		TextComponentTranslation txt = new TextComponentTranslation("botania_tweaks.commands.debug_stats." + component, args);
		sender.sendMessage(txt);
	}
}
