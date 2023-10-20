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

	public void execute(Player p, Grid grid, MultiplayerGameManager<Player> gameManager) {
		if (grid.isPlayerFixerAtPos(x, y, p.getIndex())) {
			gameManager.addToGameSummary(p.getNicknameToken() + " tried to move fixer " + id + " to (" + x + ", " + y + ") but there is already one of his fixers there.");
			return;
		}
		grid.moveFixer(id, x, y);
		gameManager.addToGameSummary(String.format("%s moved fixer %d to (%d, %d)", p.getNicknameToken(), id, x, y));
	}
}
