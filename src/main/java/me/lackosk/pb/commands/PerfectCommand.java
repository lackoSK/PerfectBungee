package me.lackosk.pb.commands;

import java.util.Collections;

import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.PlayerUtil;
import org.mineacademy.bfo.command.SimpleCommand;
import org.mineacademy.bfo.exception.CommandException;
import org.mineacademy.bfo.model.SimpleComponent;

import de.leonhard.storage.Config;
import me.lackosk.pb.PerfectBungee;
import me.lackosk.pb.utils.Manager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;

public class PerfectCommand extends SimpleCommand {

	public PerfectCommand(String command) {
		super(command);
	}

	@Override
	protected void onCommand() {

		final Config cfg = PerfectBungee.getConfig();
		final PluginDescription desc = PerfectBungee.getInstance().getDescription();

		//--------------------------------------------------------//

		SimpleComponent alert = SimpleComponent.of("");

		alert.append("&8>&a alert &8-&f send alert by title to players").onHover("Click to alert players").onClick(ClickEvent.Action.SUGGEST_COMMAND, "/perfectbungee alert ");

		//---------//

		SimpleComponent chatalert = SimpleComponent.of("");

		chatalert.append("&8>&a chatalert &8-&f send alert by chat to players").onHover("Click to alert players").onClick(ClickEvent.Action.SUGGEST_COMMAND, "/perfectbungee chatalert ");

		//---------//

		SimpleComponent online = SimpleComponent.of("");

		online.append("&8>&a online &8-&f show all online players").onHover("Click to show online players").onClick(ClickEvent.Action.RUN_COMMAND, "/perfectbungee online");

		//---------//

		SimpleComponent plugins = SimpleComponent.of("");

		plugins.append("&8>&a plugins &8-&f show all proxy plugins").onHover("Click to show proxy plugins").onClick(ClickEvent.Action.RUN_COMMAND, "/perfectbungee plugins");

		//---------//

		SimpleComponent jump = SimpleComponent.of("");

		jump.append("&8>&a jump &8-&f connect to player's server").onHover("Click to connect to player").onClick(ClickEvent.Action.SUGGEST_COMMAND, "/perfectbungee jump ");

		//---------//

		SimpleComponent reload = SimpleComponent.of("");

		reload.append("&8>&a reload &8-&f reload a config of the plugin").onHover("Click to reload the plugin").onClick(ClickEvent.Action.RUN_COMMAND, "/perfectbungee reload");

		//--------------------------------------------------------//

		if (args.length == 0) {
			//--------------------------------------------------------//
			if (Manager.isUser(sender, cfg)) {

				Manager.smoothLine(sender);
				Manager.emptyChatLine(sender);
				Manager.pluginLogo(sender);

				if (sender.hasPermission(cfg.getString("Alert.perm")))
					alert.send(sender);

				if (sender.hasPermission(cfg.getString("ChatAlert.perm")))
					chatalert.send(sender);

				if (sender.hasPermission(cfg.getString("Online.perm")))
					online.send(sender);

				if (sender.hasPermission(cfg.getString("Plugins.perm")))
					plugins.send(sender);

				if (sender.hasPermission(cfg.getString("Jump.perm")))
					jump.send(sender);

				if (sender.hasPermission(cfg.getString("Reload.perm")))
					reload.send(sender);

				Manager.emptyChatLine(sender);
				Manager.smoothLine(sender);

			} else {
				Manager.smoothLine(sender);
				Manager.pluginLogo(sender, "  ");
				Common.tell(sender, "  " + "&fA plugin by &a" + Manager.removeBrackets(desc.getAuthor()));
				Manager.smoothLine(sender);
			}

			//--------------------------------------------------------//
			return;
		}

		String param = args[0].toLowerCase();

		//--------------------------------------------------------------------------//

		switch (param) {

			case "alert": {
				Manager.checkPerm(cfg.getString("Alert.perm"), sender);

				if (args.length <= 1)
					returnTell("&cUsage: /{label} alert <message>");

				for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					final String message = new Manager().argsBuilder(1, args);

					PlayerUtil.sendTitle(player, "" + cfg.getString("Alert.title").replace("{message}", message).replace("{sender}", sender.getName()), "" + cfg.getString("Alert.subtitle").replace("{message}", message).replace("{sender}", sender.getName()));
				}

				Common.tell(sender, cfg.getString("Alert.alerted").replace("{online}", "" + ProxyServer.getInstance().getOnlineCount()));

				break;
			}

			case "chatalert": {
				Manager.checkPerm(cfg.getString("ChatAlert.perm"), sender);

				if (args.length <= 1)
					returnTell("&cUsage: /{label} chatalert <message>");

				for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					String message = new Manager().argsBuilder(1, args);

					Common.tell(player, "" + cfg.getString("ChatAlert.format").replace("{message}", message).replace("{sender}", sender.getName()));
				}

				break;
			}

