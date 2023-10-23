package com.codingame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.tooltip.TooltipModule;

public class Cell {
	private GraphicEntityModule graphicEntityModule;
	private TooltipModule tooltips;

	private int x;
	private int y;

	private Group displayGroup;
	private Circle bugCircle;
	private int bugLevel = 0;
	private int originalBugLevel = 0;
	private Random random;

	private int turnToUpgradeBug = 0;

	private List<Player> fixedAtThisTurn = new ArrayList<Player>();

	public Cell(int x, int y, GraphicEntityModule graphicEntityModule, TooltipModule tooltips, Random random) {
		this.random = random;
		this.graphicEntityModule = graphicEntityModule;
		this.tooltips = tooltips;
		this.x = x;
		this.y = y;
		int srcX = x * 150 + 64;
		int srcY = y * 150 + 248;
		Sprite computerSprite = graphicEntityModule.createSprite()
			.setImage("computer.png")
			.setX(srcX)
			.setY(srcY)
			.setAnchor(0)
			.setZIndex(1);
		bugCircle = graphicEntityModule.createCircle()
			.setX(srcX + 150/2)
			.setY(srcY + 150/2)
			.setRadius(150/2 - 20)
			.setFillColor(0x000000)
			.setAlpha(0)
			.setZIndex(2);
		displayGroup = graphicEntityModule.createGroup(computerSprite, bugCircle);
		this.updateDisplay(0, 0);
	}

	public void spawnBug() {
		if (bugLevel == 0) {
			bugLevel = random.nextInt(3) + 1;
			originalBugLevel = bugLevel;
			turnToUpgradeBug = random.nextInt(10) + 10;
		}
	}

	public void update() {
		fixedAtThisTurn.clear();
		if (turnToUpgradeBug > 0) {
			turnToUpgradeBug--;
			if (turnToUpgradeBug == 0) {
				if (bugLevel < 3)
					bugLevel++;
				if (bugLevel < 3)
					turnToUpgradeBug = random.nextInt(3) + 2;
			}
		}

		this.updateDisplay(0.7, 0.9);
	}

	private void updateDisplay(double before, double after) {
		graphicEntityModule.commitEntityState(before, bugCircle);
		if (bugLevel > 0) {
			bugCircle.setAlpha(0.5);
			switch (bugLevel) {
				case 1:
					bugCircle.setFillColor(0x00FF00);
					break;
				case 2:
					bugCircle.setFillColor(0xFFFF00);
					break;
				case 3:
					bugCircle.setFillColor(0xFF0000);
					break;
			}
		} else {
			bugCircle.setAlpha(0);
		}
		this.updateToolTip();
		graphicEntityModule.commitEntityState(after, bugCircle);
	}

	private void updateToolTip() {
		String tooltipText = "Computer " + x + ";" + y;
		if (bugLevel > 0) {
			tooltipText += "\nBug level: " + bugLevel;
			tooltipText += "\nTurns to upgrade: " + turnToUpgradeBug;
			tooltipText += "\nOriginal level: " + originalBugLevel;
		}
		tooltips.setTooltipText(displayGroup, tooltipText);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Cell))return false;
		Cell otherCell = (Cell)other;
		return otherCell.x == x && otherCell.y == y;
	}

	public String export() {
		return String.format("%d %d %d %d", x, y, bugLevel, turnToUpgradeBug == 0 ? 0 : turnToUpgradeBug - 1);
	}

	public void fix(Fixer fixer, MultiplayerGameManager<Player> gameManager) {
		if (bugLevel > 0) {
			bugLevel--;
			if (bugLevel == 0) {
				turnToUpgradeBug = 0;
			}
			this.updateDisplay(0.4, 0.6);
			Player p = gameManager.getPlayers().get(fixer.getPlayerIndex());
			fixedAtThisTurn.add(p);
			if (bugLevel != 0)
				gameManager.addToGameSummary(p.getNicknameToken() + " worked on a bug at (" + x + ";" + y + "). It's now level " + bugLevel + "!");
			else {
				int won = originalBugLevel * 5;
				for (Player pl : fixedAtThisTurn) {
					gameManager.addToGameSummary(pl.getNicknameToken() + " fixed a bug at (" + x + ";" + y + "). He won " + won + " credits!");
					pl.setScore(pl.getScore() + won);
				}
			}
		} else if (fixedAtThisTurn.size() > 0) {
			int won = originalBugLevel * 5;
			Player pl = gameManager.getPlayers().get(fixer.getPlayerIndex());
			gameManager.addToGameSummary(pl.getNicknameToken() + " fixed a bug at (" + x + ";" + y + "). He won " + won + " credits!");
			pl.setScore(pl.getScore() + won);
		}
	}
}
