package me.lackoSK.pb.commands;

import java.util.ArrayList;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

import de.leonhard.storage.Config;
import lombok.Getter;
import me.lackoSK.pb.PerfectBungee;
import me.lackoSK.pb.utils.Manager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AdminChatCommand extends SimpleCommand {

	@Getter
	public static AdminChatCommand instance;
	public ArrayList<ProxiedPlayer> mode = new ArrayList<>();

	public AdminChatCommand() {
		super("ac");

		instance = this;
		setUsage("<message>");
	}

	@Override
	protected void onCommand() {
		final Config cfg = PerfectBungee.getConfig();

		checkConsole();
		Manager.checkPerm(cfg.getString("AdminChat.perm"), getPlayer());

		if (cfg.getBoolean("AdminChat.enabled"))
			return;

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
