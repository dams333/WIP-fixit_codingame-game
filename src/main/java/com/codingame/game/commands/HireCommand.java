package com.codingame.game.commands;

import com.codingame.game.Grid;
import com.codingame.game.Player;
import com.codingame.gameengine.core.MultiplayerGameManager;

public class HireCommand extends Command {
	
	private int x;
	private int y;

	public HireCommand(int x, int y) {
		type = CommandType.MOVE;
		this.x = x;
		this.y = y;
	}

	public void execute(Player p, Grid grid, MultiplayerGameManager<Player> gameManager) {
		if (p.getScore() < 10) {
			gameManager.addToGameSummary(p.getNicknameToken() + " tried to hire a fixer at (" + x + ", " + y + ") but he doesn't have enough money.");
			return;
		}
		if (grid.isPlayerFixerAtPos(x, y, p.getIndex())) {
			gameManager.addToGameSummary(p.getNicknameToken() + " tried to hire a fixer at (" + x + ", " + y + ") but there is already one of his fixers there.");
			return;
		}
		p.setScore(p.getScore() - 10);
		grid.hireFixer(p, x, y);
		gameManager.addToGameSummary(String.format("%s hire a new fixer at (%d, %d)", p.getNicknameToken(), x, y));
	}
}
