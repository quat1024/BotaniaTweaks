package quaternary.botaniatweaks.modules.botania.command;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import quaternary.botaniatweaks.modules.botania.wsd.ManaStatisticsWsd;
import quaternary.botaniatweaks.modules.shared.lib.GeneratingFlowers;

import javax.annotation.Nullable;
import java.util.*;

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
			throw new CommandException("botania_tweaks.commands.shared.stats_disabled");
		}
		
		String flowerName;
		if(args.length == 0) {
			flowerName = "all";
		} else if(args.length == 1) {
			flowerName = args[0].toLowerCase(Locale.ROOT);
		} else {
			throw new CommandException("botania_tweaks.commands.debug_stats.usage");
		}
		
		ManaStatisticsWsd wsd = ManaStatisticsWsd.get(server.getEntityWorld());
		
		if(flowerName == null || flowerName.equals("all")) {
			for(String flower : GeneratingFlowers.flowerNames) {
				send(sender, "flower", flower, wsd.getTotalFlowerMana(flower));
			}
			send(sender, "total", wsd.getTotalMana());
		} else if(flowerName.equals("total")) {
			send(sender, "total", wsd.getTotalMana());
		} else if(GeneratingFlowers.flowerExists(flowerName)) {
			send(sender, "flower", flowerName, wsd.getTotalFlowerMana(flowerName));
		} else {
			throw new CommandException("botania_tweaks.commands.shared.unknown_generating_flower", flowerName);
		}
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if(args.length == 1) {
			ArrayList<String> list = new ArrayList<>(GeneratingFlowers.flowerNames);
			list.add("all");
			list.add("total");
			return getListOfStringsMatchingLastWord(args, list);
		} else return Collections.emptyList();
	}
	
	private static void send(ICommandSender sender, String component, Object... args) {
		TextComponentTranslation txt = new TextComponentTranslation("botania_tweaks.commands.debug_stats." + component, args);
		sender.sendMessage(txt);
	}
}
