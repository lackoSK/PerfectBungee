package me.lackosk.pb.events;

import me.lackosk.pb.PlayerCache;
import me.lackosk.pb.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AdminChatListener implements Listener {

	@EventHandler
	public void onChat(ChatEvent event) {
		final ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		final String msg = event.getMessage()
				.toLowerCase();
		final PlayerCache cache = PlayerCache.getCache(player);

		if (cache.isAdminChatEnabled() && !msg.equalsIgnoreCase("/ac")) {
			event.setCancelled(true);

			Utils.sendAdminChat(player, event.getMessage()
					.split("\\s"));
		}
	}
}
