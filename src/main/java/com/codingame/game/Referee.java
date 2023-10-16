package com.codingame.game;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.game.commands.Command;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
	@Inject private Provider<Grid> gridProvider;

	private Random random;
	private Grid grid;

    @Override
    public void init() {
		random = new Random(gameManager.getSeed());
		grid = gridProvider.get();

		gameManager.setMaxTurns(20);
		gameManager.setFrameDuration(2000);

        drawBackground();
		for (Player player : gameManager.getPlayers()) {
            int x = player.getIndex() == 0 ? 0 : 1920 - 600;
			player.initHud(x);
        }
		grid.initGrid(random, gameManager);
    }

	private void drawBackground() {
		graphicEntityModule.createSprite()
			.setImage("background.png")
			.setAnchor(0);
	}

	private void drawHud() {
        for (Player player : gameManager.getPlayers()) {
            player.updateHud();
        }
    }

    @Override
    public void gameTurn(int turn) {
		List<String> gridExport = grid.export();
        for (Player player : gameManager.getActivePlayers()) {
            player.sendInputLine("" + gridExport.size());
			for (String line : gridExport) {
				player.sendInputLine(line);
			}
            player.execute();
        }

        for (Player player : gameManager.getActivePlayers()) {
            try {
                List<String> outputs = player.getOutputs();
				if (outputs.size() != 1)
					player.deactivate(String.format("$%d outputs (1 required)!", outputs.size()));
				else {
					List<Command> commands = player.parse(outputs.get(0), grid);
					for (Command command : commands) {
						command.execute(player.getNicknameToken(), grid, gameManager);
					}
				}
            } catch (TimeoutException e) {
				gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " timeout!"));
                player.deactivate(String.format("$%d timeout!", player.getIndex()));
				player.setScore(-1);
            	endGame();
            } catch (Exception e) {
				gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " eliminated: " + e.getMessage()));
				player.deactivate(String.format("$%d invalid input!", player.getIndex()));
				player.setScore(-1);
				endGame();
			}
        }

		grid.update();
		drawHud();
    }

	private void endGame() {
        gameManager.endGame();
	}
}
