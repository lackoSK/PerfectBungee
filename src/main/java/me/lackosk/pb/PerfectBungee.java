package me.lackosk.pb;

import java.util.Arrays;
import java.util.Map;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.bungee.SimpleBungee;
import org.mineacademy.bfo.plugin.SimplePlugin;

import de.leonhard.storage.Config;
import de.leonhard.storage.LightningBuilder;
import lombok.Getter;
import me.lackosk.pb.commands.AdminChatCommand;
import me.lackosk.pb.commands.DonateCommand;
import me.lackosk.pb.commands.LobbyCommand;
import me.lackosk.pb.commands.PerfectCommand;
import me.lackosk.pb.commands.StaffListCommand;
import me.lackosk.pb.commands.messages.GlobalSpyCommand;
import me.lackosk.pb.commands.messages.PrivateMessageCommand;
import me.lackosk.pb.commands.messages.ReplyCommand;
import me.lackosk.pb.commands.messages.SpyCommand;
import me.lackosk.pb.events.AdminChatListener;
import me.lackosk.pb.events.AntiSpamListener;
import me.lackosk.pb.events.ProtectionListener;
import me.lackosk.pb.events.ServerPingListener;
import me.lackosk.pb.utils.Manager;
import me.lackosk.pb.utils.Updater;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class PerfectBungee extends SimplePlugin {

	@Getter
	public static Config config, groups;

	@Getter
	public static boolean premiumVanishHooked = false;

	private String[] getPluginLogo() {
		return new String[] { "&a  _____           __          _   ____                              ", "&a |  __ \\         / _|        | | |  _ \\                             ", "&a | |__) |__ _ __| |_ ___  ___| |_| |_) |_   _ _ __   __ _  ___  ___ ", "&a |  ___/ _ \\ '__|  _/ _ \\/ __| __|  _ <| | | | '_ \\ / _` |/ _ \\/ _ \\", "&a | |  |  __/ |  | ||  __/ (__| |_| |_) | |_| | | | | (_| |  __/  __/", "&a |_|   \\___|_|  |_| \\___|\\___|\\__|____/ \\__,_|_| |_|\\__, |\\___|\\___|", "&a                                                     __/ |          ", "&a                                                    |___/           " };
	}

	@Override
	protected void onPluginStart() {
		config = LightningBuilder.fromPath("config", getDataFolder().getAbsolutePath()).addInputStream(getResourceAsStream("config.yml")).createConfig();
		groups = LightningBuilder.fromPath("groups", getDataFolder().getAbsolutePath()).addInputStream(getResourceAsStream("groups.yml")).createConfig();

		for (Map.Entry<String, Command> cmd : ProxyServer.getInstance().getPluginManager().getCommands()) {
			System.out.println(cmd.getKey());
		}

		if (ProxyServer.getInstance().getPluginManager().getPlugin("PremiumVanish") != null) {
			premiumVanishHooked = true;

			Common.log("&aSuccessfully hooked with &f'PremiumVanish'");
		}

		registerCommand(new AdminChatCommand());
		registerCommand(new DonateCommand());
		registerCommand(new LobbyCommand());
		registerCommand(new PerfectCommand(config.getString("PerfectBungee_Commands") + "|perfectbungee"));
		Common.log("&a[+] &7Registered PerfectBungee command with: " + Arrays.asList(config.getString("PerfectBungee_Commands").split("\\|")) + "+ &aperfectbungee - the default one");
		registerCommand(new PrivateMessageCommand());
		registerCommand(new ReplyCommand());
		registerCommand(new SpyCommand());
		registerCommand(new GlobalSpyCommand());

		registerCommand(new StaffListCommand());

		registerEvents(new AdminChatListener());
		registerEvents(new ProtectionListener());
		registerEvents(new ServerPingListener());

		registerEventsIf(new AntiSpamListener(), config.getBoolean("AntiSpam.enabled"));

		Common.log(getPluginLogo());

		Common.log("&8" + Common.consoleLineSmooth(), "&7", "&aPlugin by &f" + Manager.removeBrackets(getDescription().getAuthor()) + "&a was enabled successfully!", "&aVersion: &f" + getDescription().getVersion(), "&7", "&8" + Common.consoleLineSmooth());

		Updater.checkForUpdate();

	}

	@Override
	public void onPluginStop() {
		getProxy().getScheduler().cancel(this);
	}

	@Override
	public SimpleBungee getBungeeCord() {
		return null;
	}

}


