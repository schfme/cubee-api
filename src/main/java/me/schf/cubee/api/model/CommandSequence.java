package me.schf.cubee.api.model;

import java.util.List;

public record CommandSequence(List<Command> commands) {

	public static CommandSequence empty() {
		return new CommandSequence(List.of());
	}

}
