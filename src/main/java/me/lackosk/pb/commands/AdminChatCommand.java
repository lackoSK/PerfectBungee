package me.lackosk.pb.commands;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

import de.leonhard.storage.Config;
import me.lackosk.pb.PerfectBungee;
import me.lackosk.pb.PlayerCache;
import me.lackosk.pb.utils.Utils;
import net.md_5.bungee.api.ProxyServer;

/**
 * AdminChat command for staff.
 */
public class AdminChatCommand extends SimpleCommand {

	/**
	 * The main constructor of AdminChat command
	 */
	public AdminChatCommand() {
		super("ac");

		setUsage("<message>");
		setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {
		final Config cfg = PerfectBungee.getConfig();

		checkConsole();

		if (!getPlayer().hasPermission(cfg.getString("AdminChat.perm")))
			returnTell("&cYou don't have permissions to do this!");

		if (!cfg.getBoolean("AdminChat.enabled"))
			returnTell("&cAdmin chat is disabled");

		final PlayerCache cache = PlayerCache.getCache(getPlayer());

		if (args.length == 0) {
			Common.tell(sender, cache.isAdminChatEnabled() ? cfg.getString("AdminChat.removed") : cfg.getString("AdminChat.added"));
			cache.setAdminChatEnabled(!cache.isAdminChatEnabled());

		} else
			ProxyServer.getInstance()
					.getPlayers()
					.stream()
					.filter(players -> players.hasPermission(cfg.getString("AdminChat.perm")))
					.forEach(players -> Common.tell(players, cfg.getString("AdminChat.format")
							.replace("{sender}", getPlayer().getName())
							.replace("{server}", getPlayer().getServer()
									.getInfo()
									.getName())
							.replace("{message}", Utils.getArgumentsIndex(0, args))));

	}
}
