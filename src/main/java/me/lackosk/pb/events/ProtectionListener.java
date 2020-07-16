package me.lackosk.pb.events;

import java.util.List;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.Valid;

import de.leonhard.storage.Config;
import me.lackosk.pb.PerfectBungee;
import me.lackosk.pb.commands.messages.GlobalSpyCommand;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProtectionListener implements Listener {

	@EventHandler
	public void authMeProtection(ChatEvent event) {

		final Config cfg = PerfectBungee.getConfig();
		final ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
		final String message = event.getMessage();

		if (cfg.getBoolean("GSpy.enabled") && !GlobalSpyCommand.globalspyers.isEmpty())
			for (ProxiedPlayer player : GlobalSpyCommand.globalspyers)
				Common.tell(player, cfg.getString("GSpy.format").replace("{player}", sender.getName()).replace("{message}", message));

		List<String> commands = cfg.getStringList("AuthMe.allowed-commands");

		if (cfg.getBoolean("AuthMe.protect")) {
			Valid.checkNotNull(sender);
			Valid.checkNotNull(message);
			Valid.checkBoolean(sender.isConnected());

			if (!cfg.getStringList("AuthMe.server-name").contains(sender.getServer().getInfo().getName())) {
				return;
			}

			String[] args = message.split(" ");
			boolean anyMatch = commands.stream().anyMatch(s -> s.equalsIgnoreCase(args[0]));

			if (!anyMatch) {
				event.setCancelled(true);

				if (!"none".equalsIgnoreCase(cfg.getString("AuthMe.invalid-command")))
					Common.tell(sender, cfg.getString("AuthMe.invalid-command"));
			}
		}
	}
}



