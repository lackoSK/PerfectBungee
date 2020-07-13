package me.lackoSK.pb.events;

import java.util.List;
import java.util.Random;

import de.leonhard.storage.Config;
import me.lackoSK.pb.PerfectBungee;
import me.lackoSK.pb.utils.center.CenterManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerPingListener implements Listener {

	@EventHandler(priority = 64)
	public void onServerPing(ProxyPingEvent e) {

		final ServerPing ping = e.getResponse();
		final Config cfg = PerfectBungee.getConfig();

		final int online = ProxyServer.getInstance().getOnlineCount();

		if (cfg.getBoolean("Motd.normal.enabled")) {

			final ServerPing.Players players = new ServerPing.Players(cfg.getInt("Motd.normal.max"), online, null);

			ping.setPlayers(players);

		}

		//   \n

		if (cfg.getBoolean("Motd.justonemore")) {

			final ServerPing.Players players = new ServerPing.Players(online + 1, online, null);

			ping.setPlayers(players);
		}

		List<String> top = cfg.getStringList("Motd.motd1");
		List<String> bot = cfg.getStringList("Motd.motd2");

		Random rand = new Random();

		int randomTop = rand.nextInt(top.size());
		String prepTop = top.get(randomTop);

		int randomBot = rand.nextInt(bot.size());
		String prepBot = bot.get(randomBot);

		String finalTop = prepTop.endsWith("%center") ? CenterManager.centerString(prepTop.replaceAll("%center", "").replaceAll("&", "§")) : prepTop.replaceAll("&", "§");
		String finalBot = prepBot.endsWith("%center") ? CenterManager.centerString(prepBot.replaceAll("%center", "").replaceAll("&", "§")) : prepBot.replaceAll("&", "§");

		ping.setDescription(finalTop + "\n" + finalBot);

		e.setResponse(ping);

	}
}