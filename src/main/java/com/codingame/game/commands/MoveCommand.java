package com.codingame.game.commands;

import com.codingame.game.Grid;
import com.codingame.game.Player;
import com.codingame.gameengine.core.MultiplayerGameManager;

public class MoveCommand extends Command {
	
	private int id;
	private int x;
	private int y;

	public MoveCommand(int id, int x, int y) {
		type = CommandType.MOVE;
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public void execute(String playerName, Grid grid, MultiplayerGameManager<Player> gameManager) {
		grid.moveFixer(id, x, y);
		gameManager.addToGameSummary(String.format("%s moved fixer %d to (%d, %d)", playerName, id, x, y));
	}
}
