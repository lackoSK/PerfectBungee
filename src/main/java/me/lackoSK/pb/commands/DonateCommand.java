package me.lackoSK.pb.commands;

import de.leonhard.storage.Config;
import me.lackoSK.pb.PerfectBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

public class DonateCommand extends SimpleCommand {

	public DonateCommand() {
		super("donate");

		setMinArguments(4);
		setUsage("<buyer> <vipType> <price> <currency>");
	}

	@Override
	public void onCommand() {

		final Config cfg = PerfectBungee.getConfig();
		checkPerm(cfg.getString("Donate.perm"));

		final String buyerName = args[0];
		final String vipType = args[1];
		final String price = args[2];
		final String currency = args[3];

		for (String message : cfg.getStringList("Donate.message")) {

			message = message
					.replace("{player}", buyerName)
					.replace("{vip}", vipType.toUpperCase())
					.replace("{price}", price)
					.replace("{currency}", currency.toUpperCase()
					);

			for (final ProxiedPlayer online : ProxyServer.getInstance().getPlayers())
				Common.tell(online, message);

		}

	}


}


























