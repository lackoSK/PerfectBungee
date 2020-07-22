package me.lackosk.pb.events;

import java.util.List;

import org.mineacademy.bfo.Common;

import de.leonhard.storage.Config;
import me.lackosk.pb.PerfectBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerPingListener implements Listener {

	@EventHandler(priority = 64)
	public void onServerPing(ProxyPingEvent event) {
		final ServerPing ping = event.getResponse();
		final Config cfg = PerfectBungee.getConfig();

		final int online = ProxyServer.getInstance().getOnlineCount();

		if (cfg.getBoolean("Motd.normal.enabled")) {
			final ServerPing.Players players = new ServerPing.Players(cfg.getInt("Motd.normal.max"), online, null);

			ping.setPlayers(players);
		} else if (cfg.getBoolean("Motd.justonemore")) {
			final ServerPing.Players players = new ServerPing.Players(online + 1, online, null);

			ping.setPlayers(players);
		}

		final List<String> motd = cfg.getStringList("Motd.motd");
		final BaseComponent component = new TextComponent(Common.colorize(motd.get(0) + "\n" + motd.get(1)));

		ping.setDescriptionComponent(component);

		event.setResponse(ping);
	}
}