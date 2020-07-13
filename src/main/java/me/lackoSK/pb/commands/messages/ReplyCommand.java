package me.lackoSK.pb.commands.messages;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

import de.leonhard.storage.Config;
import me.lackoSK.pb.PerfectBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReplyCommand extends SimpleCommand {

	public ReplyCommand() {
		super("r|reply");

		setMinArguments(1);
		setUsage("<message>");
	}

	@Override
	protected void onCommand() {
		final Config cfg = PerfectBungee.getConfig();
		checkConsole();
		checkPerm(cfg.getString("PrivateMessage.perm"));

		checkNotNull(sender, "Error while sending this message!");

		ProxiedPlayer receiver;
		String msg;

		if (PrivateMessageCommand.lastSender.get(sender.getName()) == null)
			returnTell(cfg.getString("PrivateMessage.reply-no-target"));

		receiver = ProxyServer.getInstance().getPlayer(PrivateMessageCommand.lastSender.get(getPlayer().getName()));

		checkNotNull(receiver, cfg.getString("PrivateMessage.offline"));

		checkBoolean(receiver.isConnected(), "PrivateMessage.offline");

	/*	if (ReplyCommand.this.pl.PremiumVanish) {

			if (VanishUtil.vanished.contains(getPlayer())) {
				returnTell(cfg.getString("PrivateMessage.offline"));
			}
	}*/

		if (getPlayer().getName().equals(receiver.getName()))
			returnTell(cfg.getString("PrivateMessage.yourself"));

		final StringBuilder msgBuilder = new StringBuilder();
		for (String arg : args)
			msgBuilder.append(arg).append(" ");

		msg = msgBuilder.toString();

		Common.tell(getPlayer(), cfg.getString("PrivateMessage.sent").replace("{receiver}", receiver.getName()).replace("{server}", receiver.getServer().getInfo().getName()).replace("{message}", msg));

		Common.tell(receiver, cfg.getString("PrivateMessage.received").replace("{sender}", getPlayer().getName()).replace("{server}", getPlayer().getServer().getInfo().getName()).replace("{message}", msg));

		PrivateMessageCommand.sendSpyMessage(receiver, sender, msg);

	}
}
