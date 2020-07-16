package me.lackosk.pb.commands.messages;

import java.util.ArrayList;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

import de.leonhard.storage.Config;
import me.lackosk.pb.PerfectBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GlobalSpyCommand extends SimpleCommand {

	public static ArrayList<ProxiedPlayer> globalspyers = new ArrayList<>();

	public GlobalSpyCommand() {
		super("gspy");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Config cfg = PerfectBungee.getConfig();

		checkPerm(cfg.getString("gspy.perm"));

		if (globalspyers.contains(getPlayer())) {
			globalspyers.remove(getPlayer());

			Common.tell(sender, cfg.getString("GSpy.disabled"));
		} else {
			globalspyers.add(getPlayer());

			Common.tell(sender, cfg.getString("GSpy.enabled"));
		}
	}
}
