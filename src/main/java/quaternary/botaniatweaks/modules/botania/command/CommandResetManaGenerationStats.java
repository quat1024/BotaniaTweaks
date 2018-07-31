package quaternary.botaniatweaks.modules.botania.command;

import com.google.common.collect.ImmutableList;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import quaternary.botaniatweaks.modules.botania.config.BotaniaConfig;
import quaternary.botaniatweaks.modules.botania.wsd.ManaStatisticsWsd;
import quaternary.botaniatweaks.modules.shared.lib.GeneratingFlowers;

import javax.annotation.Nullable;
import java.util.*;

public class CommandResetManaGenerationStats extends CommandBase {
	@Override
	public String getName() {
		return "botaniatweaks-reset-stats";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "botania_tweaks.commands.reset_stats.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(!BotaniaConfig.MANA_GENERATION_STATISTICS) {
			throw new CommandException("botania_tweaks.commands.shared.stats_disabled");
		}
		
		if(args.length != 1) {
			throw new CommandException("botania_tweaks.commands.reset_stats.usage");
		}
		
		String flowerName = args[0].toLowerCase(Locale.ROOT);
		ManaStatisticsWsd wsd = ManaStatisticsWsd.get(server.getEntityWorld());
		
		if(flowerName.equals("all")) {
			wsd.resetAllMana();
			send(sender, "success.all_flowers");
		} else {
			if(GeneratingFlowers.flowerExists(flowerName)) {
				wsd.resetManaFor(flowerName);
				send(sender, "success.for_flower", flowerName);
			} else {
				throw new CommandException("botania_tweaks.commands.shared.unknown_generating_flower", flowerName);
			}
		}
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if(args.length == 1) {
			ArrayList<String> list = new ArrayList<>(GeneratingFlowers.flowerNames);
			list.add("all");
			return getListOfStringsMatchingLastWord(args, list);
		} else return Collections.emptyList();
	}
	
	private static void send(ICommandSender sender, String component, Object... args) {
		TextComponentTranslation txt = new TextComponentTranslation("botania_tweaks.commands.reset_stats." + component, args);
		sender.sendMessage(txt);
	}
}
