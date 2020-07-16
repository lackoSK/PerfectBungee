package me.lackosk.pb.events;

import me.lackosk.pb.commands.AdminChatCommand;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AdminChatListener implements Listener {

	@EventHandler
	public void onChat(ChatEvent event) {
		final ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		final String msg = event.getMessage().toLowerCase();

		if (AdminChatCommand.getInstance().getMode().contains(player) && (!msg.equalsIgnoreCase("/ac")))
			event.setMessage("/" + AdminChatCommand.getInstance().getLabel() + " " + event.getMessage());

	}
}
