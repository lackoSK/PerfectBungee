package me.lackoSK.pb.commands.messages;

import de.leonhard.storage.Config;
import me.lackoSK.pb.PerfectBungee;
import me.lackoSK.pb.utils.Manager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PrivateMessageCommand extends SimpleCommand implements TabExecutor {

	public static HashMap<String, String> lastSender = new HashMap<>();

	public PrivateMessageCommand() {
		super("msg|tell|pm|privatemessage");

		setUsage("<player> <message>");
		setMinArguments(2);
	}


	@Override
	protected void onCommand() {

		final Config cfg = PerfectBungee.getConfig();

		checkConsole();

		Manager.checkPerm(cfg.getString("PrivateMessage.perm"), sender);

		checkNotNull(sender, "Error while sending this message!");

		ProxiedPlayer receiver;

		String msg;

		if (args[0].equals("*"))
			returnTell( "&c&l[!] &cThis is not supported.");

		if (args[0].equals("**"))
			returnTell( "&c&l[!] &cThis is not supported.");

		receiver = ProxyServer.getInstance().getPlayer(args[0]);

		checkNotNull(receiver, cfg.getString("PrivateMessage.offline"));
		checkBoolean(receiver.isConnected(), "PrivateMessage.offline");


		if (sender.getName().equalsIgnoreCase(receiver.getName()))
			returnTell(cfg.getString("PrivateMessage.yourself"));

/*
		if (PrivateMessageCommand.this.pl.PremiumVanish) {
			if (VanishUtil.vanished.contains(getPlayer())) {
				returnTell(cfg.getString("PrivateMessage.offline"));
			}

		}*/

		final StringBuilder msgBuilder = new StringBuilder();
		for (int i = 1; i < args.length; i++)
			msgBuilder.append(args[i]).append(" ");
		msg = msgBuilder.toString();


		Common.tell(sender, cfg.getString("PrivateMessage.sent")
				.replace("{receiver}", receiver.getName())
				.replace("{server}", receiver.getServer().getInfo().getName())
				.replace("{message}", msg));


		Common.tell(receiver, cfg.getString("PrivateMessage.received")
				.replace("{sender}", getPlayer().getName())
				.replace("{server}", getPlayer().getServer().getInfo().getName())
				.replace("{message}", msg));

		lastSender.put(sender.getName(), receiver.getName());
		lastSender.put(receiver.getName(), sender.getName());


		sendSpyMessage(receiver, getSender(), msg);
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

		Set<String> matches = new HashSet<>();

		if (args.length == 1) {

			String search = args[0].toLowerCase();

			for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {

				if (player.getName().toLowerCase().startsWith(search)) {
					matches.add(player.getName());
				}

			}

		}

		return matches;
	}

	public static void sendSpyMessage(ProxiedPlayer receiver, CommandSender sender, String msg) {

		final Config cfg = PerfectBungee.getConfig();

		for (final ProxiedPlayer spy : ProxyServer.getInstance().getPlayers()) {

			if ((SpyCommand.spyers.contains(sender) || SpyCommand.spyers.contains(receiver))
					&& ((sender.hasPermission(cfg.getString("PrivateMessage.ignorespy-perm")))
					|| (receiver.hasPermission(cfg.getString("PrivateMessage.ignorespy-perm"))))	) {

				return;
			}

			if (SpyCommand.spyers.contains(spy))
				Common.tell(spy, cfg.getString("Spy.format")
						.replace("{sender}", sender.getName())
						.replace("{receiver}", receiver.getName())
						.replace("{message}", msg));
		}

	}

}

