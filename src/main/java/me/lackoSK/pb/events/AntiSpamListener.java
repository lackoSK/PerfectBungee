package me.lackoSK.pb.events;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.Valid;

import de.leonhard.storage.Config;
import me.lackoSK.pb.PerfectBungee;
import me.lackoSK.pb.utils.PlayerCache;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AntiSpamListener implements Listener {

	@EventHandler
	public void chatEvent(ChatEvent e) {
		Valid.checkNotNull(e.getSender());
		Valid.checkBoolean(e.getSender().isConnected());

		final Config cfg = PerfectBungee.getConfig();
		final ProxiedPlayer sender = (ProxiedPlayer) e.getSender();

		if (cfg.getBoolean("AntiSpam.enabled")) {

			if (cfg.getBoolean("AntiSpam.allow-bypass"))
				if (sender.hasPermission(cfg.getString("AntiSpam.bypass-perm")))
					return;

			if (!e.isCommand()) {
				PlayerCache cache = PlayerCache.getCache(sender);

				long lastUsedTime = cache.getLastChatMessageTime();
				final long now = System.currentTimeMillis();
				final int COOLDOWN_TIME = cfg.getInt("AntiSpam.cooldown");

				if (lastUsedTime == 0 || (now - lastUsedTime) > COOLDOWN_TIME * 1000) {

					cache.setLastChatMessageTime(now);

				} else {

					final long remainingTime = (((COOLDOWN_TIME * 1000) - (now - lastUsedTime)) / 1000) + 1;

					Common.tell(sender, cfg.getString("AntiSpam.cancelled").replace("{seconds}", "" + remainingTime));

					e.setCancelled(true);

				}
			}
		}
	}
}

