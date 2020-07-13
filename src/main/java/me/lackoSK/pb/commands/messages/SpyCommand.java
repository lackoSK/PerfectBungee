package me.lackoSK.pb.commands.messages;

import java.util.ArrayList;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

import de.leonhard.storage.Config;
import me.lackoSK.pb.PerfectBungee;
import me.lackoSK.pb.utils.Manager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SpyCommand extends SimpleCommand {

	public static ArrayList<ProxiedPlayer> spyers = new ArrayList<>();

	public SpyCommand() {
		super("spy");
	}

	@Override
	protected void onCommand() {
		final Config cfg = PerfectBungee.getConfig();
		checkConsole();
		Manager.checkPerm(cfg.getString("Spy.perm"), sender);

		final ProxiedPlayer pl = getPlayer();

		if (spyers.contains(pl)) {
			spyers.remove(pl);

			Common.tell(sender, cfg.getString("Spy.disabled"));
		} else {
			spyers.add(pl);

			Common.tell(sender, cfg.getString("Spy.enabled"));
		}

	}
}
