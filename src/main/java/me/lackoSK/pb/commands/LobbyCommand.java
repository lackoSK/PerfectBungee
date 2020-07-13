package me.lackoSK.pb.commands;

import java.util.ArrayList;
import java.util.List;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.Valid;
import org.mineacademy.bfo.command.SimpleCommand;

import de.leonhard.storage.Config;
import me.lackoSK.pb.PerfectBungee;
import me.lackoSK.pb.utils.Manager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class LobbyCommand extends SimpleCommand {

	public LobbyCommand() {
		super("lobby|hub");

		setPermission(null);
	}

	@Override
	protected void onCommand() {
		final Config cfg = PerfectBungee.getConfig();

		if (!cfg.getBoolean("Lobby.enabled"))
			return;

		checkConsole();
		Manager.checkPerm(cfg.getString("Lobby.perm"), getPlayer());

		Valid.checkNotNull(getEmptiestServer(), "We could not find any server correctly...");

		for (String fromConfig : cfg.getStringList("Lobby.name"))
			if (getPlayer().getServer().getInfo().getName().equalsIgnoreCase(fromConfig))
				Common.tell(getPlayer(), "&cYou are already on lobby server.");

		if (getEmptiestServer() != null) {
			getPlayer().connect(getEmptiestServer());

			Common.tell(getPlayer(), cfg.getString("Lobby.connected"));
		} else
			Common.tell(getPlayer(), "&cWe could not send you to the hub. Server was not found. Issues? Report (HUB:46)");
	}

	private ServerInfo getEmptiestServer() {

		final Config cfg = PerfectBungee.getConfig();
		final List<ServerInfo> serverInfos = new ArrayList<>();
		final List<Integer> onlineDef = new ArrayList<>();
		int[] onlineWhole;

		for (String serverName : ProxyServer.getInstance().getServers().keySet())
			for (String fromConfig : cfg.getStringList("Lobby.name"))
				if (serverName.equalsIgnoreCase(fromConfig))
					serverInfos.add(ProxyServer.getInstance().getServerInfo(serverName));

		for (ServerInfo info : serverInfos) {
			onlineDef.add(info.getPlayers().size());
			onlineWhole = onlineDef.stream().mapToInt(integer -> integer).toArray();

			final int lowest = getLowestNumber(onlineWhole);

			if (lowest == info.getPlayers().size())
				return info;

		}
		return null;
	}

	private int getLowestNumber(int[] from) {
		int minValue = from[0];
		int i;

		for (i = 1; i < from.length; i++)
			if (from[i] < minValue)
				minValue = from[i];

		return minValue;
	}

}
