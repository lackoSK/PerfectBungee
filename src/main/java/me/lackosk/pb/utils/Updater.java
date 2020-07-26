package me.lackosk.pb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.mineacademy.bfo.Common;

import me.lackosk.pb.PerfectBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginDescription;

public class Updater {

	private static final PluginDescription pdf = PerfectBungee.getInstance().getDescription();
	private static final String projectId = "71074";
	private static final String thisVersion = pdf.getVersion();

	private static String latestVersion;

	public static void checkForUpdate() {
		ProxyServer.getInstance().getScheduler().runAsync(PerfectBungee.getInstance(), () -> {
			try {
				final URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectId);
				final URLConnection con = url.openConnection();

				try (BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
					latestVersion = r.readLine();
				}

				int version = 0;
				int current = 0;

				try {
					version = Integer.parseInt(latestVersion);
					current = Integer.parseInt(PerfectBungee.getVersion());
				} catch (NumberFormatException exception) {
					if (!latestVersion.equalsIgnoreCase(PerfectBungee.getVersion()))
						Common.log("&8" + Common.consoleLineSmooth(), "&7&7", "&aA new version of &f" + pdf.getName() + " &ais available", "&aCurrent: &f" + thisVersion + "&a, new: &f" + latestVersion, "&ahttps://www.spigotmc.org/resources/" + projectId + "/", "&7&7", "&8" + Common.consoleLineSmooth());
				}

				if (version < current)
					Common.log("&8" + Common.consoleLineSmooth(), "&7&7", "&aA new version of &f" + pdf.getName() + " &ais available", "&aCurrent: &f" + thisVersion + "&a, new: &f" + latestVersion, "&ahttps://www.spigotmc.org/resources/" + projectId + "/", "&7&7", "&8" + Common.consoleLineSmooth());

			} catch (IOException ex) {
				ex.printStackTrace();

				Common.log(Utils.getHoops("An unknown error while checking for update."));
			}
		});
	}
}
