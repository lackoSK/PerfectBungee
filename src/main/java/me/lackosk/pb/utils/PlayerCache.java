package me.lackosk.pb.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Stores data for player
 *
 * @author Ladislav Proc
 */
public class PlayerCache {

	/**
	 * Map that stores the cache instance(s)
	 */
	private static final Map<UUID, PlayerCache> cacheMap = new HashMap<>();
	/**
	 * UUID of the player applied as path prefix.
	 */
	private final String uuid;
	/**
	 * Stores the time when player sent his
	 * last message to chat.
	 */
	@Getter
	@Setter
	private long lastChatMessageTime;

	protected PlayerCache(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Creates a cache or returns
	 * existing one
	 *
	 * @param player player to create for
	 *
	 * @return the new PlayerCache
	 */
	public static PlayerCache getCache(final ProxiedPlayer player) {
		PlayerCache cache = cacheMap.get(player.getUniqueId());

		if (cache == null) {
			cache = new PlayerCache(player.getUniqueId().toString());

			cacheMap.put(player.getUniqueId(), cache);
		}

		return cache;
	}

}
