package me.lackosk.pb.events;

import org.mineacademy.bfo.Common;

import de.leonhard.storage.Config;
import me.lackosk.pb.PerfectBungee;
import me.lackosk.pb.utils.PlayerCache;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AntiSpamListener implements Listener {

	@EventHandler
	public void chatEvent(ChatEvent event) {
		final ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
		final Config cfg = PerfectBungee.getConfig();

		if (cfg.getBoolean("AntiSpam.allow-bypass") && sender.hasPermission(cfg.getString("AntiSpam.bypass-perm")))
			return;

		if (!event.isCommand())
			return;

		PlayerCache cache = PlayerCache.getCache(sender);

		long lastUsedTime = cache.getLastChatMessageTime();
		final long now = System.currentTimeMillis();
		final int chatDelay = cfg.getInt("AntiSpam.cooldown");

		if (lastUsedTime == 0 || (now - lastUsedTime) > chatDelay * 1000)
			cache.setLastChatMessageTime(now);
		else {
			Common.tell(sender, cfg.getString("AntiSpam.cancelled").replace("{seconds}", "" + (((chatDelay * 1000) - (now - lastUsedTime)) / 1000) + 1));

			event.setCancelled(true);
		}
	}
}

