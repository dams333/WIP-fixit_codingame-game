package com.codingame.game;
import java.util.List;
import java.util.Random;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Line;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
	@Inject TooltipModule tooltips;

	private Random random;

    @Override
    public void init() {
		random = new Random(gameManager.getSeed());

		gameManager.setMaxTurns(20);

        drawBackground();
		drawHud();
		drawGrid();
    }

	private void drawBackground() {
		graphicEntityModule.createSprite()
			.setImage("background.png")
			.setAnchor(0);
	}

	private void drawHud() {
        for (Player player : gameManager.getPlayers()) {
            int x = player.getIndex() == 0 ? 0 : 1920 - 600;


            Text name = graphicEntityModule.createText(player.getNicknameToken())
                    .setX(x + 370)
                    .setY(25)
                    .setZIndex(20)
                    .setFontSize(30)
                    .setFillColor(0xffffff)
                    .setAnchor(0.5);

			Text credit = graphicEntityModule.createText("" + player.getCredits())
                    .setX(x + 240)
                    .setY(90)
                    .setZIndex(20)
                    .setFontSize(30)
                    .setFillColor(0xffffff)
                    .setAnchor(0);

            Sprite avatar = graphicEntityModule.createSprite()
                    .setX(x)
                    .setY(0)
                    .setZIndex(20)
                    .setImage(player.getAvatarToken())
                    .setAnchor(0)
                    .setBaseHeight(130)
                    .setBaseWidth(130);

            player.hud = graphicEntityModule.createGroup(name, credit, avatar);
        }
    }


	private void drawGrid() {
		for(int x = 0; x < 12; x++) {
			for(int y = 0; y < 5; y++) {
				int srcX = x * 150 + 64;
				int srcY = y * 150 + 248;
				Sprite computer = graphicEntityModule.createSprite()
					.setImage("computer.png")
					.setX(srcX)
					.setY(srcY)
					.setAnchor(0);
				tooltips.setTooltipText(computer, "Computer " + x + ";" + y);
			}
		}
	}

    @Override
    public void gameTurn(int turn) {
        for (Player player : gameManager.getActivePlayers()) {
            player.sendInputLine("input");
            player.execute();
        }

        for (Player player : gameManager.getActivePlayers()) {
            try {
                List<String> outputs = player.getOutputs();
				if (outputs.size() != 1)
					player.deactivate(String.format("$%d outputs (1 required)!", player.getIndex()));
				else
					gameManager.addToGameSummary(String.format("Player %s sended: %s", player.getNicknameToken(), outputs.get(0)));
            } catch (TimeoutException e) {
				gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " timeout!"));
                player.deactivate(String.format("$%d timeout!", player.getIndex()));
				player.setScore(-1);
            	endGame();
            }
        }        
    }

	private void endGame() {
        gameManager.endGame();
	}
}
