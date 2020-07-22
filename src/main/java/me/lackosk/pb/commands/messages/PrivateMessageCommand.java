package me.lackosk.pb.commands.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

import de.leonhard.storage.Config;
import me.lackosk.pb.PerfectBungee;
import me.lackosk.pb.PlayerCache;
import me.lackosk.pb.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.TabExecutor;

public class PrivateMessageCommand extends SimpleCommand implements TabExecutor {

	public static HashMap<String, String> lastSender = new HashMap<>();

	public PrivateMessageCommand() {
		super("msg|tell|pm|privatemessage");

		setUsage("<player> <message>");
		setAutoHandleHelp(false);
		setMinArguments(2);
	}

	public static void sendSpyMessage(ProxiedPlayer receiver, ProxiedPlayer sender, String msg) {
		final Config cfg = PerfectBungee.getConfig();

		for (final ProxiedPlayer spy : ProxyServer.getInstance()
				.getPlayers()) {
			final PlayerCache cache = PlayerCache.getCache(spy);
			final String bypassPermission = cfg.getString("PrivateMessage.ignorespy-perm");

			if (spy.equals(receiver) || spy.equals(sender))
				continue;

			if (sender.hasPermission(bypassPermission) || receiver.hasPermission(bypassPermission))
				continue;

			if (cache.isSpyEnabled())
				Common.tell(spy, cfg.getString("Spy.format")
						.replace("{sender}", sender.getName())
						.replace("{receiver}", receiver.getName())
						.replace("{message}", msg));
		}
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Config cfg = PerfectBungee.getConfig();
		final String message = Utils.getArgumentsIndex(1, args);

		Utils.checkPerm(cfg.getString("PrivateMessage.perm"), sender);

		ProxiedPlayer receiver;

		if (args[0].equals("*")) {
			ProxyServer.getInstance()
					.getPlayers()
					.stream()
					.filter(player -> !player.getUniqueId()
							.equals(getPlayer().getUniqueId()))
					.forEach(player -> Common.tell(player, message));

			return;
		}

		receiver = ProxyServer.getInstance()
				.getPlayer(args[0]);

		checkNotNull(receiver, cfg.getString("PrivateMessage.offline"));
		checkBoolean(receiver.isConnected(), "PrivateMessage.offline");

		if (sender.getName()
				.equalsIgnoreCase(receiver.getName()))
			returnTell(cfg.getString("PrivateMessage.yourself"));

		// TODO Get vanish to work

		Common.tell(sender, cfg.getString("PrivateMessage.sent")
				.replace("{receiver}", receiver.getName())
				.replace("{server}", receiver.getServer()
						.getInfo()
						.getName())
				.replace("{message}", message));

		Common.tell(receiver, cfg.getString("PrivateMessage.received")
				.replace("{sender}", getPlayer().getName())
				.replace("{server}", getPlayer().getServer()
						.getInfo()
						.getName())
				.replace("{message}", message));

		lastSender.put(sender.getName(), receiver.getName());
		lastSender.put(receiver.getName(), sender.getName());

		sendSpyMessage(receiver, getPlayer(), message);
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length == 1)
			return ProxyServer.getInstance()
					.getPlayers()
					.stream()
					.filter(player -> player.getName()
							.toLowerCase()
							.startsWith(args[0].toLowerCase()))
					.map(CommandSender::getName)
					.collect(Collectors.toList());

		return new ArrayList<>();
	}

}

