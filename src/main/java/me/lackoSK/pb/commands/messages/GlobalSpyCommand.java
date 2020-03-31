package me.lackoSK.pb.commands.messages;

import de.leonhard.storage.Config;
import me.lackoSK.pb.PerfectBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

import java.util.ArrayList;

public class GlobalSpyCommand extends SimpleCommand {

	public GlobalSpyCommand() {
		super("gspy");
	}

	public static ArrayList<ProxiedPlayer> globalspyers = new ArrayList<>();

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