			case "online": {
				Manager.checkPerm(cfg.getString("Online.perm"), sender);

				if (ProxyServer.getInstance().getPlayers().isEmpty())
					returnTell(cfg.getString("Online.empty"));

				for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
					SimpleComponent.of(cfg.getString("Online.format").replace("{players}", "").replace("{bungee_online}", "" + ProxyServer.getInstance().getOnlineCount())).append(Manager.split(Collections.singleton(player.getName()), " ,")).onHover(player.getServer().getInfo().getName()).send(getSender());

				}

				break;
			}

			case "plugins": {
				Manager.checkPerm(cfg.getString("Plugins.perm"), sender);

				final StringBuilder builder = new StringBuilder();
				int i = 0;

				for (final Plugin plugin : BungeeCord.getInstance().getPluginManager().getPlugins()) {
					if (i != 0)
						builder.append(cfg.getString("Plugins.spectator_format"));

					builder.append(cfg.getString("Plugins.plugin_format").replace("{plugin}", plugin.getFile().getName()));
					i++;
				}
				Common.tell(sender, cfg.getString("Plugins.prefix") + builder.toString());

				break;
			}

			case "jump": {
				checkConsole();
				Manager.checkPerm(cfg.getString("Jump.perm"), getPlayer());

				if (args.length <= 1)
					returnTell("&cUsage: /{label} jump <player>");

				final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
				checkNotNull(target, cfg.getString("Jump.offline").replace("{player}", args[1]));

				final ServerInfo targetServer = ProxyServer.getInstance().getServerInfo(target.getServer().getInfo().getName());
				final ServerInfo senderServer = ProxyServer.getInstance().getServerInfo(getPlayer().getServer().getInfo().getName());

				checkNotNull(targetServer, "Error while performing this command ");
				checkNotNull(senderServer, "Error while performing this command ");

				if (targetServer.equals(senderServer))
					returnTell(cfg.getString("Jump.already"));

				getPlayer().connect(targetServer);

				Common.tell(getPlayer(), cfg.getString("Jump.connected").replace("{target}", target.getName()).replace("{server}", targetServer.getName()));

				break;

			}

			case "reload": {
				Manager.checkPerm(cfg.getString("Reload.perm"), sender);
				PerfectBungee.getInstance().reload();

				Common.tell(sender, cfg.getString("Reload.reloaded"));

				break;
			}

			default: {
				tell("&c&l[!] &cPlease provide valid argument.");

				break;
			}
		}

		//--------------------------------------------------------------------------//

	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		this.sender = sender;
		this.args = args;

		try {

			this.onCommand();

		} catch (CommandException var) {

			if (var.getMessages() != null)
				this.tell(var.getMessages());

		} catch (Throwable var1) {
			this.tellNoPrefix("&l&4Houps! &cThere was a problem running this command: {error}".replace("{error}", var1.toString()));
			Common.error(var1, "Failed to execute command /" + this.getLabel() + " " + String.join(" ", args));
		}

	}
}