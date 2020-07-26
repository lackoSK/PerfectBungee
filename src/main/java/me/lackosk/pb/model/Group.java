package me.lackosk.pb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

/**
 * Represents a group in /staff
 *
 * @author Ladislav Proc
 */
@Data
public class Group {

	/**
	 * A list of players in the group
	 */
	private final List<UUID> players = new ArrayList<>();

	/**
	 * The name of group (key)
	 */
	private final String name;

	/**
	 * The format of group in chat
	 */
	private final String format;

	/**
	 * Permission required to be shown in group
	 */
	private final String permission;

	/**
	 * Order of the group in /staff
	 */
	private final Integer order;
}