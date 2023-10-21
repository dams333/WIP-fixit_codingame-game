package com.codingame.game.commands;

import com.codingame.game.Grid;
import com.codingame.game.Player;
import com.codingame.gameengine.core.MultiplayerGameManager;

public class FireCommand extends Command {
	
	private int id;

	public FireCommand(int id) {
		type = CommandType.FIRE;
		this.id = id;
	}

	public void execute(Player p, Grid grid, MultiplayerGameManager<Player> gameManager) {
		if (!grid.isPlayerFixer(id, p.getIndex())) {
			return;
		}
		grid.fireFixer(id);
		gameManager.addToGameSummary(String.format("%s fired fixer %d", p.getNicknameToken(), id));
	}
}
