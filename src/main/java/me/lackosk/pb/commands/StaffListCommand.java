package me.lackosk.pb.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;
import org.mineacademy.bfo.model.SimpleComponent;

import de.leonhard.storage.sections.FlatFileSection;
import de.myzelyam.api.vanish.BungeeVanishAPI;
import me.lackosk.pb.Group;
import me.lackosk.pb.PerfectBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class StaffListCommand extends SimpleCommand {

	public StaffListCommand() {
		super("stafflist|staff");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final ArrayList<Group> groups = new ArrayList<>();
		final FlatFileSection section = PerfectBungee.groups.getSection("Groups");
		final ProxiedPlayer sender = getPlayer();

		final ArrayList<UUID> added = new ArrayList<>();
		for (String groupName : section.singleLayerKeySet()) {
			final String perm = section.getString(groupName + ".permission");
			Group group = new Group(groupName, section.getString(groupName + ".format"), perm, section.getInt(groupName + ".order"));

			for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
				if (player.hasPermission(perm) && !group.getPlayers().contains(player.getUniqueId()) && !added.contains(player.getUniqueId())) {
					group.getPlayers().add(player.getUniqueId());

					added.add(player.getUniqueId());
				}

			groups.add(group);
		}

		for (String header : PerfectBungee.groups.getStringList("Groups_Header")) //send header
			Common.tell(sender, header);

		groups.sort((Comparator.comparing(Group::getOrder)));

		if (groups.isEmpty() && !PerfectBungee.groups.getBoolean("If_Offline_NA"))
			for (String message : PerfectBungee.groups.getStringList("Offline_Message"))
				tell(message);

		for (Group group : groups) {
			final List<UUID> playersToShow = group.getPlayers();

			if (PerfectBungee.isPremiumVanishHooked()) {
				System.out.println("Removing players...");
				System.out.println(playersToShow);
				for (UUID uid : playersToShow) {
					System.out.println(uid.toString() + " ->> " + BungeeVanishAPI.isInvisible(ProxyServer.getInstance().getPlayer(uid)));
				}
				playersToShow.removeIf(uuid -> BungeeVanishAPI.isInvisible(ProxyServer.getInstance().getPlayer(uuid)));
				System.out.println(playersToShow);
			}

			final String format = group.getFormat();
			if (playersToShow.isEmpty()) {
				if (PerfectBungee.groups.getBoolean("If_Offline_NA"))
					tell(format + "N/A");
			} else {
				final SimpleComponent component = SimpleComponent.of(format);
				for (int i = 0; i < playersToShow.size(); i++) {
					final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playersToShow.get(i));

					if (i == playersToShow.size() - 1) // if this is the last item in the array we dont want a ,
						component.append(player.getName()).onHover(player.getServer().getInfo().getName());
					else {
						component.append(player.getName() + ", ").onHover(player.getServer().getInfo().getName());
					}

				}
				component.send(getPlayer());
			}
		}

		for (String footer : PerfectBungee.groups.getStringList("Groups_Footer")) //send footer
			Common.tell(sender, footer);
	}
}