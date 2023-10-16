package com.codingame.game.commands;

import com.codingame.game.Grid;
import com.codingame.game.Player;
import com.codingame.gameengine.core.MultiplayerGameManager;

public class Command {
	protected CommandType type;
	
	public CommandType getType() {
		return type;
	}

	public void execute(String playerName, Grid grid, MultiplayerGameManager<Player> gameManager) {}
}
