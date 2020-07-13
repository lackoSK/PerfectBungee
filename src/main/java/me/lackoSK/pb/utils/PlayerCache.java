package me.lackoSK.pb.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerCache {

	private static Map<UUID, PlayerCache> cacheMap = new HashMap<>();

	@Getter
	@Setter
	private long lastChatMessageTime;

	private String uuid;

	private PlayerCache(String uuid) {
		this.uuid = uuid;

	}

	public static PlayerCache getCache(ProxiedPlayer player) {

		PlayerCache cache = cacheMap.get(player.getUniqueId());

		if (cache == null) {
			cache = new PlayerCache(player.getUniqueId().toString());

			cacheMap.put(player.getUniqueId(), cache);
		}

		return cache;
	}

}
