package me.lackosk.pb.commands.messages;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

import de.leonhard.storage.Config;
import me.lackosk.pb.PerfectBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReplyCommand extends SimpleCommand {

	public ReplyCommand() {
		super("r|reply");

		setMinArguments(1);
		setUsage("<message>");
		setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Config cfg = PerfectBungee.getConfig();
		final String msg = Arrays.stream(args).map(arg -> arg + " ").collect(Collectors.joining());
		ProxiedPlayer receiver;

		checkPerm(cfg.getString("PrivateMessage.perm"));

		if (PrivateMessageCommand.lastSender.get(sender.getName()) == null)
			returnTell(cfg.getString("PrivateMessage.reply-no-target"));

		receiver = ProxyServer.getInstance()
				.getPlayer(PrivateMessageCommand.lastSender.get(getPlayer().getName()));

		checkNotNull(receiver, cfg.getString("PrivateMessage.offline"));
		checkBoolean(receiver.isConnected(), "PrivateMessage.offline");

		// TODO Get vanish to work

		if (getPlayer().getName()
				.equals(receiver.getName()))
			returnTell(cfg.getString("PrivateMessage.yourself"));

		Common.tell(getPlayer(), cfg.getString("PrivateMessage.sent")
				.replace("{receiver}", receiver.getName())
				.replace("{server}", receiver.getServer()
						.getInfo()
						.getName())
				.replace("{message}", msg));

		Common.tell(receiver, cfg.getString("PrivateMessage.received")
				.replace("{sender}", getPlayer().getName())
				.replace("{server}", getPlayer().getServer()
						.getInfo()
						.getName())
				.replace("{message}", msg));

		PrivateMessageCommand.sendSpyMessage(receiver, getPlayer(), msg);
	}
}
