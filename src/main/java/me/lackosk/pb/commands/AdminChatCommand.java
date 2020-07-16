package me.lackosk.pb.commands;

import java.util.ArrayList;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

import de.leonhard.storage.Config;
import lombok.Getter;
import me.lackosk.pb.PerfectBungee;
import me.lackosk.pb.utils.Manager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AdminChatCommand extends SimpleCommand {

	/**
	 * An instance of the AdminChat command to access it from other classes
	 */
	@Getter
	private static AdminChatCommand instance;

	/**
	 * Players that are in AdminChat mode
	 */
	@Getter
	private final ArrayList<ProxiedPlayer> mode = new ArrayList<>();

	/**
	 * The main constructor of AdminChat command
	 */
	public AdminChatCommand() {
		super("ac");

		instance = this;
		setUsage("<message>");
	}

	@Override
	protected void onCommand() {
		final Config cfg = PerfectBungee.getConfig();

		checkConsole();

		if (!getPlayer().hasPermission(cfg.getString("AdminChat.perm")))
			returnTell("&cYou don't have permissions to do this!");

		if (!cfg.getBoolean("AdminChat.enabled"))
			returnTell("&cAdmin chat is disabled");

		if (args.length == 0) {
			if (mode.contains(getPlayer())) {
				mode.remove(getPlayer());

				Common.tell(sender, cfg.getString("AdminChat.removed"));
			} else {
				mode.add(getPlayer());

				Common.tell(sender, cfg.getString("AdminChat.added"));
			}

		} else {
			for (final ProxiedPlayer players : ProxyServer.getInstance().getPlayers())
				if (players.hasPermission(cfg.getString("AdminChat.perm"))) {
					final String msg = new Manager().argsBuilder(0, args);

					Common.tell(players, cfg.getString("AdminChat.format").replace("{sender}", getPlayer().getName()).replace("{server}", getPlayer().getServer().getInfo().getName()).replace("{message}", msg));
				}
		}
	}

}
