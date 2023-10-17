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

		gameManager.setMaxTurns(200);
		gameManager.setFrameDuration(2000);

        drawBackground();
		int mapWidth = grid.getMapWidth();
		int mapHeight = grid.getMapHeight();
		for (Player player : gameManager.getPlayers()) {
            int x = player.getIndex() == 0 ? 0 : 1920 - 600;
			player.initHud(x);
			player.sendInputLine("" + mapWidth);
			player.sendInputLine("" + mapHeight);
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
			List<String> fixersExport = grid.exportFixers(player.getIndex());
			player.sendInputLine("" + fixersExport.size());
			for (String line : fixersExport) {
				player.sendInputLine(line);
			}
            player.execute();
        }

		boolean isEnded = false;
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
                player.deactivate(String.format("%s timeout!", player.getNicknameToken()));
				player.setScore(-1);
            	gameManager.endGame();
				isEnded = true;
				continue;
            } catch (Exception e) {
				gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " eliminated: " + e.getMessage()));
				player.deactivate(String.format("%s invalid input!", player.getNicknameToken()));
				player.setScore(-1);
				gameManager.endGame();
				isEnded = true;
				continue;
			}
        }
		if (isEnded)
			return;

		grid.update();
		drawHud();

		if (turn == 200) {
			gameManager.addToGameSummary("After 200 turns, " + gameManager.getPlayers().get(0).getNicknameToken() + " has " + gameManager.getPlayers().get(0).getScore() + " credits and " + gameManager.getPlayers().get(1).getNicknameToken() + " has " + gameManager.getPlayers().get(1).getScore() + " credits.");
			if (gameManager.getPlayers().get(0).getScore() > gameManager.getPlayers().get(1).getScore()) {
				gameManager.addToGameSummary(GameManager.formatSuccessMessage(gameManager.getPlayers().get(0).getNicknameToken() + " won!"));
				gameManager.getPlayers().get(1).deactivate(gameManager.getPlayers().get(1).getNicknameToken() + " has less credits!");
			} else if (gameManager.getPlayers().get(0).getScore() < gameManager.getPlayers().get(1).getScore()) {
				gameManager.addToGameSummary(GameManager.formatSuccessMessage(gameManager.getPlayers().get(1).getNicknameToken() + " won!"));
				gameManager.getPlayers().get(0).deactivate(gameManager.getPlayers().get(0).getNicknameToken() + " has less credits!");
			} else {
				gameManager.addToGameSummary(GameManager.formatSuccessMessage("It's a draw!"));
			}
			gameManager.endGame();
		}
    }
}
