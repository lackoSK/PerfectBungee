package me.lackosk.pb.events;

import java.util.List;

import org.mineacademy.bfo.Common;

import de.leonhard.storage.Config;
import me.lackosk.pb.PerfectBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProtectionListener implements Listener {

	@EventHandler
	public void authMeProtection(ChatEvent event) {
		final Config config = PerfectBungee.getConfig();
		final ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
		final String message = event.getMessage();
		final List<String> commands = config.getStringList("AuthMe.allowed-commands");

		if (config.getBoolean("AuthMe.protect")) {
			if (!config.getStringList("AuthMe.server-name").contains(sender.getServer().getInfo().getName()))
				return;

			String[] args = message.split("\\s");
			boolean anyMatch = commands.stream().anyMatch(s -> s.equalsIgnoreCase(args[0]));

			if (!anyMatch) {
				event.setCancelled(true);

				if (!"none".equalsIgnoreCase(config.getString("AuthMe.invalid-command")))
					Common.tell(sender, config.getString("AuthMe.invalid-command"));
			}
		}
	}
}



