package me.lackosk.pb.commands.messages;

import org.mineacademy.bfo.command.SimpleCommand;

import de.leonhard.storage.Config;
import me.lackosk.pb.PerfectBungee;
import me.lackosk.pb.PlayerCache;
import me.lackosk.pb.utils.Utils;

public class SpyCommand extends SimpleCommand {

	public SpyCommand() {
		super("spy");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		final Config cfg = PerfectBungee.getConfig();
		final PlayerCache cache = PlayerCache.getCache(getPlayer());

		Utils.checkPerm(cfg.getString("Spy.perm"), sender);

		tell(cache.isSpyEnabled() ? cfg.getString("Spy.disabled") : cfg.getString("Spy.enabled"));
		cache.setSpyEnabled(!cache.isSpyEnabled());
	}
}
