package me.lackoSK.pb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Group {

	private final String name;
	private final String format;
	private final String permission;
	private final Integer order;

	private List<UUID> players = new ArrayList<>();

	public Group(String name, String format, String permission, Integer order) {
		this.name = name;
		this.format = format;
		this.permission = permission;
		this.order = order;
	}

	public void addPlayer(UUID player) {
		players.add(player);
	}
}