package me.lackoSK.pb.events;

import me.lackoSK.pb.commands.AdminChatCommand;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AdminChatListener implements Listener {

	@EventHandler
	public void onChat(ChatEvent e) {

		final ProxiedPlayer pl = (ProxiedPlayer) e.getSender();

		final String msg = e.getMessage().toLowerCase();

		if (AdminChatCommand.getInstance().mode.contains(pl) && (!msg.equalsIgnoreCase("/ac"))) {

			e.setMessage("/" + AdminChatCommand.getInstance().getLabel() + " " + e.getMessage());

		}

	}
}
