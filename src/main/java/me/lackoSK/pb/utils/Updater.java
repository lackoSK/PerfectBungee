package me.lackoSK.pb.utils;

import me.lackoSK.pb.PerfectBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginDescription;
import org.mineacademy.bfo.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Updater {

	private static PluginDescription pdf = PerfectBungee.getInstance().getDescription();

	private static String projectId = "71074";
	private static String latestVersion;
	private static String thisVersion = pdf.getVersion();

	public static void checkForUpdate() {

		ProxyServer.getInstance().getScheduler().runAsync(PerfectBungee.getInstance(), () -> {

			try {

				URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectId);
				URLConnection con = url.openConnection();

				try (BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
					latestVersion = r.readLine();
				}

				if (!latestVersion.equals(PerfectBungee.getVersion())) {

					Common.log(
							"&8" + Common.consoleLineSmooth(),
							"&7&7",
							"&aA new version of &f" + pdf.getName() + " &ais available",
							"&aCurrent: &f" + thisVersion + "&a, new: &f" + latestVersion,
							"&ahttps://www.spigotmc.org/resources/" + projectId + "/",
							"&7&7",
							"&8" + Common.consoleLineSmooth());

				}

			}  catch (IOException ex) {
				ex.printStackTrace();

				Common.log(Manager.getHoops("An unknown error while checking for update."));
			}

		});

	}

	/*public static String[] isRunningLastestVersion() {

		try {

			URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectId);
			URLConnection con = url.openConnection();

			try (BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				latestVersion = r.readLine();
			}

		}  catch (IOException ex) {
			ex.printStackTrace();

			Common.log(Manager.getHoops("An unknown error while checking for update."));
		}

		if (thisVersion.equals(latestVersion)) {

			return new String[] {
					"&2You are running the lastest version",
					""

			};

		}

		return null;
	}*/

}
