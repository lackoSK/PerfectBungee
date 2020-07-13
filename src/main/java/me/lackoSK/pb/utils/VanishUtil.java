package me.lackoSK.pb.utils;

import java.util.ArrayList;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.Valid;

import de.myzelyam.api.vanish.BungeePlayerHideEvent;
import de.myzelyam.api.vanish.BungeePlayerShowEvent;
import de.myzelyam.api.vanish.BungeeVanishAPI;
import me.lackoSK.pb.PerfectBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class VanishUtil implements Listener {

	public static ArrayList<ProxiedPlayer> vanished = new ArrayList<>();
	private final PerfectBungee pl;

	public VanishUtil(PerfectBungee main) {
		this.pl = main;
	}

	public static boolean isVanished(ProxiedPlayer player) {
		return BungeeVanishAPI.getInvisiblePlayers().contains(player.getUniqueId());
	}

	@EventHandler
	public void onHide(BungeePlayerHideEvent e) {

		Valid.checkNotNull(e.getPlayer());

		vanished.add(e.getPlayer());

		Common.tell(e.getPlayer(), Manager.getChat("You have been to vanished list. You are now protected"));

	}

	@EventHandler
	public void onShow(BungeePlayerShowEvent e) {
		Valid.checkNotNull(e.getPlayer());

		vanished.remove(e.getPlayer());

		Common.tell(e.getPlayer(), Manager.getChat("You have been removed from vanished list. Your protection has been stopped"));
	}

	@EventHandler
	public void onDisconnect(ServerDisconnectEvent e) {

		Valid.checkNotNull(e.getPlayer());

		if (!vanished.contains(e.getPlayer()))
			return;

		if (vanished.contains(e.getPlayer())) {
			vanished.remove(e.getPlayer());

			Common.log("&aPlayer &f{player} &aleft vanished. Stopping his protection.".replace("{player}", e.getPlayer().getName()));

		}

	}

}
