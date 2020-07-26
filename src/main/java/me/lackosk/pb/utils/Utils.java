package me.lackosk.pb.utils;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.exception.CommandException;

import com.google.common.base.Joiner;

import de.leonhard.storage.Config;
import lombok.NonNull;
import me.lackosk.pb.PerfectBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginDescription;

public class Utils {

	public static String removeBrackets(String from) {
		return from.replace("[", "")
				.replace("]", "");
	}

	public static String split(Iterable<?> objects, String separators) {
		return Joiner.on(separators).join(objects);
	}

	public static void smoothLine(CommandSender sender) {
		if (sender == ProxyServer.getInstance().getConsole())
			Common.tell(sender, Common.consoleLineSmooth());
		else
			Common.tell(sender, "&8" + Common.chatLineSmooth());
	}

	public static boolean isUser(CommandSender sender, Config cfg) {
		return (sender.hasPermission(cfg.getString("Alert.perm"))) || (sender.hasPermission(cfg.getString("ChatAlert.perm"))) || (sender.hasPermission(cfg.getString("Online.perm"))) || (sender.hasPermission(cfg.getString("Jump.perm"))) || (sender.hasPermission(cfg.getString("Plugins.perm"))) || (sender.hasPermission(cfg.getString("Reload.perm")));
	}

	public static void emptyLine(CommandSender toWhom) {
		Common.tell(toWhom, "&7 ");
	}

	public static void pluginInfo(CommandSender toWhom) {

		final PluginDescription desc = PerfectBungee.getInstance()
				.getDescription();

		Common.tell(toWhom, "&a&l" + desc.getName() + " &8" + desc.getVersion());

	}

	public static void pluginInfo(CommandSender toWhom, String prefix) {
		final PluginDescription desc = PerfectBungee.getInstance()
				.getDescription();

		Common.tell(toWhom, prefix + "&a&l" + desc.getName() + " &8" + desc.getVersion());
	}

	public static void checkPerm(@NonNull String perm, @NonNull ProxiedPlayer toCheck) throws CommandException {
		if (perm == null) {
			throw new NullPointerException("perm is marked non-null but is null");
		} else if (!toCheck.hasPermission(perm)) {
			throw new CommandException("&cInsufficient permission ({permission})".replace("{permission}", perm));
		}
	}

	public static void checkPerm(@NonNull String perm, @NonNull CommandSender toCheck) throws CommandException {
		if (perm == null) {
			throw new NullPointerException("perm is marked non-null but is null");
		} else if (!toCheck.hasPermission(perm)) {
			throw new CommandException("&cInsufficient permission ({permission})".replace("{permission}", perm));
		}
	}

	public static String[] getHoops(String message) {
		return new String[] { "&c  _   _                       _ ", "&c  | | | | ___   ___  _ __  ___| |", "&c  | |_| |/ _ \\ / _ \\| '_ \\/ __| |", "&c  |  _  | (_) | (_) | |_) \\__ \\_|", "&4  |_| |_|\\___/ \\___/| .__/|___(_)", "&4                    |_|          ", "&4!-----------------------------------------------------!", "&cError: &f" + message, "&cContact me in pm to get help!", "&4!-----------------------------------------------------!" };
	}

	public static String getArgumentsIndex(int startAt, String[] arguments) {
		return IntStream.range(startAt, arguments.length)
				.mapToObj(i -> arguments[i] + " ")
				.collect(Collectors.joining());
	}

	/**
	 * Sends admin chat
	 *
	 * @param player player who sent the message
	 */
	public static void sendAdminChat(ProxiedPlayer player, String[] arguments) {
		final Config cfg = PerfectBungee.getConfig();

		ProxyServer.getInstance()
				.getPlayers()
				.stream()
				.filter(players -> players.hasPermission(cfg.getString("AdminChat.perm")))
				.forEach(players -> Common.tell(players, cfg.getString("AdminChat.format")
						.replace("{sender}", player.getName())
						.replace("{server}", player.getServer()
								.getInfo()
								.getName())
						.replace("{message}", Utils.getArgumentsIndex(0, arguments))));
	}
}


